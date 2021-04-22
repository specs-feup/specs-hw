package pt.up.fe.specs.binarytranslation.analysis;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryDisambiguator;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAddressAnalyzer extends ATraceAnalyzer {
    private RegisterProperties isaProps;

    public MemoryAddressAnalyzer(ATraceInstructionStream stream, ELFProvider elf, RegisterProperties isaProps) {
        super(stream, elf);
        this.isaProps = isaProps;
    }
   
    public void analyze(int window) {
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();
        
        for (var bb : segs) {
            System.out.println("Memory address analysis for segment:");
            bb.printSegment();
            AnalysisUtils.printSeparator(40);
            
            System.out.println("\nCalculating memory address graphs...");
            var mad = new MemoryAddressDetector(bb, insts);
            var graphs = mad.detectGraphs();
            var mergedGraph = GraphUtils.mergeGraphs(graphs);
            String sgraph = GraphUtils.graphToDot(mergedGraph);
            var url = AnalysisUtils.generateGraphURL(sgraph);
            
            System.out.println("Graph URL:\n" + url + "\n");
            printExpressions(graphs);
            AnalysisUtils.printSeparator(40);
            
            System.out.println("\nCalculating induction variables...");
            var ivd = new InductionVariablesDetector(bb, insts);
            var indVars = ivd.detectVariables(mergedGraph, false);
            
            System.out.println("Detected the following induction variable(s) and stride(s):");
            for (var reg : indVars.keySet()) {
                System.out.println("Register " + reg +" , stride = " + indVars.get(reg));
            }
            AnalysisUtils.printSeparator(40);
            
            //memory disambiguation
            var memDis = new MemoryDisambiguator(graphs, indVars, isaProps, ivd.getTracker());
            memDis.disambiguate();
            
            AnalysisUtils.printSeparator(80);
        }
    }
    
    private void printExpressions(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        System.out.println("Expressions for each memory access:");
        for (var graph : graphs) {
            String s = MemoryAddressDetector.buildMemoryExpression(graph, GraphUtils.findGraphRoot(graph));
            System.out.println(s);
        }
    }
}
