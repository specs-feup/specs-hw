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

public class ImmediateOperator extends HardwareOperator {

    private static int immcounter = 0;
    private int width;
    private final Number value;
    // TODO: private final VariableDeclaration declaration;

    /*
     * Note: for now, all immediates are represented as decimals
     */
    public ImmediateOperator(Number value, int width) {
        super(HardwareNodeType.ImmediateOperator);
        this.value = value;
        this.width = width;
        immcounter++;
    }

    @Override
    public String getAsString() {
        return Integer.toString(this.width) + "'d" + this.value;
    }

    @Override
    public ImmediateOperator getThis() {
        return this;
    }

    /*
     * TODO: this is only used to get the identifier name for uses like ranged selection...
     * is this the best way?
     */
    @Override
    public String getValue() {
        return String.valueOf(this.value.intValue());
    }

    @Override
    public String getResultName() {
        return "imm_" + this.width + "_" + immcounter;
    }

    @Override
    public int getResultWidth() {
        return this.width;
    }

    @Override
    protected ImmediateOperator copyPrivate() {
        return new ImmediateOperator(this.value, this.width);
    }

    @Override
    public ImmediateOperator copy() {
        return (ImmediateOperator) super.copy();
    }

    public static ImmediateOperator Zeroes(int numbits) {
        return new ImmediateOperator(0, numbits);
    }

    public static ImmediateOperator Ones(int numbits) {
        return new ImmediateOperator(1, numbits);
    }
}
