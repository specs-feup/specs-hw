package pt.up.fe.specs.binarytranslation.instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

public class AOperandProperties implements OperandProperties {

    private String prefix;
    private String suffix;
    private final int width;
    private List<OperandType> genericType;
    private AsmField asmfield;

    /*
     * Base constructor (called by implementations of AOperand)
     */
    public AOperandProperties(AsmField asmfield, String prefix, String suffix, int width, List<OperandType> tp) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.genericType = tp;
        this.asmfield = asmfield;

        // TODO add switch case to determine if NIBBLE, BYTE, WORD, etc, and add that type to the type list
        // or do it backwards
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

    /*
     * Setters
     */
    public void setSymbolic() {
        this.genericType.add(OperandType.SYMBOLIC);
        if (this.getMainType() == OperandType.REGISTER) {
            this.prefix = "r<";
        } else {
            this.prefix = this.asmfield.toString() + "<";
        }

        this.suffix = ">";
        return;
    }
}
