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

package org.specs.Riscv.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.specs.Riscv.instruction.RiscvRegisterDump;
import org.specs.Riscv.parsing.getters.RiscvAsmBranchTargetGetter;
import org.specs.Riscv.parsing.getters.RiscvAsmOperandGetter;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class RiscvAsmFieldData extends AsmFieldData {

    /*
     * re-mapping of <string, string> to <asmfield, string>
     */
    private final Map<RiscvAsmField, Integer> map = new HashMap<RiscvAsmField, Integer>();

    /*
     * Create raw
     */
    public RiscvAsmFieldData(Number addr, AsmFieldType type, Map<String, String> fields) {
        super(addr, type, fields);

        // get int values from fields
        for (var field : RiscvAsmField.values()) {
            if (fields.containsKey(field.getFieldName())) {
                map.put(field, Integer.parseInt(fields.get(field.getFieldName()), 2));
            }
        }
    }

    /*
     * Create from parent class
     */
    public RiscvAsmFieldData(AsmFieldData fieldData) {
        this(fieldData.get(ADDR), fieldData.get(TYPE), fieldData.get(FIELDS));
    }

    /*
     * Copy "constructor"
     */
    public RiscvAsmFieldData copy() {
        return new RiscvAsmFieldData(
                this.get(ADDR), this.getType(),
                new HashMap<String, String>(this.getFields()));
    }

    /*
     * 
     */
    public Map<RiscvAsmField, Integer> getMap() {
        return map;
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     * This manner of field parsing, maintains the operand order as parsed
     * in the AsmFields
     */
    @Override
    public List<Operand> getOperands(RegisterDump registers) {
        var riscvreg = (RiscvRegisterDump) registers;
        return RiscvAsmOperandGetter.getFrom(this, riscvreg);
    }

    /*
    * Get target of branch if instruction is branch
    */
    @Override
    public Number getBranchTarget(RegisterDump registers) {

        // operands
        var riscvreg = (RiscvRegisterDump) registers;
        return RiscvAsmBranchTargetGetter.getFrom(this, riscvreg);
    }
}
