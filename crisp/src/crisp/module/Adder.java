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

package crisp.module;

import crisp.data.Data;
import crisp.datatype.*;
import crisp.port.*;

public class Adder extends Module {

    private Adder(String name, Port... ports) {
        super(ports);
        this.name = name;
    }

    public static Adder newInstance() {
        var p1 = new PlainPort(PortDirection.input, new Data(PrimitiveTypes.logic, "indat"));
        var p2 = new PlainPort(PortDirection.output, new Data(PrimitiveTypes.logic, "oudat"));
        return new Adder("adder", p1, p2);
    }

    public static Adder newInstance(DataType type) {
        var p1 = new PlainPort(PortDirection.input, new Data(type, "indat"));
        var p2 = new PlainPort(PortDirection.output, new Data(type, "oudat"));
        return new Adder("adder", p1, p2);
    }

    // method getInstantiation??
}
