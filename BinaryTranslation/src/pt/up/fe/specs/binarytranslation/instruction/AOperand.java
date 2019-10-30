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

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

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

        if (this.isSymbolic())
            return this.svalue;

        else if (this.isRegister())
            return Integer.toString(this.value);

        else if (this.isImmediate())
            return Integer.toHexString(this.value);

        else
            return this.svalue;
    }

    ///////////////////////////////////////////////////////////// Properties //
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

    public Boolean isFloat() {
        return (this.props.getTypes().contains(FLOAT));
    }

    public AsmField getAsmField() {
        return this.props.getAsmField();
    }

    public OperandProperties getProperties() {
        return this.props;
    }

    public String getRepresentation() {
        return (props.getPrefix() + this.getStringValue() + props.getSuffix());
    }
    ///////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////// Utils //

    /*
     * Symbolify instruction (currently only useful for static frequent sequences
     */
    public void setSymbolic(String value) {
        this.props.setSymbolic();
        this.svalue = value;
        this.value = -1;
        return;
    }
    /*
    public boolean equals(Operand op) {
        return this.equals((Object) op);
    }
    
    @Override
    public boolean equals(Object op) {
        if (!(op instanceof Operand))
            return false;
    
        Operand opr = (Operand) op;
        return (this.props.getName() == opr.getProperties().getName())
                && (this.getStringValue() == opr.getStringValue());
    }
    */
}
