package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class FunctionExpressionASTNode extends ExpressionASTNode {

    private String builtin;

    public FunctionExpressionASTNode(String builtin) {
        super();
        this.type = InstructionASTNodeType.FunctionExpressionASTNode;
        this.builtin = builtin;
    }

    public FunctionExpressionASTNode(String builtin, List<ExpressionASTNode> args) {
        this(builtin);
        for (var arg : args)
            this.addChild(arg);
    }

    @Override
    public String getAsString() {
        String ret = "";
        ret += this.builtin + "(";

        var iterator = this.getChildren().iterator();

        while (iterator.hasNext()) {
            var arg = iterator.next();
            ret += arg.getAsString();
            if (iterator.hasNext())
                ret += ", ";
        }
        return ret + ")";
    }

    public ExpressionASTNode getArgument(int idx) {
        return (ExpressionASTNode) this.getChild(idx);
    }

    @Override
    protected InstructionASTNode copyPrivate() {
        return new FunctionExpressionASTNode(this.builtin);
    }
}
