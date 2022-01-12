package pt.up.fe.specs.binarytranslation.hardware.validation.generator;

public class HardwareValidationRegister extends HardwareValidationWrapper{

    private final String name;
    private Number value;
    
    public HardwareValidationRegister(String name, Number initialValue) {
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
