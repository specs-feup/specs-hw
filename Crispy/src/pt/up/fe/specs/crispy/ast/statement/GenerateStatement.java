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

public class GenerateStatement extends ForLoopStatement {

    // TODO: GENERATE LOOPS ALLOW MODULE INSTANCES WITHIN THEIR BODIES!
    // while this is currently possible, other statement types, like IfStatement
    // will also allow module instances... however, ifs inside generates SHOULD
    // allow this... so I think I'll leave it for now

    /*
     * for copying
     */
    private GenerateStatement() {
        super(HardwareNodeType.GenerateLoop);
    }

    @Override
    protected GenerateStatement copyPrivate() {
        return new GenerateStatement();
    }

    @Override
    public GenerateStatement copy() {
        return (GenerateStatement) super.copy();
    }

    // generate
    // for ... begin
    // end
    // endgenerate
    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("generate");
        builder.append(super.getAsString());
        builder.append("endgenerate\n");
        return builder.toString();
    }
}
