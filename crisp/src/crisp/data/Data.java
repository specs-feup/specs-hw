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

package crisp.data;

import crisp.datatype.*;

public class Data {

    protected DataType type;
    protected String name;

    public Data(DataType type, String name) {
        this.name = name;
        this.type = type;
    }

    public DataType getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    private String getRange(int rangesize) {
        if (rangesize == 1)
            return "";
        else
            return " [ " + Integer.toString(rangesize - 1) + " : 0 ]";
    }

    public String represent() {
        return this.type.getTypeName() +
                this.getRange(this.type.getTypeUnpackedSize()) + " "
                + this.name + this.getRange(this.type.getTypePackedSize());
    }

    public String declare() {
        return this.represent() + ";";
    }
}
