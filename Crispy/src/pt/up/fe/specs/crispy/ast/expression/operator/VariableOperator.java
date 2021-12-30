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

package pt.up.fe.specs.crispy.ast.expression.operator;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.OperatorSubscript;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.RangedSubscript;
import pt.up.fe.specs.crispy.ast.expression.operator.subscript.ScalarSubscript;

public abstract class VariableOperator extends HardwareOperator {

    private final String name;
    private final int width;
    private IdentifierDeclaration associatedIdentifier;

    /*
     * Auxiliary copy constructor
     */
    private VariableOperator(VariableOperator other) {
        super(other.type);
        this.name = other.name;
        this.width = other.width;
    }

    @Override
    public VariableOperator getThis() {
        return this;
    }

    /*
     * This should be the ONLY valid constructor, since
     * we should only be able to reference something we've
     * declared
     * (I've also added a "getReference()" method to
     * any class which inherits from @VariableDeclaration)
     */
    protected VariableOperator(HardwareNodeType type, IdentifierDeclaration namedIdentifier) {
        super(type);
        this.name = namedIdentifier.getVariableName();
        this.width = namedIdentifier.getVariableWidth();
        this.associatedIdentifier = namedIdentifier;
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

    public IdentifierDeclaration getAssociatedIdentifier() {
        return associatedIdentifier;
    }

    @Override
    public String getValue() {
        return this.getAsString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    /*@Override
    protected VariableOperator copyPrivate() {
        return new VariableOperator(this);
    }*/

    @Override
    public VariableOperator copy() {
        return (VariableOperator) super.copy();
    }

    /*
     * 
     */
    public VariableOperator idx(OperatorSubscript subs) {
        var copy = this.copy();
        copy.addChild(subs);
        return copy;
    }

    /*
     * 
     */
    public VariableOperator idx(HardwareOperator idx) {
        var copy = this.copy();
        copy.addChild(new ScalarSubscript(idx));
        return copy;
    }

    /*
     * 
     */
    public VariableOperator idx(int idx) {
        var copy = this.copy();
        copy.addChild(new ScalarSubscript(idx));
        return copy;
    }

    /*
     * 
     */
    public VariableOperator idx(HardwareOperator left, HardwareOperator right) {
        var copy = this.copy();
        copy.addChild(new RangedSubscript(left, right));
        return copy;
    }

    public VariableOperator idx(int left, int right) {
        var copy = this.copy();
        copy.addChild(new RangedSubscript(left, right));
        return copy;
    }
}
