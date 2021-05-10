package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;

public class TransformHexToDecimal extends AGraphTransform {

    public TransformHexToDecimal(Graph<AddressVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == AddressVertexType.IMMEDIATE) {
                var label = v.getLabel();
                if (label.startsWith("0x")) {
                    v.setLabel(AnalysisUtils.hexToDec(label));
                }
            }
        }
        return g;
    }

}
