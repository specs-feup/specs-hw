/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.asmparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import pt.up.fe.specs.binarytranslation.InstructionOperand;
import pt.up.fe.specs.binarytranslation.InstructionOperandType;
import pt.up.fe.specs.binarytranslation.generic.GenericInstructionOperand;

/**
 * Raw field data as extracted by an {@link IsaParser}
 * 
 * @author NunoPaulino
 *
 */
public class AsmInstructionData extends ADataClass<AsmInstructionData> {

    public static final DataKey<AsmInstructionType> TYPE = KeyFactory.object("type", AsmInstructionType.class);

    public static final DataKey<Map<String, String>> FIELDS = KeyFactory.generic("fields",
            (Map<String, String>) new LinkedHashMap<String, String>());

    /*
     * Constructor
     */
    public AsmInstructionData(AsmInstructionType type, Map<String, String> fields) {
        set(TYPE, type);
        set(FIELDS, fields);
    }

    public AsmInstructionType getType() {
        return this.get(TYPE);
    }

    /*
     * Reduces all fields with prefix name "opcode" to a single sequence of bits
     */
    public int getReducedOpcode() {
        String tmp = "";
        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();

        for (String key : keys1) {
            if (key.contains("opcode")) {
                tmp = tmp + map1.get(key);
            }
        }

        if (tmp.isEmpty())
            return 0;

        return Integer.parseInt(tmp, 2);
    }

    /*
     * Gets a list of integers which represent the operands in the fields
     */
    public List<InstructionOperand> getOperands() {

        var map1 = this.get(FIELDS);
        var keys1 = map1.keySet();
        List<InstructionOperand> operands = new ArrayList<InstructionOperand>();

        for (String key : keys1) {
            if (key.contains("register")) {
                var op = new GenericInstructionOperand(InstructionOperandType.register,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);

            } else if (key.contains("imm")) {
                var op = new GenericInstructionOperand(InstructionOperandType.immediate,
                        Integer.parseInt(map1.get(key), 2));
                operands.add(op);
            }
        }

        return operands;
    }

    /*
     * Return a default bitwidth; this method SHOULD
     * be overwritten by specific processor implementations
     */
    public int getBitWidth() {
        return 32;
    }

    /*
     * Check if instruction is simd instruction; this method SHOULD
     * be overwritten by specific processor implementations
     */
    public Boolean isSimd() {
        return false;
    }
}
