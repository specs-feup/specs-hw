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

import java.io.File;

import org.junit.Test;
// import org.junit.Test.api.Assertions.assertTrue;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.block.HardwareTestbench;
import pt.up.fe.specs.crispy.coarse.Adder;
import pt.up.fe.specs.crispy.coarse.Subtractor;
import pt.up.fe.specs.crispy.lib.Mux2to1;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class HardwareEmitTest {

    @Test
    public void testAdder2() {
        var adder1 = new Adder(32);
        adder1.emit();
        // System.out.println(DottyGenerator.buildDotty(adder1));
    }

    @Test
    public void testAdder() {
        var adder1 = new Adder(32);
        adder1.emitToFile();

        var tb = new HardwareTestbench("adder_tb.sv", adder1);
        var stim = new File("./resources/pt/up/fe/specs/crispy/test/resources/addertb.csv");
        tb.setClockFrequency(100);
        tb.addStimuli(stim);
        tb.emitToFile();

        System.out.println(DottyGenerator.buildDotty(tb));
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

        var stim = new File("./resources/pt/up/fe/specs/crispy/test/resources/dadosentrada.csv");
        tb.addStimuli(stim);

        // alternativa ler directamente os dados de um ficheiro txt
        // copiar desde aqui abaixo para um metodo no HardwareTestbench

        // var csv = new csvreader(specsio.getresource("pt/up/fe/specs/crispy/test/resources/dadosentrada.csv"), " ");
        // // criar
        // var portnamesanddirs = csv.getheader(); // um
        // var portnames = new arraylist<string>();
        //
        // // transforma a primeira linha do csv numa mapa de "nomes -> input/output"
        // var dirandnames = new hashmap<string, moduleportdirection>();
        // for (int i = 0; i < portnamesanddirs.size(); i++) {
        // var aux = portnamesanddirs.get(i); // e.g. "i:ina"
        // var dirandname = arrays.aslist(aux.split(":"));
        // var porttype = dirandname.get(0);
        // var portname = dirandname.get(1);
        // var type = (porttype.compareto("i") == 0) ? moduleportdirection.input : moduleportdirection.output;
        // dirandnames.put(portname, type);
        // portnames.add(portname);
        // }
        // // system.out.println(dirandnames);
        //
        // // para cada linha de estimulos
        // while (csv.hasnext()) {
        // var values = csv.next();
        //
        // // para cada coluna (ou seja, cada porta)
        // for (int i = 0; i < portnames.size(); i++) {
        //
        // // se prefixo for "i:"
        // if (dirandnames.get(portnames.get(i)) == moduleportdirection.input) {
        // var ref = tb.getregister("r" + portnames.get(i));
        // // todo handle if null
        // tb.setinit(ref, integer.parseint(values.get(i)));
        // }
        // }
        //
        // // wait after input values attributed
        // tb.adddelay(tb.getperiod());
        //
        // // para cada coluna (ou seja, cada porta)
        // for (int i = 0; i < portnames.size(); i++) {
        //
        // // se prefixo for "o:"
        // if (dirandnames.get(portnames.get(i)) == moduleportdirection.output) {
        // var refw = tb.getwire("w" + portnames.get(i));
        // var value = integer.parseint(values.get(i));
        // tb.doassert(refw.eq(value));
        // }
        //
        // }
        // }
        // tb.adddelay(tb.getperiod());

        // ate aqui para copiar

        // ledadosficheiro();

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
