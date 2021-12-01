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

import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.selection.RangeSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.FileHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareRootNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

public class HardwareInstanceTest {

    @Test
    public void testHardwareFast() {
        var portA = new InputPortDeclaration("cenas1", 8);
        var portB = new InputPortDeclaration("cenas2", 8);
        var module = new VerilogModuleTree("testAdder");
        module.addDeclaration(portA).addDeclaration(portB);
        module.addStatement(
                new ContinuousStatement(portA.getReference(),
                        new AdditionExpression(portA.getReference(), portB.getReference())));
        module.emit();
    }

    @Test
    public void testHardwareInstance() {
        var root = new HardwareRootNode();
        var header = new FileHeader();
        root.addChild(header);

        var module = new ModuleDeclaration("testModule");
        root.addChild(module);
        var a = new InputPortDeclaration("testA", 32);
        var b = new InputPortDeclaration("testB", 32);
        var c = new InputPortDeclaration("testC", 32);
        module.addChild(a);
        module.addChild(b);
        module.addChild(c);
        var refA = new RangeSelection(new VariableReference(b.getVariableName()), 15);
        var expr = new AdditionExpression(refA, new VariableReference(c.getVariableName()));

        var body = new AlwaysCombBlock("additionblock");
        module.addChild(body);
        body.addChild(new ProceduralNonBlockingStatement(new VariableReference(a.getVariableName()), expr));

        /*
         * Print to console!
         */
        root.emit(System.out);
    }
}
