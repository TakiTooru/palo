// Copyright (c) 2017, Baidu.com, Inc. All Rights Reserved

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.baidu.palo.persist;

import com.baidu.palo.catalog.DataProperty;
import com.baidu.palo.catalog.Partition;
import com.baidu.palo.catalog.PartitionKey;
import com.baidu.palo.catalog.RangePartitionInfo;
import com.baidu.palo.common.io.Writable;

import com.google.common.collect.Range;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PartitionPersistInfo implements Writable {
    private Long dbId;
    private Long tableId;
    private Partition partition;

    private Range<PartitionKey> range;
    private DataProperty dataProperty;
    private short replicationNum;
    
    public PartitionPersistInfo() {
    }

    public PartitionPersistInfo(long dbId, long tableId, Partition partition, Range<PartitionKey> range,
                                DataProperty dataProperty, short replicationNum) {
        this.dbId = dbId;
        this.tableId = tableId;
        this.partition = partition;

        this.range = range;
        this.dataProperty = dataProperty;

        this.replicationNum = replicationNum;
    }
    
    public Long getDbId() {
        return dbId;
    }
    
    public Long getTableId() {
        return tableId;
    }

    public Partition getPartition() {
        return partition;
    }

    public Range<PartitionKey> getRange() {
        return range;
    }

    public DataProperty getDataProperty() {
        return dataProperty;
    }
    
    public short getReplicationNum() {
        return replicationNum;
    }

    public void write(DataOutput out) throws IOException {
        out.writeLong(dbId);
        out.writeLong(tableId);
        partition.write(out);

        RangePartitionInfo.writeRange(out, range);
        dataProperty.write(out);
        out.writeShort(replicationNum);
    }
 
    public void readFields(DataInput in) throws IOException {
        dbId = in.readLong();
        tableId = in.readLong();
        partition = Partition.read(in);

        range = RangePartitionInfo.readRange(in);
        dataProperty = DataProperty.read(in);
        replicationNum = in.readShort();
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PartitionPersistInfo)) {
            return false;
        }
        
        PartitionPersistInfo info = (PartitionPersistInfo) obj;
        
        return dbId.equals(info.dbId)
                   && tableId.equals(info.tableId)
                   && partition.equals(info.partition);
    }
}
