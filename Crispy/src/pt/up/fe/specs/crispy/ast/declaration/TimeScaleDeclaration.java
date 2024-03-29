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

public class TimeScaleDeclaration extends HardwareDeclaration {

    // NOTE: presumed to be in nanoseconds
    private final int timeUnit, timePrecision;

    public TimeScaleDeclaration(int timeUnit, int timePrecision) {
        super(HardwareNodeType.TimeScaleDeclaration);
        this.timeUnit = timeUnit;
        this.timePrecision = timePrecision;
    }

    public TimeScaleDeclaration() {
        this(10, 1);
    }

    @Override
    protected TimeScaleDeclaration copyPrivate() {
        return new TimeScaleDeclaration(this.timeUnit, this.timePrecision);
    }

    @Override
    public TimeScaleDeclaration copy() {
        return (TimeScaleDeclaration) super.copy();
    }

    public int getTimePrecision() {
        return timePrecision;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String getAsString() {

        var builder = new StringBuilder();
        builder.append("\n`timescale ");

        // put time unit
        builder.append(this.timeUnit + "ns");
        builder.append("/");
        // put time precision
        builder.append(this.timePrecision + "ps\n");

        return builder.toString();
    }
}
