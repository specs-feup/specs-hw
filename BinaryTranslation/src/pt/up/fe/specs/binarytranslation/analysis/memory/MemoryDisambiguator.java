package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformHexToDecimal;
import pt.up.fe.specs.binarytranslation.analysis.memory.transforms.TransformShiftsToMult;
import pt.up.fe.specs.binarytranslation.analysis.occurrence.BasicBlockOccurrenceTracker;
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;

public class MemoryDisambiguator {

    private ArrayList<Graph<AddressVertex, DefaultEdge>> graphs;
    private HashMap<String, Integer> indVars;
    private RegisterProperties isaProps;
    private BasicBlockOccurrenceTracker tracker;

    public MemoryDisambiguator(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, 
                                HashMap<String, Integer> indVars, 
                                RegisterProperties isaProps,
                                BasicBlockOccurrenceTracker tracker) {        
        this.graphs = graphs;
        this.indVars = indVars;
        this.isaProps = isaProps;
        this.tracker = tracker;
        
        for (var g : graphs) {
            //Convert shifts to mults
            var trans1 = new TransformShiftsToMult(g);
            trans1.applyToGraph();
            //Convert hex to decimal
            var trans2 = new TransformHexToDecimal(g);
            trans2.applyToGraph();
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
            
            //disambiguate
            System.out.println("Trying to find source of address registers:\n");
            var sourceDet = new RegisterSourceDetector(tracker);
            for (var register : filtered) {
                System.out.println(register  + ":");
                sourceDet.findSource(register);
            }
            
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
     * @param graph
     * @return
     */
    private List<String> getGraphAddressRegisters(Graph<AddressVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();
        var baseStart = GraphUtils.findAllNodesWithProperty(graph, AddressVertexProperty.BASE_ADDR).get(0);
        var elems1 = getSubgraphAddressRegisters(graph, baseStart);
        var offsetStart = GraphUtils.findAllNodesWithProperty(graph, AddressVertexProperty.OFFSET).get(0);
        var elems2 = getSubgraphAddressRegisters(graph, offsetStart);
        var elems = new LinkedHashSet<AddressVertex>(elems1);
        elems.addAll(elems2);
        
        for (var elem : elems) {
            if (elem.getType() == AddressVertexType.REGISTER)
                regs.add(elem.getLabel());
        }
        return regs.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * Finds all registers used on a subgraph of a memory access (base or offset),
     * ignoring intermediate registers
     * @param graph
     * @return
     */
    private List<AddressVertex> getSubgraphAddressRegisters(Graph<AddressVertex, DefaultEdge> graph, AddressVertex start) {
        var elems = GraphUtils.findAllPredecessors(graph, start);
        var filtered = new ArrayList<AddressVertex>(); {
            for (var elem : elems) {
               if (graph.inDegreeOf(elem) == 0)
                   filtered.add(elem);
            }
        }
        return filtered;
    }
}
