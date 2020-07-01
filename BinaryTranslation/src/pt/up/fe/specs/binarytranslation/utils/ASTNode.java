package pt.up.fe.specs.binarytranslation.utils;

import java.util.List;

public interface ASTNode<T> {

    public void addChild(ASTNode<T> child);

    public void addChildLeftOf(ASTNode<T> child, ASTNode<T> sibling);

    public void addChildRightOf(ASTNode<T> child, ASTNode<T> sibling);

    public void addChildAt(ASTNode<T> child, int idx);

    public List<ASTNode<T>> getChildren();

    public ASTNode<T> getChild(int idx);

    public ASTNode<T> getParent();

    public void setParent(ASTNode<T> parent);
}
