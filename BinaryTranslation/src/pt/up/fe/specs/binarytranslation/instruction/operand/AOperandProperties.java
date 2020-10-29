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
     * Set additional type field based on operand bitwidth
     */
    public static OperandDataSize resolveWidth(int width) {
        switch (width) {
        case 4:
            return OperandDataSize.NIBBLE;
        case 8:
            return OperandDataSize.BYTE;
        case 16:
            return OperandDataSize.HALFWORD;
        case 32:
            return OperandDataSize.WORD;
        case 64:
            return OperandDataSize.DWORD;
        case 128:
            return OperandDataSize.QWORD;
        default:
            return OperandDataSize.WORD;
        }
    }

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
    public AsmField getAsmField() {
        return this.asmfield;
    }

    @Override
    public int getWidth() {
        return this.opDataSize.getValue();
    }

    /*
    @Override
    public String getSymbolicPrefix() {
    
        // if already symbolic, return currently set prefix
        if (this.opType == OperandType.SYMBOLIC)
            return this.prefix;
    
        if (this.opType == OperandType.REGISTER)
            return this.prefix + "<";
    
        else
            return this.asmfield.toString() + "<";
    }
    
    @Override
    public String getSymbolicSuffix() {
        return ">";
    }
    
    /*
     * Setters
     
    @Override
    public void setSymbolic() {
        this.prefix = this.getSymbolicPrefix();
        this.suffix = this.getSymbolicSuffix();
        this.opType.add(OperandType.SYMBOLIC);
        return;
    }*/
}
