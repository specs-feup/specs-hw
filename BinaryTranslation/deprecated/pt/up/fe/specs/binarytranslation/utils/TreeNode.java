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
        var idx = this.getChildren().indexOf(child);
        this.getChildren().add(idx, sibling);
        child.setParent(getThis());
    }

    default public void addChildRightOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(child);
        this.getChildren().add(idx + 1, sibling);
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
    
    public static <K extends TreeNode<K>> String toString(K token, String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append(token.getClass().getSimpleName());
        builder.append("\n");
        if (!token.getChildren().isEmpty()) {
            for (K child : token.getChildren()) {
                builder.append(toString(child, prefix + "  "));
            }
        }

        return builder.toString();
    }
}
