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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysFFBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.PosEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.SignalEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.ClockDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.ResetDeclaration;
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

    // TODO: move this counter logic into @ModuleBlock

    // counters used when no user specified names for blocks are given
    private int alwaysffCounter = 0;
    private int alwayscombCounter = 0;

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

    /*
     * Special ports
     */
    // addClock
    // addReset

    /*
     * Ports
     */
    public PortDeclaration addPort(PortDeclaration port) {
        this.getBody().ports.add(port); // this only adds to the port list in the header!
        this.getPortDeclarationBlock().addDeclaration(port);
        return port;
    }

    public ClockDeclaration addClock() {
        return addClock("clk");
    }

    public ClockDeclaration addClock(String clockName) {
        return (ClockDeclaration) addPort(new ClockDeclaration(clockName));
    }

    public ResetDeclaration addReset() {
        return addReset("rst");
    }

    public ResetDeclaration addReset(String rstName) {
        return (ResetDeclaration) addPort(new ResetDeclaration(rstName));
    }

    public InputPortDeclaration addInputPort(String portName, int portWidth) {
        return (InputPortDeclaration) addPort(new InputPortDeclaration(portName, portWidth));
    }

    public OutputPortDeclaration addOutputPort(String portName, int portWidth) {
        return (OutputPortDeclaration) addPort(new OutputPortDeclaration(portName, portWidth));
    }

    /*
     * Wires
     */
    public WireDeclaration addWire(WireDeclaration wire) {
        this.getWireDeclarationBlock().addDeclaration(wire);
        return wire;
    }

    public WireDeclaration addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    /*
     * registers
     */
    public RegisterDeclaration addRegister(RegisterDeclaration reg) {
        this.getRegisterDeclarationBlock().addDeclaration(reg);
        return reg;
    }

    public RegisterDeclaration addRegister(String regName, int portWidth) {
        return addRegister(new RegisterDeclaration(regName, portWidth));
    }

    /*
     * always comb blocks
     */
    public AlwaysCombBlock addAlwaysComb(String blockName) {
        var block = new AlwaysCombBlock(blockName);
        this.addCode(block);
        return block;
    }

    // create name if non given
    public AlwaysCombBlock addAlwaysCombBlock() {
        return addAlwaysComb("comb_" + this.alwayscombCounter++);
        // TODO: if I manually create a block called "comb_1" or "comb_2" etc, this will break
    }

    /*
     * always ff blocks (if no signal provided defaults to clk)
     * (if no clock on module, adds a clock declaration to the ports)
     */
    public AlwaysFFBlock addAlwaysFFPosedge(
            String blockName,
            ClockDeclaration clk,
            Function<VariableOperator, SignalEdge> edge) {

        var block = new AlwaysFFBlock(new PosEdge(clk.getReference()), blockName);
        this.addCode(block);
        return block;
    }

    public AlwaysFFBlock addAlwaysFFPosedge(String blockName) {

        var clks = this.getPorts(port -> port.isClock());

        VariableOperator clkref = null;
        if (!clks.isEmpty())
            clkref = clks.get(0).getReference();
        else {
            clkref = this.addClock().getReference();
        }

        var block = new AlwaysFFBlock(new PosEdge(clkref), blockName);
        this.addCode(block);
        return block;
    }

    public AlwaysFFBlock addAlwaysFFPosedge() {
        return addAlwaysFFPosedge("ff_" + this.alwaysffCounter++);
    }

    /*
     * statements
     */
    public HardwareStatement addStatement(HardwareStatement stat) {
        this.addCode(stat);
        return stat;
    }

    /*
     * instances of other modules
     * (instances can only be added as direct children of the module body,
     * i.e. first level children of the ModuleBlock)
     */
    public ModuleInstance addInstance(ModuleInstance instantiatedModule) {
        this.addCode(instantiatedModule);
        return instantiatedModule;
    }

    public ModuleInstance addInstance(HardwareModule instanceType,
            String instanceName, HardwareOperator... connections) {
        var instance = new ModuleInstance(instanceType, instanceName, connections);
        this.addCode(instance);
        return instance;
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
    public VariableOperator getPort(String portname) {
        // NOTE: return is known to be a VariableOperator at this point
        return (VariableOperator) this.getPortDeclarationBlock().getDeclaration(portname).get();
    }

    /*
     * get Port via predicate
     */
    public List<PortDeclaration> getPorts(Predicate<PortDeclaration> predicate) {
        return this.getPortDeclarations().stream().filter(predicate).collect(Collectors.toList());
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
     * get block by name
     */

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
