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

public class HardwareErrorMessage extends HardwareTask {

    final private String errorText;

    public HardwareErrorMessage(String commentText) {
        super(HardwareNodeType.ErrorMsg);
        this.errorText = commentText;
    }

    @Override
    public String getAsString() {
        return "$error(\"" + errorText + "\");";
    }

    @Override
    public String toContentString() {
        return this.getAsString();
    }

    @Override
    protected HardwareErrorMessage copyPrivate() {
        return new HardwareErrorMessage(this.errorText);
    }

    @Override
    public HardwareErrorMessage copy() {
        return (HardwareErrorMessage) super.copy();
    }
}
