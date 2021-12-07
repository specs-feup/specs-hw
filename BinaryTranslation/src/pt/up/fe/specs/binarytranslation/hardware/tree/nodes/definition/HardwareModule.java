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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.ModuleBlock;
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

    private ModuleBlock body;
    private DeclarationBlock ports, wires, registers;

    public HardwareModule(String moduleName) {
        super(HardwareNodeType.HardwareModule);

        // child 0
        this.addChild(new FileHeader());

        // child 1
        this.body = new ModuleBlock(moduleName);
        this.addChild(this.body);

        this.ports = new DeclarationBlock("Ports"); // Port declarations
        this.wires = new DeclarationBlock("Wires"); // Wire declarations
        this.registers = new DeclarationBlock("Registers"); // register declarations

        // children 1.0, 1.1, and 1.2
        this.body.addChild(this.ports);
        this.body.addChild(this.wires);
        this.body.addChild(this.registers);
        // contains the header and body as children nodes,
        // those kids are fetched here for add and get operations
        // without need for replication of code
    }

    public HardwareModule(String moduleName, PortDeclaration... ports) {
        this(moduleName);
        for (var port : ports)
            this.addPort(port);
    }

    /* *****************************
     * Private stuff
     */
    private ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 1);
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

    @Override
    protected HardwareModule copyPrivate() {
        return new HardwareModule(this.getName()); // WRONG: this will also copy children...
    }

    /*
    public NewHardwareModule addInstance(NewHardawareModule instanceType, connectioons) {
        this.addChild(stat);
        return this;
    }*/

    // public NewHardwareModule addBlock8()

    @Override
    public String getName() {
        return this.body.getModuleName();
    }

    @Override
    public HardwareModule copy() {
        return (HardwareModule) super.copy();
    }

    /*
    @Override
    public String getAsString() {
        var sb = new StringBuilder();
        sb.append(this.getChild(0).getAsString() + "\n"); // File Header
        sb.append(this.getChild(1).getAsString() + "\n"); // Module Header
    
        // inner body (1 nest level)
        var nest = new StringBuilder();
        var bodyParts = this.getChildren().subList(2, this.getNumChildren());
        for (var part : bodyParts) {
            sb.append(part.getAsNestedString());
        }
    
    //        for (var part : bodyParts) {
    //        
    //            // add nesting
    //            var content = "    " + part.getAsString();
    //            content = content.replace("\n", "\n    ");
    //            nest.append(content);
    //        }
    
        sb.append(nest.toString());
        sb.append("endmodule\n");
        return sb.toString();
    }*/
}
