/**
 *    Copyright 2009-2015 the original author or authors.
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
/**
 * jdbc子包和其他子包源码逻辑完全不同。
 * jdbc日志一般和mybatis日志是分开的，如果希望查看jdbc错误日志怎么办呢
 *
 * jdbc子包就是为了解决这个问题
 * Logging proxies that logs any JDBC statement.
 */
package org.apache.ibatis.logging.jdbc;
