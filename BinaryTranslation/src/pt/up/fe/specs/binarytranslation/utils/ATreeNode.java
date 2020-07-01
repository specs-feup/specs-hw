package pt.up.fe.specs.binarytranslation.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

    protected K parent;
    protected List<K> children;

    public ATreeNode() {
        this.parent = null;
        this.children = new ArrayList<K>();
    }

    /*
     * 
     */
    public abstract K getThis();

    @Override
    public void addChild(K child) {
        this.getChildren().add(child);
        child.setParent(getThis());
    }

    @Override
    public void addChildLeftOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        this.getChildren().add(idx, child);
        child.setParent(getThis());
    }

    @Override
    public void addChildRightOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        this.getChildren().add(idx - 1, child);
        child.setParent(getThis());
    }

    @Override
    public void addChildAt(K child, int idx) {
        this.getChildren().add(idx, child);
        child.setParent(getThis());
    }

    @Override
    public List<K> getChildren() {
        return this.children;
    }

    @Override
    public K getChild(int idx) {
        return this.getChildren().get(idx);
    }

    @Override
    public K getParent() {
        return this.parent;
    }

    @Override
    public void setParent(K parent) {
        this.parent = parent;
    }

    @Override
    public void replaceChild(K oldChild, K newChild) {
        for (var c : this.getChildren()) {
            if (c == oldChild) {
                int idx = this.getChildren().indexOf(c);
                this.getChildren().set(idx, newChild);
                return;
            }
        }
    }
}
