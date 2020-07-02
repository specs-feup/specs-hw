package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class ImmediateOperandASTNode extends ConcreteOperandASTNode {

    /*
     * This class represents ONLY operands which are literal values (i.e., numbers)
     * But which NECESSARILY have an Operand attached (i.e., they come from filling an immediate field with a value)
     * This is different from literal number fields in the pseudocode, which DO NOT have an associated ASM field
     */
    public ImmediateOperandASTNode(Operand op) {
        super(op);
        this.type = InstructionASTNodeType.LiteralNode;
    }

    public Number getValue() {
        return this.op.getValue();
    }
}
