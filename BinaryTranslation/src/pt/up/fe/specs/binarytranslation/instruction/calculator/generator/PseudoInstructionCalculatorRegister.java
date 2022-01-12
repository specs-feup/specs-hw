package pt.up.fe.specs.binarytranslation.instruction.calculator.generator;

public class PseudoInstructionCalculatorRegister extends PseudoInstructionCalculatorWrapper{

    private final String name;
    private Number value;
    
    public PseudoInstructionCalculatorRegister(String name, Number initialValue) {
        this.name = name;
        this.value = initialValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Number getValue() {
        return this.value;
    }
    
    public void setValue(Number value) {
        this.value = value;
    }
}
