package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;

public class TransformShiftsToMult extends AGraphTransform {

    public TransformShiftsToMult(Graph<AddressVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION) {
                var label = v.getLabel();
                if (label.equals(">>") || label.equals("<<")) {
                    transformShift(g, v);
                }
            }
        }
        return g;
    }

    private void transformShift(Graph<AddressVertex, DefaultEdge> g, AddressVertex v) {
        var parents = GraphUtils.getParents(g, v);
        var imm = AddressVertex.nullVertex;
        for (var parent : parents) {
            if (parent.getType() == AddressVertexType.IMMEDIATE)
                imm = parent;
        }
        if (imm == AddressVertex.nullVertex)
            return;
        
        long immVal = Long.decode(imm.getLabel());
        double multVal = Math.pow(2, immVal);
        
        imm.setLabel(Integer.toString((int) multVal));
        v.setLabel("*");
    }
}
