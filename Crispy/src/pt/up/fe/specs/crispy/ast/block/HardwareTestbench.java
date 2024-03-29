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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.ModulePortDirection;
import pt.up.fe.specs.crispy.ast.declaration.port.PortDeclaration;
import pt.up.fe.specs.crispy.ast.expression.comparison.EqualsToExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.expression.operator.Port;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.meta.FileHeader;
import pt.up.fe.specs.crispy.ast.task.AssertTask;
import pt.up.fe.specs.specshw.SpecsHwUtils;
import pt.up.fe.specs.util.csv.CsvReader;

public class HardwareTestbench extends HardwareModule {

    private int period = -1;
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
        this.addChild(new FileHeader(SpecsHwUtils.generateFileHeader()));

        // child 1
        this.addChild(new TimeScaleDeclaration(1, 1));

        // child 2
        this.addChild(new ModuleBlock(testBenchName));

        // auto-create registers and wires based on DUT ports
        var connections = new ArrayList<HardwareOperator>();

        for (var in : dut.getInputPorts())
            connections.add(this.addRegister("r" + in.getVariableName(), in.getVariableWidth()));

        for (var out : dut.getOutputPorts())
            connections.add(this.addWire("w" + out.getVariableName(), out.getVariableWidth()));

        // this.addInstance(dut.instantiate("dutInstance1", connections));
        this.instantiate(dut, connections);

        // TODO: move all content of @HardwareTestbenchGenerator into @Verilog as a static
        // factory like method?
    }

    @Override
    public ModuleBlock getBody() {
        return this.getChild(ModuleBlock.class, 2);
    }

    // returns clock period; only valid after setClockFrequency is called
    public int getPeriod() {
        return this.period;
    }

    private InitialBlock getInitialBlock() {
        InitialBlock ini = null;
        var initials = getBody().getChildrenOf(InitialBlock.class);
        if (initials.isEmpty()) {
            ini = this.initial("initBlock");
            // var time = getChild(TimeScaleDeclaration.class, 1).getTimeUnit();
            // ini.delay(time * 10);
        } else
            ini = initials.get(0);

        return ini;
    }

    // TODO: add one initial block for each call of setInit,
    // have two different types of set init, one with 1 value
    // and one with 1 value, a delay, and another value
    // after, prior to emission, consolidate all init blocks
    // into less block, based on content

    public HardwareTestbench setInit(VariableOperator op, int value) {

        // check if declaration exists in ModuleBlock
        if (getDeclaration(op.getName()) == null)
            this.addDeclaration(op.getAssociatedIdentifier());

        // set init
        getInitialBlock().nonBlocking(op, value);

        return this;
    }

    public HardwareTestbench setClockFrequency(int mhz) {
        var time = getChild(TimeScaleDeclaration.class, 1);
        // this.period = (int) (((1.0 / mhz) / Math.pow(10, -9)) / time.getTimeUnit());

        this.period = (int) ((1.0 / (mhz * Math.pow(10, -3))) / time.getTimeUnit());

        var clockBlock = new AlwaysBlock("clockBlock");
        this.addBlockAfter(clockBlock, getRegisterDeclarationBlock());

        VariableOperator clk = null;
        if ((clk = (VariableOperator) this.getRegister("rclk")) == null)
            clk = this.addRegister("rclk", 1);

        clockBlock.blocking(clk, clk.not()); // clk = ~clk
        clockBlock.delay(period); // #(period)

        // add to initial
        // this.setInit(clk, 0);
        // this.initial.blocking(clk, 0);
        // this.initial.delay(period * 10);

        return this;
    }

    public HardwareTestbench setClockInit() {
        this.setInit(this.getRegister("rclk"), 1);
        this.getInitialBlock().delay(this.getPeriod());
        return this;
    }

    public HardwareTestbench setResetInit() {
        this.setInit(this.getRegister("rrst"), 1);
        this.getInitialBlock().delay(10 * this.getPeriod());
        this.setInit(this.getRegister("rrst"), 0);
        return this;
    }

    public HardwareTestbench addDelay(int period) {
        this.getInitialBlock().delay(period);
        return this;
    }

    public HardwareTestbench doAssert(EqualsToExpression eq) {
        getInitialBlock()._do(new AssertTask(eq));
        return this;
    }

    @Override
    protected Port addPort(PortDeclaration port) {
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

    public HardwareTestbench addStimuli(File stimuli) {
        // var csv = new CsvReader(SpecsIO.getresource("pt/up/fe/specs/crispy/test/resources/dadosentrada.csv"), " ");
        // // criar
        var csv = new CsvReader(stimuli, " "); // criar
        var portNamesAndDirs = csv.getHeader(); // um
        var portNames = new ArrayList<String>();

        // transforma a primeira linha do csv numa mapa de "nomes -> input/output"
        var dirAndNames = new HashMap<String, ModulePortDirection>();
        for (int i = 0; i < portNamesAndDirs.size(); i++) {
            var aux = portNamesAndDirs.get(i); // e.g. "i:ina"
            var dirAndName = Arrays.asList(aux.split(":"));
            var portType = dirAndName.get(0);
            var portName = dirAndName.get(1);
            var type = (portType.compareTo("i") == 0) ? ModulePortDirection.input : ModulePortDirection.output;
            dirAndNames.put(portName, type);
            portNames.add(portName);
        }
        // system.out.println(dirAndNames);

        // para cada linha de estimulos
        while (csv.hasNext()) {
            var values = csv.next();

            // para cada coluna (ou seja, cada porta)
            for (int i = 0; i < portNames.size(); i++) {

                // se prefixo for "i:"
                if (dirAndNames.get(portNames.get(i)) == ModulePortDirection.input) {
                    var ref = this.getRegister("r" + portNames.get(i));
                    // todo handle if null
                    this.setInit(ref, Integer.parseInt(values.get(i)));
                }
            }

            // wait after input values attributed
            this.addDelay(this.getPeriod());

            // para cada coluna (ou seja, cada porta)
            for (int i = 0; i < portNames.size(); i++) {

                // se prefixo for "o:"
                if (dirAndNames.get(portNames.get(i)) == ModulePortDirection.output) {
                    var refw = this.getWire("w" + portNames.get(i));
                    var value = Integer.parseInt(values.get(i));
                    this.doAssert(refw.eq(value));
                }

            }
        }
        this.addDelay(this.getPeriod());
        return this;
    }
}
