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

package pt.up.fe.specs.binarytranslation.hardware.tree;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;

public class VerilogModuleTree extends AHardwareTree {

    private FileHeader header; // first child of tree root
    private ModuleHeader module; // second child of tree root
    private DeclarationBlock portDeclarations; // 1st child of ModuleHeader
    private DeclarationBlock wireDeclarations; // 2nd child of ModuleHeader
    private DeclarationBlock registerDeclarations; // 3rd child of ModuleHeader
    // private DeclarationBlock moduleDeclarations; // 4th child of ModuleHeader

    public VerilogModuleTree(String moduleName) {
        super();

        // two children of root
        this.header = new FileHeader();
        this.module = new ModuleHeader(moduleName);
        this.root.addChild(this.header);
        this.root.addChild(this.module);

        // 4 children of second child of root
        this.portDeclarations = new DeclarationBlock();
        this.wireDeclarations = new DeclarationBlock();
        this.registerDeclarations = new DeclarationBlock();
        // this.moduleDeclarations = new DeclarationBlock();

        this.module.addChild(this.portDeclarations);
        this.module.addChild(this.wireDeclarations);
        this.module.addChild(this.registerDeclarations);
        // this.module.addChild(this.moduleDeclarations);
    }

    public VerilogModuleTree addPort(PortDeclaration declare) {
        this.portDeclarations.addChild(declare);
        return this;
    }

    public VerilogModuleTree addStatement(HardwareNode node) {
        this.module.addChild(node);
        return this;
    }

    @Override
    public List<PortDeclaration> getPortDeclarations() {
        return portDeclarations.getChildren(PortDeclaration.class);
    }

    @Override
    public List<InputPortDeclaration> getInputPortDeclarations() {
        return portDeclarations.getChildrenOf(InputPortDeclaration.class);
    }

    @Override
    public List<OutputPortDeclaration> getOutputPortDeclarations() {
        return portDeclarations.getChildrenOf(OutputPortDeclaration.class);
    }

    /*    public ModuleHeader getModule() {
        return module;
    }*/

    // TODO: helpers like "newRegister" which returns the RegisterDeclaration
    // this helps reduce the verbosity of the "new" keywords in user code
}
