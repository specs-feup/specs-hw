/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.binarytranslation.analysis.graphs;

public class BtfVertex {
    public enum BtfVertexType {
        OPERATION,
        REGISTER,
        IMMEDIATE,
        MEMORY,
        LOAD_TARGET,
        STORE_TARGET,
        CHECK,
        JUMP,
        AGU,
        NULL 
    };
    public enum BtfVertexProperty {
        OFFSET,
        BASE_ADDR,
        INDUCTION_VAR,
        STRIDE,
        NULL
    }
    public enum BtfVertexIsaInfo {
        RA,
        RB,
        RD,
        NULL
    }
    
    private String label;
    private BtfVertexType type;
    private BtfVertexProperty property = BtfVertexProperty.NULL;
    private BtfVertexIsaInfo isaInfo = BtfVertexIsaInfo.NULL;
    private String color = "black";
    private int latency = 0;
    private int priority = -1;
    private int loadStoreOrder = -1;
    private int tempId = -1;
    public static BtfVertex nullVertex = new BtfVertex("", BtfVertexType.NULL);
    
    public BtfVertex(String label, BtfVertexType type) {
        this.label = label;
        this.type = type;
    }
    
    public BtfVertex(String label, BtfVertexType type, BtfVertexProperty property) {
        this(label, type);
        this.property = property;
    }
    
    public BtfVertex(String label, BtfVertexType type, BtfVertexIsaInfo isaInfo) {
        this(label, type);
        this.isaInfo = isaInfo;
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Vertex ").append(label).append(", type=").append(type).append(", property=")
        .append(property).append(", isaInfo=").append(isaInfo);
        return sb.toString();
    }

    public String getLabel() {
        return label;
    }
    
    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    public BtfVertexType getType() {
        return type;
    }
    
    public void setType(BtfVertexType type) {
        this.type = type;
    }

    public BtfVertexProperty getProperty() {
        return property;
    }

    public void setProperty(BtfVertexProperty property) {
        this.property = property;
    }

    public BtfVertexIsaInfo getIsaInfo() {
        return isaInfo;
    }

    public void setIsaInfo(BtfVertexIsaInfo isaInfo) {
        this.isaInfo = isaInfo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public void setPriority(int i) {
        this.priority = i;
    }
    
    public int getPriority() {
        return priority;
    }

    public void setLoadStoreOrder(int order) {
        this.loadStoreOrder = order;
    }
    
    public int getLoadStoreOrder() {
        return this.loadStoreOrder;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }
}
