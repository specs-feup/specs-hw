package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class InstructionASTNode extends ATreeNode<InstructionASTNode> {

    protected InstructionASTNodeType type;

    public InstructionASTNode() {
        super(null);
    }

    public InstructionASTNodeType getType() {
        return type;
    }

    public abstract String getAsString();

    @Override
    public InstructionASTNode getThis() {
        return this;
    }
    /*
    @Override
    protected InstructionASTNode copyPrivate() {
        Constructor<? extends InstructionASTNode> cs = null;
        InstructionASTNode node = null;
    
        try {
            cs = getThis().getClass().getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        try {
            node = cs.newInstance();
    
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        return node;
    }*/
}
