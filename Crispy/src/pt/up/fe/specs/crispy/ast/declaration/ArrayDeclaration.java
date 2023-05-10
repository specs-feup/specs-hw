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

package pt.up.fe.specs.crispy.ast.declaration;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.integer.IntegerDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.register.RegisterDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.wire.WireDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.VArray;

public class ArrayDeclaration extends IdentifierDeclaration {

    private int size;

    private ArrayDeclaration(IdentifierDeclaration variable) {
        super(variable.getVariableName(), variable.getVariableWidth(), HardwareNodeType.ArrayDeclaration);
        this.addChild(variable);
    }

    /*
     * Copy constructor
     */
    private ArrayDeclaration(ArrayDeclaration other) {
        this(other.getVariable().copy());
    }

    /*
     * TODO: fix later, but for now, 3 different constructors 
     * just too prevent instantiating ArrayDeclaration as an
     * array of type PortDeclaration (not permitted in Verilog)
     */
    private ArrayDeclaration(WireDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    private ArrayDeclaration(RegisterDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    private ArrayDeclaration(IntegerDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    private ArrayDeclaration(IdentifierDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    /*
     * 
     */
    public static ArrayDeclaration of(IdentifierDeclaration decl, int arraySize) {
        return new ArrayDeclaration(decl, arraySize);
    }

    public static ArrayDeclaration ofWires(String wireName, int numBits, int arraySize) {
        return ArrayDeclaration.of(new WireDeclaration(wireName, numBits), arraySize);
    }

    public static ArrayDeclaration ofRegisters(String regName, int numBits, int arraySize) {
        return ArrayDeclaration.of(new RegisterDeclaration(regName, numBits), arraySize);
    }

    public static ArrayDeclaration ofInteger(String intName, int arraySize) {
        return ArrayDeclaration.of(new IntegerDeclaration(intName), arraySize);
    }

    public IdentifierDeclaration getVariable() {
        return this.getChild(IdentifierDeclaration.class, 0);
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public String getAsString() {
        return super.getAsString() + this.getChild(0).getAsString().replace(";", "") + " [" + (this.getSize() - 1)
                + " : 0];";
    }

    @Override
    public VArray getReference() {
        return new VArray(this);
    }

    @Override
    protected ArrayDeclaration copyPrivate() {
        return new ArrayDeclaration(this);
    }

    @Override
    public ArrayDeclaration copy() {
        return (ArrayDeclaration) super.copy();
    }
}
