package pt.up.fe.specs.binarytranslation.graphs.edge;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class GraphOutput extends AGraphEdge {

    public GraphOutput(Operand op) {
        super(op, GraphEdgeDirection.output, op.getRepresentation());

        // TODO how to determine is operand is a data or control output? would need instruction, or to add a field to

        // TODO actually, the branch oeprations of the microblaze have NO output register

        // OperandTypes
        // if(op)

        this.etype = GraphEdgeType.noderesult;
        // default to "data", change to liveout if the owner of
        // this output is the last one to write to this register (?)

        this.op = op;
    }

    public void setOutputAs(GraphEdgeType type, String value) {
        this.setEdgeAs(type, value);
    }

    /*
     * 
     */
    public String rawDotty() {
        return "out_" + this.getRepresentation();
    }
}
