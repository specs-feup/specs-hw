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

package pt.up.fe.specs.crispy.ast.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.constructs.AlwaysCombBlock;
import pt.up.fe.specs.crispy.ast.constructs.AlwaysFFBlock;
import pt.up.fe.specs.crispy.ast.constructs.HardwareBlock;
import pt.up.fe.specs.crispy.ast.constructs.HardwareBlockInterface;
import pt.up.fe.specs.crispy.ast.constructs.InitialBlock;
import pt.up.fe.specs.crispy.ast.constructs.NegEdge;
import pt.up.fe.specs.crispy.ast.constructs.PosEdge;
import pt.up.fe.specs.crispy.ast.constructs.SignalEdge;
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
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;
import pt.up.fe.specs.crispy.ast.meta.DeclarationBlock;
import pt.up.fe.specs.crispy.ast.statement.ContinuousStatement;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;

public interface ModuleBlockInterface extends HardwareBlockInterface {

    public abstract List<PortDeclaration> getPortList();

    public default DeclarationBlock getPortDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 0);
    }

    public default DeclarationBlock getWireDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 1);
    }

    public default DeclarationBlock getRegisterDeclarationBlock() {
        return getBody().getChild(DeclarationBlock.class, 2);
    }

    /////////////////////////////////////////////////////////////////////////////
    // ADDERS ///////////////////////////////////////////////////////////////////

    public default VariableOperator addDeclaration(IdentifierDeclaration id) {
        if (id instanceof PortDeclaration)
            return addDeclaration((PortDeclaration) id);

        else if (id instanceof WireDeclaration)
            return addDeclaration((WireDeclaration) id);

        else if (id instanceof RegisterDeclaration)
            return addDeclaration((RegisterDeclaration) id);

        else
            return id.getReference();
    }

    /*
    public default Port addDeclaration(PortDeclaration port) {
        return this.addPort(port);
    }
    
    public default Wire addDeclaration(WireDeclaration wire) {
        return this.addWire(wire);
    }
    
    public default Register addDeclaration(RegisterDeclaration reg) {
        return this.addRegister(reg);
    }*/

    /*
     * Special ports
     */
    public default InputPort addClock() {
        return addClock("clk");
    }

    public default InputPort addClock(String clockName) {
        return addInputPort(new ClockDeclaration(clockName));
    }

    public default InputPort addReset() {
        return addReset("rst");
    }

    public default InputPort addReset(String rstName) {
        return addInputPort(new ResetDeclaration(rstName));
    }

    /*
     * Ports
     */
    public default Port addPort(PortDeclaration port) {
        if (port instanceof InputPortDeclaration)
            return addInputPort((InputPortDeclaration) port);

        else if (port instanceof OutputPortDeclaration)
            return addOutputPort((OutputPortDeclaration) port);

        else
            return null;

        // getPortList().add(port); // this only adds to the port list in the header!
        // return (Port) ((PortDeclaration) getPortDeclarationBlock().addDeclaration(port)).getReference();
    }

    public default InputPort addInputPort(InputPortDeclaration inputdeclaration) {
        getPortList().add(inputdeclaration); // this only adds to the port list in the header!
        return (InputPort) getPortDeclarationBlock().addDeclaration(inputdeclaration).getReference();
    }

    public default InputPort addInputPort(String portName, int portWidth) {
        return (InputPort) addInputPort(new InputPortDeclaration(portName, portWidth));
    }

    public default OutputPort addOutputPort(OutputPortDeclaration outputdeclaration) {
        getPortList().add(outputdeclaration); // this only adds to the port list in the header!
        return (OutputPort) getPortDeclarationBlock().addDeclaration(outputdeclaration).getReference();
    }

    public default OutputPort addOutputPort(String portName, int portWidth) {
        return (OutputPort) addOutputPort(new OutputPortDeclaration(portName, portWidth));
    }

    /*
     * Wires
     */
    public default Wire addWire(WireDeclaration wire) {
        return ((WireDeclaration) getWireDeclarationBlock().addDeclaration(wire)).getReference();
    }

    public default Wire addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    /*
     * registers
     */
    public default Register addRegister(RegisterDeclaration reg) {
        return ((RegisterDeclaration) getRegisterDeclarationBlock().addDeclaration(reg)).getReference();
    }

    public default Register addRegister(String regName, int portWidth) {
        return addRegister(new RegisterDeclaration(regName, portWidth));
    }

    /*
     * The ModuleBlock is the only block type that allows other HardwareBlocks as children, 
     * for example always_comb, initial, etc; it is also the only type of block 
     * that allows children of type ModuleInstance
     */
    public default HardwareBlock addBlock(HardwareBlock block) {
        sanityCheck(block);
        getBody().addChild(block);
        return block;
    }

    public default HardwareBlock addBlockBefore(HardwareBlock block, HardwareNode other) {
        sanityCheck(block);
        getBody().addChildLeftOf(other, block);
        return block;
    }

    public default HardwareBlock addBlockAfter(HardwareBlock block, HardwareNode other) {
        sanityCheck(block);
        getBody().addChildRightOf(other, block);
        return block;
    }

    /*
     * 
     */
    public abstract int incrementCombCounter();

    /*
     * 
     */
    public abstract int incrementFFCounter();

    /*
     * add always_comb blocks
     */
    public default AlwaysCombBlock alwayscomb(String blockName) {
        return (AlwaysCombBlock) addBlock(new AlwaysCombBlock(blockName));
    }

    // create name if non given
    public default AlwaysCombBlock alwayscomb() {
        return (AlwaysCombBlock) addBlock(new AlwaysCombBlock("comb_" + incrementCombCounter()));
        // TODO: if I manually create a block called "comb_1" or "comb_2" etc, this will break
    }

    /*
     * always ff blocks (if no signal provided defaults to clk)
     * (if no clock on module, adds a clock declaration to the ports)
     */
    public default AlwaysFFBlock alwaysff(
            String blockName, ClockDeclaration clk,
            Function<VariableOperator, SignalEdge> edge) {

        var block = new AlwaysFFBlock(edge.apply(clk.getReference()), blockName);
        getBody().addChild(block);
        return block;
    }

    /*
     * Alwaysff posedge
     */
    public default AlwaysFFBlock alwaysposedge(String blockName, ClockDeclaration clk) {
        return alwaysff(blockName, clk, (signal) -> new PosEdge(signal));
    }

    public default AlwaysFFBlock alwaysposedge(String blockName) {
        return alwaysposedge(blockName, getClock());
    }

    public default AlwaysFFBlock alwaysposedge() {
        return alwaysposedge("ff_" + incrementFFCounter());
    }

    /*
     * Alwaysff negedge
     */
    public default AlwaysFFBlock alwaysnegedge(String blockName, ClockDeclaration clk) {
        return alwaysff(blockName, clk, (signal) -> new NegEdge(signal));
    }

    public default AlwaysFFBlock alwaysnegedge(String blockName) {
        return alwaysnegedge(blockName, getClock());
    }

    public default AlwaysFFBlock alwaysnegedge() {
        return alwaysnegedge("ff_" + incrementFFCounter());
    }

    /*
     * initial
     */
    public default InitialBlock initial() {
        return initial("");
    }

    public default InitialBlock initial(String blockName) {
        var block = new InitialBlock(blockName);
        getBody().addChild(block);
        return block;
    }

    /*
     * instances of other modules
     * (instances can only be added as direct children of the module body,
     * i.e. first level children of the ModuleBlock)
     */
    /*public default ModuleInstance addInstance(ModuleInstance instantiatedModule) {
        sanityCheck(instantiatedModule);
        return (ModuleInstance) getBody().addChild(instantiatedModule);
    }*/
    public default HardwareModule instantiate(HardwareModule instance, List<? extends HardwareOperator> connections) {
        getBody()._do(new ModuleInstance(instance,
                instance.getName() + Integer.toString(instance.hashCode()).substring(0, 4), connections));
        return instance;
    }

    public default HardwareModule instantiate(HardwareModule instance,
            List<? extends HardwareOperator> connections1,
            HardwareOperator... connections2) {
        var connections = new ArrayList<HardwareOperator>();
        connections.addAll(connections1);
        connections.addAll(Arrays.asList(connections2));
        return instantiate(instance, connections);
    }

    public default HardwareModule instantiate(HardwareModule instance, HardwareOperator... connections) {
        return instantiate(instance, Arrays.asList(connections));
    }

    /*
     * assign
     */
    public default VariableOperator assign(HardwareExpression expr) {
        return createAssigment(expr.getResultName(), expr, (t, u) -> new ContinuousStatement(t, u));
    }

    public default VariableOperator assign(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ContinuousStatement(t, u));
    }

    public default VariableOperator assign(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ContinuousStatement(t, u));
    }

    public default VariableOperator assign(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getResultWidth());
        return createAssigment(target, imm, (t, u) -> new ContinuousStatement(t, u));
    }

    public default VariableOperator assign(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ContinuousStatement(t, u));
    }

    /////////////////////////////////////////////////////////////////////////////
    // GETTERS //////////////////////////////////////////////////////////////////

    public default ClockDeclaration getClock() {

        var clks = getPorts(port -> port.isClock());

        ClockDeclaration clk = null;
        if (!clks.isEmpty())
            clk = (ClockDeclaration) clks.get(0);
        else {
            clk = (ClockDeclaration) addClock().getAssociatedIdentifier();
        }
        return clk;
    }

    /*
     * *****************************************************************************
     * Get port lists
     */
    public default List<PortDeclaration> getPorts() {
        return getPortDeclarationBlock().getChildrenOf(PortDeclaration.class);
    }

    public default List<InputPortDeclaration> getInputPorts() {
        return getPortDeclarationBlock().getChildrenOf(InputPortDeclaration.class);
    }

    public default List<OutputPortDeclaration> getOutputPorts() {
        return getPortDeclarationBlock().getChildrenOf(OutputPortDeclaration.class);
    }

    /*
     * get Port as a index
     */
    public default VariableOperator getPort(int idx) {
        return getPorts().get(idx).getReference();
    }

    /*
     * get Port by name
     */
    public default VariableOperator getPort(String portname) {
        // NOTE: return is known to be a VariableOperator at this point
        return (VariableOperator) getPortDeclarationBlock().getDeclaration(portname);
    }

    /*
     * get Port via predicate
     */
    public default List<PortDeclaration> getPorts(Predicate<PortDeclaration> predicate) {
        return getPorts().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get wire lists
     */
    public default List<WireDeclaration> getWires() {
        return getWireDeclarationBlock().getChildrenOf(WireDeclaration.class);
    }

    /*
     * get Wire as a index
     */
    public default HardwareOperator getWire(int idx) {
        return getWires().get(idx).getReference();
    }

    /*
     * get Wire by name
     */
    public default HardwareOperator getWire(String wirename) {
        return getWireDeclarationBlock().getDeclaration(wirename);
    }

    /*
     * get wire via predicate
     */
    public default List<WireDeclaration> getWires(Predicate<WireDeclaration> predicate) {
        return getWires().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get Reg lists
     */
    public default List<RegisterDeclaration> getRegisters() {
        return getRegisterDeclarationBlock().getChildrenOf(RegisterDeclaration.class);
    }

    /*
     * get Reg as a index
     */
    public default HardwareOperator getRegister(int idx) {
        return getRegisters().get(idx).getReference();
    }

    /*
     * get Reg by name
     */
    public default HardwareOperator getRegister(String regname) {
        return getRegisterDeclarationBlock().getDeclaration(regname);
    }

    /*
     * get Reg via predicate
     */
    public default List<RegisterDeclaration> getRegisters(Predicate<RegisterDeclaration> predicate) {
        return getRegisters().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * Get any declaration that matches name
     */
    public default HardwareOperator getDeclaration(String name) {

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
    public default List<HardwareBlock> getBlocks() {
        return getBody().getChildren(HardwareBlock.class);
    }

    /*
     * get block by index
     */
    public default HardwareBlock getBlock(int idx) {
        return getBlocks().get(idx);
    }

    /*
     * get block via predicate
     */
    public default List<HardwareBlock> getBlocks(Predicate<HardwareBlock> predicate) {
        return getBlocks().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * get block by name
     // TODO: need maybe a delcaration block for different type of hardwareBlock?
     // seems like a good idea since i can reuse the nameMaps
    public default HardwareBlock getBlock(String regname) {
        return getBlocks().getDeclaration(regname).get();
    }*/

    public default List<ModuleInstance> getInstances() {
        return getBody().getChildrenOf(ModuleInstance.class);
    }
}
