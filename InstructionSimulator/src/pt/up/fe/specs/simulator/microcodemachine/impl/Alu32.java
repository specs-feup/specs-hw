/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.simulator.microcodemachine.impl;

import pt.up.fe.specs.simulator.microcodemachine.Alu;

public class Alu32 implements Alu {

    @Override
    public String toBinaryString(Number value) {
        return Integer.toBinaryString(value.intValue());
    }

    @Override
    public Number parseBinaryString(String binaryValue) {
        return Integer.parseInt(binaryValue, 2);
    }

    @Override
    public Number or(Number operand1, Number operand2) {
        return operand1.intValue() | operand2.intValue();
    }

    // @Override
    // public boolean isBitSet(Number value, int bit) {
    // var binaryString = Integer.toBinaryString(value.intValue());
    //
    // // If bit value is the same as the size or larger than the size of the binary string, assume bit is not set
    // if (bit >= binaryString.length()) {
    // return false;
    // }
    //
    // var bitIndex = SpecsBits.fromLsbToStringIndex(bit, binaryString.length());
    //
    // var bitChar = binaryString.charAt(bitIndex);
    //
    // if (bitChar == '0') {
    // return false;
    // }
    //
    // if (bitChar == '1') {
    // return true;
    // }
    //
    // throw new RuntimeException("Character not expected, only 0 or 1: " + bitChar);
    // }
    //
    // @Override
    // public Number signExtend(Number value, int signalBit) {
    // var binaryValue = Integer.toBinaryString(value.intValue());
    //
    // // If signal bit not represented in the binary value, not extension is needed
    // if (signalBit >= binaryValue.length()) {
    // return value;
    // }
    //
    // var extendedValue = SpecsBits.signExtend(binaryValue, signalBit);
    // return Integer.parseInt(extendedValue, 2);
    // }

}
