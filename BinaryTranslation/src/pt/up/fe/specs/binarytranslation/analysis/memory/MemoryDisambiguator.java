package pt.up.fe.specs.binarytranslation.analysis.memory;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

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
        // TODO Auto-generated method stub
        
    }

}
