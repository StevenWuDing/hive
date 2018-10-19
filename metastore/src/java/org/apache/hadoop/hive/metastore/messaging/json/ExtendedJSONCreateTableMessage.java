/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.metastore.messaging.json;


import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.hadoop.hive.metastore.api.Table;
import org.codehaus.jackson.annotate.JsonProperty;

public class ExtendedJSONCreateTableMessage extends JSONCreateTableMessage {
  @JsonProperty
  private String location;
  @JsonProperty
  private PrincipalType ownerType;
  @JsonProperty
  private String ownerName;

  public ExtendedJSONCreateTableMessage() {
  }

  public ExtendedJSONCreateTableMessage(String server, String servicePrincipal, String db, String table, Long timestamp, String location) {
    super(server, servicePrincipal, db, table, timestamp);
    this.location = location;
  }

  public ExtendedJSONCreateTableMessage(String server, String servicePrincipal, Table tableObj, Long timestamp,
      String location) {
    super(server, servicePrincipal, tableObj, timestamp);
    this.location = location;
  }

  public ExtendedJSONCreateTableMessage(String server, String servicePrincipal, Long timestamp, Table table) {
    super(server, servicePrincipal, table, timestamp);
    this.location = (table.getSd() != null) ? table.getSd().getLocation() : null;
    this.ownerType = table.getOwnerType();
    this.ownerName = table.getOwner();
  }

  public String getLocation() {
    return location;
  }

  public PrincipalType getOwnerType() {
    return ownerType;
  }

  public String getOwnerName() {
    return ownerName;
  }

  @Override
  public String toString() {
    return ExtendedJSONMessageDeserializer.serialize(this);
  }
}
