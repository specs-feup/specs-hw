package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import pt.up.fe.specs.binarytranslation.utils.ATreeNode;

public abstract class AHardwareNode extends ATreeNode<AHardwareNode> implements HardwareNode {

    public AHardwareNode() {

    }

    @Override
    public AHardwareNode getThis() {
        return this;
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        for (HardwareNode comp : this.getChildren()) {
            builder.append(comp.getAsString() + "\n");
        }
        return builder.toString();
    }

    /*
     * 
     */
    @Override
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
