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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers.memory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.ocurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.AGraphTransform;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformBaseAddress;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.util.SpecsLogs;

@Deprecated
public class MemoryDisambiguator {

    private ArrayList<Graph<BtfVertex, DefaultEdge>> graphs;
    private HashMap<String, Integer> indVars;
    private RegisterProperties isaProps;
    private BasicBlockOccurrenceTracker tracker;
    private Class<?> transforms[] = {
            TransformBaseAddress.class,
            TransformHexToDecimal.class,
            TransformShiftsToMult.class
    };
    private Map<String, List<String>> prologueDeps;

    public MemoryDisambiguator(ArrayList<Graph<BtfVertex, DefaultEdge>> graphs,
            HashMap<String, Integer> indVars,
            RegisterProperties isaProps,
            BasicBlockOccurrenceTracker tracker, Map<String, List<String>> prologueDeps) {
        this.graphs = graphs;
        this.indVars = indVars;
        this.isaProps = isaProps;
        this.tracker = tracker;
        this.prologueDeps = prologueDeps;

        for (var graph : graphs) {
            applyTransforms(graph);
        }
    }

    private void applyTransforms(Graph<BtfVertex, DefaultEdge> graph) {
        for (var cl : transforms) {
            try {
                Constructor<?> cons = cl.getConstructor(cl);
                var trans = (AGraphTransform) cons.newInstance(graph);
                trans.applyToGraph();

            } catch (Exception e) {
                SpecsLogs.warn("Error message:\n", e);
            }
        }
    }

    public void disambiguate() {
        System.out.println("");
        var totalRegisters = new ArrayList<String>();
        var memoryFunctions = new ArrayList<String>();

        for (var graph : graphs) {
            String expr = MemoryAddressDetector.buildMemoryExpression(graph);
            System.out.println("Memory disambiguation for memory access " + expr + ":");

            // Get all registers used for address
            var registers = getGraphAddressRegisters(graph);
            var filtered = filterRegisterList(registers);
            System.out.println("Registers involved in address: " + filtered);

            // disambiguate
            System.out.println("TBD\n");

            // Add memory function to report later
            var fun = MemoryAddressDetector.buildAddressFunction(graph, registers, indVars);
            memoryFunctions.add(fun);

            // Add registers to basic bloc list for reporting
            totalRegisters.addAll(registers);
            System.out.println("");
        }

        // Report on all registers used for addresses in this basic block
        var res = totalRegisters.stream().distinct().collect(Collectors.toList());
        System.out.println("All registers involved in addresses in this basic block: " + res);

        // Report memory functions
        System.out.println("Memory access functions:");
        for (var fun : memoryFunctions)
            System.out.println(fun);
    }

    /**
     * Filters induction variables out of the register list
     * 
     * @param graph
     * @return
     */
    private ArrayList<String> filterRegisterList(List<String> registers) {
        var res = new ArrayList<String>();

        for (var reg : registers) {
            printRegisterProperties(reg);
            if (!indVars.containsKey(reg))
                res.add(reg);
        }
        return res;
    }

    private void printRegisterProperties(String reg) {
        var props = new ArrayList<String>();

        if (isaProps.isParameter(reg))
            props.add("Parameter");
        if (isaProps.isReturn(reg))
            props.add("Return value");
        if (isaProps.isStackPointer(reg))
            props.add("Stack pointer");
        if (isaProps.isTemporary(reg))
            props.add("Temporary value");
        if (isaProps.isZero(reg))
            props.add("Zero");
        if (indVars.containsKey(reg))
            props.add("Induction variable of stride " + indVars.get(reg));

        System.out.println(reg + ": " + String.join(", ", props));
    }

    /**
     * Finds all registers used on a memory address calculation
     * 
     * @param graph
     * @return
     */
    private List<String> getGraphAddressRegisters(Graph<BtfVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();
        var baseStart = GraphUtils.findAllNodesWithProperty(graph, BtfVertexProperty.BASE_ADDR).get(0);
        var elems1 = getSubgraphAddressRegisters(graph, baseStart);
        var offsetStart = GraphUtils.findAllNodesWithProperty(graph, BtfVertexProperty.OFFSET).get(0);
        var elems2 = getSubgraphAddressRegisters(graph, offsetStart);
        var elems = new LinkedHashSet<BtfVertex>(elems1);
        elems.addAll(elems2);

        for (var elem : elems) {
            if (elem.getType() == BtfVertexType.REGISTER)
                regs.add(elem.getLabel());
        }
        return regs.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Finds all registers used on a subgraph of a memory access (base or offset), ignoring intermediate registers
     * 
     * @param graph
     * @return
     */
    private List<BtfVertex> getSubgraphAddressRegisters(Graph<BtfVertex, DefaultEdge> graph,
            BtfVertex start) {
        var elems = GraphUtils.findAllPredecessors(graph, start);
        var filtered = new ArrayList<BtfVertex>();
        {
            for (var elem : elems) {
                if (graph.inDegreeOf(elem) == 0)
                    filtered.add(elem);
            }
        }
        return filtered;
    }
}
