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
