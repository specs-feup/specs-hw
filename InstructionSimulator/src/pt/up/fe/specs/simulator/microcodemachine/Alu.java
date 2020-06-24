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

package pt.up.fe.specs.simulator.microcodemachine;

import pt.up.fe.specs.util.SpecsBits;

public interface Alu {

    /**
     * ORs two numbers.
     * 
     * @param operand1
     * @param operand2
     * @return
     */
    Number or(Number operand1, Number operand2);

    String toBinaryString(Number value);

    Number parseBinaryString(String binaryValue);

    /**
     * 
     * @param value
     * @param signalBit
     *            0-based, LSB order index of the signal bit.
     * @return
     */
    default Number signExtend(Number value, int signalBit) {
        var binaryValue = toBinaryString(value);

        // If signal bit not represented in the binary value, not extension is needed
        if (signalBit >= binaryValue.length()) {
            return value;
        }

        var extendedValue = SpecsBits.signExtend(binaryValue, signalBit);
        return parseBinaryString(extendedValue);
    }

    /**
     * 
     * @param value
     * @param signalBit
     *            0-based, LSB order index of the bit to check.
     * @return
     */
    default boolean isBitSet(Number value, int bit) {
        var binaryString = toBinaryString(value);

        // If bit value is the same as the size or larger than the size of the binary string, assume bit is not set
        if (bit >= binaryString.length()) {
            return false;
        }

        var bitIndex = SpecsBits.fromLsbToStringIndex(bit, binaryString.length());

        var bitChar = binaryString.charAt(bitIndex);

        if (bitChar == '0') {
            return false;
        }

        if (bitChar == '1') {
            return true;
        }

        throw new RuntimeException("Character not expected, only 0 or 1: " + bitChar);
    }

}
