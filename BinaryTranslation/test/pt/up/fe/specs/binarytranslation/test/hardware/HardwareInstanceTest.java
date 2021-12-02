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

import pt.up.fe.specs.binarytranslation.hardware.VerilogModule;
import pt.up.fe.specs.binarytranslation.hardware.testbench.HardwareTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.IdentifierReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.IndexedSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.RangedSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

public class HardwareInstanceTest {

    @Test
    public void testOperatorReferences() {

        var decl1 = new RegisterDeclaration("testReg", 32);
        decl1.emit();

        var imm = new ImmediateOperator(14, 32);
        imm.emit();

        var ref1 = new IdentifierReference(decl1);
        ref1.emit();

        var ref1b = decl1.getReference();
        ref1b.emit();

        // 1d index
        var index = new IndexedSelection(decl1.getReference(), imm);
        index.emit();

        // 2d index
        var index2 = new IndexedSelection(index, imm);
        index2.emit();

        // 2d index left hand
        var index3 = new IndexedSelection(imm, index);
        index3.emit();

        // var select = new RangedReference(index, 32);

    }

    @Test
    public void testHardwareFast() {
        var portA = new InputPortDeclaration("cenas1", 8);
        var portB = new InputPortDeclaration("cenas2", 8);
        var tree = new VerilogModuleTree("testAdder");
        tree.addDeclaration(portA).addDeclaration(portB);
        tree.addStatement(
                new ContinuousStatement(portA.getReference(),
                        new AdditionExpression(portA.getReference(), portB.getReference())));

        var module = new VerilogModule("testinstance", "moduleA", tree);
        module.emit();

        /*
         * test testbench too 
         */
        var tb = HardwareTestbenchGenerator.generate(module, 100, "testinput.mem", "testoutput.mem");
        tb.emit();
    }

    @Test
    public void testHardwareInstance() {
        var module = new VerilogModuleTree("testModule");
        var a = new InputPortDeclaration("testA", 32);
        var b = new InputPortDeclaration("testB", 32);
        var c = new InputPortDeclaration("testC", 32);
        module.addDeclaration(a).addDeclaration(b).addDeclaration(c);

        var refA = new RangedSelection(b.getReference(), 15);
        var expr = new AdditionExpression(refA, c.getReference());

        var body = new AlwaysCombBlock("additionblock");
        module.addStatement(body);
        body.addChild(new ProceduralNonBlockingStatement(a.getReference(), expr));

        /*
         * Print to console!
         */
        module.emit();
    }
}
