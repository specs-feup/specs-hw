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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ParenthesisExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.DivisionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.LeftShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.MultiplicationExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.RightArithmeticShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.RightLogicalShiftExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.SubtractionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseAndExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseNotExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseOrExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.bitwise.BitWiseXorExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.ComparisonExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.EqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.GreaterThanExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.GreaterThanOrEqualsExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.LessThanExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.LessThanOrEqualsExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.logical.LogicalAndExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.logical.LogicalOrExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

/**
 * a generic interface that classes like VariableOperator and HardwareBlock may inherit, in order to obtain some sugar
 */
public interface HardwareOperationsInterface {

    // e.g. this could be a VariableOperator
    // TODO: what it its another expression? the addition would have to first put that expression into parenthesis...
    // this would entail node replacement...
    public abstract HardwareExpression getThis();

    /*
     * 
     */
    public default AdditionExpression add(HardwareExpression refB) {
        return new AdditionExpression(getThis(), refB);
    }

    public default DivisionExpression div(HardwareExpression refB) {
        return new DivisionExpression(getThis(), refB);
    }

    public default MultiplicationExpression mul(HardwareExpression refB) {
        return new MultiplicationExpression(getThis(), refB);
    }

    public default LeftShiftExpression lsl(HardwareExpression refB) {
        return new LeftShiftExpression(getThis(), refB);
    }

    public default RightLogicalShiftExpression rsl(HardwareExpression refB) {
        return new RightLogicalShiftExpression(getThis(), refB);
    }

    public default RightArithmeticShiftExpression rsa(HardwareExpression refB) {
        return new RightArithmeticShiftExpression(getThis(), refB);
    }

    public default SubtractionExpression sub(HardwareExpression refB) {
        return new SubtractionExpression(getThis(), refB);
    }

    public default BitWiseAndExpression and(HardwareExpression refB) {
        return new BitWiseAndExpression(getThis(), refB);
    }

    public default BitWiseNotExpression not() {
        return new BitWiseNotExpression(getThis());
    }

    public default BitWiseOrExpression or(HardwareExpression refB) {
        return new BitWiseOrExpression(getThis(), refB);
    }

    public default BitWiseXorExpression xor(HardwareExpression refB) {
        return new BitWiseXorExpression(getThis(), refB);
    }

    public default ComparisonExpression cmp(HardwareExpression refB) {
        return new ComparisonExpression(getThis(), refB);
    }

    public default EqualsToExpression eq(HardwareExpression refB) {
        return new EqualsToExpression(getThis(), refB);
    }

    public default GreaterThanExpression gt(HardwareExpression refB) {
        return new GreaterThanExpression(getThis(), refB);
    }

    public default GreaterThanOrEqualsExpression geqt(HardwareExpression refB) {
        return new GreaterThanOrEqualsExpression(getThis(), refB);
    }

    public default LessThanExpression lt(HardwareExpression refB) {
        return new LessThanExpression(getThis(), refB);
    }

    public default LessThanOrEqualsExpression leqt(HardwareExpression refB) {
        return new LessThanOrEqualsExpression(getThis(), refB);
    }

    public default NotEqualsToExpression noteq(HardwareExpression refB) {
        return new NotEqualsToExpression(getThis(), refB);
    }

    public default LogicalAndExpression land(HardwareExpression refB) {
        return new LogicalAndExpression(getThis(), refB);
    }

    public default LogicalOrExpression lor(HardwareExpression refB) {
        return new LogicalOrExpression(getThis(), refB);
    }

    public default ParenthesisExpression paren() {
        return new ParenthesisExpression(getThis());
    }

    /*
     * nonBlocking
     */
    public default ProceduralNonBlockingStatement nonBlocking(int literalConstant) {
        return nonBlocking(new ImmediateOperator(literalConstant, getThis().getResultWidth()));
    }

    public default ProceduralNonBlockingStatement nonBlocking(HardwareExpression expr) {
        return new ProceduralNonBlockingStatement((VariableOperator) getThis(), expr);
    }

    /*
     * blocking
     */
    public default ProceduralBlockingStatement blocking(int literalConstant) {
        return blocking(new ImmediateOperator(literalConstant, getThis().getResultWidth()));
    }

    public default ProceduralBlockingStatement blocking(HardwareExpression expr) {
        return new ProceduralBlockingStatement((VariableOperator) getThis(), expr);
    }

    /*
     * assign
     */
    public default ContinuousStatement assign(int literalConstant) {
        return assign(new ImmediateOperator(literalConstant, getThis().getResultWidth()));
    }

    public default ContinuousStatement assign(HardwareExpression expr) {
        return new ContinuousStatement((VariableOperator) getThis(), expr);
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
