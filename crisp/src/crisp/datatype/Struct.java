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

import java.util.*;

import crisp.data.Data;

public class Struct extends ADataType {

    private final Map<String, DataType> data;

    public Struct(String structname, HashMap<String, DataType> map) {
        super(structname);
        this.data = map;
        this.qualifier = TypeQualifier.struct;
    }

    public Map<String, DataType> getFields() {
        return this.data;
    }

    public DataType getField(String name) {
        return this.data.get(name);
    }

    @Override
    public String getTypeName() {
        return "struct " + super.getTypeName();
    }

    @Override
    public String define() {
        String ret = this.qualifier.toString() + " " + this.getTypeName() + " {\n";
        for (String name : this.data.keySet()) {
            ret += "\t" + this.data.get(name).define() + " " + name + ";\n";
        }
        ret += "};\n";
        return ret;
    }
}
