package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class HardwareNode extends ATreeNode<HardwareNode> {

    protected HardwareNodeType type;

    public HardwareNode() {
        super(null);
    }

    public HardwareNodeType getType() {
        return this.type;
    }

    @Override
    public HardwareNode getThis() {
        return this;
    }

    public String getAsString() {

        var builder = new StringBuilder();
        for (HardwareNode comp : this.getChildren()) {
            builder.append(comp.getAsString() + "\n");
        }
        return builder.toString();
    }

    @Override
    public String toContentString() {
        return this.getAsString();
    }

    /*
     * 
     */
    public void emit(OutputStream os) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(this.getAsString());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
