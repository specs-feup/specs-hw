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

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
    }

    /*
     * 
     */
    private Operand getOperandByAsmField(List<Operand> ops, String asmFieldName) {
        for (var op : ops) {
            if (op.getAsmField().toString().equals(asmFieldName))
                return op;
        }
        return null;

        // TODO fix null return
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

        // get tree and visit it using this derived visitor class
        var tree = inst.getPseudocode().getTree();

        // get tokens that are ASM FIELDS, as Strings
        var fields = PseudoInstructionGetters.getAsmFields(tree);
        var operands = inst.getData().getOperands();

        // build all ports, based on operands, which we look up using field names
        for (var f : fields) {
            var op = getOperandByAsmField(operands, f);

        }

        /*        // get tokens that are ASM FIELDS
        for (var s : statements) {
            
            var expr = s.expression()
            
            var tTokens = s.getTokens(PseudoInstructionLexer.ASMFIELD);
            for (var t : tTokens) {
                System.out.println(t.getText());
            }
        }
        */
        /*
        // list operands
        for (var t : tTokens) {
            System.out.println(t.getText());
        }
        */
        // var components = this.visit(tree);
        return new SingleInstructionModule(components);
    }
}
