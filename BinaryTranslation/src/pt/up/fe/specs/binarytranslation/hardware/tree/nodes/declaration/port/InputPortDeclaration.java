/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port;

import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.VariableDeclaration;

public class InputPortDeclaration extends PortDeclaration{

    /*
    public InputPortDeclaration(String portName, int portWidth) {
        super(portName, portWidth, ModulePortDirection.input);
    }
    
    public InputPortDeclaration(String portName, int portWidth, VerilogModuleTree moduleTree) {
        super(portName, portWidth, ModulePortDirection.input);
        
        moduleTree.addDeclaration(this);
        
    }
    */
    
    public InputPortDeclaration(VariableDeclaration port) {
        super(port, ModulePortDirection.input);
    }
    
    public InputPortDeclaration(VariableDeclaration port, VerilogModuleTree moduleTree) {
        this(port);
        moduleTree.addDeclaration(this);
    }
    
}
