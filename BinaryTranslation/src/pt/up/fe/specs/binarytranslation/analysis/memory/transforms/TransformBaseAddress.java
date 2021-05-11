package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;

public class TransformBaseAddress extends AGraphTransform {

    public TransformBaseAddress(Graph<AddressVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g) {
        var rA = GraphUtils.findAllNodesWithIsaInfo(g, AddressVertexIsaInfo.RA).get(0);
        var rB = GraphUtils.findAllNodesWithIsaInfo(g, AddressVertexIsaInfo.RB).get(0);

        if (rB.getType() != AddressVertexType.IMMEDIATE)
            return g;

        var rAParents = GraphUtils.getParents(g, rA);
        boolean isOffset = false;
        
        //Heuristic: consider it an offset if it has a shift
        for (var vertex : rAParents) {
            if (vertex.getType() == AddressVertexType.OPERATION) {
                var l = vertex.getLabel();
                if (l.equals("<<") || l.equals(">>")) {
                    isOffset = true;
                }
            }
        }
        rA.setProperty(isOffset ? AddressVertexProperty.OFFSET : AddressVertexProperty.BASE_ADDR);
        return g;
    }

}
