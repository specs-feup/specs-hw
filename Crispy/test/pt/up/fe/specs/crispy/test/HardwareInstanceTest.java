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

package pt.up.fe.specs.crispy.test;

import org.junit.Test;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.block.HardwareTestbench;
import pt.up.fe.specs.crispy.ast.declaration.RegisterDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.ScalarSubscript;
import pt.up.fe.specs.crispy.ast.statement.ContinuousStatement;
import pt.up.fe.specs.crispy.ast.statement.IfStatement;
import pt.up.fe.specs.crispy.coarse.Adder;
import pt.up.fe.specs.crispy.lib.CrossBarNxM;
import pt.up.fe.specs.crispy.lib.DecoderNxM;
import pt.up.fe.specs.crispy.lib.Mux2to1;
import pt.up.fe.specs.crispy.lib.MuxNto1;
import pt.up.fe.specs.crispy.lib.RAMemory;
import pt.up.fe.specs.crispy.lib.RegisterBank;
import pt.up.fe.specs.crispy.lib.ShiftRegister;
import pt.up.fe.specs.crispy.test.workshop.Add3;
import pt.up.fe.specs.util.treenode.utils.DottyGenerator;

public class HardwareInstanceTest {

    @Test
    public void workshopExample1() {

        var a = (new RegisterDeclaration("regA", 8)).getReference();
        var b = (new RegisterDeclaration("regB", 8)).getReference();
        var c = (new RegisterDeclaration("regC", 8)).getReference();

        var r = new ContinuousStatement(c, new AdditionExpression(a, b));
        r.emit();

        System.out.println(DottyGenerator.buildDotty(r));
    }

    @Test
    public void workshopExample2() {
        var adder = new HardwareModule("testAdder");
        var a = adder.addInputPort("testA", 32);
        var b = adder.addInputPort("testB", 32);
        var c = adder.addOutputPort("testC", 32);
        adder.alwayscomb()._do(c.nonBlocking(a.add(b)));

        adder.emit();

        System.out.println(DottyGenerator.buildDotty(adder));
    }

    @Test
    public void workshopExample3() {

        var add3 = new Add3();
        add3.emit();
    }

    @Test
    public void workshopExample4() {

        // create a class which inherits the
        // syntax (wrapper methods)
        var ex = new HardwareModule("example");

        // creates a "WireDeclaration", but returns
        // a "Wire", which is a reference to the
        // declared name (same for Registers and Ports)
        var wire = ex.addWire("ex1", 8);

        // new port
        var a = ex.addInputPort("pA", 8);

        // create an assign at the level of the module body
        ex.assign(a, wire.lsl(2));

        // emit to stdout (eventually, to files)
        ex.emit();
    }

    @Test
    public void testOperatorReferences() {

        var decl1 = new RegisterDeclaration("testReg", 32);
        decl1.emit();

        var imm = new Immediate(14, 32);
        imm.emit();

        // var ref1 = new VariableOperator(decl1);
        // ref1.emit();

        var ref1 = decl1.getReference();
        ref1.emit();

        // index1
        var index = new ScalarSubscript(1);
        index.emit();

        var index2 = new RangedSubscript(4, 8);
        index2.emit();

        var opWithSubscript = ref1.copy().idx(index).idx(index2);
        opWithSubscript.emit();

        var opWithSubscript2 = ref1.copy().idx(7).idx(4, 7);
        opWithSubscript2.emit();

        /*
        // 1d index
        var index = new IndexedSelection(decl1.getReference(), imm);
        index.emit();
        
        // 2d index
        var index2 = new IndexedSelection(index, imm);
        index2.emit();
        
        // 2d index left hand
        var index3 = new IndexedSelection(imm, index);
        index3.emit();*/

        // var select = new RangedReference(index, 32);

    }

    private static HardwareModule getAdder() {
        var adder = new HardwareModule("testAdder");
        var a = adder.addInputPort("testA", 32);
        var b = adder.addInputPort("testB", 32);
        var c = adder.addOutputPort("testC", 32);
        adder.alwayscomb()._do(c.nonBlocking(a.add(b)));
        return adder;
    }

