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

import java.util.List;

import pt.up.fe.specs.binarytranslation.asmparser.AsmInstructionType;

/**
 * Each instruction in an ISA is defined by properties. This class is to be implemented by an enum per ISA, which lists
 * all the instruction in the set, and implements the methods to retrieve properties about each instruction. This enum
 * is also used to initialize instances of {@link InstructionSet}, which automatically builds the necessary internals to
 * parse and decode any incoming instruction of that instruction set
 * 
 * @author NunoPaulino
 *
 */
public interface InstructionProperties {

    /*
     * helper method too look up the list
     */
    default int getLatency() {
        return 1;
    };

    /*
     * helper method too look up the list
     */
    default int getDelay() {
        return 1;
    };

    /*
     * helper method too get full opcode
     */
    public int getOpCode();

    /*
     * helper method too get only the bits that matter
     */
    public int getReducedOpCode();

    /*
     * helper method too look up type of instruction format
     */
    public AsmInstructionType getCodeType();

    /*
     * helper method too look up type in the list
     */
    public List<InstructionType> getGenericType();

    /*
     * helper method too look up name the list
     */
    public String getName();
    
    /*
     * Returns name of enum (should be unique)
     */
    public String getEnumName();
}
