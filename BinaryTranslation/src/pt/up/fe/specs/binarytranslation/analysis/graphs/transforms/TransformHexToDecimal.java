package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;

public class TransformHexToDecimal extends AGraphTransform {

    public TransformHexToDecimal(Graph<DataFlowVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<DataFlowVertex, DefaultEdge> applyTransform(Graph<DataFlowVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == DataFlowVertexType.IMMEDIATE) {
                var label = v.getLabel();
                if (label.startsWith("0x")) {
                    v.setLabel(AnalysisUtils.hexToDec(label));
                }
            }
        }
        return g;
    }

}