    @Test
    public void testAdderEmit() {
        var testAdder = HardwareInstanceTest.getAdder();

        // testAdder.test(); // test get fields hash

        testAdder.emit();

        System.out.println(DottyGenerator.buildDotty(testAdder));
    }

    @Test
    public void testHardwareModuleInstanceNewFamily() {

        // previously defined HardwareModule
        var testAdder = HardwareInstanceTest.getAdder();

        var wrap = new HardwareModule("adderWrapper",
                new InputPortDeclaration("winA", 32),
                new InputPortDeclaration("winB", 32),
                new OutputPortDeclaration("woutC", 32));

        // new wire
        var wire = wrap.addWire("tmp", 32);

        // new adder instance
        wrap.instantiate(testAdder,
                wrap.getPort(0), wrap.getPort(1), wire);

        // connect output of adder instance +1 to output port woutC
        wrap.nonBlocking("woutC", wire.add(Immediate.Ones(15)).paren());

        /*
         * Test module instantiation inside other module
         */
        wrap.emit();
    }

    @Test
    public void testHardwareNewFamily() {

        /*
         * With explicit class creation
         * 
         * 
        var adder = new NewHardwareModule("adderDef");
        var a = new InputPortDeclaration("inA", 32);
        var b = new InputPortDeclaration("inB", 32);
        var c = new InputPortDeclaration("outC", 32);
        adder.addPort(a).addPort(b).addPort(c);
        var expr = new AdditionExpression(a.getReference(), b.getReference());
        var stat = new ProceduralNonBlockingStatement(c.getReference(), expr);
        */

        /*
         * With sugar
         *          
        var adder = new NewHardwareModule("adderDef");
        adder.addInputPort("inA", 32) .addInputPort("inB", 32).addOutputPort("outC", 32);        
        var ports = adder.getPortDeclarations();
        var a = ports.get(0);
        var b = ports.get(1);
        var c = ports.get(2);
        var expr = new AdditionExpression(a.getReference(), b.getReference());
        var stat = new ProceduralNonBlockingStatement(c.getReference(), expr);
        */

        /*
         * With more sugar
         
        var adder = new NewHardwareModule("adderDef");
        adder.addInputPort("inA", 32).addInputPort("inB", 32).addOutputPort("outC", 32);
        var expr = new AdditionExpression(adder.getPort(0), adder.getPort(1));
        var stat = new ProceduralNonBlockingStatement(adder.getPort(2), expr);
        */

        /*
         * With more more sugar
         */
        var adder = new HardwareModule("adderDef");
        adder.addInputPort("inA", 32);
        adder.addInputPort("inB", 32);
        adder.addOutputPort("outC", 32);
        adder.nonBlocking(adder.getPort(2), adder.getPort(0).add(adder.getPort(1)));

        // adder.addStatement(Verilog.nonBlocking(adder.getPort(2), Verilog.add(adder.getPort(0), adder.getPort(1))));

        /*
         * 
         */
        adder.emit();
    }

    @Test
    public void testHardwareTestbenchNewFamily() {

        // previously defined HardwareModule
        var testAdder = HardwareInstanceTest.getAdder();
        var tb = new HardwareTestbench("testbench1", testAdder);
        tb.setClockFrequency(100);
        tb.emit();
    }

    @Test
    public void testAddingBlocksNewFamily() {
        var adder = new HardwareModule("adderDef");
        adder.addClock();
        var rst = adder.addReset();
        var a = adder.addInputPort("inA", 32);
        var b = adder.addInputPort("inB", 32);
        var c = adder.addOutputPort("outC", 32);

        var ff1 = adder.alwaysposedge("testBlock");
        ff1._ifelse(rst)
                .then()
                ._do(c.nonBlocking(0))
                .orElse()
                ._do(c.nonBlocking(a.add(b)));

        /*
        var ff1 = adder.alwaysposedge("testBlock");
        var stat1 = Verilog.nonBlocking(adder.getPort(4), Verilog.add(adder.getPort(2), adder.getPort(3)));
        var stat2 = Verilog.nonBlocking(adder.getPort(4), new ImmediateOperator(0, 32));
        
        var ifelstat = new IfElseStatement(adder.getPort("rst"));
        ifelstat.addIfStatement(stat2);
        ifelstat.addElseStatement(stat1);
        
        ff1.addChild(ifelstat);*/

        adder.emit();
    }

