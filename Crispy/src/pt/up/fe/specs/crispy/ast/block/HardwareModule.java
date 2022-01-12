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

package pt.up.fe.specs.crispy.ast.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.constructs.NegEdge;
import pt.up.fe.specs.crispy.ast.constructs.PosEdge;
import pt.up.fe.specs.crispy.ast.constructs.SignalEdge;
import pt.up.fe.specs.crispy.ast.declaration.ArrayDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.RegisterDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.ClockDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.PortDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.ResetDeclaration;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;
import pt.up.fe.specs.crispy.ast.expression.operator.Register;
import pt.up.fe.specs.crispy.ast.expression.operator.VArray;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.ast.meta.DeclarationBlock;
import pt.up.fe.specs.crispy.ast.meta.FileHeader;
import pt.up.fe.specs.crispy.ast.statement.ContinuousStatement;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;
import pt.up.fe.specs.specshw.SpecsHwUtils;

public class HardwareModule extends HardwareBlock { // implements ModuleBlockInterface {

    protected static int MAXCHILDREN;
    protected static String ADDCHILDERRMSG;
    static {
        MAXCHILDREN = 2;
        ADDCHILDERRMSG = "HardwareModule: Expected only two children! " +
                "Use addStatement() and addBlock() to add content to the module body!";
    }

    /*
     * Outer most node of the Hardware module definition, 
     * which includes copyright, header and body
     */
    public HardwareModule(String moduleName) {
        super(HardwareNodeType.HardwareModule);

        // child 0
        this.addChild(new FileHeader(SpecsHwUtils.generateFileHeader()));

        // child 1
        this.addChild(new ModuleBlock(moduleName));

        // this.assign = new assignMethods(this.getBody());
    }

    public HardwareModule(String moduleName, PortDeclaration... ports) {
        this(moduleName);
        for (var port : ports)
            this.addPort(port);
    }

    /* *****************************
     * For copying (children are handled as usual by @ATreeNode.copy)
     */
    protected HardwareModule(HardwareNodeType type) {
        super(type);
    }

