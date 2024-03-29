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

package pt.up.fe.specs.binarytranslation.instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;

/**
 * Represents the hook into an instruction set. Its initialized with a list of {@link InstructionProperties} and all
 * types of instructions {@link AsmFieldType} in that set, and can decode a given instruction's parsed fields into
 * useful data.
 * 
 * @author JoaoBispo
 *
 */
public class InstructionSet {

    private List<InstructionProperties> instList;
    private List<AsmFieldType> codeTypes;
    private Map<AsmFieldType, List<InstructionProperties>> typeLists;

    /*
     * Constructor of instruction set; set is constructed from list of instruction
     * implemented as an enum, which implements InstructionProperties
     */
    public InstructionSet(List<InstructionProperties> instList, List<AsmFieldType> codeTypes) {
        this.instList = instList;
        this.codeTypes = codeTypes;
        this.typeLists = makeTypeLists();
    }

    /*
     * Gets only the instructions which match format of type "type"
     */
    private List<InstructionProperties> getTypeList(AsmFieldType type) {
        return this.typeLists.get(type);
    }

    /*
     * Constructs a map where each key is a type of 
     * instruction format, and the list are the instructions of that format
     */
    private Map<AsmFieldType, List<InstructionProperties>> makeTypeLists() {

        var ret = new HashMap<AsmFieldType, List<InstructionProperties>>();

        // For each instruction format
        for (var codetype : codeTypes) {
            var nlist = new ArrayList<InstructionProperties>();

            // For each instruction in the set, which fits that format, make the list
            for (var inst : instList) {
                if (inst.getCodeType().equals(codetype)) {
                    nlist.add(inst);
                }
            }
            ret.put(codetype, nlist);
        }
        return ret;
    }

    /*
     * Gets all properties of instruction from ISA, based on raw field data
     */
    private InstructionProperties getProperties(AsmFieldData asmData) {
        InstructionProperties props = null;
        for (var inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode()) {
                return inst;
            }
        }

        // System.out.print(Integer.toBinaryString(asmData.getReducedOpcode()) + "\n");

        // find the type labeled as generic unknown otherwise
        for (var inst : instList) {
            if (inst.getGenericTypes().contains(InstructionType.G_UNKN)) {
                return inst;
            }
        }

        return props;
    }

    /*
     * Get correct general properties from ISA
     */
    public InstructionProperties process(AsmFieldData fieldData) {

        // uses opcode fields to determine properties
        return getProperties(fieldData);
    }
}
