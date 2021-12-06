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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition;

import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

public class NewHardwareModule extends HardwareDefinition {

    private final String moduleName;

    public NewHardwareModule(String moduleName) {
        super(HardwareNodeType.ModuleDefinition);
        this.moduleName = moduleName;

        this.addChild(new FileHeader());
        this.addChild(new ModuleHeader(moduleName));
        this.addChild(new DeclarationBlock("Ports")); // Port declarations
        this.addChild(new DeclarationBlock("Wires")); // Wire declarations
        this.addChild(new DeclarationBlock("Registers")); // register declarations
    }

    /* *****************************
     * Private stuff
     */
    private ModuleHeader getHeader() {
        return this.getChild(ModuleHeader.class, 1);
    }

    private DeclarationBlock getPortDeclarationBlock() {
        return this.getChild(DeclarationBlock.class, 2);
    }

    private DeclarationBlock getWireDeclarationBlock() {
        return this.getChild(DeclarationBlock.class, 3);
    }

    private DeclarationBlock getRegisterDeclarationBlock() {
        return this.getChild(DeclarationBlock.class, 4);
    }

    private NewHardwareModule addCode(HardwareNode node) {
        this.addChild(node);
        return this;
    }

    /* *****************************
     * Public stuff
     */
    public NewHardwareModule addPort(PortDeclaration port) {
        this.getHeader().addChild(port);
        this.getPortDeclarationBlock().addChild(port);
        return this;
    }

    public NewHardwareModule addInputPort(String portName, int portWidth) {
        return addPort(new InputPortDeclaration(portName, portWidth));
    }

    public NewHardwareModule addOutputPort(String portName, int portWidth) {
        return addPort(new OutputPortDeclaration(portName, portWidth));
    }

    public NewHardwareModule addWire(WireDeclaration wire) {
        this.getWireDeclarationBlock().addChild(wire);
        return this;
    }

    public NewHardwareModule addRegister(RegisterDeclaration reg) {
        this.getRegisterDeclarationBlock().addChild(reg);
        return this;
    }

    public NewHardwareModule addStatement(HardwareStatement stat) {
        return this.addCode(stat);
    }

    public NewHardwareModule addInstance(ModuleInstance instantiatedModule) {
        return this.addCode(instantiatedModule);
    }

    /*
    public NewHardwareModule addInstance(NewHardawareModule instanceType, connectioons) {
        this.addChild(stat);
        return this;
    }*/

    // public NewHardwareModule addBlock8()

    @Override
    public String getName() {
        return this.moduleName;
    }

    /*
     * get Port as a reference
     */
    public VariableOperator getPort(int idx) {
        return this.getPortDeclarations().get(idx).getReference();
    }

    /*
     * 
     */
    public List<PortDeclaration> getPortDeclarations() {
        var block = this.getPortDeclarationBlock();
        return block.getChildrenOf(PortDeclaration.class);
    }

    public List<InputPortDeclaration> getInputPortDeclarations() {
        var block = this.getPortDeclarationBlock();
        return block.getChildrenOf(InputPortDeclaration.class);
    }

    public List<OutputPortDeclaration> getOutputPortDeclarations() {
        var block = this.getPortDeclarationBlock();
        return block.getChildrenOf(OutputPortDeclaration.class);
    }

    @Override
    protected HardwareNode copyPrivate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NewHardwareModule copy() {
        // TODO Auto-generated method stub
        return (NewHardwareModule) super.copy();
    }

    @Override
    public String getAsString() {
        /*
        var sb = new StringBuilder();
        for (var child : this.getChildren())
            sb.append(child.getAsString() + "\n");
        sb.append("endmodule\n");*/

        var sb = new StringBuilder();
        sb.append(super.getAsString());
        sb.append("endmodule\n");
        return sb.toString();
    }
}
