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

package pt.up.fe.specs.binarytranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionData;
import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;
import pt.up.fe.specs.binarytranslation.generic.GenericInstructionOperand;
import pt.up.fe.specs.binarytranslation.generic.InstructionData;

/**
 * Represents the hook into an instruction set. Its initialized with a list of {@link InstructionProperties} and all
 * types of instructions {@link AsmInstructionType} in that set, and can decode a given instruction's parsed fields into
 * useful data.
 * 
 * @author JoaoBispo
 *
 */
public class InstructionSet {

    private List<InstructionProperties> instList;
    private List<AsmInstructionType> codeTypes;
    private Map<AsmInstructionType, List<InstructionProperties>> typeLists;

    /*
     * Constructor of instruction set; set is constructed from list of instruction
     * implemented as an enum, which implements InstructionProperties
     */
    public InstructionSet(List<InstructionProperties> instList, List<AsmInstructionType> codeTypes) {
        this.instList = instList;
        this.codeTypes = codeTypes;
        this.typeLists = makeTypeLists();
    }

    /*
     * Gets only the instructions which match format of type "type"
     */
    private List<InstructionProperties> getTypeList(AsmInstructionType type) {
        return this.typeLists.get(type);
    }

    /*
     * Constructs a map where each key is a type of 
     * instruction format, and the list are the instructions of that format
     */
    private Map<AsmInstructionType, List<InstructionProperties>> makeTypeLists() {

        var ret = new HashMap<AsmInstructionType, List<InstructionProperties>>();

        // For each instruction format
        for (AsmInstructionType codetype : codeTypes) {
            var nlist = new ArrayList<InstructionProperties>();

            // For each instruction in the set, which fits that format, make the list
            for (InstructionProperties inst : instList) {
                if (inst.getCodeType() == codetype) {
                    nlist.add(inst);
                }
            }
            ret.put(codetype, nlist);
        }
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private String getName(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getName();
        }
        return "Unknown Instruction!";
        // TODO throw something here
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private List<InstructionType> getGenericType(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getGenericType();
        }
        // return InstructionType.unknownType;
        List<InstructionType> ret = new ArrayList<InstructionType>();
        ret.add(InstructionType.G_UNKN);
        return ret;
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private int getLatency(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getLatency();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Initializes field in constructor for MicroBlazeInstruction
     */
    private int getDelay(AsmInstructionData asmData) {
        for (InstructionProperties inst : getTypeList(asmData.getType())) {
            if (inst.getReducedOpCode() == asmData.getReducedOpcode())
                return inst.getDelay();
        }
        return -1; // TODO replace with exception
    }

    /*
     * Interpret field data given by parsers
     */
    public InstructionData process(AsmInstructionData fieldData) {

        String plainname = getName(fieldData);
        int latency = getLatency(fieldData);
        int delay = getDelay(fieldData);
        List<InstructionType> genericTypes = getGenericType(fieldData);
        List<GenericInstructionOperand> operands = fieldData.getOperands();

        return new InstructionData(plainname, latency, delay, genericTypes, operands);
    }
}
