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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;

public abstract class HardwareBlock extends HardwareNode implements HardwareBlockInterface {

    protected HardwareBlock(HardwareNodeType type) {
        super(type);
    }

    private HardwareModule getParentModule() {
        var parent = this.getParent();
        return (HardwareModule) parent;
    }

    @Override
    public String getAsString() {
        return nestContent(super.getAsString());
    }

    public static class nonBlocking {

        // TODO: try and make these methods create new declarations as needed and add them to the hardware moddule
        // maybe ill need a setParentModule method..
        public static ProceduralBlockingStatement add(HardwareExpression refA, HardwareExpression refB) {
            var add = new AdditionExpression(refA, refB);
            
            var resultBits = add.getResultWidth();
            // var resultName = add.getResultName(); 
            // TODO: generate result names based on input var names, if no named output is given
            
            var newresult = new WireDeclaration("test", resultBits);
            
            // TODO: how to refer to parent of HardwareBlock from this static class?
            
            var stat = new ProceduralBlockingStatement()
        }
    }
}
