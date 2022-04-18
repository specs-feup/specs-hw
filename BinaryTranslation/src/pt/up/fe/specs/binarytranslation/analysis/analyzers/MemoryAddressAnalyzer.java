package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.InstructionSets;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.graphs.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class MemoryAddressAnalyzer extends ASegmentAnalyzer {
    private RegisterProperties isaProps;

    public MemoryAddressAnalyzer(ATraceInstructionStream stream,
            ZippedELFProvider elf, RegisterProperties isaProps, int window) {
        super(stream, elf, window);
        this.isaProps = isaProps;
    }

    public void analyze() {
        var segs = getSegments();

        for (var bb : segs) {
            // Print BB instructions
            handleBasicBlockSummary(bb);

            // Get Basic Block In/Outs
            var inouts = handleInOuts(bb);

            // Prologue dependencies
            // var prologueDeps = handlePrologueDependencies(tracker);

            // Memory graphs
            var graphs = handleMemoryGraphs(bb);

            // Clean up memory graphs
            cleanUpGraphs(graphs);

            // Induction variables
            var indVars = handleInductionVariables(bb, graphs);

            // Check alias for every RAW dependency
            // handleMemoryComparison(prologueDeps, graphs, indVars, inouts);
        }
    }

    private InstructionSets handleInOuts(BinarySegment bb) {
        var inoutFinder = new SimpleBasicBlockInOuts(bb.getInstructions());
        inoutFinder.calculateInOuts();
        return inoutFinder.getInouts();
    }

    private void cleanUpGraphs(List<Graph<BtfVertex, DefaultEdge>> graphs) {
        for (var graph : graphs) {
            var t2 = new TransformHexToDecimal(graph);
            t2.applyToGraph();
            var t3 = new TransformShiftsToMult(graph);
            t3.applyToGraph();
            var t4 = new TransformRemoveTemporaryVertices(graph);
            t4.applyToGraph();
        }

        var mergedGraph = GraphUtils.mergeGraphs(graphs);
        String sgraph = GraphUtils.graphToDot(mergedGraph);
        var url = GraphUtils.generateGraphURL(sgraph);
        System.out.println("Graph URL:\n" + url + "\n");
    }

    private ArrayList<Graph<BtfVertex, DefaultEdge>> handleMemoryGraphs(BinarySegment bb) {
        System.out.println("\nCalculating memory address graphs...");
        var mad = new MemoryAddressDetector(bb);
        var graphs = mad.detectGraphs();

        printExpressions(graphs);
        AnalysisUtils.printSeparator(40);
        return graphs;
    }

    private void handleBasicBlockSummary(BinarySegment bb) {
        System.out.println("Memory address analysis for segment:");
        bb.printSegment();
        AnalysisUtils.printSeparator(40);
    }

    private HashMap<String, Integer> handleInductionVariables(BinarySegment bb,
            ArrayList<Graph<BtfVertex, DefaultEdge>> graphs) {
        System.out.println("\nCalculating induction variables...");
        var ivd = new InductionVariablesDetector(bb);
        var indVars = ivd.detectVariables(graphs, false);

        System.out.println("Detected the following induction variable(s) and stride(s):");
        for (var reg : indVars.keySet()) {
            System.out.println("Register " + reg + " , stride = " + indVars.get(reg));
        }
        AnalysisUtils.printSeparator(40);
        return indVars;
    }

    public static void printExpressions(ArrayList<Graph<BtfVertex, DefaultEdge>> graphs) {
        System.out.println("Expressions for each memory access:");
        for (var graph : graphs) {
            String s = MemoryAddressDetector.buildMemoryExpression(graph, GraphUtils.findGraphRoot(graph));
            System.out.println(s);
        }
    }
}
