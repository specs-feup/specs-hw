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

import static pt.up.fe.specs.binarytranslation.OperandType.*;

import pt.up.fe.specs.binarytranslation.Operand;
import pt.up.fe.specs.binarytranslation.OperandProperties;

/**
 * An instruction operand implementation that can be used by all ISAs, in theory This class can be promoted to an
 * abstract class if any ISAs need very specific treatment of data to implement the methods
 * 
 * @author NunoPaulino
 *
 */
public abstract class AOperand implements Operand {

    protected Integer value;
    protected OperandProperties props;

    public AOperand(OperandProperties props, Integer value) {
        this.props = props;
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean isRegister() {
        return (this.props.getTypes().contains(REGISTER));
    }

    public Boolean isImmediate() {
        return (this.props.getTypes().contains(IMMEDIATE));
    }

    public OperandProperties getProperties() {
        return this.props;
    }

    @Override
    public String getRepresentation() {
        String ret = "";
        if (this.isRegister())
            ret = (props.getPrefix() + Integer.toString(this.value)) + props.getSuffix();

        else if (this.isImmediate())
            ret = (props.getPrefix() + Integer.toHexString(this.value)) + props.getSuffix();

        return ret;
    }
}
