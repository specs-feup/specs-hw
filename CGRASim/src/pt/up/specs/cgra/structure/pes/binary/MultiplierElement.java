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
import pt.up.specs.cgra.structure.pes.PEType;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public class MultiplierElement extends BinaryProcessingElement {

    public MultiplierElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public MultiplierElement(int latency) {
        this(latency, 0);
    }

    public MultiplierElement() {
        this(1, 0);
    }

    @Override
    protected PEData _execute() {
        return this.getOperand(0).mul(this.getOperand(1));
    }

    @Override
    public ProcessingElement copy() {
        return new MultiplierElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    protected MultiplierElement getThis() {
        return this;
    }

    @Override
    public PEType getType() {
        return PEType.MultiplierElement;
    }
}
