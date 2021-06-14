package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class TransformBaseAddress extends AGraphTransform {

    public TransformBaseAddress(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        var rA = GraphUtils.findAllNodesWithIsaInfo(g, BtfVertexIsaInfo.RA).get(0);
        var rB = GraphUtils.findAllNodesWithIsaInfo(g, BtfVertexIsaInfo.RB).get(0);

        if (rB.getType() != BtfVertexType.IMMEDIATE)
            return g;

        var rAParents = GraphUtils.getParents(g, rA);
        boolean isOffset = false;
        
        //Heuristic: consider it an offset if it has a shift
        for (var vertex : rAParents) {
            if (vertex.getType() == BtfVertexType.OPERATION) {
                var l = vertex.getLabel();
                if (l.equals("<<") || l.equals(">>")) {
                    isOffset = true;
                }
            }
        }
        rA.setProperty(isOffset ? BtfVertexProperty.OFFSET : BtfVertexProperty.BASE_ADDR);
        return g;
    }

}
