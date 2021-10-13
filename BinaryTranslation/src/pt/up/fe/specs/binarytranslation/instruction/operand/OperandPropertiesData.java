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
 
package pt.up.fe.specs.binarytranslation.instruction.operand;

import java.util.List;

public interface OperandPropertiesData {

    /*
     * returns generic operandtype
     */
    public List<OperandType> getTypes();

    /*
     * Should return a main archetype (i.e. REGISTER or IMMEDIATE)
     */
    public OperandType getMainType();

    /*
     * Gets the human readable prefix of the operand, e.g. "r", or "q"
     */
    public String getPrefix();

    /*
     * Gets the human readable suffix of the operand
     */
    public String getSuffix();

    /*
     * Width of register or immediate operand
     */
    public int getWidth();
}
