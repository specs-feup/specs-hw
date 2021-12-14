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

package pt.up.fe.specs.binarytranslation.hardware.factory;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.HardwareBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;

public class nonBlockingMethods { // extends HardwareExpressionMethods?

    private HardwareNode user;

    public nonBlockingMethods(HardwareNode block) {
        this.user = block;
    }

    private HardwareModule getParentModule() {
        var opt = this.user.getAncestorTry(HardwareModule.class);
        return opt.orElse(null);
    }

    /*
     * addition over user of type VariableOperator
     */
    private VariableOperator add(VariableOperator user, HardwareExpression expr) {
        var stat = new ProceduralBlockingStatement(user, expr);

        // if Operator is in a declared block, get direct parent
        var parent = this.user.getAncestorTry(HardwareBlock.class).orElseGet(null);
        if (parent != null) {
            parent.addStatement(stat);
        }

        // TODO: if "user" does not exist in ModuleBlock, if such a block is parent of
        // fetched user parent "parent" (lol)

        // TODO: what to return when user has no parent?? maybe this method should always return the
        // created expression?? or null
        // if the parent is null, then the created expression is gone... its never returned
        // OR
        // i only allow the use of this syntax in VariableOperators declared inside a parent block?

        return user;
    }

    // TODO: these helper methods can be sent to an abstract class
    // since the type of "expr" could be any that extends HardwareExpression
    /*
     * addition over user of type HardwareBlock
     */
    private VariableOperator add(HardwareBlock user, HardwareExpression expr) {

        var resultBits = expr.getResultWidth();
        var newresult = new WireDeclaration("test", resultBits);
        var parenthwmodule = this.getParentModule();
        if (parenthwmodule != null)
            parenthwmodule.addWire(newresult);

        var ref = ((VariableOperator) this.user);
        var stat = new ProceduralBlockingStatement(ref, expr);

        // if Operator is in a declared module

        return ref;
    }

    // TODO: try and make these methods create new declarations as needed and add them to the hardware moddule
    // maybe ill need a setParentModule method..
    public HardwareOperator add(HardwareExpression refA, HardwareExpression refB) {
        var add = new AdditionExpression(refA, refB);

       
        // var resultName = add.getResultName();
        // TODO: generate result names based on input var names, if no named output is given





        // if user context is a Variable, then the addition returns the user itself
        // and adds the add expression to the parent context of the user, i.e., an AlwaysCombBlock etc
        // TODO: this addition to the AlwaysCombBlock must itself propagate up to the ModuleBlock if the
        // node has that parent...

        if (this.user instanceof VariableOperator) {
            return (HardwareOperator) this.user;
            
        } else if() {
            
            var ref = newresult.getReference();
            var stat = new ProceduralBlockingStatement(ref, add);
        }

        // this.user.addStatement(stat);

        return ref;
    }
}