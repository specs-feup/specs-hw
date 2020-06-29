package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class AHardwareNode implements HardwareNode {

    /*
     * The originating AST node which this hardware node implements
     */
    // private InstructionASTNode instructionASTNode;

    /*
     * The node's relations
     */
    protected HardwareNode parent;
    protected List<HardwareNode> children;

    public AHardwareNode() {
        this.children = new ArrayList<HardwareNode>();
    }

    @Override
    public void addChild(HardwareNode child) {
        this.children.add(child);
        child.setParent(this);
    }

    @Override
    public void addChildLeftOf(HardwareNode child, HardwareNode sibling) {
        var idx = this.children.indexOf(sibling);
        this.children.add(idx, child);
        child.setParent(this);
    }

    @Override
    public void addChildRightOf(HardwareNode child, HardwareNode sibling) {
        var idx = this.children.indexOf(sibling);
        this.children.add(idx - 1, child);
        child.setParent(this);
    }

    @Override
    public void addChildAt(HardwareNode child, int idx) {
        this.children.add(idx, child);
        child.setParent(this);
    }

    /*
     * 
     */
    // public HardwareComponentType getType();

    @Override
    public List<HardwareNode> getChildren() {
        return this.children;
    }

    @Override
    public HardwareNode getChild(int idx) {
        return this.children.get(idx);
    }

    @Override
    public HardwareNode getParent() {
        return this.parent;
    }

    @Override
    public void setParent(HardwareNode parent) {
        this.parent = parent;
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        for (HardwareNode comp : this.children) {
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
