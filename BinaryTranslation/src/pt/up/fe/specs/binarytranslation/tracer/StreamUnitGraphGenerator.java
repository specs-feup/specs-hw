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
 
package pt.up.fe.specs.binarytranslation.tracer;

import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public class StreamUnitGraphGenerator {

    /*
     * 
     */
    private StreamUnitGenerator tracer;

    public StreamUnitGraphGenerator(TraceInstructionStream istream) {
        this.tracer = new StreamUnitGenerator(istream);
    }

    /*
     * 
     */
    private void skipTo(Number startAddr) {

        this.tracer.advanceTo(startAddr.longValue());
        // return tracer.nextInstruction();

        /*
        // advance until start of kernel
        StreamUnit next = null;
        do {
            next = tracer.nextInstruction();
        } while (next.getStart().getAddress().longValue() != startAddr.longValue());
        
        return next;*/
    }

    /*
     * 
     */
    public StreamUnitGraph generateInstructionGraph() {
        return this.generateGraph((v) -> v.nextInstruction(), 0x00000000, 0xFFFFFFFF);
    }

    /*
     * 
     */
    public StreamUnitGraph generateInstructionGraph(Number startAddr, Number stopAddr) {
        return this.generateGraph((v) -> v.nextInstruction(), startAddr, stopAddr);
    }

    /*
     * 
     */
    public StreamUnitGraph generateBasicBlockGraph() {
        return this.generateGraph((v) -> v.nextBasicBlock(), 0x00000000, 0xFFFFFFFF);
    }

    /*
     * 
     */
    public StreamUnitGraph generateBasicBlockGraph(Number startAddr, Number stopAddr) {
        return this.generateGraph((v) -> v.nextBasicBlock(), startAddr, stopAddr);
    }

    /*
     * 
    
    public StreamUnitGraph generateSuperBlockGraph() {
        return this.generateGraph((v) -> v.nextSuperBlock(), 0x00000000, 0xFFFFFFFF);
    }
    
    public StreamUnitGraph generateSuperBlockGraph(Number startAddr, Number stopAddr) {
        return this.generateGraph((v) -> v.nextSuperBlock(), startAddr, stopAddr);
    }
    */

    /*
     * 
     */
    private StreamUnitGraph generateGraph(
            Function<StreamUnitGenerator, StreamUnit> unitGetter,
            Number startAddr, Number stopAddr) {

        // basic blocks
        var tgraph = new StreamUnitGraph();

        // skip to start
        // var first = this.skipTo(startAddr);
        // tgraph.insert(first);
        if (startAddr.longValue() > 0)
            this.skipTo(startAddr);

        while (tracer.hasNext()) {
            var next = unitGetter.apply(this.tracer);
            tgraph.insert(next);
            if (next.getEnd().getAddress().longValue() == stopAddr.longValue())
                break;
        }

        return tgraph;
    }
}
