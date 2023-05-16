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

package pt.up.specs.cgra.structure.pes.binary;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public class AdderElement extends BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?

    public AdderElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public AdderElement(int latency) {
        this(latency, 0);
    }

    public AdderElement() {
        this(1, 0);
    }

    @Override
    protected PEData _execute() {
        return this.getOperand(0).add(this.getOperand(1));
    }

    @Override
    public ProcessingElement copy() {
        return new AdderElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    protected AdderElement getThis() {
        return this;
    }

    @Override
    public String toString() {
        return "Adder";
    }
}