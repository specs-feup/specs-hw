package pt.up.fe.specs.binarytranslation.analysis.dataflow;

public class DataFlowVertex {
    public enum DataFlowVertexType {
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
    public enum DataFlowVertexProperty {
        OFFSET,
        BASE_ADDR,
        INDUCTION_VAR,
        STRIDE,
        NULL
    }
    public enum DataFlowVertexIsaInfo {
        RA,
        RB,
        RD,
        NULL
    }
    
    private String label;
    private DataFlowVertexType type;
    private DataFlowVertexProperty property = DataFlowVertexProperty.NULL;
    private DataFlowVertexIsaInfo isaInfo = DataFlowVertexIsaInfo.NULL;
    private String color = "black";
    public static DataFlowVertex nullVertex = new DataFlowVertex("", DataFlowVertexType.NULL);
    
    public DataFlowVertex(String label, DataFlowVertexType type) {
        this.label = label;
        this.type = type;
    }
    
    public DataFlowVertex(String label, DataFlowVertexType type, DataFlowVertexProperty property) {
        this(label, type);
        this.property = property;
    }
    
    public DataFlowVertex(String label, DataFlowVertexType type, DataFlowVertexIsaInfo isaInfo) {
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

    public DataFlowVertexType getType() {
        return type;
    }
    
    public void setType(DataFlowVertexType type) {
        this.type = type;
    }

    public DataFlowVertexProperty getProperty() {
        return property;
    }

    public void setProperty(DataFlowVertexProperty property) {
        this.property = property;
    }

    public DataFlowVertexIsaInfo getIsaInfo() {
        return isaInfo;
    }

    public void setIsaInfo(DataFlowVertexIsaInfo isaInfo) {
        this.isaInfo = isaInfo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
