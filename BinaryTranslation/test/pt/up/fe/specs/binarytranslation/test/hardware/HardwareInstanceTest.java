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

import java.util.ArrayList;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.hardware.factory.Verilog;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.ScalarSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;

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
        adder.addInputPort("testA", 32).addInputPort("testB", 32).addOutputPort("testC", 32);
        var block = adder.addChild(new AlwaysCombBlock("additionblock"));
        var refA = adder.getPort(1).addSubscript(15);
        block.addChild(Verilog.nonBlocking(adder.getPort(2), Verilog.add(refA, adder.getPort(0))));
        return adder;
    }

    @Test
    public void testHardwareModuleInstanceNewFamily() {
        var testAdder = HardwareInstanceTest.getAdder();
        testAdder.emit();
        // this var is HardwareModule

        var wrapperadder = new HardwareModule("adderWrapper");
        wrapperadder.addInputPort("winA", 32).addInputPort("winB", 32).addOutputPort("woutC", 32);

        var wire = new WireDeclaration("tmp", 32);
        wrapperadder.addWire(wire);

        var connections = new ArrayList<HardwareOperator>();
        connections.add(wrapperadder.getPort(0));
        connections.add(wrapperadder.getPort(1));
        connections.add(wire.getReference());
        var adderInstantiation = new ModuleInstance(testAdder, "adder1", connections);
        wrapperadder.addStatement(adderInstantiation);
        wrapperadder.addStatement(Verilog.nonBlocking(wrapperadder.getPort(2),
                Verilog.add(wire.getReference(), ImmediateOperator.Ones(15))));

        /*
         * Test module instantiation inside other module
         */
        wrapperadder.emit();
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
        adder.addInputPort("inA", 32).addInputPort("inB", 32).addOutputPort("outC", 32);
        adder.addStatement(Verilog.nonBlocking(adder.getPort(2), Verilog.add(adder.getPort(0), adder.getPort(1))));

        /*
         * 
         */
        adder.emit();
    }
}
