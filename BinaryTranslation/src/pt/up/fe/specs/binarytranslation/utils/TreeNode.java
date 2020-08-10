package pt.up.fe.specs.binarytranslation.utils;

import java.util.List;

/**
 * 
 * @author Nuno
 *
 * @param <K>
 */
public interface TreeNode<K extends TreeNode<K>> {

    /*
     * 
     */
    public K getThis();

    /*
     * 
     */
    public K getParent();

    /*
     * 
     */
    public List<K> getChildren();

    /*
     * 
     */
    public void setParent(K parent);

    default public void addChild(K child) {
        this.getChildren().add(child);
        child.setParent(getThis());
    }

    default public void addChildLeftOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        this.getChildren().add(idx, child);
        child.setParent(getThis());
    }

    default public void addChildRightOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        this.getChildren().add(idx - 1, child);
        child.setParent(getThis());
    }

    default public void addChildAt(K child, int idx) {
        this.getChildren().add(idx, child);
        child.setParent(getThis());
    }

    default public K getChild(int idx) {
        return this.getChildren().get(idx);
    }

    default public void replaceChild(K oldChild, K newChild) {
        int idx = this.getChildren().indexOf(oldChild);
        this.getChildren().set(idx, newChild);
        newChild.setParent(getThis());
        return;
    }
}
