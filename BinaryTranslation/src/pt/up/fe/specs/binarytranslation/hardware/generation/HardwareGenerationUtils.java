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
import java.util.HashMap;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.component.PlainCode;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.ExpressionContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperandContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.OperatorContext;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.StatementContext;

public class HardwareGenerationUtils {

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

    // TODO: replace this
    private static String cleaner(String in) {
        return in.replace("<", "").replace(">", "");
    }

    /*
     * EXPERIMENTAL: Recursively process an expression context
     */
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

    /*
     * 
     */
    // private

    // IDEA: walk the statemtn tree, and on all exitExpression calls, push the expression onto a stack
    // the process the stack, which would be (?) from the bottom of the tree upwards
    // would it??

    /*
     * 
     */
    public static PlainCode generateAssignStatement(Instruction inst, ExpressionContext ctx) {

        return null;
    }

    /*
     * Generate one or more Verilog assign statements from a StatementContext
     */
    public static PlainCode generateAssignStatement(Instruction inst, StatementContext ctx) {

        int ctr = 0;
        var exprMap = new HashMap<ExpressionContext, String>();

        // expression of StatementContext (statement: operator rlop expression STATEMENTEND)
        var topExpr = ctx.expression();
        exprMap.put(topExpr, "expr" + ctr);

        // children of the StatementContext expression
        // each expression could be a conjunction of expressions, see grammar rules PseudoInstruction.g4
        /*for (var expr : topExpr.expression()) {
            exprMap.put(expr, "expr" + ctr++);
        }*/

        var code = new ArrayList<String>();

        var asmfield = ctx.operand();

        var instOperand = getOperandByAsmField(inst.getData().getOperands(), asmfield.getText());
        var targetname = cleaner(instOperand.getRepresentation());

        // top level statement should be the last !

        // the expression!
        return null; // new PlainCode("assign " + targetname + " = " + processExpression(inst, expr) + ";");
    }

}
