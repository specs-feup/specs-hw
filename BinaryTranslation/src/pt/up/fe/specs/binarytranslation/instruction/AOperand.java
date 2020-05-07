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

import java.io.Serializable;

import pt.up.fe.specs.binarytranslation.parsing.AsmField;

/**
 * An instruction operand implementation that can be used by all ISAs, in theory This class can be promoted to an
 * abstract class if any ISAs need very specific treatment of data to implement the methods
 * 
 * @author NunoPaulino
 *
 */
public abstract class AOperand implements Operand, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7403586195612171177L;

    /*
     * members
     */
    protected Number value;
    protected String svalue;
    protected OperandProperties props;

    /*
     * Constructor
     */
    public AOperand(OperandProperties props, Number value) {
        this.props = props;
        this.value = value;
        this.setStringValue(value);
    }

    public void setStringValue(Number value) {

        // registers are decimals
        if (this.isRegister()) {
            this.svalue = Integer.toString(this.value.intValue());
        }

        // immediates are hexes (automatically chooses byte width from bit width)
        else {
            if (value instanceof Long || value instanceof Integer)
                this.svalue = String.format("%x", value);

            else if (value instanceof Float)
                this.svalue = String.format("%f", value);
        }
    }

    /*
     * necessary because some registers cannot be represented with numbers
     * (e.g., "SF" for ARM)
     */
    public AOperand(OperandProperties props, String value) {
        this.props = props;
        this.svalue = value;
        this.value = -1L;
    }

    public Number getValue() {
        return this.value;
    }

    public String getStringValue() {
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

    public Boolean isSubOperation() {
        return (this.props.getTypes().contains(SUBOPERATION));
    }

    public Boolean isSpecial() {
        return (this.props.getTypes().contains(SPECIAL));
    }

    public AsmField getAsmField() {
        return this.props.getAsmField();
    }

    public OperandProperties getProperties() {
        return this.props;
    }

    /*
     * Get current representation of operand
     */
    public String getRepresentation() {
        return (props.getPrefix() + this.getStringValue() + props.getSuffix());
    }

    /*
     * Get what the operand WOULD look like if symbolified 
     */
    public String getPossibleSymbolicRepresentation(String val) {
        return (props.getSymbolicPrefix() + val + props.getSymbolicSuffix());
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
     * Override operand value (currently only works for overriding non symbolic operands...)
     */
    public void overrideValue(Number value) {
        this.value = value;
        this.setStringValue(value);
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
