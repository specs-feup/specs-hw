package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexIsaInfo;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexProperty;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;

public class TransformBaseAddress extends AGraphTransform {

    public TransformBaseAddress(Graph<DataFlowVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<DataFlowVertex, DefaultEdge> applyTransform(Graph<DataFlowVertex, DefaultEdge> g) {
        var rA = GraphUtils.findAllNodesWithIsaInfo(g, DataFlowVertexIsaInfo.RA).get(0);
        var rB = GraphUtils.findAllNodesWithIsaInfo(g, DataFlowVertexIsaInfo.RB).get(0);

        if (rB.getType() != DataFlowVertexType.IMMEDIATE)
            return g;

        var rAParents = GraphUtils.getParents(g, rA);
        boolean isOffset = false;
        
        //Heuristic: consider it an offset if it has a shift
        for (var vertex : rAParents) {
            if (vertex.getType() == DataFlowVertexType.OPERATION) {
                var l = vertex.getLabel();
                if (l.equals("<<") || l.equals(">>")) {
                    isOffset = true;
                }
            }
        }
        rA.setProperty(isOffset ? DataFlowVertexProperty.OFFSET : DataFlowVertexProperty.BASE_ADDR);
        return g;
    }

}
