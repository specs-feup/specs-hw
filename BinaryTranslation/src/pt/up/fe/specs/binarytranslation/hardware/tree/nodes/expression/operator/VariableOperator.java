/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator;

import pt.up.fe.specs.binarytranslation.hardware.factory.nonBlockingMethods;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.OperatorSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.subscript.ScalarSubscript;

public class VariableOperator extends HardwareOperator {

    private final String name;
    private final int width;
    public nonBlockingMethods nonBlocking;

    /*
     * Auxiliary copy constructor
     */
    private VariableOperator(VariableOperator other) {
        super(other.type);
        this.name = other.name;
        this.width = other.width;
        this.nonBlocking = new nonBlockingMethods(this);
    }

    /*
     * This should be the ONLY valid constructor, since
     * we should only be able to reference something we've
     * declared
     * (I've also added a "getReference()" method to
     * any class which inherits from @VariableDeclaration)
     */
    public VariableOperator(IdentifierDeclaration namedIdentifier) {
        super(HardwareNodeType.VariableOperator);
        this.name = namedIdentifier.getVariableName();
        this.width = namedIdentifier.getVariableWidth();
    }

    @Override
    public String getAsString() {

        var sb = new StringBuilder();
        sb.append(this.name);

        // print any right hand indexing children;
        // TODO: ensure that right hand children are only indexing nodes
        for (var indexes : this.getChildren(OperatorSubscript.class)) {
            sb.append(indexes.getAsString());
        }

        return sb.toString();
    }

    @Override
    public String getValue() {
        return this.getAsString();
    }

    @Override
    public int getResultWidth() {
        return this.width;
    }

    @Override
    protected VariableOperator copyPrivate() {
        return new VariableOperator(this);
    }

    @Override
    public VariableOperator copy() {
        return (VariableOperator) super.copy();
    }

    /*
     * 
     */
    public VariableOperator addSubscript(OperatorSubscript subs) {
        this.addChild(subs);
        return this;
    }

    /*
     * 
     */
    public VariableOperator addSubscript(HardwareOperator idx) {
        this.addChild(new ScalarSubscript(idx));
        return this;
    }

    /*
     * 
     */
    public VariableOperator addSubscript(int idx) {
        this.addChild(new ScalarSubscript(idx));
        return this;
    }

    /*
     * 
     */
    public VariableOperator addSubscript(HardwareOperator left, HardwareOperator right) {
        this.addChild(new RangedSubscript(left, right));
        return this;
    }

    /*
     * 
     */
    public VariableOperator addSubscript(int left, int right) {
        this.addChild(new RangedSubscript(left, right));
        return this;
    }

    /*
     * 
    
    public ProceduralNonBlockingStatement nonBlocking(HardwareExpression expression) {
        return new ProceduralNonBlockingStatement(this, expression);
    }
    
    public ProceduralNonBlockingStatement nonBlocking(HardwareExpression refA, HardwareExpression refB) {
        return new ProceduralNonBlockingStatement(this, nonBlocking.add(refA, refB));// !!!
    }*/
}
