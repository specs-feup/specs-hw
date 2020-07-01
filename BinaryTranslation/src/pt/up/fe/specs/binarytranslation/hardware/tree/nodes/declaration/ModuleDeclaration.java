package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class ModuleDeclaration extends AHardwareNode implements HardwareDeclaration {

    /*
     * 
     */
    private String moduleName;

    // TODO: what else??

    public ModuleDeclaration(String moduleName) {
        this.moduleName = moduleName;
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
}
