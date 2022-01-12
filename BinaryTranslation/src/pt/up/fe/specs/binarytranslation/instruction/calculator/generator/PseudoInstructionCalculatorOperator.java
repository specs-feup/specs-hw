package pt.up.fe.specs.binarytranslation.instruction.calculator.generator;

import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.APseudoInstructionCalculatorOperation;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.binary.PseudoInstructionCalculatorBinaryOperationMap;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.operation.unary.PseudoInstructionCalculatorUnaryOperationMap;

public class PseudoInstructionCalculatorOperator extends PseudoInstructionCalculatorWrapper{

    private final APseudoInstructionCalculatorOperation operation;
    
    public PseudoInstructionCalculatorOperator(String operator) {
        this.operation = (PseudoInstructionCalculatorBinaryOperationMap.get(operator) != null) ?  PseudoInstructionCalculatorBinaryOperationMap.get(operator) : PseudoInstructionCalculatorUnaryOperationMap.get(operator);
    }
    
    public APseudoInstructionCalculatorOperation getOperation() {
        return this.operation;
    }
}
