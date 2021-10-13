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
 
package pt.up.fe.specs.binarytranslation.instruction.symbolic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * A special type of instruction which contains only string type operands, and can be converted to a concrete
 * instruction when a context is applied; the context should include operand values and inst addr
 * 
 * @author nuno
 *
 */
public class SymbolicInstruction {

    private String mmemonic;
    private List<String> operandSymbol = new ArrayList<String>();

    // TODO: needs list of Operand type objects so I can query sizes and stuf??
    // or should this Instruction NOT implement Instruction interface??

    public SymbolicInstruction(Instruction i, Map<String, String> regremap) {

        this.mmemonic = i.getName();
        for (var op : i.getData().getOperands()) {

            try {
                var rep = op.getRepresentation();
                if (!regremap.containsKey(rep))
                    throw new Exception();

                this.operandSymbol.add(regremap.get(rep));

                // TODO: handle beter
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String getRepresentation() {
        var bld = new StringBuilder();
        bld.append(this.mmemonic);
        for (var op : this.operandSymbol)
            bld.append(" ").append(op);

        return bld.toString();
    }
}
