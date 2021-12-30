/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.crispy.ast.block;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.port.PortDeclaration;
import pt.up.fe.specs.crispy.ast.meta.DeclarationBlock;

/*
 * This type of block only makes sense inside a HarwareModule or a Hardwaretestbench. Its ugly, but prevents ModuleBlock
 * from being instantiated and having children added outside the context of a HardwareModule; the ModuleBlock is
 * required to nest the contents of the block when printing, and also simplifies the hierarchy of content in the tree
 * 
 * I also want to ensure that methods like getPortDeclarationBlock dont break by ensuring that the internal structure of
 * ModuleBlock is consistent across all instantiations, and also that HardwareModule has functions like addPort or
 * addWire exposed at its toplevel without need for code replication or making some methods public, as would be required
 * with a public ModuleBlock class
 * 
 * ModuleBlock is **package private.**
 * 
 * The ModuleBlock is the only block type that allows other HardwareBlocks as children, for example always_comb,
 * initial, etc; it is also the only type of block that allows children of type ModuleInstance
 */
class ModuleBlock extends HardwareBlock {

    protected String moduleName;
    protected List<PortDeclaration> ports;

    // counters used when no user specified names for blocks are given
    protected int alwaysffCounter = 0;
    protected int alwayscombCounter = 0;

    public ModuleBlock(String moduleName) {
        super(HardwareNodeType.ModuleBlock);
        this.moduleName = moduleName;
        this.ports = new ArrayList<PortDeclaration>();

        // children 0, 1, and 1
        this.addChild(new DeclarationBlock("Ports")); // Port declarations
        this.addChild(new DeclarationBlock("Wires")); // Wire declarations
        this.addChild(new DeclarationBlock("Registers")); // register declarations
    }

    /*
     * copy without children
     */
    private ModuleBlock(ModuleBlock other) {
        super(HardwareNodeType.ModuleBlock);
        this.moduleName = other.moduleName;
        this.ports = new ArrayList<PortDeclaration>();
        for (var port : other.ports)
            this.ports.add(port.copy());
    }

    @Override
    protected HardwareBlock getBody() {
        return this;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("\nmodule " + this.moduleName);

        if (this.ports.size() > 0) {
            builder.append("(");
            for (int i = 0; i < this.ports.size(); i++) {
                builder.append(ports.get(i).getVariableName());
                if (i < this.ports.size() - 1)
                    builder.append(", ");
            }
            builder.append(")");
        }
        builder.append(";\n\n");

        // GET ALL NESTED CONTENT IN BODY BLOCK
        builder.append(nestContent(super.getAsString()));

        builder.append("endmodule //" + this.moduleName + "\n");
        return builder.toString();
    }

    @Override
    protected ModuleBlock copyPrivate() {
        return new ModuleBlock(this);
    }

    @Override
    public ModuleBlock copy() {
        return (ModuleBlock) super.copy();
    }
}
