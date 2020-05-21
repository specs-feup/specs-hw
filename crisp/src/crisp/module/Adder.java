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
import crisp.port.*;

public class Adder extends Module {

    private Adder(String name, Port... ports) {
        super(ports);
        this.name = name;
    }

    // public static Adder newInstance() {
    // var p1 = new PlainPort(PortDirection.input, new Data("indat", 32));
    // var p2 = new PlainPort(PortDirection.output, new Data("outdat", 32));
    // return new Adder("adder", p1, p2);
    // }

    /*
    // static??
    String getDefinition() {
    
        System.out.println("module adder(input fudata_input indat, output fudata_output outdat);");
    
        
        assign {outdat.carryBit, outdat.results[0]} = indat.operands[0] + indat.operands[1];
        assign outdat.results[1] = {'0, outdat.carryBit};
                endmodule
    }*/

    // method getInstantiation??
}
