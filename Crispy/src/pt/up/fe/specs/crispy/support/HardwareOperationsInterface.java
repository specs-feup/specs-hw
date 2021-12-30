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

package pt.up.fe.specs.crispy.support;

import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.ParenthesisExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.DivisionExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.LeftShiftExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.MultiplicationExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.RightArithmeticShiftExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.RightLogicalShiftExpression;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.SubtractionExpression;
import pt.up.fe.specs.crispy.ast.expression.bitwise.BitWiseAndExpression;
import pt.up.fe.specs.crispy.ast.expression.bitwise.BitWiseNotExpression;
import pt.up.fe.specs.crispy.ast.expression.bitwise.BitWiseOrExpression;
import pt.up.fe.specs.crispy.ast.expression.bitwise.BitWiseXorExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.ComparisonExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.EqualsToExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.GreaterThanExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.GreaterThanOrEqualsExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.LessThanExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.LessThanOrEqualsExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.crispy.ast.expression.logical.LogicalAndExpression;
import pt.up.fe.specs.crispy.ast.expression.logical.LogicalOrExpression;
import pt.up.fe.specs.crispy.ast.expression.logical.ReplicationExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.ContinuousStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralNonBlockingStatement;

/**
 * An interface inheritable by @HardwareExpression
 */
public interface HardwareOperationsInterface {

    // e.g. this could be a VariableOperator
    // TODO: what it its another expression? the addition would have to first put that expression into parenthesis...
    // this would entail node replacement...
    public abstract HardwareExpression getThis();

    /*
     * 
     */
    public default AdditionExpression add(int literalConstant) {
        return new AdditionExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default AdditionExpression add(HardwareExpression refB) {
        return new AdditionExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default DivisionExpression div(int literalConstant) {
        return new DivisionExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default DivisionExpression div(HardwareExpression refB) {
        return new DivisionExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default MultiplicationExpression mul(int literalConstant) {
        return new MultiplicationExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default MultiplicationExpression mul(HardwareExpression refB) {
        return new MultiplicationExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default LeftShiftExpression lsl(int literalConstant) {
        return new LeftShiftExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default LeftShiftExpression lsl(HardwareExpression refB) {
        return new LeftShiftExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default RightLogicalShiftExpression rsl(int literalConstant) {
        return new RightLogicalShiftExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default RightLogicalShiftExpression rsl(HardwareExpression refB) {
        return new RightLogicalShiftExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default RightArithmeticShiftExpression rsa(int literalConstant) {
        return new RightArithmeticShiftExpression(getThis(),
                new Immediate(literalConstant, getThis().getWidth()));
    }

    public default RightArithmeticShiftExpression rsa(HardwareExpression refB) {
        return new RightArithmeticShiftExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default RightArithmeticShiftExpression sub(int literalConstant) {
        return new RightArithmeticShiftExpression(getThis(),
                new Immediate(literalConstant, getThis().getWidth()));
    }

    public default SubtractionExpression sub(HardwareExpression refB) {
        return new SubtractionExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default BitWiseAndExpression and(int literalConstant) {
        return new BitWiseAndExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default BitWiseAndExpression and(HardwareExpression refB) {
        return new BitWiseAndExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default BitWiseNotExpression not() {
        return new BitWiseNotExpression(getThis());
    }

    /*
     * 
     */
    public default BitWiseOrExpression or(int literalConstant) {
        return new BitWiseOrExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default BitWiseOrExpression or(HardwareExpression refB) {
        return new BitWiseOrExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default BitWiseXorExpression xor(int literalConstant) {
        return new BitWiseXorExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default BitWiseXorExpression xor(HardwareExpression refB) {
        return new BitWiseXorExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default ComparisonExpression cmp(int literalConstant) {
        return new ComparisonExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default ComparisonExpression cmp(HardwareExpression refB) {
        return new ComparisonExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default EqualsToExpression eq(int literalConstant) {
        return new EqualsToExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default EqualsToExpression eq(HardwareExpression refB) {
        return new EqualsToExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default GreaterThanExpression gt(int literalConstant) {
        return new GreaterThanExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default GreaterThanExpression gt(HardwareExpression refB) {
        return new GreaterThanExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default GreaterThanOrEqualsExpression geqt(int literalConstant) {
        return new GreaterThanOrEqualsExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default GreaterThanOrEqualsExpression geqt(HardwareExpression refB) {
        return new GreaterThanOrEqualsExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default LessThanExpression lt(int literalConstant) {
        return new LessThanExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default LessThanExpression lt(HardwareExpression refB) {
        return new LessThanExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default LessThanOrEqualsExpression leqt(int literalConstant) {
        return new LessThanOrEqualsExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default LessThanOrEqualsExpression leqt(HardwareExpression refB) {
        return new LessThanOrEqualsExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default NotEqualsToExpression noteq(int literalConstant) {
        return new NotEqualsToExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default NotEqualsToExpression noteq(HardwareExpression refB) {
        return new NotEqualsToExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default LogicalAndExpression land(int literalConstant) {
        return new LogicalAndExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default LogicalAndExpression land(HardwareExpression refB) {
        return new LogicalAndExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default LogicalOrExpression lor(int literalConstant) {
        return new LogicalOrExpression(getThis(), new Immediate(literalConstant, getThis().getWidth()));
    }

    public default LogicalOrExpression lor(HardwareExpression refB) {
        return new LogicalOrExpression(getThis(), refB);
    }

    /*
     * 
     */
    public default ReplicationExpression rep(int replicateCount) {
        return new ReplicationExpression(replicateCount, getThis());
    }

    /*
     * 
     */
    public default ParenthesisExpression paren() {
        return new ParenthesisExpression(getThis());
    }

    /*
     * nonBlocking
     */
    public default ProceduralNonBlockingStatement nonBlocking(int literalConstant) {
        return nonBlocking(new Immediate(literalConstant, getThis().getWidth()));
    }

    public default ProceduralNonBlockingStatement nonBlocking(HardwareExpression expr) {
        return new ProceduralNonBlockingStatement((VariableOperator) getThis(), expr);
    }

    /*
     * blocking
     */
    public default ProceduralBlockingStatement blocking(int literalConstant) {
        return blocking(new Immediate(literalConstant, getThis().getWidth()));
    }

    public default ProceduralBlockingStatement blocking(HardwareExpression expr) {
        return new ProceduralBlockingStatement((VariableOperator) getThis(), expr);
    }

    /*
     * assign
     */
    public default ContinuousStatement assign(int literalConstant) {
        return assign(new Immediate(literalConstant, getThis().getWidth()));
    }

    public default ContinuousStatement assign(HardwareExpression expr) {
        return new ContinuousStatement((VariableOperator) getThis(), expr);
    }
}
