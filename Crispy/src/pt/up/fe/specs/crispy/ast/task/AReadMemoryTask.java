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
import pt.up.fe.specs.crispy.ast.declaration.ArrayDeclaration;

public abstract class AReadMemoryTask extends HardwareTask {

    private String fileName;
    private ArrayDeclaration array;

    private Number startAddress;
    private Number endAddress;

    // TODO: memory file should be passed as a File which is verified to exist
    // and not as a String

    protected AReadMemoryTask(String fileName, ArrayDeclaration array,
            Number startAddress, Number endAddress, HardwareNodeType type) {
        super(type);
        this.fileName = fileName;
        this.array = array;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    protected AReadMemoryTask(String fileName, ArrayDeclaration array, HardwareNodeType type) {
        this(fileName, array, null, null, type);
    }

    public String getFileName() {
        return this.fileName;
    }

    public ArrayDeclaration getArray() {
        return this.array;
    }

    public Number getStartAddress() {
        return this.startAddress;
    }

    public Number getEndAddress() {
        return this.endAddress;
    }

    public boolean hasBoundaries() {
        return ((this.getStartAddress() != null) && (this.getEndAddress() != null));
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        builder.append("(");
        builder.append("\"" + this.getFileName() + "\",");
        builder.append(this.getArray().getVariable().getVariableName());

        if (this.hasBoundaries()) {
            builder.append("," + String.valueOf(this.getStartAddress()));
            builder.append("," + String.valueOf(this.getEndAddress()));
        }

        builder.append(")");
        return builder.toString();
    }
}
