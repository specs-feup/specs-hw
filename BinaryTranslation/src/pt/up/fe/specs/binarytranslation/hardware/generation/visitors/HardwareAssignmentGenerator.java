package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.generation.exception.HardwareGenerationException;
import pt.up.fe.specs.binarytranslation.hardware.generation.exception.UnimplementedExpressionException;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.MultiplicationExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.SubtractionExpression;
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

/**
 * Converts a single {@AssignmentExpressionASTNode} into a single corresponding hardware expression
 * 
 * @author nuno
 * 
 */
public class HardwareAssignmentGenerator extends InstructionASTVisitor<HardwareNode> {

    /*
     * Trees under lookup/transformation
     */
    // private HardwareTree tree; // TODO: replace with class HardwareTree, which should ALWAYS contain a root node, and
    // a
    // DeclarationNode whose children are all teh variable declaratiosn; then, every node int
    // he HWtree should have a getDeclarationNode method, so a child can be added when a new
    // variable is needed at any point during HWtree genration
    // private InstructionAST ast;

    public HardwareAssignmentGenerator() {
        // TODO Auto-generated constructor stub
    }

    private HardwareExpression convertExpression(AssignmentExpressionASTNode astexpr)
            throws HardwareGenerationException {
        HardwareNode expr = null;
        try {
            expr = visit(astexpr.getExpr());

        } catch (HardwareGenerationException e) {
            throw e;

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        return (HardwareExpression) expr;
    }

    public ContinuousStatement generateAssign(AssignmentExpressionASTNode node)
            throws HardwareGenerationException {

        return new ContinuousStatement(new VariableReference(node.getTarget().getAsString()),
                convertExpression(node));
    }

    public ProceduralBlockingStatement generateBlocking(AssignmentExpressionASTNode node)
            throws HardwareGenerationException {
        return new ProceduralBlockingStatement(new VariableReference(node.getTarget().getAsString()),
                convertExpression(node));
    }

    public ProceduralNonBlockingStatement generateNonBlocking(AssignmentExpressionASTNode node)
            throws HardwareGenerationException {
        return new ProceduralNonBlockingStatement(new VariableReference(node.getTarget().getAsString()),
                convertExpression(node));
    }

    @Override
    protected HardwareNode visit(BinaryExpressionASTNode node) throws Exception {

        // left and right node (could be subtrees)
        var lnode = visit(node.getLeft());
        var rnode = visit(node.getRight());

        HardwareNode finalexpr = null;
        String op = node.getOperator().getAsString();
        switch (op) {
        case "+":
            finalexpr = new AdditionExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        case "-":
            finalexpr = new SubtractionExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        case "*":
            finalexpr = new MultiplicationExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        default:
            // finalexpr = new UnimplementedExpression((HardwareExpression) lnode, (HardwareExpression) rnode, op);
            throw new UnimplementedExpressionException(node.getAsString());
        }

        return finalexpr;
    }

    @Override
    protected HardwareNode visit(UnaryExpressionASTNode node) throws Exception {

        throw new UnimplementedExpressionException(node.getAsString());
        /*    
        // right node, could be subtree
        var rnode = visit(node.getRight());
        
        HardwareExpression finalexpr = null;
        String op = node.getOperator().getAsString();
        switch (op) {
        // case "+":
        // finalexpr = new AdditionExpression(lnode, rnode);
        // break;
        default:
            // finalexpr = new UnimplementedExpression((HardwareExpression) rnode, op);
            throw new UnimplementedExpressionException("test");
        }
        
        // TODO: finish this!!
        
        return finalexpr;*/
    }

    @Override
    protected HardwareNode visit(VariableOperandASTNode node) {

        var variablename = node.getAsString();

        // Find reference to variable of this name
        // VariableReference varref = hwtree.findDeclaration(variablename);
        // if(varref = )

        // TODO: otherwise create

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
