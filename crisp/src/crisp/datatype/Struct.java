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

    private final List<Data> data;

    public Struct(String structname, List<Data> map) {
        super(structname);
        this.data = map;
        this.qualifier = TypeQualifier.struct;
    }

    public List<Data> getFields() {
        return this.data;
    }

    public Data getField(String name) {
        for (Data d : this.data) {
            if (d.getName() == name)
                return d;
        }
        return null;
    }

    @Override
    public String getTypeName() {
        return "struct " + super.getTypeName();
    }

    @Override
    public String define() {
        String ret = this.getTypeName() + " {\n";
        for (Data d : this.data) {
            ret += "\t" + d.declare() + "\n";
        }
        ret += "}";
        return ret;
    }
}
