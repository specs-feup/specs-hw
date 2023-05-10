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

package pt.up.fe.specs.crispy.experimental;

import java.util.function.BiFunction;

import pt.up.fe.specs.crispy.ast.block.HardwareBlock;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.declaration.wire.WireDeclaration;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.SubtractionExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.HardwareStatement;

public abstract class HardwareExpressionMethods<T extends HardwareStatement> {

    private HardwareBlock user;
    private BiFunction<VariableOperator, HardwareExpression, T> supplier;

    public HardwareExpressionMethods(HardwareBlock user,
            BiFunction<VariableOperator, HardwareExpression, T> supplier) {
        this.user = user;
        this.supplier = supplier;
    }

    protected HardwareModule getParentModule() {
        var opt = this.user.getAncestorTry(HardwareModule.class);
        return opt.orElse(null);
    }

    /*
     * 
     */
    public VariableOperator add(HardwareExpression refA, HardwareExpression refB) {
        return getResult(this.user, new AdditionExpression(refA, refB));
    }

    public VariableOperator sub(HardwareExpression refA, HardwareExpression refB) {
        return getResult(this.user, new SubtractionExpression(refA, refB));
    }

    /*
     * only allow users of type HardwareBlock for now
     */
    private VariableOperator getResult(HardwareBlock user, HardwareExpression expr) {

        var resultBits = expr.getWidth();
        var newresult = new WireDeclaration(expr.getName(), resultBits); // TODO: auto-gen name
        var stat = this.supplier.apply(newresult.getReference(), expr);

        var parenthwmodule = user.getAncestorTry(HardwareModule.class).orElse(null);
        if (parenthwmodule != null) {
            parenthwmodule.addWire(newresult); // add new wire to declaration block
        }
        user._do(stat);

        return newresult.getReference();

        /*
        
        // undeclared variable (user is Hardware)
        if (type == HardwareNodeType.ModuleBlock || type == HardwareNodeType.BeginEndBlock) {
        
            var resultBits = expr.getResultWidth();
            var newresult = new WireDeclaration("test", resultBits); // TODO: auto-gen name
            var parenthwmodule = user.getAncestorTry(HardwareModule.class).orElse(null);
            if (parenthwmodule != null)
                parenthwmodule.addWire(newresult); // add new wire to declaration block
        
            return this.supplier.apply(newresult.getReference(), expr);
        }
        
        // already declared variable
        else if (type == HardwareNodeType.VariableOperator) {
        
            return (U) this.supplier.apply((VariableOperator) user, expr);
        }*/

    }

    /*
    private VariableOperator add(HardwareBlock user, HardwareStatement stat) {
    
        var parenthwmodule = this.getParentModule();
        if (parenthwmodule != null)
            parenthwmodule.addWire(newresult);
    
        var ref = ((VariableOperator) this.user);
        var stat = new ProceduralBlockingStatement(ref, expr);
    
        // if Operator is in a declared module
    
        return ref;
    }*/

    // this method will alow for the follow syntax in child classes:
    // nonBlocking.add(refA, refB)
    // which will call private method:
    // add(this, refA, refB)
    // which wil call parent method
    // add(refA, refB) which returns and expression
    // which then is made into a statement
    // and then added to the parent based on type?

}
