/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.crispy.lib.riscv;

import static pt.up.fe.specs.crispy.lib.riscv.RISCVALUCodes.*;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;
import pt.up.fe.specs.crispy.lib.Mux2to1;

public class ALUI32 extends HardwareModule {

    public InputPort A = addInputPort("A", 32);
    public InputPort B = addInputPort("B", 32);
    public InputPort alu_op = addInputPort("alu_op", 4);
    public OutputPort out = addOutputPort("out", 32);
    public OutputPort sum = addOutputPort("sum", 32);

    public ALUI32() {
        super(ALUI32.class.getSimpleName());

        // plus or minus B
        var sum = addWire("sum", A.getWidth());
        instantiate(new Mux2to1(A.getWidth()),
                assign(Immediate.Zeroes(B.getWidth()).sub(B)), B, alu_op.idx(0), sum);

        // comparators
        var isAdd = alu_op.eq(ALU_ADD.getCode());
        var isSub = alu_op.eq(ALU_SUB.getCode());
        var addOrSub = isAdd.or(isSub);
        var isAnd = alu_op.eq(ALU_AND.getCode());

        // math
        var addorSubop = out.nonBlocking(sum);
        var andop = out.nonBlocking(A.and(B));

        // choose op
        alwayscomb()
                ._ifelse(addOrSub).then()._do(addorSubop).orElse()
                ._ifelse(isAnd).then()._do(andop);
    }
}
