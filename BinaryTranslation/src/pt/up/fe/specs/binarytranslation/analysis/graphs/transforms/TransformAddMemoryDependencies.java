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

package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;

public class TransformAddMemoryDependencies extends AGraphTransform {

    public TransformAddMemoryDependencies(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        var memOps = GraphUtils.findAllNodesOfType(g, BtfVertexType.MEMORY);
        Collections.sort(memOps, new LoadStoreOrderComparator());

        // Old RAW method
        // BtfVertex lastStore = BtfVertex.nullVertex;
        // for (var v : memOps) {
        // if (v.getLabel().equals("Load")) {
        // if (lastStore != BtfVertex.nullVertex) {
        // g.addEdge(lastStore, v);
        // }
        // }
        // else {
        // lastStore = v;
        // }
        // }
        
        for (var op : memOps) {
            // Add RAW and WAR
            var succList = getSuccessorsOfOpposingType(memOps, op);
            for (var succ : succList)
                g.addEdge(op, succ);
            
            // Add WAW
            if (op.getLabel().equals("Store")) {
                succList = getSucceedingStores(memOps, op);
                for (var succ : succList)
                    g.addEdge(op, succ);
            }
        }
        
        return g;
    }

    private List<BtfVertex> getSucceedingStores(ArrayList<BtfVertex> nodes, BtfVertex start) {
        var res = new ArrayList<BtfVertex>();
        var idx = start.getLoadStoreOrder();
        for (var i = idx; i < nodes.size(); i++) {
            var n = nodes.get(i);
            if (n.getLabel().equals("Store"))
                res.add(n);
        }
        return res;
    }

    private List<BtfVertex> getSuccessorsOfOpposingType(List<BtfVertex> nodes, BtfVertex start) {
        var res = new ArrayList<BtfVertex>();
        var idx = start.getLoadStoreOrder();
        for (var i = idx; i < nodes.size(); i++) {
            var n = nodes.get(i);
            if ((n.getLabel().equals("Load") && !start.getLabel().equals("Load")) || (!n.getLabel().equals("Load") && start.getLabel().equals("Load")))
                res.add(n);
        }
        return res;
    }

    class LoadStoreOrderComparator implements Comparator<BtfVertex> {

        @Override
        public int compare(BtfVertex o1, BtfVertex o2) {
            int order1 = o1.getLoadStoreOrder();
            int order2 = o2.getLoadStoreOrder();
            return order1 > order2 ? 1 : order1 < order2 ? -1 : 0;
        }

    }

}
