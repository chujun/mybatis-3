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
package org.apache.ibatis.reflection.wrapper;

import java.util.List;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/**
 * 对象包装器接口,对外部隐藏，主要通过MetaObject对外提供各种能力
 * @author Clinton Begin
 */
public interface ObjectWrapper {
  /**
   * 获得被包装对象某个属性的值
   */
  Object get(PropertyTokenizer prop);

  /**
   * 设置被包装对象某个属性的值
   */
  void set(PropertyTokenizer prop, Object value);

  /**
   * 找到对应的属性名称
   */
  String findProperty(String name, boolean useCamelCaseMapping);

  /**
   * 获得所有属性get方法名称
   */
  String[] getGetterNames();

  /**
   * 获得所有属性set方法名称
   */
  String[] getSetterNames();

  /**
   * 获得指定属性set方法类型
   */
  Class<?> getSetterType(String name);

  /**
   * 获得指定属性get方法类型
   */
  Class<?> getGetterType(String name);

  /**
   * 判断某个属性是否有对应的set方法
   */
  boolean hasSetter(String name);

  /**
   * 判断某个属性是否有对应的get方法
   */
  boolean hasGetter(String name);

  /**
   * 实例化某个属性的值
   */
  MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

  /**
   * 判断被包装对象是否是集合
   */
  boolean isCollection();

  /**
   * 被包装对象添加集合元素
   */
  void add(Object element);

  /**
   * 被包装对象添加多个集合元素
   */
  <E> void addAll(List<E> element);

}
