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
        BASE_ADDR_START,
        OFFSET_START,
        INDUCTION_VAR,
        STRIDE,
        NULL
    }
    
    private String label;
    private AddressVertexType type;
    private AddressVertexProperty property = AddressVertexProperty.NULL;
    public static AddressVertex nullVertex = new AddressVertex("", AddressVertexType.NULL);
    
    public AddressVertex(String label, AddressVertexType type) {
        this.label = label;
        this.type = type;
    }
    
    public AddressVertex(String label, AddressVertexType type, AddressVertexProperty property) {
        this(label, type);
        this.property = property;
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
}
