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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.HardwareBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.NegEdge;
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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

public class HardwareModule extends HardwareDefinition implements ModuleBlockInterface {

    protected static int MAXCHILDREN;
    protected static String ADDCHILDERRMSG;
    static {
        MAXCHILDREN = 2;
        ADDCHILDERRMSG = "HardwareModule: Expected only two children! " +
                "Use addStatement() and addBlock() to add content to the module body!";
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
    @Override
    public ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 1);
    }

    /*
    
    private DeclarationBlock getPortDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 0);
    }
    
    private DeclarationBlock getWireDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 1);
    }
    
    private DeclarationBlock getRegisterDeclarationBlock() {
        return this.getBody().getChild(DeclarationBlock.class, 2);
    }
    */
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
    @Override
    public ClockDeclaration addClock() {
        return getBody().addClock();
    }

    @Override
    public ClockDeclaration addClock(String clockName) {
        return getBody().addClock(clockName);
    }

    @Override
    public ResetDeclaration addReset() {
        return getBody().addReset();
    }

    @Override
    public ResetDeclaration addReset(String rstName) {
        return getBody().addReset(rstName);
    }

    /*
     * Ports
     */
    @Override
    public PortDeclaration addPort(PortDeclaration port) {
        return getBody().addPort(port);
    }

    @Override
    public InputPortDeclaration addInputPort(String portName, int portWidth) {
        return getBody().addInputPort(portName, portWidth);
    }

    @Override
    public OutputPortDeclaration addOutputPort(String portName, int portWidth) {
        return getBody().addOutputPort(portName, portWidth);
    }

    /*
     * Wires
     */
    @Override
    public WireDeclaration addWire(WireDeclaration wire) {
        return getBody().addWire(wire);
    }

    @Override
    public WireDeclaration addWire(String portName, int portWidth) {
        return getBody().addWire(portName, portWidth);
    }

    /*
     * registers
     */
    @Override
    public RegisterDeclaration addRegister(RegisterDeclaration reg) {
        return getBody().addRegister(reg);
    }

    @Override
    public RegisterDeclaration addRegister(String regName, int portWidth) {
        return getBody().addRegister(regName, portWidth);
    }

    /*
     * Add code to tier-0 body! (i.e., inside the module - endmodule, but outside any 
     * other blocks, like always or initials)
     */
    public HardwareStatement addStatement(HardwareStatement stat) {
        return getBody().addStatement(stat);
    }

    @Override
    public HardwareBlock addBlock(HardwareBlock block) {
        return getBody().addBlock(block);
    }

    /*
     * add always_comb blocks
     */
    public AlwaysCombBlock addAlwaysComb(String blockName) {
        var block = new AlwaysCombBlock(blockName);
        this.getBody().addChild(block);
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
    private AlwaysFFBlock addAlwaysFF(
            String blockName, ClockDeclaration clk,
            Function<VariableOperator, SignalEdge> edge) {

        var block = new AlwaysFFBlock(edge.apply(clk.getReference()), blockName);
        this.getBody().addChild(block);
        return block;
    }

    /*
     * aux clock getter
     */
    private ClockDeclaration getClockForAlways() {

        var clks = this.getPorts(port -> port.isClock());

        ClockDeclaration clk = null;
        if (!clks.isEmpty())
            clk = (ClockDeclaration) clks.get(0);
        else {
            clk = this.addClock();
        }
        return clk;
    }

    /*
     * Alwaysff posedge
     */
    public AlwaysFFBlock addAlwaysFFPosedge(String blockName, ClockDeclaration clk) {
        return addAlwaysFF(blockName, clk, (signal) -> new PosEdge(signal));
    }

    public AlwaysFFBlock addAlwaysFFPosedge(String blockName) {
        return addAlwaysFFPosedge(blockName, getClockForAlways());
    }

    public AlwaysFFBlock addAlwaysFFPosedge() {
        return addAlwaysFFPosedge("ff_" + this.alwaysffCounter++);
    }

    /*
     * Alwaysff negedge
     */
    public AlwaysFFBlock addAlwaysFFNegedge(String blockName, ClockDeclaration clk) {
        return addAlwaysFF(blockName, clk, (signal) -> new NegEdge(signal));
    }

    public AlwaysFFBlock addAlwaysFFNegedge(String blockName) {
        return addAlwaysFFNegedge(blockName, getClockForAlways());
    }

    public AlwaysFFBlock addAlwaysFFNegedge() {
        return addAlwaysFFNegedge("ff_" + this.alwaysffCounter++);
    }

    /*
     * instances of other modules
     * (instances can only be added as direct children of the module body,
     * i.e. first level children of the ModuleBlock)
     */
    public ModuleInstance addInstance(ModuleInstance instantiatedModule) {
        this.addChild(instantiatedModule);
        return instantiatedModule;
    }

    public ModuleInstance addInstance(HardwareModule instanceType,
            String instanceName, HardwareOperator... connections) {
        var instance = new ModuleInstance(instanceType, instanceName, connections);
        this.addChild(instance);
        return instance;
    }

    @Override
    public final List<ModuleInstance> getInstances() {
        return this.getChildren(ModuleInstance.class);
    }

    /*
     * get Port as a reference
     */
    @Override
    public VariableOperator getPort(int idx) {
        return this.getPortDeclarations().get(idx).getReference();
    }

    /*
     * get Port by name
     */
    @Override
    public VariableOperator getPort(String portname) {
        // NOTE: return is known to be a VariableOperator at this point
        return (VariableOperator) this.getPortDeclarationBlock().getDeclaration(portname).get();
    }

    /*
     * get Port via predicate
     */
    @Override
    public List<PortDeclaration> getPorts(Predicate<PortDeclaration> predicate) {
        return this.getPortDeclarations().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * get Wire by name
     */
    @Override
    public HardwareOperator getWire(String wirename) {
        return this.getWireDeclarationBlock().getDeclaration(wirename).get();
    }

    /*
     * get Reg by name
     */
    @Override
    public HardwareOperator getRegister(String regname) {
        return this.getRegisterDeclarationBlock().getDeclaration(regname).get();
    }

    /*
     * get block by name
     */

    /*
     * Get port lists
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
        return getBody().moduleName;
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
