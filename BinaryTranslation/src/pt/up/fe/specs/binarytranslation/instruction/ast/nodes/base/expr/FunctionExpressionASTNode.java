package pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr;

import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;

public class FunctionExpressionASTNode extends ExpressionASTNode {

    private String builtin;

    public FunctionExpressionASTNode(String builtin, ExpressionASTNode... args) {
        super();
        this.type = InstructionASTNodeType.FunctionExpressionASTNode;
        this.builtin = builtin;
        for (var arg : Arrays.asList(args))
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
}
