package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class TransformShiftsToMult extends AGraphTransform {

    public TransformShiftsToMult(Graph<BtfVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<BtfVertex, DefaultEdge> applyTransform(Graph<BtfVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == BtfVertexType.OPERATION) {
                var label = v.getLabel();
                if (label.equals(">>") || label.equals("<<")) {
                    transformShift(g, v);
                }
            }
        }
        return g;
    }

    private void transformShift(Graph<BtfVertex, DefaultEdge> g, BtfVertex v) {
        var parents = GraphUtils.getParents(g, v);
        var imm = BtfVertex.nullVertex;
        for (var parent : parents) {
            if (parent.getType() == BtfVertexType.IMMEDIATE)
                imm = parent;
        }
        if (imm == BtfVertex.nullVertex)
            return;
        
        long immVal = Long.decode(imm.getLabel());
        double multVal = Math.pow(2, immVal);
        
        imm.setLabel(Integer.toString((int) multVal));
        v.setLabel("*");
    }
}
