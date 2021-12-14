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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysFFBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.NegEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.PosEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.SignalEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.ClockDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

public class HardwareModule extends HardwareDefinition implements ModuleBlockInterface {

    protected static int MAXCHILDREN;
    protected static String ADDCHILDERRMSG;
    static {
        MAXCHILDREN = 2;
        ADDCHILDERRMSG = "HardwareModule: Expected only two children! " +
                "Use addStatement() and addBlock() to add content to the module body!";
    }

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
    public List<PortDeclaration> getPortList() {
        return this.getBody().getPortList();
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
     * add always_comb blocks
     */
    public AlwaysCombBlock addAlwaysComb(String blockName) {
        return (AlwaysCombBlock) this.addBlock(new AlwaysCombBlock(blockName));
    }

    // create name if non given
    public AlwaysCombBlock addAlwaysComb() {
        return (AlwaysCombBlock) this.addBlock(new AlwaysCombBlock("comb_" + this.alwayscombCounter++));
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
