package pt.up.fe.specs.binarytranslation.graph2dotty;

import pt.up.fe.specs.util.treenode.TreeNode;

/**
 * Base class for any walker of anything that extends @{pt.up.fe.specs.util.treenode.ATreeNode}
 * 
 * @author Nuno
 *
 * @param <K>
 */
public abstract class TreeNodeWalker<K extends TreeNode<K>> {

    protected void visitChildren(TreeNode<K> node) {
        for (var c : node.getChildren()) {
            this.visit(c);
        }
    }

    public void visit(TreeNode<K> node) {
        this.visitChildren(node);
    };
}
