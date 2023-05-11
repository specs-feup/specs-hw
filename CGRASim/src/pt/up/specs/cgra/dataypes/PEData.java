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

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Required methods for any data type which is meant to be used as data to be processed by a @ProcessingElement Any
 * PEData should only be attributed a value ONCE
 * 
 * @author nuno
 *
 */

public interface PEData {

    public default PEData passNull(PEData operandB) {
        return null;
    }

    public default PEData add(PEData operandB) {
        throw new NotImplementedException("PEData: add not implemented");
    }

    public default PEData sub(PEData operandB) {
        throw new NotImplementedException("PEData: sub not implemented");
    }

    public default PEData mul(PEData operandB) {
        throw new NotImplementedException("PEData: mul not implemented");
    }

    public default PEData div(PEData operandB) {
        throw new NotImplementedException("PEData: div not implemented");
    }

    public default PEData lshift(PEData operandB) {
        throw new NotImplementedException("PEData: lshift not implemented");
    }

    public default PEData rshift(PEData operandB) {
        throw new NotImplementedException("PEData: rshift not implemented");
    }

    public default PEData and(PEData operandB) {
        throw new NotImplementedException("PEData: and not implemented");
    }

    public default PEData or(PEData operandB) {
        throw new NotImplementedException("PEData: or not implemented");
    }

    public default PEData xor(PEData operandB) {
        throw new NotImplementedException("PEData: xor not implemented");
    }

    public default PEData passl(PEData operandB) {
        throw new NotImplementedException("PEData: passl not implemented");
    }

    public default PEData passr(PEData operandB) {
        throw new NotImplementedException("PEData: passr not implemented");
    }

    public default PEData slt(PEData operandB) {
        throw new NotImplementedException("PEData: slt not implemented");
    }

    public default PEData seq(PEData operandB) {
        throw new NotImplementedException("PEData: seq not implemented");
    }

    public default Number getValue() {
        throw new NotImplementedException("PEData: getValue not implemented");
    }

    public default PEData copy() {
        throw new NotImplementedException("PEData: copy not implemented");
    }

    @Override
    public String toString();

    // public PEData partSelect(PEData operandB);
}
