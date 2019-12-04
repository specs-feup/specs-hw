/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.SimpleInstruction;

public class HashedSequence {

    private Number addr;
    private Map<String, String> regremap;
    private List<SimpleInstruction> instlist;

    HashedSequence(List<Instruction> instlist, Number addr, Map<String, String> regremap) {

        // deep copy
        this.instlist = new ArrayList<SimpleInstruction>();
        for (Instruction i : instlist)
            this.instlist.add(new SimpleInstruction(i));

        this.addr = addr;
        this.regremap = regremap;
    }

    public List<SimpleInstruction> getInstlist() {
        return instlist;
    }

    public Map<String, String> getRegremap() {
        return regremap;
    }

    public Number getAddr() {
        return addr;
    }
}
