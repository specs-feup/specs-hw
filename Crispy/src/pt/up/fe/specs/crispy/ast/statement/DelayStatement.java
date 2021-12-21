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

package pt.up.fe.specs.crispy.ast.statement;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;

public class DelayStatement extends HardwareStatement {

    private Number value;

    public DelayStatement(Number value) {
        super(HardwareNodeType.DelayStatement);
        this.value = value;
    }

    @Override
    protected DelayStatement copyPrivate() {
        return new DelayStatement(this.value);
    }

    @Override
    public DelayStatement copy() {
        return (DelayStatement) super.copy();
    }

    @Override
    public String getAsString() {
        return "#" + String.valueOf(this.value) + ";";
    }
}
