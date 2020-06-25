package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class StatementASTNode extends AInstructionASTNode {

    private OperandASTNode target;
    private ExpressionASTNode expr;

    public StatementASTNode(InstructionASTNode target, InstructionASTNode expr) {
        super();
        this.target = (OperandASTNode) target;
        this.expr = (ExpressionASTNode) expr;
        this.type = InstructionASTNodeType.StatementNode;
    }

    @Override
    public String getAsString() {
        return target.getAsString() + " = " + expr.getAsString() + ";";
    }
}
