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

    protected static int MAXCHILDREN;
    protected static String ADDCHILDERRMSG;
    static {
        MAXCHILDREN = 2;
        ADDCHILDERRMSG = "HardwareModule: Expected only two children! " +
                "Use addCode() to add content to the module body!";
    }

    /*
     * Outer most node of the Hardware module definition, 
     * which includes copyright, header and body
     */
    public HardwareModule(String moduleName) {
        super(HardwareNodeType.HardwareModule);

        // child 0
        this.addChild(new FileHeader());

        // child 1
        this.addChild(new ModuleBlock(moduleName));
    }

    public HardwareModule(String moduleName, PortDeclaration... ports) {
        this(moduleName);
        for (var port : ports)
            this.addPort(port);
    }

    /*
     * For copying (children are handled as usual by @ATreeNode.copy)
     */
    protected HardwareModule(HardwareNodeType type) {
        super(type);
    }

    /* *****************************
     * Private stuff
     */
    protected ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 1);
    }

    private DeclarationBlock getPortDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 0);
    }

    private DeclarationBlock getWireDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 1);
    }

    private DeclarationBlock getRegisterDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 2);
    }

    // TODO: enforce a more specific typing here?
    public HardwareNode addCode(HardwareNode node) {
        this.getBody().addChild(node);
        return node;
    }

    @Override
    public HardwareNode addChild(HardwareNode node) {
        if (getNumChildren() >= MAXCHILDREN)
            throw new RuntimeException(ADDCHILDERRMSG);
        return super.addChild(node);
    }

    /* *****************************
     * Public stuff
     */
    @Override
    public ModuleInstance instantiate(String instanceName, List<HardwareOperator> connections) {
        return new ModuleInstance(this, instanceName, connections);
    }

    public PortDeclaration addPort(PortDeclaration port) {
        this.getBody().ports.add(port); // this only adds to the port list in the header!
        this.getPortDeclarationBlock().addDeclaration(port);
        return port;
    }

    public InputPortDeclaration addInputPort(String portName, int portWidth) {
        return (InputPortDeclaration) addPort(new InputPortDeclaration(portName, portWidth));
    }

    public OutputPortDeclaration addOutputPort(String portName, int portWidth) {
        return (OutputPortDeclaration) addPort(new OutputPortDeclaration(portName, portWidth));
    }

    public WireDeclaration addWire(WireDeclaration wire) {
        this.getWireDeclarationBlock().addDeclaration(wire);
        return wire;
    }

    public WireDeclaration addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    public RegisterDeclaration addRegister(RegisterDeclaration reg) {
        this.getRegisterDeclarationBlock().addDeclaration(reg);
        return reg;
    }

    public RegisterDeclaration addRegister(String regName, int portWidth) {
        return addRegister(new RegisterDeclaration(regName, portWidth));
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
    public NewHardwareModule addInstance(HardawareModule instanceType, connectioons) {
        this.addChild(stat);
        return this;
    }*/

    // public NewHardwareModule addBlock8()

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
    public String getName() {
        return this.getBody().moduleName;
    }

    @Override
    protected HardwareModule copyPrivate() {
        return new HardwareModule(this.type);
    }

    @Override
    public HardwareModule copy() {
        return (HardwareModule) super.copy();
    }
}
