package pt.up.fe.specs.binarytranslation.hardware.tree;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.*;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;

public class VerilogModuleTree extends AHardwareTree {

    private FileHeader header; // first child of tree root
    private DeclarationBlock declarations; // second child of tree root
    private ModuleDeclaration module;

    public VerilogModuleTree(String moduleName) {
        super();
        this.header = new FileHeader();
        this.module = new ModuleDeclaration(moduleName);
        this.root.addChild(this.header);
        this.root.addChild(this.module);

        this.declarations = new DeclarationBlock();
        this.module.addChild(declarations);
    }

    public void addDeclaration(HardwareDeclaration declare) {
        this.module.getChild(0).addChild(declare);
    }

    public DeclarationBlock getDeclarations() {
        return declarations;
    }

    public ModuleDeclaration getModule() {
        return module;
    }
}