    @Test
    public void testIfElse() {
        var sig = new WireDeclaration("testWire", 1);
        var if1 = new IfStatement("block1", sig.getReference());

        var sigA = new WireDeclaration("inA", 8).getReference();
        var sigB = new WireDeclaration("inB", 8).getReference();
        var sigC = new WireDeclaration("outC", 8).getReference();

        // if1.then().nonBlocking(sigC, sigA.add(sigB));
        if1.then()._do(sigC.nonBlocking(sigA.add(sigB)));
        // NOTE: both syntaxes are currently valid! (17-12-2021)

        // TODO: now i need that VariableOperator also implements a "nonBlocking" method etc
        // so that I can do
        // if1.then(sigC.nonBlocking(sigA.add(sigB));

        // var stat1 = Verilog.nonBlocking(sigC, Verilog.add(sigA, sigB));
        // if1.addStatement(stat1);
        // if1.addStatement(stat1.copy());

        if1.emit();
    }

    @Test
    public void test1() {

        var mod = new HardwareModule("testMod");
        mod.addClock();
        mod.addReset();
        var refA = mod.addInputPort("inA", 8);
        var refB = mod.addInputPort("inB", 8);
        var refC = mod.addOutputPort("outC", 8);

        var block = mod.alwayscomb("comb1");

        // var stat = Verilog.nonBlocking(refC, Verilog.add(refA, refB));
        // block.addStatement(stat);

        block.nonBlocking(refC, refA.add(refB));

        mod.emit();

        /*
         * 
         */
        var tb = new HardwareTestbench("tb1", mod);
        tb.emit();

    }

    @Test
    public void testBlocksyntax() {
        var adder = new HardwareModule("adderDef");
        var inA = adder.addInputPort("inA", 32);
        var inB = adder.addInputPort("inB", 32);
        var outC = adder.addOutputPort("outC", 32);
        var block = adder.alwayscomb("testBlock");
        var result = block.nonBlocking(inA.add(inB));
        adder.assign("outC", result);

        /*
         * 
         */
        adder.emit();
    }

    @Test
    public void testModuleAlwaysFF() {
        var adder = new HardwareModule("adderDef");
        var inA = adder.addInputPort("inA", 32);
        var inB = adder.addInputPort("inB", 32);
        var outC = adder.addOutputPort("outC", 32);
        adder.alwaysposedge().nonBlocking(outC, inA.add(inB));
        adder.emit();
    }

    @Test
    public void testModuleAlwaysFFWithReset() {
        var adder = new HardwareModule("adderDef");
        var clk = adder.addClock();
        var rst = adder.addReset();
        var inA = adder.addInputPort("inA", 8);
        var inB = adder.addInputPort("inB", 8);
        var outC = adder.addOutputPort("outC", 8);

        adder.alwaysposedge()._ifelse(rst)
                .then()._do(outC.nonBlocking(0))
                .orElse()._do(outC.nonBlocking(inA.add(inB)));

        /*
        var ifelse1 = adder.alwaysposedge()._ifelse(rst);
        ifelse1.then().nonBlocking(outC, 0);
        ifelse1.orElse().nonBlocking(outC, inA.add(inB));*/

        adder.emit();
    }

    @Test
    public void testCoarseModule() {

        var adder1 = new Adder(32);
        adder1.emit();
    }

    @Test
    public void testUlineApply() {

        var adder = new HardwareModule("adder2");
        var inA = adder.addInputPort("inA", 8);
        var inB = adder.addInputPort("inB", 8);
        var outC = adder.addOutputPort("outC", 8);

        var out = Adder._do(Adder._do(inA, inB), inA);
        outC.assign(out);

        adder.emit();
    }

    @Test
    public void testCoarseModule2() {

        var mux1 = new Mux2to1(8);
        mux1.emit();
    }

