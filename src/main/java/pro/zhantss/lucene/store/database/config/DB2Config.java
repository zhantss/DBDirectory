/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.zhantss.lucene.store.database.config;

/**
 * DB2 Database config.
 *
 */
public class DB2Config extends DatabaseConfig {

    private static final String DB2_CONFIG = "db2.sql";

    public DB2Config() {
        super(DB2_CONFIG);
    }

    public DB2Config(final long threshold) {
        super(DB2_CONFIG, threshold);
    }
}
