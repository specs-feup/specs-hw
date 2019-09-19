/*
 * Copyright 2011 SPeCS Research Group.
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
 * Represents a simple instruction: an integer for the address and a string for the instruction.
 *
 * @author Joao Bispo
 */
public interface SimpleInstruction32 extends Instruction {

    int getAddress();

    @Override
    default String getAddressAsString() {
        return Integer.toString(getAddress());
    }

}