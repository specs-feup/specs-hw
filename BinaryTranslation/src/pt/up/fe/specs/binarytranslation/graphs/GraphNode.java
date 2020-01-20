package pt.up.fe.specs.binarytranslation.graphs;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

public class GraphNode {

    private int nodenr;
    private int level;
    private int numchildren;
    private int numparents;
    private final Instruction inst;

    private List<GraphInput> inputs;
    private List<GraphOutput> outputs;

    // private GraphOperation op;

    private static int nodecounter = 0;

    /*private GraphNode(GraphOperation op, int level, int numchildren, int numparents) {
        this.op = op;
        this.level = level;
        this.numchildren = numchildren;
        this.numparents = numparents;
    }*/

    public GraphNode(Instruction i) {
        // this.op = new GraphOperation(GraphNodeCatalogue.convertInstruction(i));
        this.inst = i;
        this.nodenr = nodecounter;
        nodecounter++;

        // convert operations in instruction to node inputs/outputs
        this.inputs = new ArrayList<GraphInput>();
        this.outputs = new ArrayList<GraphOutput>();

        for (Operand op : i.getData().getOperands()) {
            if (op.isRead())
                this.inputs.add(new GraphInput(op));

            else if (op.isWrite())
                this.outputs.add(new GraphOutput(op));
        }
    }

    public Instruction getInst() {
        return inst;
    }

    public List<GraphInput> getInputs() {
        return inputs;
    }

    public List<GraphOutput> getOutputs() {
        return outputs;
    }

    public int getLevel() {
        return level;
    }

    public int getNumchildren() {
        return numchildren;
    }

    public int getNumparents() {
        return numparents;
    }

    public String getRepresentation() {
        return Integer.toString(this.nodenr) + ":" + this.inst.getRepresentation();
    }
}
