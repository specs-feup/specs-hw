package pt.up.fe.specs.binarytranslation.hardware.validation.generator;

import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.AHardwareValidationOperation;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.binary.HardwareValidationBinaryOperationMap;
import pt.up.fe.specs.binarytranslation.hardware.validation.generator.operation.unary.HardwareValidationUnaryOperationMap;

public class HardwareValidationOperator extends HardwareValidationWrapper{

    private final AHardwareValidationOperation operation;
    
    public HardwareValidationOperator(String operator) {
        this.operation = (HardwareValidationBinaryOperationMap.get(operator) != null) ?  HardwareValidationBinaryOperationMap.get(operator) : HardwareValidationUnaryOperationMap.get(operator);
    }
    
    public AHardwareValidationOperation getOperation() {
        return this.operation;
    }
}
