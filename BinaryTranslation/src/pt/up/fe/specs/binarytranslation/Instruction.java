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

/**
 * Represents a generic assembly instruction.
 * 
 * @author JoaoBispo
 *
 */
public interface Instruction {

    String getAddressAsString();

    String getInstruction();

    /**
     * 
     * @param instruction
     * @return the value 0 if the address of the given instruction is equal to the address of this instruction; a value
     *         less than 0 if the address of this instruction is less than the address of given instruction; and a value
     *         greater than 0 if the address of this instruction is greater than the address of given instruction.
     */
    default int compareAddr(Instruction instruction) {
        return getAddressAsString().compareTo(instruction.getAddressAsString());
    }
}
