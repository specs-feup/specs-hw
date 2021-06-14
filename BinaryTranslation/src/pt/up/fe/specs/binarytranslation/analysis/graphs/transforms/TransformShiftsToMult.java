package pt.up.fe.specs.binarytranslation.analysis.graphs.transforms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex;
import pt.up.fe.specs.binarytranslation.analysis.dataflow.DataFlowVertex.DataFlowVertexType;
import pt.up.fe.specs.binarytranslation.analysis.memory.GraphUtils;

public class TransformShiftsToMult extends AGraphTransform {

    public TransformShiftsToMult(Graph<DataFlowVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<DataFlowVertex, DefaultEdge> applyTransform(Graph<DataFlowVertex, DefaultEdge> g) {
        for (var v : g.vertexSet()) {
            if (v.getType() == DataFlowVertexType.OPERATION) {
                var label = v.getLabel();
                if (label.equals(">>") || label.equals("<<")) {
                    transformShift(g, v);
                }
            }
        }
        return g;
    }

    private void transformShift(Graph<DataFlowVertex, DefaultEdge> g, DataFlowVertex v) {
        var parents = GraphUtils.getParents(g, v);
        var imm = DataFlowVertex.nullVertex;
        for (var parent : parents) {
            if (parent.getType() == DataFlowVertexType.IMMEDIATE)
                imm = parent;
        }
        if (imm == DataFlowVertex.nullVertex)
            return;
        
        long immVal = Long.decode(imm.getLabel());
        double multVal = Math.pow(2, immVal);
        
        imm.setLabel(Integer.toString((int) multVal));
        v.setLabel("*");
    }
}
