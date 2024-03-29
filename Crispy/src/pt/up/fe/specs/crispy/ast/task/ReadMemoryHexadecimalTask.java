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

package pt.up.fe.specs.crispy.ast.task;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.operator.VArray;

public class ReadMemoryHexadecimalTask extends AReadMemoryTask {

    public ReadMemoryHexadecimalTask(String fileName,
            VArray array, Number startAddress, Number endAddress) {
        super(fileName, array, startAddress, endAddress, HardwareNodeType.ReadMemoryHexTask);
    }

    public ReadMemoryHexadecimalTask(String fileName, VArray array) {
        this(fileName, array, null, null);
    }

    private ReadMemoryHexadecimalTask(ReadMemoryHexadecimalTask other) {
        this(other.getFileName(), other.getArray().copy(), other.getStartAddress(), other.getEndAddress());
    }

    @Override
    protected ReadMemoryHexadecimalTask copyPrivate() {
        return new ReadMemoryHexadecimalTask(this);
    }

    @Override
    public ReadMemoryHexadecimalTask copy() {
        return (ReadMemoryHexadecimalTask) super.copy();
    }

    @Override
    public String getAsString() {
        return "$readmemh" + super.getAsString() + ";";
    }
}
