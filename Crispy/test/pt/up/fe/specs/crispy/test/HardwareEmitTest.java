/**
 * Copyright 2023 SPeCS.
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

package pt.up.fe.specs.crispy.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
// import org.junit.Test.api.Assertions.assertTrue;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.block.HardwareTestbench;
import pt.up.fe.specs.crispy.ast.declaration.port.ModulePortDirection;
import pt.up.fe.specs.crispy.coarse.Adder;
import pt.up.fe.specs.crispy.coarse.Subtractor;
import pt.up.fe.specs.crispy.lib.Mux2to1;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.csv.CsvReader;

public class HardwareEmitTest {

    @Test
    public void testAdder() {
        var adder1 = new Adder(32);
        adder1.emitToFile();
    }

    @Test
    public void testMuxTb() {

        var mux1 = new Mux2to1(8);
        mux1.emitToFile();

        // como se cria um muxNto1 ??
        // var mux2=new Mux4to1(8);
        // mux2.emitToFile();

        var tb = new HardwareTestbench("testbench1", mux1);
        tb.emitToFile();

        // var tb = new HardwareTestbench("testbench2", mux2);
        // tb.emitToFile();
    }

    @Test
    public void testAdderTb() {
        var adder1 = new Adder(32);
        var tb = new HardwareTestbench("testbench1", adder1);
        tb.setClockFrequency(100);
        tb.emitToFile();
    }

    @Test

    public void testSubTb() {
        var sub1 = new Subtractor(32);

        // ??
        var clck = sub1.addClock();
        var rst = sub1.addReset();
        var refA = sub1.addInputPort("inA", 8);
        var refB = sub1.addInputPort("inB", 8);
        var refC = sub1.addInputPort("inC", 8);
        var refoA = sub1.addOutputRegisterPort("outA", 8);
        var refoB = sub1.addOutputRegisterPort("outB", 8);

        var block1 = sub1.alwaysposedge();
        block1._ifelse(rst).then()
                ._do(refC.nonBlocking(0))
                ._do(refoA.nonBlocking(0))
                .orElse()
                ._do(refoA.nonBlocking(refA.add(refB)))
                ._do(refoB.nonBlocking(refA.sub(refC.add(refB))));

        sub1.emitToFile();

        var tb = new HardwareTestbench("tb_sub", sub1);
        tb.setClockFrequency(100);

        // ??
        tb.addDelay(5 * tb.getPeriod());
        tb.setClockInit();
        tb.setResetInit();

        tb.emitToFile();
        // System.out.println(TreeNodeUtils.toString(tb, ""));
        // System.out.println(DottyGenerator.buildDotty(tb));
    }

    @Test
    public void test1() {

        var mod = new HardwareModule("testMod");
        var clk = mod.addClock();
        var rst = mod.addReset();
        var refA = mod.addInputPort("inA", 8);
        var refB = mod.addInputPort("inB", 8);
        var refC = mod.addInputPort("inC", 8);
        var refoA = mod.addOutputRegisterPort("outA", 8);
        var refoB = mod.addOutputRegisterPort("outB", 8);

        var block1 = mod.alwaysposedge();
        block1._ifelse(rst).then()
                ._do(refC.nonBlocking(0))
                ._do(refoA.nonBlocking(0))
                .orElse()
                ._do(refoA.nonBlocking(refA.add(refB)))
                ._do(refoB.nonBlocking(refA.sub(refC.add(refB))));

        mod.emitToFile();

        //
        var tb = new HardwareTestbench("testMod_tb", mod);
        tb.setClockFrequency(100);
        tb.setClockInit();
        tb.setResetInit();

        // var refrinA = tb.getRegister("rinA");
        // tb.setInit(refrinA, 0); // ?? verificar depois

        // var refrinB = tb.getRegister("rinB");
        // tb.setInit(refrinB, 0);

        tb.addDelay(5 * tb.getPeriod()); // ??verificar

        // alternativa ler directamente os dados de um ficheiro txt
        var csv = new CsvReader(SpecsIo.getResource("pt/up/fe/specs/crispy/test/resources/dadosentrada.csv"), " "); // criar
        var portNamesAndDirs = csv.getHeader(); // um
        var portNames = new ArrayList<String>();

        // transforma a primeira linha do CSV numa mapa de "nomes -> input/output"
        var dirAndNames = new HashMap<String, ModulePortDirection>();
        for (int i = 0; i < portNamesAndDirs.size(); i++) {
            var aux = portNamesAndDirs.get(i); // e.g. "i:inA"
            var dirAndName = Arrays.asList(aux.split(":"));
            var portType = dirAndName.get(0);
            var portName = dirAndName.get(1);
            var type = (portType.compareTo("i") == 0) ? ModulePortDirection.input : ModulePortDirection.output;
            dirAndNames.put(portName, type);
            portNames.add(portName);
        }
        // System.out.println(dirAndNames);

        // para cada linha de estimulos
        while (csv.hasNext()) {
            var values = csv.next();

            // para cada coluna (ou seja, cada porta)
            for (int i = 0; i < portNames.size(); i++) {

                // se prefixo for "i:"
                if (dirAndNames.get(portNames.get(i)) == ModulePortDirection.input) {
                    var ref = tb.getRegister("r" + portNames.get(i));
                    // TODO handle if null
                    tb.setInit(ref, Integer.parseInt(values.get(i)));
                }
            }

            // wait after input values attributed
            tb.addDelay(tb.getPeriod());

            // para cada coluna (ou seja, cada porta)
            for (int i = 0; i < portNames.size(); i++) {

                // se prefixo for "o:"
                if (dirAndNames.get(portNames.get(i)) == ModulePortDirection.output) {
                    var refW = tb.getWire("w" + portNames.get(i));
                    var value = Integer.parseInt(values.get(i));
                    tb.doAssert(refW.eq(value));
                }

            }
        }
        tb.addDelay(tb.getPeriod());

        // tb.addStimuli(new File("dados"))

        // tb.setResetInit();

        /*
        var refwoutC = tb.getRegister("outC");
        tb.setInit(refwoutC, 0);
        
        var refwoutD = tb.getRegister("outD");
        tb.setInit(refwoutD, 0);
        */
        tb.emitToFile();
    }
}
