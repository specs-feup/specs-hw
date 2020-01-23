package pt.up.fe.specs.binarytranslation.instruction;

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public class AOperandProperties implements OperandProperties {

    private String prefix;
    private String suffix;
    private final int width;
    private List<OperandType> genericType;
    private final AsmField asmfield;

    /*
     * Set additional type field based on operand bitwidth
     */
    private static OperandType resolveWidth(int width) {
        switch (width) {
        case 4:
            return NIBBLE;
        case 8:
            return BYTE;
        case 16:
            return HALFWORD;
        case 32:
            return WORD;
        case 64:
            return DWORD;
        case 128:
            return QWORD;
        default:
            return WORD;
        }
    }

    /*
     * Base constructor (called by implementations of AOperand)
     */
    public AOperandProperties(AsmField asmfield, String prefix, String suffix, int width, List<OperandType> tp) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.asmfield = asmfield;

        // list enters as abstract list
        this.genericType = new ArrayList<OperandType>();
        for (OperandType type : tp) {
            this.genericType.add(type);
        }
        this.genericType.add(resolveWidth(width));
    }

    /*
     * Base constructor (called by implementations of AOperand)
     */
    public AOperandProperties(AsmField asmfield, String prefix, String suffix, int width, OperandType... tp) {
        this(asmfield, prefix, suffix, width, Arrays.asList(tp));
    }

    /*
     * Copy constructor
     */
    public AOperandProperties copy() {
        var prefix = new String(this.getPrefix());
        var suffix = new String(this.getSuffix());
        var types = new ArrayList<OperandType>();
        for (OperandType tp : this.getTypes()) {
            types.add(tp);
        }
        return new AOperandProperties(this.asmfield, prefix, suffix, this.width, types);
    }

    /*
     * Getters
     */
    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public int getWidth() {
        return this.width;
    }

    public List<OperandType> getTypes() {
        return this.genericType;
    }

    public OperandType getMainType() {
        return this.genericType.get(0);
    }

    public AsmField getAsmField() {
        return this.asmfield;
    }

    public String getSymbolicPrefix() {

        // if already symbolic, return currently set prefix
        if (this.genericType.contains(SYMBOLIC))
            return this.prefix;

        if (this.getMainType() == OperandType.REGISTER) {
            return this.prefix + "<";

        } else {
            return this.asmfield.toString() + "<";
        }
    }

    public String getSymbolicSuffix() {
        return ">";
    }

    /*
     * Setters
     */
    public void setSymbolic() {
        this.prefix = this.getSymbolicPrefix();
        this.suffix = this.getSymbolicSuffix();
        this.genericType.add(OperandType.SYMBOLIC);
        return;
    }
}
