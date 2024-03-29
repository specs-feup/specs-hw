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

package pt.up.specs.cgra.dataypes;

public class PEInteger implements PEData {

    int value = 0;

    public PEInteger(int value) {
        this.value = value;
    }

    @Override
    public PEData copy() {
        return new PEInteger(this.value);
    }

    // @Override
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public PEData add(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value + intB.getValue());
    }

    @Override
    public PEData sub(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value - intB.getValue());
    }

    @Override
    public PEData mul(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value * intB.getValue());
    }

    @Override
    public PEData div(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value / intB.getValue());
    }

    @Override
    public PEData lshift(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value << intB.getValue());
    }

    @Override
    public PEData rshift(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value >> intB.getValue());
    }

    /*@Override
    public PEData mod(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value % intB.getValue());
    
    }*/

    @Override
    public PEData and(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value & intB.getValue());

    }

    @Override
    public PEData or(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value | intB.getValue());

    }

    @Override
    public PEData xor(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value ^ intB.getValue());

    }

    @Override
    public PEData slt(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEBoolean(this.value < intB.value ? true : false);
    }

    @Override
    public PEData seq(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEBoolean(this.value == intB.value ? true : false);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PEInteger))
            return false;

        else if (obj == this)
            return true;

        return (this.value == ((PEInteger) obj).value);// TODO: unsafe
    }

    /*
    @Override
    public PEData partSelect(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value >> intB.getValue());
    }*/
}
