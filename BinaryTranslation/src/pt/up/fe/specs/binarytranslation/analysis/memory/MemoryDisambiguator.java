package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

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
        for (var graph : graphs) {
            var addrRegs = getAddressRegisters(graph);
            for (var reg : addrRegs) {
                printRegisterProperties(reg);
            }
        }
    }
    
    private void printRegisterProperties(String reg) {
        System.out.println("Properties of register " + reg + ":");
        if (isaProps.isParameter(reg))
            System.out.println(reg + " is a function parameter");
        if (isaProps.isReturn(reg))
            System.out.println(reg + " is a return value");
        if (isaProps.isStackPointer(reg))
            System.out.println(reg + " is the stack pointer");
        if (isaProps.isTemporary(reg))
            System.out.println(reg + " is a temporary value");
        if (isaProps.isZero(reg))
            System.out.println(reg + " is zero");
        if (indVars.containsKey(reg))
            System.out.println(reg + " is an induction variable of stride " + indVars.get(reg));
    }

    private ArrayList<String> getAddressRegisters(Graph<AddressVertex, DefaultEdge> graph) {
        var regs = new ArrayList<String>();
        var baseStart = getBaseAddressStart(graph);

        
        return regs;
    }
    
    private AddressVertex getBaseAddressStart(Graph<AddressVertex, DefaultEdge> graph) {
        var mem = GraphUtils.findAllNodesOfType(graph, AddressVertexType.MEMORY).get(0);
        var op = AddressVertex.nullVertex;
        return null;
    }

}
