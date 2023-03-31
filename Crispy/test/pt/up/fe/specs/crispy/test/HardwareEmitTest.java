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

import org.junit.Test;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.block.HardwareTestbench;
import pt.up.fe.specs.crispy.coarse.Adder;
import pt.up.fe.specs.crispy.coarse.Subtractor;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.csv.CsvReader;

public class HardwareEmitTest {

    @Test
    public void testAdder() {
        var adder1 = new Adder(32);
        adder1.emitToFile();
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
        sub1.emitToFile();

        var tb = new HardwareTestbench("tb_sub", sub1);
        tb.setClockFrequency(100);
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
        var refC = mod.addOutputRegisterPort("outC", 8);
        var refD = mod.addOutputRegisterPort("outD", 8);

        var block1 = mod.alwaysposedge();
        block1._ifelse(rst).then()
                ._do(refC.nonBlocking(0))
                ._do(refD.nonBlocking(0))
                .orElse()
                ._do(refC.nonBlocking(refA.add(refB)))
                ._do(refD.nonBlocking(refA.sub(refA.add(refB))));

        /*  block.nonBlocking(refC, refA.add(refB)); // outC <= inA + inB;
        block.nonBlocking(refD, refA.sub(refA.add(refB))); // outD <= inA + (inB - inA);*/

        mod.emitToFile();

        //
        var tb = new HardwareTestbench("testMod_tb", mod);
        // tb.addClock(); // ??
        tb.setClockFrequency(100);
        tb.setClockInit();
        tb.setResetInit();
        // tb.addStimulus(... ??

        var refrinA = tb.getRegister("rinA");
        tb.setInit(refrinA, 0); // ?? verificar depois

        var refrinB = tb.getRegister("rinB");
        tb.setInit(refrinB, 0);

        tb.addDelay(5 * tb.getPeriod()); // ??verificar
        /*
        tb.setInit(refrinA, 3);
        tb.setInit(refrinB, 2); */
        // tb.addDelay(tb.getPeriod());

        /*
        tb.setInit(refrinA, 1);
        tb.setInit(refrinB, 1);*/
        // tb.addDelay(tb.getPeriod());

        /*
        tb.setInit(refrinA, 3);
        tb.setInit(refrinB, 9);*/
        // tb.addDelay(tb.getPeriod());

        // alternativa ler directamente os dados de um ficheiro txt
        var csv = new CsvReader(SpecsIo.getResource("pt/up/fe/specs/crispy/test/resources/dadosentrada.csv"), " "); // criar
                                                                                                                    // um
                                                                                                                    // loop
        while (csv.hasNext()) {
            var values = csv.next();
            System.out.println("Data: " + values); // prints "Data: input1 input2"
            tb.setInit(refrinA, Integer.parseInt(values.get(0)));
            tb.setInit(refrinB, Integer.parseInt(values.get(1)));
            tb.addDelay(tb.getPeriod());
        }

        tb.setResetInit();

        /*
        var refwoutC = tb.getRegister("outC");
        tb.setInit(refwoutC, 0);
        
        var refwoutD = tb.getRegister("outD");
        tb.setInit(refwoutD, 0);
        */
        tb.emitToFile();
    }
}
