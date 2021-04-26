package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;

public class TransformBaseAddress extends AGraphTransform {

    protected TransformBaseAddress(Graph<AddressVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g) {
        var rA = GraphUtils.findAllNodesWithIsaInfo(g, AddressVertexIsaInfo.RA).get(0);
        var rB = GraphUtils.findAllNodesWithIsaInfo(g, AddressVertexIsaInfo.RB).get(0);
        
        if (rB.getType() == AddressVertexType.IMMEDIATE)
            return g;
        
        int rAParents = GraphUtils.getParents(g, rA).size();
        int rBParents = GraphUtils.getParents(g, rB).size();
        
        if (rAParents > rBParents) {
            rA.setProperty(AddressVertexProperty.OFFSET);
            rB.setProperty(AddressVertexProperty.BASE_ADDR);
        }
        else {
            rA.setProperty(AddressVertexProperty.BASE_ADDR);
            rB.setProperty(AddressVertexProperty.OFFSET);
        }
        
        return g;
    }

}
