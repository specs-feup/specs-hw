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

package pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule;

import java.util.*;

import pt.up.fe.specs.binarytranslation.hardware.*;
import pt.up.fe.specs.binarytranslation.hardware.component.*;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.lex.PseudoInstructionGetters;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.*;

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
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

    // TODO: replace this
    private static String cleaner(String in) {
        return in.replace("<", "").replace(">", "");
    }

    /**
     * 
     * @param inst
     *            The instruction thats being used for generation
     */
    @Override
    public HardwareInstance generateHarware(Instruction inst) {

        // The list of components
        List<HardwareComponent> components = new ArrayList<HardwareComponent>();
        components.add(new ModuleHeader(inst));

        // module declaration
        components.add(new PlainCode("module " + inst.getName() + ";"));

        // build all ports, based on operands
        var instOperands = inst.getData().getOperands();
        for (var op : instOperands) {
            if (!op.isImmediate())
                components.add(ModulePort.newPort(op)); // TODO: how to create output register types??
        }

        // get tree and visit it using this derived visitor class
        var tree = inst.getPseudocode().getTree();

        // get tokens that are ASM FIELDS, as Strings
        // I need this to know which asmfields are which operands
        // var fields = PseudoInstructionGetters.getAsmFields(tree);

        // build mapping between asmFieldNames and Verilog signal names here??
        // TODO

        // TODO make a visitor like getResultRegister, getOperation, getLOperand, etc??
        // do this per statement rule?

        // for every statement in the instruction
        components.add(new PlainCode("always_comb begin"));
        for (var s : tree.statement()) {

            //

            // target operand and relation operator (only assignment (=) for now)
            var asmfield = s.operand();
            var rlop = s.rlop();
            // TODO: should be "="; check!

            var instOperand = getOperandByAsmField(instOperands, asmfield.getText());
            var targetname = cleaner(instOperand.getRepresentation());

            // expression (could be a conjunction of expressions, see grammar rules PseudoInstruction.g4)
            var expr = s.expression();
            String exprString = "assign " + targetname + " = ";

            for (var child : expr.children) {

                if (child instanceof ExpressionContext) {

                    var grandchild = child.getChild(0);
                    var aux = getOperandByAsmField(instOperands, grandchild.getText());

                    if (grandchild instanceof OperandContext) {
                        exprString += cleaner(aux.getRepresentation());
                    }

                    else if (child instanceof NumberContext) {
                        exprString += aux.getRepresentation().replace("0x",
                                (aux.getProperties().getWidth() - 1) + "\'");
                    }
                }

                else if (child instanceof OperatorContext) {
                    exprString += " " + child.getText() + " ";
                }
            }

            // the expression!
            components.add(new PlainCode(exprString + ";"));

            // TODO: new AssignmentStatement(target, source) - variable types??

            // var nodes = PseudoInstructionGetters.getAllNodes(s);
        }
        components.add(new PlainCode("end"));
        components.add(new PlainCode("endmodule"));

        return new SingleInstructionModule(components);
    }
}
