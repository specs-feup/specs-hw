package pt.up.fe.specs.binarytranslation.hardware.tree;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;

public class VerilogModuleTree extends AHardwareTree {

    private FileHeader header; // first child of tree root
    private DeclarationBlock declarations; // second child of tree root

    public VerilogModuleTree(String moduleName) {
        super();
        this.header = new FileHeader();
        this.declarations = new DeclarationBlock();
        this.root.addChild(this.header);
        this.root.addChild(this.declarations);
        this.root.addChild(new ModuleDeclaration(moduleName));
    }

    public DeclarationBlock getDeclarations() {
        return declarations;
    }
}