    @Test
    public void testMuxNto1() {

        var mux = new MuxNto1(12, 8);
        mux.emit();
    }

    @Test
    public void testCrossBarNxM() {

        var mux = new CrossBarNxM(4, 4, 8);
        mux.emit();
    }

    @Test
    public void testDecoderNxM() {

        var decoder = new DecoderNxM(3);
        decoder.emit();
    }

    @Test
    public void testRegisterBank() {

        var bank = new RegisterBank(2, 2);
        bank.emit();

        // var tb = new HardwareTestbench("tbTest", bank);
        // tb.setClockFrequency(100);
        // tb.emit();
    }

    @Test
    public void testRAMemory() {

        var ram = new RAMemory(1024, 4);
        ram.emit();
    }

    @Test
    public void testShiftRegister() {
        var sreg = new ShiftRegister(10, 4);
        sreg.emit();
    }

    @Test
    public void testCoarseModuleWrapper() {

        var adder = new Adder(32);

        var wrap = new HardwareModule("adderWrapper",
                new InputPortDeclaration("winA", 32),
                new InputPortDeclaration("winB", 32),
                new OutputPortDeclaration("woutC", 32));

        var clk = wrap.addRegister(new RegisterDeclaration("clk", 1));
        var rst = wrap.addRegister(new RegisterDeclaration("rst", 1));

        // wrap.getPorts(); TODO make this return List<Port> instead of List<PortDeclaration>

        // new adder instance
        wrap.instantiate(adder, clk, rst,
                wrap.getPort(0), wrap.getPort(1), wrap.getPort(2));

        wrap.emit();
    }

    @Test
    public void testModuleSyntax() {
        var adder = new HardwareModule("adderDef");
        adder.addClock();
        adder.addReset();
        var inA = adder.addInputPort("inA", 32);
        var inB = adder.addInputPort("inB", 32);
        var outC = adder.addOutputPort("outC", 32);

        // adder.getPort("outC").assign(adder.getPort("inA").add(adder.getPort("inB")));

        // var block = adder.addAlwaysComb("testBlock");

        var block = adder.alwaysposedge("testBlock");

        // block.nonBlocking(adder.getPort("outC"), adder.getPort("inA").add(adder.getPort("inB")));

        // var stat = auxA.assign(inA.add(inB);
        // adder.addStatement(stat); ???

        var auxA = block.nonBlocking("auxA", inA.add(inB).add(inA));

        // block.assign("auxA", adder.getPort("inA").add("inB"));
        // TODO: how to support string named fetching in HardwareOperationsInterface??

        // adder.assign("outC", adder.getWire("auxA"));

        adder.assign("outC", auxA);

        // TODO
        // what if
        // adder.assign("outC", getPort("inA").add(adder.getPort("inB"));
        // the adder is the block, the statement goes into the block
        // the "outC" is fetched by name, getPort("inA").add(adder.getPort("inB") the returns
        // a parentless addition expression

        //
        //
        //
        //
        //
        //
        //
        //
        //
        //

        // var block = adder.addAlwaysComb("testBlock");
        // var result = block.nonBlocking.add(adder.getPort("inA"), adder.getPort("inB"));

        // var newresult = adder.assign.add(adder.getPort("outC"), result);
        // works, but not what I want!

        // this syntax for an assign doesnt make much sense...
        // its the sme problem from before.. how do I perform syntax over an already declared identifier?
        // ideally:
        // adder.getPort("outC").assign(result);

        // adder.addStatement(new ContinuousStatement(adder.getPort("outC"), result));

        // TODO: this syntax doesnt allow for using an already declared identifier and assigning it a result...
        // I need assign, nonBlocking, and blocking method groups in the HardwareOperator class somehow... as well as in
        // the HardwareBlocks..
        // maybe the "nonBlockingMethods" should be part of HardwareOperator, and they add the statement to their parent
        // block if it exists!
        // no, the parent block of any declared identifier will always be the ModuleBlock (specifically the
        // DeclarationsBlock)..

        adder.emit();

        // want this:
        // var result = block.nonBlocking.add(refA, refB); !!
    }
}
