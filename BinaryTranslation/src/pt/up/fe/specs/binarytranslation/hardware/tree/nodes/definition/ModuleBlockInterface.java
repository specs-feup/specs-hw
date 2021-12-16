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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysFFBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.HardwareBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.HardwareBlockInterface;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.NegEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.PosEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.SignalEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.ClockDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.ResetDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.DeclarationBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

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

    public default VariableOperator addDeclaration(PortDeclaration port) {
        return this.addPort(port);
    }

    public default VariableOperator addDeclaration(WireDeclaration wire) {
        return this.addWire(wire);
    }

    public default VariableOperator addDeclaration(RegisterDeclaration reg) {
        return this.addRegister(reg);
    }

    /*
     * Special ports
     */
    public default VariableOperator addClock() {
        return addClock("clk");
    }

    public default VariableOperator addClock(String clockName) {
        return addPort(new ClockDeclaration(clockName));
    }

    public default VariableOperator addReset() {
        return addReset("rst");
    }

    public default VariableOperator addReset(String rstName) {
        return addPort(new ResetDeclaration(rstName));
    }

    /*
     * Ports
     */
    public default VariableOperator addPort(PortDeclaration port) {
        getPortList().add(port); // this only adds to the port list in the header!
        return ((PortDeclaration) getPortDeclarationBlock().addDeclaration(port)).getReference();
    }

    public default VariableOperator addInputPort(String portName, int portWidth) {
        return addPort(new InputPortDeclaration(portName, portWidth));
    }

    public default VariableOperator addOutputPort(String portName, int portWidth) {
        return addPort(new OutputPortDeclaration(portName, portWidth));
    }

    /*
     * Wires
     */
    public default VariableOperator addWire(WireDeclaration wire) {
        return ((WireDeclaration) getWireDeclarationBlock().addDeclaration(wire)).getReference();
    }

    public default VariableOperator addWire(String portName, int portWidth) {
        return addWire(new WireDeclaration(portName, portWidth));
    }

    /*
     * registers
     */
    public default VariableOperator addRegister(RegisterDeclaration reg) {
        return ((RegisterDeclaration) getRegisterDeclarationBlock().addDeclaration(reg)).getReference();
    }

    public default VariableOperator addRegister(String regName, int portWidth) {
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
     * instances of other modules
     * (instances can only be added as direct children of the module body,
     * i.e. first level children of the ModuleBlock)
     */
    public default ModuleInstance addInstance(ModuleInstance instantiatedModule) {
        sanityCheck(instantiatedModule);
        return (ModuleInstance) getBody().addChild(instantiatedModule);
    }

    public default ModuleInstance addInstance(HardwareModule instanceType,
            String instanceName, HardwareOperator... connections) {
        return addInstance(new ModuleInstance(instanceType, instanceName, connections));
    }

    /*
     * assign
     */
    public default VariableOperator assign(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> assign(t, u));
    }

    public default VariableOperator assign(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> assign(t, u));
    }

    public default VariableOperator assign(VariableOperator target, int literalConstant) {
        return assign(target, new ImmediateOperator(literalConstant, target.getResultWidth()));
    }

    public default VariableOperator assign(VariableOperator target, HardwareExpression expr) {
        getBody().addChild(new ContinuousStatement(target, expr));
        // TODO: will this handle adding the variable operator to the declaration block if it doesnt exist? NO
        return target;
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
        return getBody().getChildren(ModuleInstance.class);
    }
}
