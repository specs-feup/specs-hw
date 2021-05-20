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

package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveOrphanOperations;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;

public class MemoryAddressComparator {

    private Map<String, List<String>> prologueDeps;
    private ArrayList<Graph<AddressVertex, DefaultEdge>> graphs;
    private HashMap<String, Integer> indVars;
    private RegisterProperties isaProps;

    public MemoryAddressComparator(Map<String, List<String>> prologueDeps,
            ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, HashMap<String, Integer> indVars,
            RegisterProperties isaProps) {
        this.prologueDeps = prologueDeps;
        this.graphs = graphs;
        this.indVars = indVars;
        this.isaProps = isaProps;
    }

    public boolean compare(Graph<AddressVertex, DefaultEdge> load, Graph<AddressVertex, DefaultEdge> store) {
        var reducedLoad = treeSubtract(load, store);
        var reducedStore = treeSubtract(store, load);

        printComparisonGraph(reducedLoad, reducedStore);

        return compareSimplifiedGraphs(reducedLoad, reducedStore);
    }

    private void printComparisonGraph(Graph<AddressVertex, DefaultEdge> reducedLoad,
            Graph<AddressVertex, DefaultEdge> reducedStore) {
        var loadRoot = GraphUtils.findGraphRoot(reducedLoad);
        var storeRoot = GraphUtils.findGraphRoot(reducedStore);

        Graph<AddressVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(merged, reducedLoad);
        Graphs.addGraph(merged, reducedStore);

        var comparison = new AddressVertex("==", AddressVertexType.CHECK);
        merged.addVertex(comparison);
        merged.addEdge(loadRoot, comparison);
        merged.addEdge(storeRoot, comparison);

        var dot = GraphUtils.graphToDot(merged);
        var url = GraphUtils.generateGraphURL(dot);
        System.out.println("Alias check graph:\n" + url + "\n");

        var expr1 = MemoryAddressDetector.buildAddressExpression(reducedLoad, loadRoot, true);
        var expr2 = MemoryAddressDetector.buildAddressExpression(reducedStore, storeRoot, true);
        System.out.println("ALIAS IF " + expr1 + " == " + expr2 + "\n");
    }

    private boolean compareSimplifiedGraphs(Graph<AddressVertex, DefaultEdge> reducedLoad,
            Graph<AddressVertex, DefaultEdge> reducedStore) {
        var regLd = GraphUtils.getVerticesWithType(reducedLoad, AddressVertexType.REGISTER);
        var regSt = GraphUtils.getVerticesWithType(reducedStore, AddressVertexType.REGISTER);

        System.out.println("Registers used in Load:");
        for (var v : regLd)
            printRegisterProperties(v.getLabel());
        System.out.println("Registers used in Store:");
        for (var v : regSt)
            printRegisterProperties(v.getLabel());

        return false;
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

        System.out.println("  - " + reg + ": " + String.join(", ", props));
    }

    private Graph<AddressVertex, DefaultEdge> treeSubtract(Graph<AddressVertex, DefaultEdge> main,
            Graph<AddressVertex, DefaultEdge> sub) {
        var mainStart = GraphUtils.getExpressionStart(main);
        var subStart = GraphUtils.getExpressionStart(sub);

        // Create main-clone
        Graph<AddressVertex, DefaultEdge> mainClone = new DefaultDirectedGraph<>(DefaultEdge.class);
        Graphs.addGraph(mainClone, main);

        // Go through each node of sub, and remove from main-clone
        // Use post-order traversal!!!
        postOrderTraversal(mainClone, mainStart, sub, subStart);

        // Remove stuff after the memory access
        removeExtraNodes(mainStart, mainClone);

        // Remove orphan operation vertices
        var trans1 = new TransformRemoveOrphanOperations(mainClone);
        trans1.applyToGraph();

        // Remove intermediary operation vertices
        var trans2 = new TransformRemoveTemporaryVertices(mainClone, AddressVertexType.OPERATION);
        trans2.applyToGraph();

        // Remove orphans again, as the previous transform might create more
        trans1.applyToGraph();

        return mainClone;
    }

    private void removeExtraNodes(AddressVertex mainStart, Graph<AddressVertex, DefaultEdge> mainClone) {
        var memoryOp = GraphUtils.getMemoryOp(mainClone);
        var toRemove = new ArrayList<AddressVertex>();
        toRemove.add(memoryOp);

        var edges = mainClone.outgoingEdgesOf(memoryOp);
        for (var edge : edges) {
            var v = mainClone.getEdgeTarget(edge);
            toRemove.add(v);
        }
        edges = mainClone.incomingEdgesOf(memoryOp);
        for (var edge : edges) {
            var v = mainClone.getEdgeSource(edge);
            if (v != mainStart)
                toRemove.add(v);
        }
        mainClone.removeAllVertices(toRemove);
    }

    private void postOrderTraversal(Graph<AddressVertex, DefaultEdge> mainClone, AddressVertex mainStart,
            Graph<AddressVertex, DefaultEdge> sub, AddressVertex currVertex) {
        if (currVertex == AddressVertex.nullVertex)
            return;

        var parents = GraphUtils.getParents(sub, currVertex);
        var left = parents.size() > 0 ? parents.get(0) : AddressVertex.nullVertex;
        postOrderTraversal(mainClone, mainStart, sub, left);
        var right = parents.size() > 1 ? parents.get(1) : AddressVertex.nullVertex;
        postOrderTraversal(mainClone, mainStart, sub, right);

        // Do the comparison to mainClone, and remove if it exists
        var subStart = GraphUtils.getExpressionStart(sub);

        if (currVertex.getType() == AddressVertexType.OPERATION)
            return;
        AddressVertex toRemove = treeHasVertex(mainClone, sub, currVertex, mainStart, subStart);
        if (toRemove != AddressVertex.nullVertex)
            mainClone.removeVertex(toRemove);
    }

    private AddressVertex treeHasVertex(Graph<AddressVertex, DefaultEdge> mainClone,
            Graph<AddressVertex, DefaultEdge> sub, AddressVertex currVertex, AddressVertex mainStart,
            AddressVertex subStart) {
        var ret = AddressVertex.nullVertex;

        for (var v : mainClone.vertexSet()) {
            if (v.getLabel().equals(currVertex.getLabel())) {
                String path1 = GraphUtils.pathBetweenTwoVertices(sub, currVertex, subStart, 3);
                String path2 = GraphUtils.pathBetweenTwoVertices(mainClone, v, mainStart, 3);
                if (path1.equals(path2))
                    ret = v;
            }
        }

        return ret;
    }
}
