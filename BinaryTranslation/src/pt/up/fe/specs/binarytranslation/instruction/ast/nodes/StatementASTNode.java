package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public class StatementASTNode extends AInstructionASTNode {

    private OperandASTNode target;
    private ExpressionASTNode expr;

    public StatementASTNode(InstructionASTNode target, InstructionASTNode expr) {
        super();
        this.target = (OperandASTNode) target;
        this.expr = (ExpressionASTNode) expr; // TODO: works?
        this.type = InstructionASTNodeType.StatementNode;
        this.children.add(this.target);
        this.children.add(this.expr);
    }

    @Override
    public String getAsString() {
        return target.getAsString() + " = " + expr.getAsString() + ";";
    }

    public OperandASTNode getTarget() {
        return target;
    }

    public ExpressionASTNode getExpr() {
        return expr;
    }
}
