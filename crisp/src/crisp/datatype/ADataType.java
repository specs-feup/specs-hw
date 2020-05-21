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

package crisp.datatype;

public abstract class ADataType implements DataType {

    protected TypeQualifier qualifier;
    protected int packedsize = 1, unpackedsize = 1;

    public ADataType() {
    }

    public ADataType(int sizes[]) {
        this.unpackedsize = sizes[0];
        this.packedsize = sizes[1];

        // TODO exception if basestype already has a packed or unpacked size
        //

        // exception if packedsize on types which do not permit it
    }

    @Override
    public int getTypePackedSize() {
        return this.packedsize;
    }

    @Override
    public int getTypeUnpackedSize() {
        return this.unpackedsize;
    }

    @Override
    public TypeQualifier getQualifier() {
        return this.qualifier;
    }
}
