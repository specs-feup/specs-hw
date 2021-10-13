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
 
package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public class TraceBasicBlock extends ABasicBlock {

    /*
     * Addr, Frequency Count
     */
    private Map<Integer, Integer> startAddresses;

    /*
     * 
     */
    public TraceBasicBlock(List<Instruction> ilist,
            List<SegmentContext> contexts, Application appinfo) {
        super(ilist, contexts, appinfo);
        this.segtype = BinarySegmentType.TRACE_BASIC_BLOCK;
        this.startAddresses = new HashMap<Integer, Integer>();
        for (SegmentContext context : contexts) {
            this.startAddresses.put(context.getStartaddresses(), context.getOcurrences());
        }
    }

    @Override
    public List<Integer> getAddresses() {
        return new ArrayList<Integer>(this.startAddresses.keySet());
    }

    @Override
    protected String getAddressListRepresentation() {
        String ret = "";
        for (Map.Entry<Integer, Integer> entry : this.startAddresses.entrySet()) {
            var addr = entry.getKey();
            var count = entry.getValue();
            ret += "0x" + Integer.toHexString(addr) + "(" + count + ")" + " ";
        }
        ret += "] " + "Total execution cycles: " + this.getExecutionCycles() + "\n";
        return ret;
    }
}
