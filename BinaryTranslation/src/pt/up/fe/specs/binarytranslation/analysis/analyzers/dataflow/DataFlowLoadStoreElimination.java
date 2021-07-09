/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.isomorphism.VF2GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.address.AddressGraph;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public class DataFlowLoadStoreElimination {
    private List<Instruction> insts;

    public DataFlowLoadStoreElimination(List<Instruction> insts) {
        this.insts = insts;
    }
    
    public String compareAllPairs() {
        var res = new ArrayList<String>();
        var pairs = findLoadStorePairs();
        
        for (var store : pairs.keySet()) {
            for (var load : pairs.get(store)) {
                var r = compareMemoryAccesses(store, load);
                if (r.length() > 0)
                    res.add(r);
            }
        }
        return String.join(",", res);
    }
    
    private String compareMemoryAccesses(Instruction store, Instruction load) {
        System.out.println("Comparing memory accesses " + store.getRepresentation() + " and " + load.getRepresentation());
        var sGraph = new AddressGraph(insts, store);
        var lGraph = new AddressGraph(insts, load);
        var iso = new VF2GraphIsomorphismInspector<BtfVertex, DefaultEdge>(sGraph, lGraph,
                new VertexComparator(), new EdgeComparator());
        if (iso.isomorphismExists())
            return "[" + store.getRepresentation() + "|" + load.getRepresentation() + "]";
        else
            return "";
    }

    private Map<Instruction, List<Instruction>> findLoadStorePairs() {
        var res = new HashMap<Instruction, List<Instruction>>();
        
        for (var inst : insts) {
            if (inst.isStore()) {
                int idx = insts.indexOf(inst);
                var loads = new ArrayList<Instruction>();
                
                for (var i = 0/*idx + 1*/; i < insts.size(); i++) {
                    if (i == idx)
                        continue;
                    var next = insts.get(i);
                    if (next.isLoad())
                        loads.add(next);
                }
                res.put(inst, loads);
            }
        }
        return res;
    }
    
    private class VertexComparator implements Comparator<BtfVertex> {
        @Override
        public int compare(BtfVertex o1, BtfVertex o2) {
            var label1 = o1.getLabel();
            var label2 = o2.getLabel();
            return label1.equals(label2) ? 0 : -1;
        }
    }

    private class EdgeComparator implements Comparator<DefaultEdge> {
        @Override
        public int compare(DefaultEdge e1, DefaultEdge e2) {
            return 0;
        }
    }
}
