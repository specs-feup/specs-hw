package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAddressAnalyzer extends ATraceAnalyzer {

    public MemoryAddressAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }
   
    public void analyze(int window) {
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        
        for (var bb : segs) {
            AnalysisUtils.printSeparator(40);
            System.out.println("Memory address analysis for segment:");
            bb.printSegment();
            
            System.out.println("\nCalculating memory address graphs...");
            var mad = new MemoryAddressDetector(bb, insts);
            var graphs = mad.detectGraphs();
            
            //can handle each graph individually, or merge them into one
            //merging for now, to simplify output
            var mergedGraph = mergeGraphs(graphs);
            String sgraph = AnalysisUtils.graphToDot(mergedGraph);
            var url = AnalysisUtils.generateGraphURL(sgraph);
            System.out.println("Graph URL:\n" + url + "\n");
            printExpressions(graphs);
            
            //induction vars
            System.out.println("\nCalculating induction variables...");
            var ivd = new InductionVariablesDetector(bb, insts);
            var regs = ivd.detectVariables(mergedGraph, false);
            
            System.out.println("Detected the following induction variable(s) and stride(s):");
            for (var reg : regs.keySet()) {
                System.out.println("Register " + reg +" , stride = " + regs.get(reg));
            }
            AnalysisUtils.printSeparator(40);
        }
    }
    
    private void printExpressions(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        System.out.println("Expressions for each memory access:");
        for (var graph : graphs) {
            String s = MemoryAddressDetector.buildMemoryExpression(graph, findGraphRoot(graph));
            System.out.println(s);
        }
    }

    private AddressVertex findGraphRoot(Graph<AddressVertex, DefaultEdge> graph) {
        var root = AddressVertex.nullVertex;
        for (var v : graph.vertexSet()) {
            if (graph.outDegreeOf(v) == 0)
                root = v;
        }
        return root;
    }

    public Graph<AddressVertex, DefaultEdge> mergeGraphs(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        Graph<AddressVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var graph : graphs) {
            Graphs.addGraph(merged, graph);
        }
        return merged;
    }
}
