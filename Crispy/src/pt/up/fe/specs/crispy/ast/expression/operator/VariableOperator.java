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

public class VariableOperator extends HardwareOperator {

    // TODO: make abstract

    private final String name;
    private final int width;
    private IdentifierDeclaration associatedIdentifier;
    // public nonBlockingMethods nonBlocking;

    /*
     * Auxiliary copy constructor
     */
    private VariableOperator(VariableOperator other) {
        super(other.type);
        this.name = other.name;
        this.width = other.width;
        // this.nonBlocking = new nonBlockingMethods(this);
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
    public VariableOperator(IdentifierDeclaration namedIdentifier) {
        super(HardwareNodeType.VariableOperator);
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
    public String getResultName() {
        return this.name;
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

    public VariableOperator addSubscript(int left, int right) {
        this.addChild(new RangedSubscript(left, right));
        return this;
    }

    /*
     * only allow users of type HardwareBlock for now
     
    private void updateTree(ASingleStatement stat) {
    
        // TODO: this parentage check isnt going to work because the
        // VariableOperator is a Child of the ContinuousStatement being created!
        // when I do getPort, it returns a reference of an indentifier,
        // but that reference has no parent!
    
        var beblock = this.parent.getAncestorTry(BeginEndBlock.class).orElse(null);
        var hwmod = this.parent.getAncestorTry(HardwareModule.class).orElse(null);
    
        // check if statement is in BeginEndBlock
        if (beblock != null) {
            beblock.addStatement(stat);
        }
    
        else if (hwmod != null) {
            hwmod.addStatement(stat);
        }
    
        // if the BeginEndBlock is inside a ModuleBlock, see if declared
        if (hwmod != null) {
            if (hwmod.indexOfChild(this.associatedIdentifier) == -1)
                hwmod.addDeclaration(this.associatedIdentifier);
        }
    }*/

    /*
     * 
     
    public ContinuousStatement assign(HardwareExpression refB) {
        var stat = new ContinuousStatement(this, refB);
        updateTree(stat);
        return stat;
    }
    
    public ProceduralNonBlockingStatement nonBlocking(HardwareExpression expression) {
        var stat = new ProceduralNonBlockingStatement(this, expression);
        updateTree(stat);
        return stat;
    }
    
    public ProceduralBlockingStatement blocking(HardwareExpression expression) {
        var stat = new ProceduralBlockingStatement(this, expression);
        updateTree(stat);
        return stat;
    }*/

    /*    public ProceduralNonBlockingStatement nonBlocking(HardwareExpression refA, HardwareExpression refB) {
        return new ProceduralNonBlockingStatement(this, nonBlocking.add(refA, refB));// !!!
    }*/
}
