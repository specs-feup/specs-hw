/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.util.*;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.*;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.*;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.*;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class HardwareGenerationUtils {

    /*
     * Public call method to get all OperandASTNode instances from a certain node downwards
     */
    public static List<OperandASTNode> getOperands(InstructionASTNode node) {
        var list = new ArrayList<OperandASTNode>();
        return HardwareGenerationUtils.getOperands(node, list);
    }

    /*
     * Private call method to help recurse on the tree (doesn't include creation of the list object to return)
     */
    private static List<OperandASTNode> getOperands(InstructionASTNode node, List<OperandASTNode> list) {
        for (var c : node.getChildren()) {
            if (c instanceof OperandASTNode)
                list.add((OperandASTNode) c);
            else
                getOperands(c, list);
        }
        return list;
    }

    /*
     * Replaces ASM field names in the AST to concrete register names or operand values
     */
    public static void convertOperandsToConcrete(InstructionAST ast) {
        var instoplist = ast.getInst().getData().getOperands();
        var astoplist = HardwareGenerationUtils.getOperands(ast.getRootnode());

        // Anything thats an OperandASTNode necessarily
        // comes from an ASM field of the pseudocode.
        // Any other variables are intermediate results of expressions
        for (var astOP : astoplist) {
            var instOp = HardwareGenerationUtils.getOperandByAsmField(instoplist, astOP.getOperandName());

            if (instOp.isImmediate())
                astOP.setOperandName(instOp.getValue()); // value returned here should be decimal
            else
                astOP.setOperandName(instOp.getRepresentation());
        }
    }

    /*
     * 
     */
    private static Operand getOperandByAsmField(List<Operand> ops, String asmFieldName) {
        for (var op : ops) {
            if (op.getAsmField().toString().equals(asmFieldName))
                return op;
        }
        return null;

        // TODO fix null return
    }

    /*
     * Returns a sub-tree of HardwareNode implementing a particular StatementASTNode
     */
    public static HardwareStatement convertASTStatement(StatementASTNode statement) {

        // TODO: resolving the multiple nested expressions will require VariableDeclaration nodes at the top level of
        // this node return...
        // maybe do that after returning this, at the calling context, by walking the resulting subtree
        // and adding declarations of new variable references??
        // Does this need a new type of HardwareNode? something like HardwareDeclarationPremable (similar to
        // HardwareRootNode)

        // new node
        var subroot = convertASTExpression(statement.getExpr());

        // finish statement after expressions resolved
        var target = statement.getTarget();
        return new AssignStatement(new VariableReference(target.getAsString()), subroot);
    }

    /*
     * Dispatcher (ExpressionASTNode level down)
     */
    public static HardwareExpression convertASTExpression(ExpressionASTNode expr) {

        if (expr instanceof BinaryExpressionASTNode) {
            return convertASTExpression((BinaryExpressionASTNode) expr);
        }

        else if (expr instanceof UnaryExpressionASTNode) {
            return convertASTExpression((UnaryExpressionASTNode) expr);
        }

        // return a node
        else {// if (expr instanceof OperandASTNode) {
            if (expr.getType() == InstructionASTNodeType.AsmFieldNode)
                return new VariableReference(((OperandASTNode) expr).getOperandName());

            else // InstructionASTNodeType.LiteralNode
                return new ImmediateReference(((OperandASTNode) expr).getOperandValue(), 32);
            // TODO: return a HardwareReference (VariableReference or RangeSelection)
        }
    }

    /*
     * 
     */
    public static HardwareExpression convertASTExpression(BinaryExpressionASTNode expr) {

        // left and right node (could be subtrees)
        var lnode = convertASTExpression(expr.getLeft());
        var rnode = convertASTExpression(expr.getRight());

        HardwareExpression finalexpr = null;
        String op = expr.getOperator().getAsString();
        switch (op) {
        case "+":
            finalexpr = new AdditionExpression(lnode, rnode);
            break;
        default:
            break;
        }

        return finalexpr;
    }

    /*
     * 
     */
    public static HardwareExpression convertASTExpression(UnaryExpressionASTNode expr) {

        // right node, could be subtree
        var rnode = convertASTExpression(expr.getRight());

        HardwareExpression finalexpr = null;
        String op = expr.getOperator().getAsString();
        switch (op) {
        // case "+":
        // finalexpr = new AdditionExpression(lnode, rnode);
        // break;
        default:
            break;
        }

        return finalexpr;
    }

    /*
     * Convert a base expression, where left and right are references to Expression results or Operands
     
    public static HardwareNode convertASTExpression(OperandASTNode left, OperatorASTNode operator,
            OperandASTNode right) {
    
        // var hwref = new VariableReference(left.getOperandName());
        // TODO: this ^
    
        if (operator.getAsString().contentEquals("+"))
            return new AdditionExpression(left, right);
    
        else
            return null;
    }*/

}
