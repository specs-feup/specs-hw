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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.AssignStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.BinaryExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.ExpressionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.OperandASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.StatementASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.UnaryExpressionASTNode;
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
        for (var op : astoplist) {
            var instOperand = HardwareGenerationUtils.getOperandByAsmField(instoplist, op.getOperandName());
            op.setOperandName(instOperand.getRepresentation());
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
        else { // if (expr instanceof OperandASTNode) {
            return new VariableReference(((OperandASTNode) expr).getOperandName());
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

// OLD GARBAGE:

/*

// TODO: replace this
private static String cleaner(String in) {
    return in.replace("<", "").replace(">", "");
}


 * EXPERIMENTAL: Recursively process an expression context
 
private static String processExpression(Instruction inst, ExpressionContext ctx) {

    String ret = "";
    for (var child : ctx.children) {

        // is terminal (Operator)
        if (child instanceof OperatorContext) {
            ret += " " + child.getText();
        }

        // is terminal (Operand)
        else if (child instanceof OperandContext) {
            var op = getOperandByAsmField(inst.getData().getOperands(), child.getText());
            ret += " " + op.getRepresentation();
        }

        // recurse!
        else if (child.getChildCount() > 1) {
            ret += "(" + processExpression(inst, (ExpressionContext) child) + ")";
        }

        // recurse!
        else {
            ret += processExpression(inst, (ExpressionContext) child);
        }
    }
    return ret;
}

// private

// IDEA: walk the statemtn tree, and on all exitExpression calls, push the expression onto a stack
// the process the stack, which would be (?) from the bottom of the tree upwards
// would it??


 * 
 
public static PlainCode generateAssignStatement(Instruction inst, ExpressionContext ctx) {

    return null;
}


 * Generate one or more Verilog assign statements from a StatementContext
 
public static PlainCode generateAssignStatement(Instruction inst, StatementContext ctx) {

    int ctr = 0;
    var exprMap = new HashMap<ExpressionContext, String>();

    // expression of StatementContext (statement: operator rlop expression STATEMENTEND)
    var topExpr = ctx.expression();
    exprMap.put(topExpr, "expr" + ctr);

    // children of the StatementContext expression
    // each expression could be a conjunction of expressions, see grammar rules PseudoInstruction.g4
    //for (var expr : topExpr.expression()) {
    //    exprMap.put(expr, "expr" + ctr++);
    //}

    var code = new ArrayList<String>();

    var asmfield = ctx.operand();

    var instOperand = getOperandByAsmField(inst.getData().getOperands(), asmfield.getText());
    var targetname = cleaner(instOperand.getRepresentation());

    // top level statement should be the last !

    // the expression!
    return null; // new PlainCode("assign " + targetname + " = " + processExpression(inst, expr) + ";");
}
 */
