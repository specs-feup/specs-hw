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

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.SubtractionExpression;

/**
 * a generic interface that classes like VariableOperator and HardwareBlock may inherit, in order to obtain some sugar
 */
public interface HardwareOperationsInterface {

    // e.g. this could be a VariableOperator
    // TODO: what it its another expression? the addition would have to first put that expression into parenthesis...
    // this would entail node replacement...
    public abstract HardwareExpression getThis();

    // public abstract HardwareNode updateStatus(HardwareExpression myself, HardwareExpression newExpr);

    /*
     * 
     */
    public default AdditionExpression add(HardwareExpression refB) {
        var expr = new AdditionExpression(getThis(), refB);
        // updateStatus(getThis(), expr);
        return expr;
    }

    public default SubtractionExpression sub(HardwareExpression refB) {
        var expr = new SubtractionExpression(getThis(), refB);
        // updateStatus(getThis(), expr);
        return expr;
    }

    /*
    // TODO: more
    
    private T getResult(HardwareExpression expr) {
    
        var resultBits = expr.getResultWidth();
        var newresult = new WireDeclaration(expr.getResultName(), resultBits); // TODO: auto-gen name
        var stat = this.supplier.apply(newresult.getReference(), expr);
    
        var parenthwmodule = user.getAncestorTry(HardwareModule.class).orElse(null);
        if (parenthwmodule != null) {
            parenthwmodule.addWire(newresult); // add new wire to declaration block
        }
        user.addStatement(stat);
    
        return newresult.getReference();
    }*/
}
