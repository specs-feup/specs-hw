package pt.up.fe.specs.binarytranslation.graph2dotty;

import pt.up.fe.specs.util.treenode.TreeNode;

public class DottyGenerator<K extends TreeNode<K>> extends TreeNodeWalker<K> {

    // TODO: generation options?

    public DottyGenerator() {

    }

    @Override
    public void visit(TreeNode<K> node) {

        // this node name
        var me = node.toContentString();
        var tagname = node.hashCode();

        // my label
        System.out.println(tagname + "[shape = box, label = \"" + me.replace("\n", "\\l") + "\"];");

        // my children
        for (var kid : node.getChildren())
            System.out.println(tagname + " -> " + kid.hashCode());

        // visit children
        super.visit(node);
    }

    /*
     * Puts each node in a dotty node and prints everything!
     */
    public void generateDotty(TreeNode<K> rootNode) {

        System.out.println("digraph D {");
        super.visit(rootNode);
        System.out.println("}");
    }

    // TODO: overload generateDotty with output file argument
}
