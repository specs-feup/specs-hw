package pt.up.fe.specs.binarytranslation;

public enum InstructionOperandType {

    register("r"),
    immediate("");

    private String prefix;

    private InstructionOperandType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
