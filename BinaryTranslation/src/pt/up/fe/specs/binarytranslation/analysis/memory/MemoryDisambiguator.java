package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
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
        for (var graph : graphs) {
            String expr = MemoryAddressDetector.buildMemoryExpression(graph);
            System.out.println("Memory disambiguation for memory access " + expr + ":");
            var addrRegs = getAddressRegisters(graph);
            for (var reg : addrRegs) {
                printRegisterProperties(reg);
            }
            System.out.println("");
        }
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

    private List<String> getAddressRegisters(Graph<AddressVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();
        var baseStart = GraphUtils.findAllNodesWithProperty(graph, AddressVertexProperty.BASE_ADDR_START).get(0);
        var elems = GraphUtils.findAllPredecessors(graph, baseStart);
        
        for (var elem : elems) {
            if (elem.getType() == AddressVertexType.REGISTER)
                regs.add(elem.getLabel());
        }
        return regs.stream().distinct().collect(Collectors.toList());
    }
}
