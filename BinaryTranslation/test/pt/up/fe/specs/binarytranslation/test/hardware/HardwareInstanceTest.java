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

package pt.up.fe.specs.binarytranslation.test.hardware;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.hardware.factory.Verilog;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareTestbench;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.ScalarSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;

public class HardwareInstanceTest {

    @Test
    public void testOperatorReferences() {

        var decl1 = new RegisterDeclaration("testReg", 32);
        decl1.emit();

        var imm = new ImmediateOperator(14, 32);
        imm.emit();

        var ref1 = new VariableOperator(decl1);
        ref1.emit();

        var ref1b = decl1.getReference();
        ref1b.emit();

        // index1
        var index = new ScalarSubscript(1);
        index.emit();

        var index2 = new RangedSubscript(4, 8);
        index2.emit();

        var opWithSubscript = ref1.copy().addSubscript(index).addSubscript(index2);
        opWithSubscript.emit();

        var opWithSubscript2 = ref1.copy().addSubscript(7).addSubscript(4, 7);
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
        adder.addInputPort("testA", 32);
        adder.addInputPort("testB", 32);
        adder.addOutputPort("testC", 32);
        // var block = adder.addBlock(new AlwaysCombBlock("additionblock"));
        var block = adder.addAlwaysComb("additionBlock");
        var refA = adder.getPort(1).addSubscript(15);
        block.addChild(Verilog.nonBlocking(adder.getPort(2), Verilog.add(refA, adder.getPort(0))));
        return adder;
    }

    @Test
    public void testAdderEmit() {
        var testAdder = HardwareInstanceTest.getAdder();
        testAdder.emit();
    }

    @Test
    public void testHardwareModuleInstanceNewFamily() {
        var testAdder = HardwareInstanceTest.getAdder();
        // this var is HardwareModule

        var wrap = new HardwareModule("adderWrapper",
                new InputPortDeclaration("winA", 32),
                new InputPortDeclaration("winB", 32),
                new InputPortDeclaration("woutC", 32));

        var wire = wrap.addWire("tmp", 32).getReference();

        wrap.addInstance(testAdder.instantiate("adder1",
                wrap.getPort(0), wrap.getPort(1), wire));

        wrap.addStatement(Verilog.nonBlocking(wrap.getPort(2),
                Verilog.add(wire, ImmediateOperator.Ones(15))));

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
        adder.addStatement(Verilog.nonBlocking(adder.getPort(2), Verilog.add(adder.getPort(0), adder.getPort(1))));

        /*
         * 
         */
        adder.emit();
    }

    @Test
    public void testHardwareTestbenchNewFamily() {
        var testAdder = HardwareInstanceTest.getAdder();
        testAdder.emit();
        // this var is HardwareModule

        var tb = new HardwareTestbench("testbench1", testAdder);
        tb.emit();

    }

    @Test
    public void testAddingBlocksNewFamily() {
        var adder = new HardwareModule("adderDef");
        adder.addClock();
        adder.addReset();
        adder.addInputPort("inA", 32);
        adder.addInputPort("inB", 32);
        adder.addOutputPort("outC", 32);

        var ff1 = adder.addAlwaysFFPosedge("testBlock");
        var stat1 = Verilog.nonBlocking(adder.getPort(4), Verilog.add(adder.getPort(2), adder.getPort(3)));
        var stat2 = Verilog.nonBlocking(adder.getPort(4), new ImmediateOperator(0, 32));

        var ifelstat = new IfElseStatement(adder.getPort("rst"));
        ifelstat.addIfStatement(stat2);
        ifelstat.addElseStatement(stat1);

        ff1.addChild(ifelstat);

        adder.emit();
    }

    @Test
    public void testIfElse() {
        var sig = new WireDeclaration("testWire", 1);
        var if1 = new IfStatement(sig.getReference(), "block1");

        var sigA = new WireDeclaration("inA", 8).getReference();
        var sigB = new WireDeclaration("inB", 8).getReference();
        var sigC = new WireDeclaration("outC", 8).getReference();
        var stat1 = Verilog.nonBlocking(sigC, Verilog.add(sigA, sigB));
        if1.addStatement(stat1);
        // if1.addStatement(stat1.copy());
        if1.emit();
    }

    @Test
    public void test1() {

        var mod = new HardwareModule("testMod");
        mod.addClock();
        mod.addReset();
        var refA = mod.addInputPort("inA", 8).getReference();
        var refB = mod.addInputPort("inB", 8).getReference();
        var refC = mod.addOutputPort("outC", 8).getReference();

        var block = mod.addAlwaysComb("comb1");

        var stat = Verilog.nonBlocking(refC, Verilog.add(refA, refB));
        block.addStatement(stat);

        mod.emit();

        /*
         * 
         */
        var tb = new HardwareTestbench("tb1", mod);
        tb.emit();

    }
}
