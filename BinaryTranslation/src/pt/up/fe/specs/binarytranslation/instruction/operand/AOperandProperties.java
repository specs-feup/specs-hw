package pt.up.fe.specs.binarytranslation.instruction.operand;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public class AOperandProperties implements OperandProperties {

    private String prefix;
    private String suffix;
    private OperandType opType;
    private OperandAccessType opRnw;
    private OperandDataType opDataType;
    private OperandDataSize opDataSize;
    private final AsmField asmfield;

    /*
     * Base constructor (called by implementations of AOperand)
     */
    public AOperandProperties(AsmField asmfield, String prefix, String suffix,
            OperandType opType, OperandAccessType opRnw,
            OperandDataType opDataType, OperandDataSize opDataSize) {

        this.prefix = prefix;
        this.suffix = suffix;
        this.asmfield = asmfield;
        this.opType = opType;
        this.opRnw = opRnw;
        this.opDataType = opDataType;
        this.opDataSize = opDataSize;

        // this.opType.add(resolveWidth(width);
    }

    /*
     * Copy constructor
     */
    @Override
    public AOperandProperties copy() {
        var prefix = new String(this.getPrefix());
        var suffix = new String(this.getSuffix());
        return new AOperandProperties(this.asmfield, prefix, suffix,
                this.opType, this.opRnw, this.opDataType, this.opDataSize);
    }

    /*
     * Getters
     */
    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public OperandType getType() {
        return this.opType;
    }

    @Override
    public OperandAccessType getAccessType() {
        return this.opRnw;
    }

    @Override
    public OperandDataSize getDataSize() {
        return this.opDataSize;
    }

    @Override
    public OperandDataType getDataType() {
        return this.opDataType;
    }

    @Override
    public int getWidth() {
        return this.opDataSize.getValue();
    }
}
