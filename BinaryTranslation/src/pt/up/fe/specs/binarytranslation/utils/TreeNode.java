package pt.up.fe.specs.binarytranslation.utils;

import java.util.List;

public interface TreeNode<T extends TreeNode<T>> {

    public void addChild(T child);

    public void addChildLeftOf(T child, T sibling);

    public void addChildRightOf(T child, T sibling);

    public void addChildAt(T child, int idx);

    public List<T> getChildren();

    public T getChild(int idx);

    public T getParent();

    public void replaceChild(T oldChild, T newChild);

    public void setParent(T parent);
}
