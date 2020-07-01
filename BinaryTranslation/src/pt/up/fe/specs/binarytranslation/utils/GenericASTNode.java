package pt.up.fe.specs.binarytranslation.utils;

import java.util.List;

public abstract class GenericASTNode<T> implements ASTNode<T> {

    protected ASTNode<T> parent;
    protected List<ASTNode<T>> children;

    @Override
    public void addChild(ASTNode<T> child) {
        this.children.add(child);
        child.setParent(this);
    }

    @Override
    public void addChildLeftOf(ASTNode<T> child, ASTNode<T> sibling) {
        var idx = this.children.indexOf(sibling);
        this.children.add(idx, child);
        child.setParent(this);
    }

    @Override
    public void addChildRightOf(ASTNode<T> child, ASTNode<T> sibling) {
        var idx = this.children.indexOf(sibling);
        this.children.add(idx - 1, child);
        child.setParent(this);
    }

    @Override
    public void addChildAt(ASTNode<T> child, int idx) {
        this.children.add(idx, child);
        child.setParent(this);
    }

    @Override
    public List<ASTNode<T>> getChildren() {
        return this.children;
    }

    @Override
    public ASTNode<T> getChild(int idx) {
        return this.children.get(idx);
    }

    @Override
    public ASTNode<T> getParent() {
        return this.parent;
    }

    @Override
    public void setParent(ASTNode<T> parent) {
        this.parent = parent;
    }
}
