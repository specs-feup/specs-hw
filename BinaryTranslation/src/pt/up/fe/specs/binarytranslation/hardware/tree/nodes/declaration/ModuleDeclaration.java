package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ModuleDeclaration extends HardwareDeclaration {

    /*
     * 
     */
    private String moduleName;

    // TODO: what else??

    public ModuleDeclaration(String moduleName) {
        this.moduleName = moduleName;
        this.type = HardwareNodeType.ModuleDeclaration;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("module " + this.moduleName + ";\n");

        /*
         * The children should be port declarations, followed by body (?)
         */
        for (HardwareNode comp : this.getChildren()) {
            builder.append(comp.getAsString() + "\n");
        }

        builder.append("endmodule");
        return builder.toString();
    }

    @Override
    public String toContentString() {
        return "module " + this.moduleName;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new ModuleDeclaration(this.moduleName);
    }
}
