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

import static pt.up.fe.specs.binarytranslation.instruction.OperandType.*;

/**
 * An instruction operand implementation that can be used by all ISAs, in theory This class can be promoted to an
 * abstract class if any ISAs need very specific treatment of data to implement the methods
 * 
 * @author NunoPaulino
 *
 */
public abstract class AOperand implements Operand {

    protected Integer value;
    protected String svalue; // used for symbolic representation (TODO refactor to another class? e.g., SymbolicOperand)
    protected OperandProperties props;

    public AOperand(OperandProperties props, Integer value) {
        this.props = props;
        this.value = value;
        this.svalue = Integer.toHexString(this.value);
    }

    public Integer getIntegerValue() {
        return this.value;
    }

    public String getStringValue() {
        return this.svalue;
    }

    public Boolean isRegister() {
        return (this.props.getTypes().contains(REGISTER));
    }

    public Boolean isImmediate() {
        return (this.props.getTypes().contains(IMMEDIATE));
    }

    public Boolean isSymbolic() {
        return (this.props.getTypes().contains(SYMBOLIC));
    }

    public Boolean isRead() {
        return (this.props.getTypes().contains(READ));
    }

    public Boolean isWrite() {
        return (this.props.getTypes().contains(WRITE));
    }

    public OperandProperties getProperties() {
        return this.props;
    }

    public String getRepresentation() {
        return (props.getPrefix() + this.getStringValue() + props.getSuffix());
    }

    public Boolean isEqual(Operand op) {
        return (this.props.getName() == op.getProperties().getName())
                && (this.getIntegerValue() == op.getIntegerValue());
    }
}
