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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;

public class ArrayDeclaration extends HardwareDeclaration {

    private int size;

    private ArrayDeclaration(VariableDeclaration variable) {
        super(HardwareNodeType.ArrayDeclaration);
        this.addChild(variable);
    }

    /*
     * Copy constructor
     */
    private ArrayDeclaration(ArrayDeclaration other) {
        this((VariableDeclaration) other.getChild(0));
        // TODO: make cleaner
    }

    /*
     * TODO: fix later, but for now, 3 different constructors 
     * just too prevent instantiating ArrayDeclaration as an
     * array of type PortDeclaration (not permitted in Verilog)
     */
    public ArrayDeclaration(WireDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    public ArrayDeclaration(RegisterDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    public ArrayDeclaration(IntegerDeclaration variable, int size) {
        this(variable);
        this.size = size;
    }

    public String getVariableName() {
        return ((VariableDeclaration) this.getChild(0)).getVariableName();
        // TODO: make cleaner
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public String getAsString() {
        return this.getChild(0).getNodeName() + " [" + (this.getSize() - 1) + " : 0];";
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
