package pt.up.fe.specs.binarytranslation.tracer;

import pt.up.fe.specs.util.treenode.ATreeNode;

public class TraceGraphNode extends ATreeNode<TraceGraphNode> {

    // int takenWeight, nonTakenWeight;
    private TraceUnit tunit;
    private TraceGraphNode taken, nontaken;

    public TraceGraphNode(TraceUnit tunit) {
        super(null);
        this.tunit = tunit;
    }

    public TraceUnit getUnit() {
        return tunit;
    }

    public void setTaken(TraceGraphNode taken) {
        this.taken = taken;
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
