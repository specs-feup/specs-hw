package pt.up.fe.specs.binarytranslation.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

    private K parent;
    private List<K> children;

    public ATreeNode() {
        this.parent = null;
        this.children = new ArrayList<K>();
    }

    @Override
    public abstract K getThis();

    @Override
    public List<K> getChildren() {
        return this.children;
    }

    @Override
    public K getParent() {
        return this.parent;
    }

    @Override
    public void setParent(K parent) {
        this.parent = parent;
    }
}
