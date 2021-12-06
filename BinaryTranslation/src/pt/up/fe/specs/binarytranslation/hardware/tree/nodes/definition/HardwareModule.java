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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

public class HardwareModule extends HardwareDefinition {

    private final String moduleName;

    public HardwareModule(String moduleName) {
        super(HardwareNodeType.ModuleDefinition);
        this.moduleName = moduleName;

        this.addChild(new FileHeader());
        this.addChild(new ModuleHeader(moduleName));
        this.addChild(new DeclarationBlock("Ports")); // Port declarations
        this.addChild(new DeclarationBlock("Wires")); // Wire declarations
        this.addChild(new DeclarationBlock("Registers")); // register declarations
    }

    public HardwareModule(String moduleName, PortDeclaration... ports) {
        this(moduleName);
        for (var port : ports)
            this.addPort(port);
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

    private HardwareNode addCode(HardwareNode node) {
        this.addChild(node);
        return node;
    }

    /* *****************************
     * Public stuff
     */
    @Override
    public ModuleInstance instantiate(String instanceName, HardwareOperator... connections) {
        return new ModuleInstance(this, instanceName, connections);
    }

    public PortDeclaration addPort(PortDeclaration port) {
        this.getHeader().addChild(port);
        this.getPortDeclarationBlock().addChild(port);
        return port;
    }

    public InputPortDeclaration addInputPort(String portName, int portWidth) {
        return (InputPortDeclaration) addPort(new InputPortDeclaration(portName, portWidth));
    }

    public OutputPortDeclaration addOutputPort(String portName, int portWidth) {
        return (OutputPortDeclaration) addPort(new OutputPortDeclaration(portName, portWidth));
    }

    public WireDeclaration addWire(WireDeclaration wire) {
        this.getWireDeclarationBlock().addChild(wire);
        return wire;
    }

    public WireDeclaration addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    public RegisterDeclaration addRegister(RegisterDeclaration reg) {
        this.getRegisterDeclarationBlock().addChild(reg);
        return reg;
    }

    public HardwareStatement addStatement(HardwareStatement stat) {
        this.addCode(stat);
        return stat;
    }

    public ModuleInstance addInstance(ModuleInstance instantiatedModule) {
        this.addCode(instantiatedModule);
        return instantiatedModule;
    }

    /*
     * get Port as a reference
     */
    public VariableOperator getPort(int idx) {
        return this.getPortDeclarations().get(idx).getReference();
    }

    /*
     * get Port by name
     */
    public HardwareOperator getPort(String portname) {
        return this.getPortDeclarationBlock().getDeclaration(portname).get();
    }

    /*
     * get Wire by name
     */
    public HardwareOperator getWire(String wirename) {
        return this.getWireDeclarationBlock().getDeclaration(wirename).get();
    }

    /*
     * get Reg by name
     */
    public HardwareOperator getRegister(String regname) {
        return this.getRegisterDeclarationBlock().getDeclaration(regname).get();
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
    protected HardwareModule copyPrivate() {
        return new HardwareModule(this.moduleName);
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

    @Override
    public HardwareModule copy() {
        return (HardwareModule) super.copy();
    }

    @Override
    public String getAsString() {
        var sb = new StringBuilder();
        sb.append(super.getAsString());
        sb.append("endmodule\n");
        return sb.toString();
    }
}
