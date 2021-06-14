package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class TransformHexToDecimal extends AGraphTransform {

    public TransformHexToDecimal(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == BtfVertexType.IMMEDIATE) {
                var label = v.getLabel();
                if (label.startsWith("0x")) {
                    v.setLabel(AnalysisUtils.hexToDec(label));
                }
            }
        }
        return g;
    }

}
