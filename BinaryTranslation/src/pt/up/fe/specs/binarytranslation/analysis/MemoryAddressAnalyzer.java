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
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAddressAnalyzer extends ATraceAnalyzer {

    private ELFProvider elf;

    public MemoryAddressAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream);
        this.elf = elf;
    }
    
    private TraceBasicBlockDetector buildDetector(int window) {
        stream.silent(false);
        stream.advanceTo(elf.getKernelStart().longValue());
        System.out.println("Looking for segments of size: " + window);

        var detector = new TraceBasicBlockDetector(// new FrequentTraceSequenceDetector(
                new DetectorConfigurationBuilder()
                        .withMaxWindow(window)
                        .withStartAddr(elf.getKernelStart())
                        .withStopAddr(elf.getKernelStop())
                        .withPrematureStopAddr(elf.getKernelStop().longValue())
                        .build());
        return detector;
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
            System.out.println("Graph URL:\n" + url);
            
            //induction vars
            System.out.println("\nCalculating induction variables...");
            var ivd = new InductionVariablesDetector(bb, insts);
            var regs = ivd.detectVariables(mergedGraph, true);
            
            System.out.println("Detected the following candidate variables:");
            System.out.println(String.join(", ", regs));
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
