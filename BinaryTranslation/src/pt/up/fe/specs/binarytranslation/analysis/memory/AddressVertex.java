package pt.up.fe.specs.binarytranslation.analysis.memory;

public class AddressVertex {
    public enum AddressVertexType {
        OPERATION,
        REGISTER,
        IMMEDIATE,
        MEMORY,
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

    public String getLabel() {
        return label;
    }
    
    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    public AddressVertexType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return getLabel() + " - " + getType();
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
}
