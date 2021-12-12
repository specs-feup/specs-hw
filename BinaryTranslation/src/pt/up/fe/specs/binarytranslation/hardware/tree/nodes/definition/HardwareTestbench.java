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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;

public class HardwareTestbench extends HardwareModule {

    private final String testBenchName;
    private final HardwareModule dut;

    static {
        MAXCHILDREN = 3;
        ADDCHILDERRMSG = "HardwareTestBench: Expected only three children!"
                + "Use addStatement() and addBlock() to add content to the testbench body!";
    }

    /*
     * for copying
     */
    private HardwareTestbench(HardwareTestbench other) {
        super(HardwareNodeType.TestBenchDefinition);
        this.testBenchName = other.testBenchName;
        this.dut = other.dut;
    }

    public HardwareTestbench(String testBenchName, HardwareModule dut) {
        super(HardwareNodeType.TestBenchDefinition);
        this.testBenchName = testBenchName;
        this.dut = dut;

        // child 0
        this.addChild(new FileHeader());

        // child 1
        this.addChild(new TimeScaleDeclaration());

        // child 2
        this.addChild(new ModuleBlock(testBenchName));

        // auto-create registers and wires based on DUT ports
        var connections = new ArrayList<HardwareOperator>();

        for (var in : dut.getInputPortDeclarations()) {
            var reg = this.addRegister("r" + in.getVariableName(), in.getVariableWidth());
            connections.add(reg.getReference());
        }

        for (var out : dut.getOutputPortDeclarations()) {
            var wire = this.addWire("w" + out.getVariableName(), out.getVariableWidth());
            connections.add(wire.getReference());
        }

        this.addInstance(dut.instantiate("dutInstance1", connections));

        // TODO: move all content of @HardwareTestbenchGenerator into @Verilog as a static
        // factory like method?
    }

    @Override
    protected ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 2);
    }

    @Override
    public PortDeclaration addPort(PortDeclaration port) {
        throw new RuntimeException(
                "HardwareTestbench: testbenches are not allowed to have ports!");
    }

    @Override
    protected HardwareTestbench copyPrivate() {
        return new HardwareTestbench(this.testBenchName, this.dut);
    }

    @Override
    public HardwareTestbench copy() {
        return (HardwareTestbench) super.copy();
    }

    @Override
    public String getName() {
        return testBenchName;
    }
}
