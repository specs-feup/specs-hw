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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.HardwareDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
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
