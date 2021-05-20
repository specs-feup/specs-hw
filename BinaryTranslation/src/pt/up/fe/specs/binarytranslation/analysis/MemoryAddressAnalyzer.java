package pt.up.fe.specs.binarytranslation.analysis;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.inouts.InstructionSets;
import pt.up.fe.specs.binarytranslation.analysis.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.AGraphTransform;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformBaseAddress;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformRemoveTemporaryVertices;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.InductionVariablesDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressComparator;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryAddressDetector;
import pt.up.fe.specs.binarytranslation.analysis.memory.MemoryDisambiguator;
import pt.up.fe.specs.binarytranslation.analysis.memory.PrologueDetector;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

public class MemoryAddressAnalyzer extends ATraceAnalyzer {
    private RegisterProperties isaProps;
    private Class<?> transforms[] = {
            TransformBaseAddress.class,
            TransformHexToDecimal.class,
            TransformShiftsToMult.class
    };

    public MemoryAddressAnalyzer(ATraceInstructionStream stream, ELFProvider elf, RegisterProperties isaProps) {
        super(stream, elf);
        this.isaProps = isaProps;
    }

    public void analyze(int window) {
        var det = buildDetector(window);
        List<BinarySegment> segs = AnalysisUtils.getSegments(stream, det);
        List<Instruction> insts = det.getProcessedInsts();

        for (var bb : segs) {
            var tracker = new BasicBlockOccurrenceTracker(bb, insts);

            // Print BB instructions
            handleBasicBlockSummary(bb);

            // Get Basic Block In/Outs
            var inouts = handleInOuts(bb);

            // Prologue dependencies
            var prologueDeps = handlePrologueDependencies(tracker);

            // Memory graphs
            var graphs = handleMemoryGraphs(insts, bb);

            // Clean up memory graphs
            cleanUpGraphs(graphs);

            // Induction variables
            var indVars = handleInductionVariables(tracker, graphs);

            // Check alias for every RAW dependency
            handleMemoryComparison(prologueDeps, graphs, indVars, inouts);
        }
    }

    private InstructionSets handleInOuts(BinarySegment bb) {
        var inoutFinder = new SimpleBasicBlockInOuts(bb);
        inoutFinder.calculateInOuts();
        return inoutFinder.getInouts();
    }

    private void cleanUpGraphs(List<Graph<AddressVertex, DefaultEdge>> graphs) {
        for (var graph : graphs) {
            // for (var cl : transforms) {
            // try {
            // Constructor<?> cons = cl.getConstructor(cl);
            // var trans = (AGraphTransform) cons.newInstance(graph);
            // trans.applyToGraph();
            //
            // } catch (Exception e) {
            // SpecsLogs.warn("Error message:\n", e);
            // }
            // }
            //var t1 = new TransformBaseAddress(graph);
            //t1.applyToGraph();
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

    private void handleMemoryComparison(Map<String, List<String>> prologueDeps,
            ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, HashMap<String, Integer> indVars,
            InstructionSets inouts) {
        var loadGraphs = new ArrayList<Graph<AddressVertex, DefaultEdge>>();
        var storeGraphs = new ArrayList<Graph<AddressVertex, DefaultEdge>>();
        for (var graph : graphs) {
            if (GraphUtils.getVerticesWithType(graph, AddressVertexType.LOAD_TARGET).size() != 0)
                loadGraphs.add(graph);
            else
                storeGraphs.add(graph);
        }

        var comparator = new MemoryAddressComparator(prologueDeps, graphs, indVars, isaProps);
        for (int i = 0; i < storeGraphs.size(); i++) {
            var store = storeGraphs.get(i);

            for (int j = 0; j < loadGraphs.size(); j++) {
                var load = loadGraphs.get(j);

                System.out.println("Alias check for load/store pair L" + j + "-S" + i + ":");
                var noConflict = comparator.compare(load, store);
                System.out.println(noConflict ? "No alias" : "Possible alias");
                AnalysisUtils.printSeparator(20);
            }
        }
    }

    private ArrayList<Graph<AddressVertex, DefaultEdge>> handleMemoryGraphs(List<Instruction> insts, BinarySegment bb) {
        System.out.println("\nCalculating memory address graphs...");
        var mad = new MemoryAddressDetector(bb, insts);
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

    @Deprecated
    private void handleMemoryDisambiguation(BasicBlockOccurrenceTracker tracker, Map<String, List<String>> prologueDeps,
            ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, HashMap<String, Integer> indVars) {
        var memDis = new MemoryDisambiguator(graphs, indVars, isaProps, tracker, prologueDeps);
        memDis.disambiguate();
        // TODO
        AnalysisUtils.printSeparator(80);
    }

    private Map<String, List<String>> handlePrologueDependencies(BasicBlockOccurrenceTracker tracker) {
        System.out.println("\nDetected following dependencies in function prologue:");
        var prologue = new PrologueDetector(tracker, isaProps);
        var prologueDeps = prologue.getRegisterState();
        PrologueDetector.printPrologueState(prologueDeps);
        AnalysisUtils.printSeparator(40);
        return prologueDeps;
    }

    private HashMap<String, Integer> handleInductionVariables(BasicBlockOccurrenceTracker tracker,
            ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        System.out.println("\nCalculating induction variables...");
        var ivd = new InductionVariablesDetector(tracker);
        var indVars = ivd.detectVariables(graphs, false);

        System.out.println("Detected the following induction variable(s) and stride(s):");
        for (var reg : indVars.keySet()) {
            System.out.println("Register " + reg + " , stride = " + indVars.get(reg));
        }
        AnalysisUtils.printSeparator(40);
        return indVars;
    }

    public static void printExpressions(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs) {
        System.out.println("Expressions for each memory access:");
        for (var graph : graphs) {
            String s = MemoryAddressDetector.buildMemoryExpression(graph, GraphUtils.findGraphRoot(graph));
            System.out.println(s);
        }
    }
}
