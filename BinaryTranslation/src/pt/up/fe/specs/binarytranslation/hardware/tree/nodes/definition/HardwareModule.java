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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.HardwareBlock;
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

    /*
     * This type of block only makes sense inside a @HarwareModule;
     * its ugly, but prevents @ModuleBlock from being instantiated
     * and having children added outside the context of a @HardwareModule;
     * the @ModuleBlock is required to nest the contents of the block 
     * when printing, and also simplifies the hierarchy of content in the 
     * tree
     * 
     * I also want to ensure that methods like getPortDeclarationBlock
     * dont break by ensuring that the internal structure of
     * @ModuleBlock is consistent across all instantiations, and
     * also that @HardwareModule has functions like addPort or addWire
     * exposed at its toplevel without need for code replication or
     * making some methods public, as would be required with a public
     * @ModuleBlock class 
     */
    private class ModuleBlock extends HardwareBlock {

        private String moduleName;
        private List<PortDeclaration> ports;

        public ModuleBlock(String moduleName) {
            super(HardwareNodeType.ModuleHeader);
            this.moduleName = moduleName;
            this.ports = new ArrayList<PortDeclaration>();

            // children 0, 1, and 1
            this.addChild(new DeclarationBlock("Ports")); // Port declarations
            this.addChild(new DeclarationBlock("Wires")); // Wire declarations
            this.addChild(new DeclarationBlock("Registers")); // register declarations
        }

        public ModuleBlock(ModuleBlock other) {
            super(HardwareNodeType.ModuleHeader);
            this.moduleName = other.moduleName;
            this.ports = other.ports;
        }

        @Override
        public String getAsString() {
            var builder = new StringBuilder();
            builder.append("\nmodule " + this.moduleName + "(");

            for (int i = 0; i < this.ports.size(); i++) {
                builder.append(ports.get(i).getVariableName());
                if (i < this.ports.size() - 1)
                    builder.append(", ");
            }
            builder.append(");\n");

            // GET ALL NESTED CONTENT IN BODY BLOCK
            builder.append(super.getAsString());

            builder.append("end //" + this.moduleName + "\n");
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
    private HardwareModule() {
        super(HardwareNodeType.HardwareModule);
    }

    /* *****************************
     * Private stuff
     */
    private ModuleBlock getBody() {
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
        if (getNumChildren() >= 2) {
            throw new RuntimeException(
                    "HardwareModule: Expected only two children! " +
                            "Use addCode() to add content to the module body!");
        }
        return super.addChild(node);
    }

    /* *****************************
     * Public stuff
     */
    @Override
    public ModuleInstance instantiate(String instanceName, HardwareOperator... connections) {
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

    /*
    public NewHardwareModule addInstance(NewHardawareModule instanceType, connectioons) {
        this.addChild(stat);
        return this;
    }*/

    // public NewHardwareModule addBlock8()

    @Override
    public String getName() {
        return this.getBody().moduleName;
    }

    @Override
    protected HardwareModule copyPrivate() {
        return new HardwareModule();
    }

    @Override
    public HardwareModule copy() {
        return (HardwareModule) super.copy();
    }
}
