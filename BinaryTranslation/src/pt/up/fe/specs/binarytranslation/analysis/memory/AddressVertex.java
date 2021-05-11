package pt.up.fe.specs.binarytranslation.analysis.memory;

public class AddressVertex {
    public enum AddressVertexType {
        OPERATION,
        REGISTER,
        IMMEDIATE,
        MEMORY,
        LOAD_TARGET,
        STORE_TARGET,
        CHECK,
        NULL
    };
    public enum AddressVertexProperty {
        OFFSET,
        BASE_ADDR,
        INDUCTION_VAR,
        STRIDE,
        NULL
    }
    public enum AddressVertexIsaInfo {
        RA,
        RB,
        RD,
        NULL
    }
    
    private String label;
    private boolean keep = false;
    private AddressVertexType type;
    private AddressVertexProperty property = AddressVertexProperty.NULL;
    private AddressVertexIsaInfo isaInfo = AddressVertexIsaInfo.NULL;
    public static AddressVertex nullVertex = new AddressVertex("", AddressVertexType.NULL);
    
    public AddressVertex(String label, AddressVertexType type) {
        this.label = label;
        this.type = type;
    }
    
    public AddressVertex(String label, AddressVertexType type, AddressVertexProperty property) {
        this(label, type);
        this.property = property;
    }
    
    public AddressVertex(String label, AddressVertexType type, AddressVertexIsaInfo isaInfo) {
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

    public AddressVertexType getType() {
        return type;
    }
    
    public void setType(AddressVertexType type) {
        this.type = type;
    }

    public AddressVertexProperty getProperty() {
        return property;
    }

    public void setProperty(AddressVertexProperty property) {
        this.property = property;
    }

    public AddressVertexIsaInfo getIsaInfo() {
        return isaInfo;
    }

    public void setIsaInfo(AddressVertexIsaInfo isaInfo) {
        this.isaInfo = isaInfo;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }
}