    @Override
    public ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 1);
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

    @Override
    public String getAsString() {
        return super.getAsString();
    }

    @Override
    public void emit() {

        // emit this
        super.emit();

        var childrenModules = this.getInstances();
        for (var mod : childrenModules) {
            var def = mod.getModuleDefinition();
            def.emit();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public List<PortDeclaration> getPortList() {
        return this.getBody().ports;
    }

    public DeclarationBlock getPortDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 0);
    }

    public DeclarationBlock getWireDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 1);
    }

    public DeclarationBlock getRegisterDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 2);
    }

    /////////////////////////////////////////////////////////////////////////////
    // ADDERS ///////////////////////////////////////////////////////////////////

    public VariableOperator addDeclaration(IdentifierDeclaration id) {
        if (id instanceof PortDeclaration)
            return addDeclaration((PortDeclaration) id);

        else if (id instanceof WireDeclaration)
            return addDeclaration((WireDeclaration) id);

        else if (id instanceof RegisterDeclaration)
            return addDeclaration((RegisterDeclaration) id);

        else
            return id.getReference();
    }

    public Port addDeclaration(PortDeclaration port) {
        return this.addPort(port);
    }

    public Wire addDeclaration(WireDeclaration wire) {
        return this.addWire(wire);
    }

    public Register addDeclaration(RegisterDeclaration reg) {
        return this.addRegister(reg);
    }

    /*
     * Special ports
     */
    public InputPort addClock() {
        return addClock("clk");
    }

    public InputPort addClock(String clockName) {
        return (InputPort) addPort(new ClockDeclaration(clockName));
    }

    public InputPort addReset() {
        return addReset("rst");
    }

    public InputPort addReset(String rstName) {
        return (InputPort) addPort(new ResetDeclaration(rstName));
    }

    /*
     * Ports
     */
    protected Port addPort(PortDeclaration port) {
        getPortList().add(port); // this only adds to the port list in the header!
        getPortDeclarationBlock().addDeclaration(port);
        return (Port) port.getReference();
    }

    public InputPort addInputPort(String portName, int portWidth) {
        return (InputPort) addPort(new InputPortDeclaration(portName, portWidth));
    }

    public OutputPort addOutputPort(String portName, int portWidth) {
        return (OutputPort) addPort(new OutputPortDeclaration(portName, portWidth));
    }
    
    public OutputPort addOutputRegisterPort(String portName, int portWidth) {
        return (OutputPort) addPort(new OutputPortDeclaration(portName, portWidth, true));
    }

    /*
     * Wires
     */
    public Wire addWire(WireDeclaration wire) {
        return ((WireDeclaration) getWireDeclarationBlock().addDeclaration(wire)).getReference();
    }

    public Wire addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    /*
     * registers
     */
    public Register addRegister(RegisterDeclaration reg) {
        return ((RegisterDeclaration) getRegisterDeclarationBlock().addDeclaration(reg)).getReference();
    }

    public Register addRegister(String regName, int portWidth) {
        return addRegister(new RegisterDeclaration(regName, portWidth));
    }

    /*
     * Arrays??
     */
    public VArray addArray(ArrayDeclaration array) {
        var type = array.getVariable().getType();

        if (type == HardwareNodeType.WireDeclaration) {
            getWireDeclarationBlock().addDeclaration(array);
        } else if (type == HardwareNodeType.RegisterDeclaration) {
            getRegisterDeclarationBlock().addDeclaration(array);
        }

        // TODO: other array types

        return array.getReference();
    }

    /*
     * The ModuleBlock is the only block type that allows other HardwareBlocks as children, 
     * for example always_comb, initial, etc; it is also the only type of block 
     * that allows children of type ModuleInstance
     */
    public HardwareBlock addBlock(HardwareBlock block) {
        sanityCheck(block);
        getBody().addChild(block);
        return block;
    }

    public HardwareBlock addBlockBefore(HardwareBlock block, HardwareNode other) {
        sanityCheck(block);
        getBody().addChildLeftOf(other, block);
        return block;
    }

    public HardwareBlock addBlockAfter(HardwareBlock block, HardwareNode other) {
        sanityCheck(block);
        getBody().addChildRightOf(other, block);
        return block;
    }

    /*
     * add always_comb blocks
     */
    public AlwaysCombBlock alwayscomb(String blockName) {
        return (AlwaysCombBlock) addBlock(new AlwaysCombBlock(blockName));
    }

    // create name if non given
    public AlwaysCombBlock alwayscomb() {
        return (AlwaysCombBlock) addBlock(new AlwaysCombBlock("comb_" + getBody().alwayscombCounter++));
        // TODO: if I manually create a block called "comb_1" or "comb_2" etc, this will break
    }

    /*
     * always ff blocks (if no signal provided defaults to clk)
     * (if no clock on module, adds a clock declaration to the ports)
     */
    public AlwaysFFBlock alwaysff(
            String blockName, VariableOperator clk,
            Function<VariableOperator, SignalEdge> edge) {

        var block = new AlwaysFFBlock(edge.apply(clk), blockName);
        getBody().addChild(block);
        return block;
    }

    /*
     * Alwaysff posedge
     */
    public AlwaysFFBlock alwaysposedge(String blockName, VariableOperator clk) {
        return alwaysff(blockName, clk, (signal) -> new PosEdge(signal));
    }

    public AlwaysFFBlock alwaysposedge(String blockName) {
        return alwaysposedge(blockName, getClock());
    }

    public AlwaysFFBlock alwaysposedge() {
        return alwaysposedge("ff_" + getBody().alwaysffCounter++);
    }

    /*
     * Alwaysff negedge
     */
    public AlwaysFFBlock alwaysnegedge(String blockName, VariableOperator clk) {
        return alwaysff(blockName, clk, (signal) -> new NegEdge(signal));
    }

    public AlwaysFFBlock alwaysnegedge(String blockName) {
        return alwaysnegedge(blockName, getClock());
    }

    public AlwaysFFBlock alwaysnegedge() {
        return alwaysnegedge("ff_" + getBody().alwaysffCounter++);
    }

    /*
     * initial
     */
    public InitialBlock initial() {
        return initial("");
    }

    public InitialBlock initial(String blockName) {
        var block = new InitialBlock(blockName);
        getBody().addChild(block);
        return block;
    }

    /*
     * instances of other modules
     * (instances can only be added as direct children of the module body,
     * i.e. first level children of the ModuleBlock)
     */
    public HardwareModule instantiate(HardwareModule instance, List<? extends HardwareOperator> connections) {
        getBody()._do(new ModuleInstance(instance,
                instance.getName() + "_" + Integer.toString(instance.hashCode()).substring(0, 4), connections));
        return instance;
    }

    public HardwareModule instantiate(HardwareModule instance,
            List<? extends HardwareOperator> connections1,
            HardwareOperator... connections2) {
        var connections = new ArrayList<HardwareOperator>();
        connections.addAll(connections1);
        connections.addAll(Arrays.asList(connections2));
        return instantiate(instance, connections);
    }

    public HardwareModule instantiate(HardwareModule instance, HardwareOperator... connections) {
        return instantiate(instance, Arrays.asList(connections));
    }

    /*
     * assign
     */
    public VariableOperator assign(HardwareExpression expr) {
        return createAssigment(expr.getName(), expr, (t, u) -> new ContinuousStatement(t, u));
    }

    public VariableOperator assign(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ContinuousStatement(t, u));
    }

    public VariableOperator assign(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ContinuousStatement(t, u));
    }

    public VariableOperator assign(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getWidth());
        return createAssigment(target, imm, (t, u) -> new ContinuousStatement(t, u));
    }

    public VariableOperator assign(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ContinuousStatement(t, u));
    }

    /////////////////////////////////////////////////////////////////////////////
    // GETTERS //////////////////////////////////////////////////////////////////

    public InputPort getClock() {

        var clks = getPorts(port -> port.isClock());

        ClockDeclaration clk = null;
        if (!clks.isEmpty())
            clk = (ClockDeclaration) clks.get(0);
        else {
            clk = (ClockDeclaration) addClock().getAssociatedIdentifier();
        }
        return clk.getReference();
    }

    /*
     * *****************************************************************************
     * Get port lists
     */
    public List<PortDeclaration> getPorts() {
        return getPortDeclarationBlock().getChildrenOf(PortDeclaration.class);
    }

    public List<InputPortDeclaration> getInputPorts() {
        return getPortDeclarationBlock().getChildrenOf(InputPortDeclaration.class);
    }

    public List<OutputPortDeclaration> getOutputPorts() {
        return getPortDeclarationBlock().getChildrenOf(OutputPortDeclaration.class);
    }

    /*
     * get Port as a index
     */
    public VariableOperator getPort(int idx) {
        return getPorts().get(idx).getReference();
    }

    /*
     * get Port by name
     */
    public VariableOperator getPort(String portname) {
        // NOTE: return is known to be a VariableOperator at this point
        return (VariableOperator) getPortDeclarationBlock().getDeclaration(portname);
    }

    /*
     * get Port via predicate
     */
    public List<PortDeclaration> getPorts(Predicate<PortDeclaration> predicate) {
        return getPorts().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get wire lists
     */
    public List<WireDeclaration> getWires() {
        return getWireDeclarationBlock().getChildrenOf(WireDeclaration.class);
    }

    /*
     * get Wire as a index
     */
    public HardwareOperator getWire(int idx) {
        return getWires().get(idx).getReference();
    }

    /*
     * get Wire by name
     */
    public HardwareOperator getWire(String wirename) {
        return getWireDeclarationBlock().getDeclaration(wirename);
    }

    /*
     * get wire via predicate
     */
    public List<WireDeclaration> getWires(Predicate<WireDeclaration> predicate) {
        return getWires().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get Reg lists
     */
    public List<RegisterDeclaration> getRegisters() {
        return getRegisterDeclarationBlock().getChildrenOf(RegisterDeclaration.class);
    }

    /*
     * get Reg as a index
     */
    public HardwareOperator getRegister(int idx) {
        return getRegisters().get(idx).getReference();
    }

    /*
     * get Reg by name
     */
    public HardwareOperator getRegister(String regname) {
        return getRegisterDeclarationBlock().getDeclaration(regname);
    }

    /*
     * get Reg via predicate
     */
    public List<RegisterDeclaration> getRegisters(Predicate<RegisterDeclaration> predicate) {
        return getRegisters().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get any declaration that matches name
     */
    public HardwareOperator getDeclaration(String name) {

        HardwareOperator operator = null;

        if ((operator = getPortDeclarationBlock().getDeclaration(name)) != null)
            return operator;

        else if ((operator = getWireDeclarationBlock().getDeclaration(name)) != null)
            return operator;

        else if ((operator = getRegisterDeclarationBlock().getDeclaration(name)) != null)
            return operator;

        else
            return null;
    }

    /*
     * *****************************************************************************
     * get blocks list
     */
    public List<HardwareBlock> getBlocks() {
        return getBody().getChildren(HardwareBlock.class);
    }

    /*
     * get block by index
     */
    public HardwareBlock getBlock(int idx) {
        return getBlocks().get(idx);
    }

    /*
     * get block via predicate
     */
    public List<HardwareBlock> getBlocks(Predicate<HardwareBlock> predicate) {
        return getBlocks().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * get block by name
     // TODO: need maybe a delcaration block for different type of hardwareBlock?
     // seems like a good idea since i can reuse the nameMaps
    public HardwareBlock getBlock(String regname) {
        return getBlocks().getDeclaration(regname).get();
    }*/

    public List<ModuleInstance> getInstances() {
        return getBody().getChildrenOf(ModuleInstance.class);
    }
}
