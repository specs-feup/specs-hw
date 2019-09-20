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
 * Generic implementation of interface instruction.
 * 
 * @author JoaoBispo
 *
 */
class GenericInstruction implements Instruction {

    private final String address;
    private final String instruction;

    public GenericInstruction(String address, String instruction) {
        this.address = address;
        this.instruction = instruction;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return address + ": " + instruction;
    }

}
