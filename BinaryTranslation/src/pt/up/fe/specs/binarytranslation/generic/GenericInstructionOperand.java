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

package pt.up.fe.specs.binarytranslation.generic;

import pt.up.fe.specs.binarytranslation.InstructionOperand;
import pt.up.fe.specs.binarytranslation.InstructionOperandType;

/**
 * An instruction operand implementation that can be used by all ISAs, in theory This class can be promoted to an
 * abstract class if any ISAs need very specific treatment of data to implement the methods
 * 
 * @author NunoPaulino
 *
 */
public class GenericInstructionOperand implements InstructionOperand {

    private InstructionOperandType type;
    protected Integer value;

    public GenericInstructionOperand(InstructionOperandType type, Integer value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public Integer getType() {
        return this.getType();
    }

    @Override
    public String getRepresentation() {
        String ret = "";
        if (type == InstructionOperandType.register)
            ret = (type.getPrefix() + Integer.toString(this.value));

        else if (type == InstructionOperandType.immediate)
            ret = (type.getPrefix() + "0x" + Integer.toHexString(this.value));

        return ret;
    }
}
