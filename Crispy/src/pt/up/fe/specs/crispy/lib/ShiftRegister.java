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

package pt.up.fe.specs.crispy.lib;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.Register;

public class ShiftRegister extends HardwareModule {

    public InputPort clk = addClock();
    public InputPort rst = addReset();
    public InputPort enable = addInputPort("en", 1);
    public InputPort in;
    public OutputPort out;

    private List<Register> shiftReg;

    public ShiftRegister(int depth, int bitwidth) {
        super(ShiftRegister.class.getSimpleName() + "d" + depth + "w" + bitwidth);

        in = addInputPort("in", bitwidth);
        out = addOutputPort("out", bitwidth);

        shiftReg = new ArrayList<Register>();
        for (int i = 0; i < depth; i++)
            shiftReg.add(addRegister("reg" + i, bitwidth));

        var thenBlock = alwaysposedge()._ifelse(rst).then();
        for (int i = 0; i < depth; i++)
            thenBlock._do(shiftReg.get(i).nonBlocking(0));

        var elseBlock = thenBlock.orElse()._if(enable).then();
        for (int i = 1; i < depth; i++)
            elseBlock._do(shiftReg.get(i).nonBlocking(shiftReg.get(i - 1)));

        _do(out.assign(shiftReg.get(depth - 1)));
    }

    public Register getShiftRegister(int idx) {
        return shiftReg.get(idx);
    }
}
