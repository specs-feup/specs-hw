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
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.dataflow.DataFlowCriticalPath;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.dataflow.ASegmentDataFlowGraph;

public class ListScheduler {
    private ASegmentDataFlowGraph graph;

    public ListScheduler(ASegmentDataFlowGraph g) {
        this.graph = g;
    }

    public Map<BtfVertex, Integer> schedule(int alus, int memoryPorts) {
        var plist = getPriorityList();
        int cycle = 0;
        var ready = new ArrayList<BtfVertex>();
        var active = new ArrayList<BtfVertex>();
        ready.addAll(findRoots());

        var sched = new HashMap<BtfVertex, Integer>();
        var availableAlus = alus;
        var availableMemPorts = memoryPorts;
        var aluMap = new HashMap<BtfVertex, Integer>();
        var memMap = new HashMap<BtfVertex, Integer>();

        while (ready.size() != 0 || active.size() != 0) {
            var toRemove = new ArrayList<BtfVertex>();
            for (var op : active) {
                if (sched.get(op) + op.getLatency() <= cycle) {
                    toRemove.add(op);
                    if (op.getType() == BtfVertexType.MEMORY)
                        availableMemPorts++;
                    if (op.getType() == BtfVertexType.OPERATION)
                        availableAlus++;

                    for (var s : Graphs.successorListOf(graph, op)) {
                        if (s.getType() == BtfVertexType.MEMORY || s.getType() == BtfVertexType.OPERATION)
                            ready.add(s);
                        else
                            ready.addAll(Graphs.successorListOf(graph, s));
                    }
                }
            }
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
                        memMap.put(op, availableMemPorts);
                        availableMemPorts--;
                    }
                }
                if (op.getType() == BtfVertexType.OPERATION) {
                    if (availableAlus > 0) {
                        toRemove.add(op);
                        sched.put(op, cycle);
                        active.add(op);
                        aluMap.put(op, availableAlus);
                        availableAlus--;
                    }
                }
            }
            ready.removeAll(toRemove);
            sortPriorityList(ready);
            sortPriorityList(active);
            cycle++;
        }
        //printSchedule(sched, memMap, aluMap, alus, memoryPorts);
        return sched;
    }

    @Deprecated
    private void printSchedule(HashMap<BtfVertex, Integer> sched, HashMap<BtfVertex, Integer> memMap,
            HashMap<BtfVertex, Integer> aluMap, int alus, int memPorts) {
        var finalAlu = new HashMap<Integer, ArrayList<String>>();
        var finalMemPorts = new HashMap<Integer, ArrayList<String>>();
        var schedLength = getScheduleLength(sched);
        
        for (int i = 1; i <= alus; i++) {
            finalAlu.put(i, new ArrayList<String>(Collections.nCopies(schedLength, "--")));
        }
        for (int i = 1; i <= memPorts; i++) {
            finalMemPorts.put(i, new ArrayList<String>(Collections.nCopies(schedLength, "--")));
        }
        for (var k : sched.keySet()) {
            if (k.getType() == BtfVertexType.MEMORY) {
                var cycle = sched.get(k);
                var idx = memMap.get(k);
                var arr = finalMemPorts.get(idx);
                arr.set(cycle - 1, k.getLabel());
            }
            if (k.getType() == BtfVertexType.OPERATION) {
                var cycle = sched.get(k);
                var idx = aluMap.get(k);
                var arr = finalAlu.get(idx);
                arr.set(cycle - 1, k.getLabel());
            }
        }
        
        System.out.println("Schedule:");
        System.out.println("ALUs:");
        for (var k : finalAlu.keySet()) {
            System.out.println("ALU " + k + ": " + String.join("|", finalAlu.get(k)));
        }
        System.out.println("Memory Ports:");
        for (var k : finalMemPorts.keySet()) {
            System.out.println("MEM " + k + ": " + String.join("|", finalMemPorts.get(k)));
        }
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

    public List<BtfVertex> getPriorityList() {
        var vx = graph.vertexSet();
        for (var v : vx) {
            v.setPriority(calculatePriority(v));
        }

        var list = new ArrayList<BtfVertex>(vx);
        sortPriorityList(list);
        return list;
    }

    private void sortPriorityList(List<BtfVertex> list) {
        Collections.sort(list, new Comparator<BtfVertex>() {
            @Override
            public int compare(BtfVertex lhs, BtfVertex rhs) {
                var lp = lhs.getPriority();
                var rp = rhs.getPriority();
                if (lp > rp)
                    return -1;
                else if (lp < rp)
                    return 1;
                else
                    return 0;
            }
        });
    }

    private int calculatePriority(BtfVertex v) {
        var dfcp = new DataFlowCriticalPath(graph);
        var path = dfcp.calculatePath(v, dfcp.findSinks());
        return dfcp.pathSize(path);
    }
}
