package pt.up.fe.specs.binarytranslation.analysis.memory;

public class AddressVertex {
    public enum AddressVertexType {
        OPERATION,
        REGISTER,
        IMMEDIATE,
        MEMORY,
        NULL
    };
    
    private String label;
    private AddressVertexType type;
    public static AddressVertex nullVertex = new AddressVertex("", AddressVertexType.NULL);
    
    public AddressVertex(String label, AddressVertexType type) {
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public AddressVertexType getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return getLabel() + " - " + getType();
    }
}
