package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTVisitor;

public class HardwareStatementGenerator extends InstructionASTVisitor<HardwareNode> {

    /*
     * Trees under lookup/transformation
     */
    // private HardwareTree tree; // TODO: replace with class HardwareTree, which should ALWAYS contain a root node, and
    // a
    // DeclarationNode whose children are all teh variable declaratiosn; then, every node int
    // he HWtree should have a getDeclarationNode method, so a child can be added when a new
    // variable is needed at any point during HWtree genration
    // private InstructionAST ast;

    public HardwareStatementGenerator() {
        // TODO Auto-generated constructor stub
    }

    public ContinuousStatement generateAssign(AssignmentExpressionASTNode node) {
        return new ContinuousStatement(new VariableReference(node.getTarget().getAsString()),
                (HardwareExpression) visit(node.getExpr()));
    }

    public ProceduralBlockingStatement generateBlocking(AssignmentExpressionASTNode node) {
        return new ProceduralBlockingStatement(new VariableReference(node.getTarget().getAsString()),
                (HardwareExpression) visit(node.getExpr()));
    }

    public ProceduralNonBlockingStatement generateNonBlocking(AssignmentExpressionASTNode node) {
        return new ProceduralNonBlockingStatement(new VariableReference(node.getTarget().getAsString()),
                (HardwareExpression) visit(node.getExpr()));
    }

    @Override
    protected HardwareNode visit(BinaryExpressionASTNode node) {

        // left and right node (could be subtrees)
        var lnode = visit(node.getLeft());
        var rnode = visit(node.getRight());

        HardwareExpression finalexpr = null;
        String op = node.getOperator().getAsString();
        switch (op) {
        case "+":
            finalexpr = new AdditionExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        default:
            break;
        }

        return finalexpr;
    }

    @Override
    protected HardwareNode visit(UnaryExpressionASTNode node) {

        // right node, could be subtree
        var rnode = visit(node.getRight());

        HardwareExpression finalexpr = null;
        String op = node.getOperator().getAsString();
        switch (op) {
        // case "+":
        // finalexpr = new AdditionExpression(lnode, rnode);
        // break;
        default:
            break;
        }

        // TODO: finish this!!

        return finalexpr;
    }

    @Override
    protected HardwareNode visit(VariableOperandASTNode node) {

        var variablename = node.getAsString();

        // Find reference to variable of this name
        // VariableReference varref = hwtree.findDeclaration(variablename);
        // if(varref = )

        // otherwise create

        return new VariableReference(variablename);
    }

    @Override
    protected HardwareNode visit(ImmediateOperandASTNode node) {
        return new ImmediateReference(node.getValue(), node.getWidth());
    }

    @Override
    protected HardwareNode visit(LiteralOperandASTNode node) {
        return new ImmediateReference(node.getValue(), 32);
        // TODO: support in g4 grammar for specifying bitwidth (?)
        // not sure if good idea, since i already have bitwidth info in the encoding parsing and instruction/operand
        // classes...
    }
}
