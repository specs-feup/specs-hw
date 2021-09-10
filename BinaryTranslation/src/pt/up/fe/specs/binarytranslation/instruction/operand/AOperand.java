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

package pt.up.fe.specs.binarytranslation.instruction.operand;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedRegister;

/**
 * An instruction operand implementation that can be used by all ISAs, in theory This class can be promoted to an
 * abstract class if any ISAs need very specific treatment of data to implement the methods
 * 
 * @author NunoPaulino
 *
 */
public abstract class AOperand implements Operand {

    /*
     * members
     */
    // protected Number registerIDasNumber; // "value" is the numeric identifier of the register, NOT its contents
    // protected String registerIDasString; // this is the string representation of the register
    protected final OperandProperties props;
    // protected finalNumber dataValue;
    protected final ExecutedRegister containerRegister;
    // the methods to generate the register representation should be contained in the "Register" class!

    /*
     * Constructor
     */
    public AOperand(OperandProperties props, ExecutedRegister containerRegister) {
        this.props = props;
        this.containerRegister = containerRegister;
        /*        this.registerIDasNumber = value;
        this.setStringValue(value);*/
    }

    /*
     * Copy constructors
     */
    protected AOperand(AOperand other) {
        // this(other.getProperties().copy(), other.getNumberValue(), other.getDataValue());
        this(other.props, other.containerRegister);
    }

    @Override
    public ExecutedRegister getContainerRegister() {
        return containerRegister;
    }

    @Override
    public AsmField getAsmField() {
        return this.containerRegister.getAsmField();
    }

    /*
    @Override
    public OperandProperties getProperties() {
        return this.props;
    }*/

    @Override
    public Number getDataValue() {
        return this.containerRegister.getDataValue();
    }

    /*
    @Override
    public Number getNumberValue() {
        return this.containerRegister.getRegisterDefinition()
    }*/

    @Override
    public String getStringValue() {
        return this.containerRegister.getRegisterDefinition().getName();
    }

    /*
    @Override
    public void setNumberValue(Number value) {
        this.registerIDasNumber = value;
    }
    
    @Override
    public void setStringValue(String svalue) {
        this.registerIDasString = svalue;
    
        // assume integer radix...
        this.registerIDasNumber = Integer.valueOf(svalue);
    }
    
    @Override
    public void setStringValue(Number value) {
    
        // registers are decimals
        if (this.isRegister()) {
            this.registerIDasString = Integer.toString(value.intValue());
        }
    
        // immediates are hexes (automatically chooses byte width from bit width)
        else {
            if (value instanceof Long || value instanceof Integer)
                this.registerIDasString = String.format("%x", value);
    
            else if (value instanceof Float)
                this.registerIDasString = String.format("%f", value);
        }
    }
    */

    /*
     * necessary because some registers cannot be represented with numbers
     * (e.g., "SF" for ARM)
     
    public AOperand(OperandProperties props, String value) {
        this.props = props;
        this.registerIDasString = value;
        // this.value = -1L;
    }*/

    ///////////////////////////////////////////////////////////// Properties //
    @Override
    public Boolean isRegister() {
        return (this.props.getType() == OperandType.REGISTER);
    }

    @Override
    public Boolean isImmediate() {
        return (this.props.getType() == OperandType.IMMEDIATE);
    }

    @Override
    public Boolean isSymbolic() {
        return (this.props.getType() == OperandType.SYMBOLIC);
    }

    @Override
    public Boolean isRead() {
        return (this.props.getAccessType() == OperandAccessType.READ);
    }

    @Override
    public Boolean isWrite() {
        return (this.props.getAccessType() == OperandAccessType.WRITE);
    }

    @Override
    public Boolean isFloat() {
        return (this.props.getDataType() == OperandDataType.SCALAR_FLOAT);
    }

    @Override
    public Boolean isSubOperation() {
        return (this.props.getType() == OperandType.SUBOPERATION);
    }

    @Override
    public Boolean isSpecial() {
        return (this.props.getType() == OperandType.SPECIAL);
    }

    /*
     * Get current representation of operand
     */
    @Override
    public String getRepresentation() {
        return (props.getPrefix() + this.getStringValue() + props.getSuffix());
    }

    /*
     * Get what the operand WOULD look like if symbolified 
     
    @Override
    public String getPossibleSymbolicRepresentation(String val) {
        return (props.getSymbolicPrefix() + val + props.getSymbolicSuffix());
    }
    
    ////////////////////////////////////////////////////////////////// Utils //
    
    /*
     * Symbolify instruction
     
    @Override
    public void setSymbolic(String svalue) {
        this.props.setSymbolic();
        this.svalue = svalue;
        // this.value = -1;
    }
    */

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
