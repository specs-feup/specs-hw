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

package pt.up.fe.specs.binarytranslation.hardware;

import java.util.*;

import pt.up.fe.specs.binarytranslation.graphs.GraphNode;
import pt.up.fe.specs.binarytranslation.hardware.component.HardwareComponent;
import pt.up.fe.specs.binarytranslation.instruction.InstructionType;

public class HardwareTranslator {

    private static final Map<InstructionType, String> TYPEMAP;
    static {
        var amap = new HashMap<InstructionType, String>();
        amap.put(InstructionType.G_ADD, "+");
        amap.put(InstructionType.G_SUB, "-");
        TYPEMAP = Collections.unmodifiableMap(amap);
    }

    /*
     * 
     
    public static HardwareComponent transform(GraphNode node) {
    
        // get the types
        var types = node.getInst().getProperties().getGenericType();
    
        // transform to statement type?
        // var stype =
    
        // create the statement
        // var statement = new PlainCode();
    }*/
}
