/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

/**
 * 通用标记解析器
 * @author Clinton Begin
 */
public class GenericTokenParser {
  //占位符的起始标志
  private final String openToken;
  //占位符的结束标志
  private final String closeToken;
  //占位符处理器
  private final TokenHandler handler;

  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  /**
   * 解析算法
   * 该解析方法主要完成占位符的定位问题，具体替换工作交给TokenHandler处理
   *
   * 示例
   * 假设openToken="＃{", closeToken="}”，
   * 向 GenericTokenParser中的 parse方法传入的参数为
   * “jdbc：mysql：//127.0.0.1：3306/${dbname}？serverTimezone=UTC”，
   * 则 parse方法会将被“＃{”和“}”包围的 dbname 字符串解析出来，
   * 作为输入参数传入 handler 中的handleToken方法，
   * 然后用 handleToken方法的返回值替换“${dbname}”字符串。
   *
   * 这个解析方法貌似不支持嵌套表达式,形如"${a${w}c}"
   * @param text
   * @return
   */
  public String parse(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    // search open token
    int start = text.indexOf(openToken);
    if (start == -1) {
      //没有找到占位符，直接返回
      return text;
    }
    char[] src = text.toCharArray();
    int offset = 0;
    final StringBuilder builder = new StringBuilder();
    StringBuilder expression = null;
    while (start > -1) {
      if (start > 0 && src[start - 1] == '\\') {
        //处理转义字符,形如"\\${",说明这是一个普通字符串"${",继续往前进搜索下一个符合条件的开始标记
        // this open token is escaped. remove the backslash and continue.
        builder.append(src, offset, start - offset - 1).append(openToken);
        offset = start + openToken.length();
      } else {
        // found open token. let's search close token.
        // expression存储"${xxx}"里的xxx字符串
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        //保留开始标志位之前的普通字符串
        builder.append(src, offset, start - offset);
        offset = start + openToken.length();
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if (end > offset && src[end - 1] == '\\') {
            //处理转义字符,形如"\\}",说明这是一个普通字符串"}",继续往前进搜索下一个符合条件的结束标记
            // this close token is escaped. remove the backslash and continue.
            expression.append(src, offset, end - offset - 1).append(closeToken);
            offset = end + closeToken.length();
            end = text.indexOf(closeToken, offset);
            //继续寻找一下结束标志
          } else {
            //截取出标志里包含的字符串变量名
            expression.append(src, offset, end - offset);
            //找到了可以退出循环了
            break;
          }
        }
        if (end == -1) {
          // close token was not found.
          //未发现结束标识，那就保留剩下的所有字符串
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          //得到字符串变量名对应的变量值，添加到最终的字符串中去
          builder.append(handler.handleToken(expression.toString()));
          offset = end + closeToken.length();
        }
      }
      //继续寻找下一个起始标志位
      start = text.indexOf(openToken, offset);
    }
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    return builder.toString();
  }
}
