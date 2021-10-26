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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.ASegmentDataFlowGraph;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dependency.DependencyGraph;

public class ListScheduler {
    private DependencyGraph graph;

    public ListScheduler(DependencyGraph g) {
        this.graph = g;
        setPriorities();
    }

    public Schedule scheduleWithDiscreteResources(int alus, int memoryPorts) {
        int cycle = 0;
        var ready = new ArrayList<BtfVertex>();
        var active = new ArrayList<BtfVertex>();
        var done = new ArrayList<BtfVertex>();
        ready.addAll(findRoots());

        var sched = new HashMap<BtfVertex, Integer>();
        var availableAlus = alus;
        var availableMemPorts = memoryPorts;

        // Discrete resource tracker
        var fullSched = new Schedule(alus, memoryPorts, graph.vertexSet());

        while (ready.size() != 0 || active.size() != 0) {
            var toRemove = new ArrayList<BtfVertex>();
            for (var op : active) {
                if (sched.get(op) + op.getLatency() <= cycle) {
                    toRemove.add(op);
                    if (op.getType() == BtfVertexType.MEMORY) {
                        availableMemPorts++;
                        fullSched.dequeueMemoryPortOp(op);
                    }
                    if (op.getType() == BtfVertexType.OPERATION) {
                        availableAlus++;
                        fullSched.dequeueAluOp(op);
                    }
                    done.add(op);

                    for (var s : Graphs.successorListOf(graph, op)) {
                        if (s.getType() == BtfVertexType.MEMORY || s.getType() == BtfVertexType.OPERATION) {
                            var pred = Graphs.predecessorListOf(graph, s);
                            var allAncestorsDone = true;
                            for (var p : pred) {
                                if (!done.contains(p))
                                    allAncestorsDone = false;
                            }
                            if (allAncestorsDone) {
                                var opIsActive = active.contains(s);
                                var opIsReady = ready.contains(s);
                                if (!opIsActive && !opIsReady)
                                    ready.add(s);
                            }
                        }
                    }
                }
            }
            // Remove duplicates from the ready list
            ready = new ArrayList<>(new HashSet<>(ready));

            active.removeAll(toRemove);
            sortPriorityList(active);
            sortPriorityList(ready);
            toRemove = new ArrayList<BtfVertex>();

            for (var op : ready) {
                if (op.getLatency() == 0) {
                    toRemove.add(op);
                    sched.put(op, cycle);
                    active.add(op);
                }
                if (op.getType() == BtfVertexType.MEMORY) {
                    if (availableMemPorts > 0) {
                        toRemove.add(op);
                        sched.put(op, cycle);
                        active.add(op);
                        availableMemPorts--;

                        // Add to an available discrete resource
                        fullSched.enqueueMemoryPortOp(op);
                    }
                }
                if (op.getType() == BtfVertexType.OPERATION) {
                    if (availableAlus > 0) {
                        toRemove.add(op);
                        sched.put(op, cycle);
                        active.add(op);
                        availableAlus--;

                        // Add to an available discrete resource
                        fullSched.enqueueAluOp(op);
                    }
                }
            }
            ready.removeAll(toRemove);
            sortPriorityList(ready);
            sortPriorityList(active);
            fullSched.endCycle();
            cycle++;
        }

        return fullSched;
    }

    public int getScheduleLength(Map<BtfVertex, Integer> sched) {
        int max = 0;
        BtfVertex maxVertex = null;

        for (var k : sched.keySet()) {
            var val = sched.get(k);
            if (val > max) {
                max = val;
                maxVertex = k;
            }
        }
        int total = max + maxVertex.getLatency();
        return total;
    }

    public List<BtfVertex> findRoots() {
        var sources = new ArrayList<BtfVertex>();

        for (var v : graph.vertexSet()) {
            if (graph.inDegreeOf(v) == 0 && graph.outDegreeOf(v) != 0)
                sources.add(v);
        }
        return sources;
    }

    public void setPriorities() {
        var vx = graph.vertexSet();
        for (var v : vx) {
            v.setPriority(calculatePriority(v));
        }
    }

    private void sortPriorityList(List<BtfVertex> list) {
        Collections.sort(list, new Comparator<BtfVertex>() {
            @Override
            public int compare(BtfVertex lhs, BtfVertex rhs) {
                var lp = lhs.getPriority();
                var rp = rhs.getPriority();
                if (lp > rp)
                    return -1;          // -1 gives priority to lhs, 1 otherwise
                else if (lp < rp)
                    return 1;
                else {
                    int res = tieBreakerSuccessors(lhs, rhs);
                    if (res == 0)
                        res = tieBreakerLatency(lhs, rhs);
                    return res;
                }
            }
        });
    }
    
    private int tieBreakerSuccessors(BtfVertex lhs, BtfVertex rhs) {
        int lp = graph.outDegreeOf(lhs);
        int rp = graph.outDegreeOf(rhs);
        
        if (lp > rp)
            return -1;
        if (lp < rp) 
            return 1;
        return 0;
    }
    
    @Deprecated
    private int tieBreakerDescendants(BtfVertex lhs, BtfVertex rhs) {
        return 0;
    }
    
    private int tieBreakerLatency(BtfVertex lhs, BtfVertex rhs) {
        int lp = lhs.getLatency();
        int rp = rhs.getLatency();
        
        if (lp > rp)
            return -1;
        if (lp < rp) 
            return 1;
        return 0;
    }

    private int calculatePriority(BtfVertex v) {
//        var dfcp = new DataFlowCriticalPath(graph);
//        var path = dfcp.calculatePath(v, dfcp.findSinks());
//        var res1 =  dfcp.pathSize(path);
        
        
        var calc = new PriorityCalculator(graph);
        var res2 = calc.calculatePriority(v);
        
//        if (res1 != res2)
//            System.out.println("[" + res1 + " = " + res2 + "]");
        return res2;
    }
}
