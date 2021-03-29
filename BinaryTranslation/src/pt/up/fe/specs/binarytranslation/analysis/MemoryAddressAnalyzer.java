package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAddressAnalyzer extends ATraceAnalyzer {

    public MemoryAddressAnalyzer(ATraceInstructionStream stream) {
        super(stream);
    }

    public void analyze() {
        var det = new TraceBasicBlockDetector();
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        
        for (var bb : segs) {
            System.out.println("Memory address analysis for segment:");
            bb.printSegment();
            
            System.out.println("\nCalculating memory address graphs...");
            var mad = new MemoryAddressDetector(bb, insts);
            var graphs = mad.detectGraphs();
            System.out.println("\nDetected the following graphs:");
//            for (var graph : graphs) {
//                String s = AnalysisUtils.graphToDot(graph);
//                System.out.println(s);
//            }
            var merged = mergeGraphs(graphs);
            System.out.println(AnalysisUtils.graphToDot(merged));
            
            //induction vars
            System.out.println("\nCalculating induction variables...");
            var ivd = new InductionVariablesDetector(bb, insts);
            //...
            System.out.println("Detected the following induction variables:");
            //...
            AnalysisUtils.printSeparator(40);
        }
    }
    
    public Graph<AddressVertex, DefaultEdge> mergeGraphs(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        Graph<AddressVertex, DefaultEdge> merged = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (var graph : graphs) {
            Graphs.addGraph(merged, graph);
        }
        return merged;
    }
}
