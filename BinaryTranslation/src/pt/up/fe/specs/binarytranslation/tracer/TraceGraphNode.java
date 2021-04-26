package pt.up.fe.specs.binarytranslation.tracer;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.treenode.ATreeNode;

public class TraceGraphNode extends ATreeNode<TraceGraphNode> {

    private TraceUnit tunit;
    private List<Integer> childWeight = new ArrayList<>();

    public TraceGraphNode(TraceUnit tunit) {
        super(null);
        this.tunit = tunit;
    }

    public TraceUnit getUnit() {
        return tunit;
    }

    public void setTaken(TraceGraphNode taken) {
        this.addChild(taken);
    }

    @Override
    public String toContentString() {
        return this.tunit.toString(); // TODO add edge info?
    }

    @Override
    protected TraceGraphNode copyPrivate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TraceGraphNode getThis() {
        return this;
    }
}
