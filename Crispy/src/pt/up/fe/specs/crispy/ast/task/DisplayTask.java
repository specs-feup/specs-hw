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

package pt.up.fe.specs.crispy.ast.task;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;

public class DisplayTask extends HardwareTask {

    private String arguments;

    public DisplayTask(String arguments) {
        super(HardwareNodeType.DisplayTask);
        this.arguments = arguments;
    }

    @Override
    protected DisplayTask copyPrivate() {
        return new DisplayTask(this.arguments);
    }

    @Override
    public DisplayTask copy() {
        return (DisplayTask) super.copy();
    }

    @Override
    public String getAsString() {
        return "$display(\"" + this.arguments + "\");";
    }
}
