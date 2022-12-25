/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mybatis.provider;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.function.Supplier;

/**
 * sql缓存
 *
 * @author liuzh
 */
public class SqlCache {
  /**
   * 空对象
   */
  public static final SqlCache                           NULL = new SqlCache(null, null, null, null);
  /**
   * 执行方法上下文
   */
  private final       ProviderContext                    providerContext;
  /**
   * 实体类信息
   */
  private final       EntityTable                        entity;
  /**
   * sql 提供者
   */
  private final       Supplier<String>                   sqlScriptSupplier;
  /**
   * ms 定制处理
   */
  private final       SqlScript.MappedStatementCustomize customize;

  SqlCache(ProviderContext providerContext, EntityTable entity, Supplier<String> sqlScriptSupplier, SqlScript.MappedStatementCustomize customize) {
    this.providerContext = providerContext;
    this.entity = entity;
    this.sqlScriptSupplier = sqlScriptSupplier;
    this.customize = customize;
  }

  /**
   * 该方法延迟到最终生成 SqlSource 时才执行
   */
  public String getSqlScript() {
    return sqlScriptSupplier.get();
  }

  /**
   * 对 ms 进行定制处理
   *
   * @param ms 当前方法对应的 MappedStatement
   */
  public void customize(MappedStatement ms) {
    if (this.customize != null) {
      this.customize.customize(entity, ms, providerContext);
    }
  }

  /**
   * @return 执行方法上下文
   */
  public ProviderContext getProviderContext() {
    return providerContext;
  }

  /**
   * @return 实体类信息
   */
  public EntityTable getEntity() {
    return entity;
  }

}
