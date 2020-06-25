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
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareGenerationUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
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

        // start block
        components.add(new PlainCode("always_comb begin"));

        // for every statement in the instruction
        for (var s : tree.statement()) {
            // the expression!
            components.add(HardwareGenerationUtils.generateAssignStatement(inst, s));
        }
        components.add(new PlainCode("end"));
        components.add(new PlainCode("endmodule"));

        return new SingleInstructionModule(components);
    }

    // find operator
    /* String statmentOperator = null;
    for (var child : expr.children) {
        if (child instanceof OperatorContext) {
            statmentOperator = child.getText();
            break;
        }
    }*/
    // TODO: replace this with other action, especially if operator is divisor

    // find operands
    /*int i = 0;
    OperandContext[] opCtx = { null, null };
    for (var child : expr.children) {
    
        // Expression could be a OperandContext, which is
        // either a NumberContext or AsmFieldContext
        if (child instanceof ExpressionContext) {
            opCtx[i] = (OperandContext) child.getChild(0);
        }
    }*/

    /*
    var grandchild = child.getChild(0);
    var aux = getOperandByAsmField(instOperands, grandchild.getText());
    
    if (grandchild instanceof OperandContext) {
        exprString += cleaner(aux.getRepresentation());
    }
    
    else if (child instanceof NumberContext) {
        exprString += aux.getRepresentation().replace("0x",
                (aux.getProperties().getWidth() - 1) + "\'");
    }*/

    // TODO: new AssignmentStatement(target, source) - variable types??

    // var nodes = PseudoInstructionGetters.getAllNodes(s);
}
