/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors;

import pt.up.fe.specs.binarytranslation.hardware.generation.exception.HardwareGenerationException;
import pt.up.fe.specs.binarytranslation.hardware.generation.exception.UnimplementedExpressionException;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.AssignmentExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.expr.UnaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.LiteralOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.operand.MetaOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.ImmediateOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.transformed.VariableOperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.InstructionASTVisitor;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.ParenthesisExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.LeftShiftExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.MultiplicationExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.SubtractionExpression;
import pt.up.fe.specs.crispy.ast.expression.bitwise.BitWiseAndExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.ComparisonExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.ContinuousStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.specshw.hardware.tree.nodes.expression.operator.ImmediateOperator;

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

        return new ContinuousStatement(new VariableOperator(node.getTarget().getAsString()),
                convertExpression(node));
    }

    public ProceduralBlockingStatement generateBlocking(AssignmentExpressionASTNode node)
            throws HardwareGenerationException {
        return new ProceduralBlockingStatement(new VariableOperator(node.getTarget().getAsString()),
                convertExpression(node));
    }

    public ProceduralNonBlockingStatement generateNonBlocking(AssignmentExpressionASTNode node)
            throws HardwareGenerationException {
        return new ProceduralNonBlockingStatement(new VariableOperator(node.getTarget().getAsString()),
                convertExpression(node));
    }

    @Override
    protected HardwareNode visit(BinaryExpressionASTNode node) throws Exception {

        // left and right node (could be subtrees)
        var lnode = visit(node.getLeft());
        var rnode = visit(node.getRight());

        HardwareExpression finalexpr = null;
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
        case "<<":
            finalexpr = new LeftShiftExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        case "&":
            finalexpr = new BitWiseAndExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        case "==":
            finalexpr = new ComparisonExpression((HardwareExpression) lnode, (HardwareExpression) rnode);
            break;
        default:
            // finalexpr = new UnimplementedExpression((HardwareExpression) lnode, (HardwareExpression) rnode, op);
            throw new UnimplementedExpressionException(node.getAsString());
        }

        return new ParenthesisExpression(finalexpr);
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

        return new VariableOperator(variablename);
    }

    @Override
    protected HardwareNode visit(MetaOperandASTNode node) throws Exception {
        return new VariableOperator(node.getAsString());
    }

    @Override
    protected HardwareNode visit(ImmediateOperandASTNode node) {
        return new ImmediateOperator(node.getValue(), node.getWidth());
    }

    @Override
    protected HardwareNode visit(LiteralOperandASTNode node) {
        return new ImmediateOperator(node.getValue(), 32);
        // TODO: support in g4 grammar for specifying bitwidth (?)
        // not sure if good idea, since i already have bitwidth info in the encoding parsing and instruction/operand
        // classes...
    }
}
