package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.AInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class StatementASTNode extends AInstructionASTNode {

    private OperandASTNode target;
    private ExpressionASTNode expr;

    public StatementASTNode(OperandASTNode target, ExpressionASTNode expr) {
        super();
        this.target = target;
        this.expr = expr; // TODO: works?
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
