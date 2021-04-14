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
import pt.up.fe.specs.binarytranslation.asm.RegisterProperties;

public class MemoryDisambiguator {

    private ArrayList<Graph<AddressVertex, DefaultEdge>> graphs;
    private HashMap<String, Integer> indVars;
    private RegisterProperties isaProps;

    public MemoryDisambiguator(ArrayList<Graph<AddressVertex, DefaultEdge>> graphs, HashMap<String, Integer> indVars, RegisterProperties isaProps) {
        this.graphs = graphs;
        this.indVars = indVars;
        this.isaProps = isaProps;
    }

    public void disambiguate() {
        System.out.println("");
        var addressRegs = new ArrayList<String>();
        
        for (var graph : graphs) {
            String expr = MemoryAddressDetector.buildMemoryExpression(graph);
            System.out.println("Memory disambiguation for memory access " + expr + ":");
            var addrRegs = getFilteredRegisters(graph);
            for (var reg : addrRegs) {
                printRegisterProperties(reg);
                if (isaProps.isParameter(reg) || isaProps.isTemporary(reg)) {
                    if (!indVars.containsKey(reg))
                        addressRegs.add(reg);
                }
            }
            System.out.println("");
        }
        System.out.println("Address registers: " + addressRegs.stream().distinct().collect(Collectors.toList()));
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

    private List<String> getFilteredRegisters(Graph<AddressVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();
        var baseStart = GraphUtils.findAllNodesWithProperty(graph, AddressVertexProperty.BASE_ADDR_START).get(0);
        var elems1 = findAddressRegisters(graph, baseStart);
        var offsetStart = GraphUtils.findAllNodesWithProperty(graph, AddressVertexProperty.OFFSET_START).get(0);
        var elems2 = findAddressRegisters(graph, offsetStart);
        var elems = new LinkedHashSet<AddressVertex>(elems1);
        elems.addAll(elems2);
        
        for (var elem : elems) {
            if (elem.getType() == AddressVertexType.REGISTER)
                regs.add(elem.getLabel());
        }
        return regs.stream().distinct().collect(Collectors.toList());
    }
    
    private List<AddressVertex> findAddressRegisters(Graph<AddressVertex, DefaultEdge> graph, AddressVertex start) {
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
