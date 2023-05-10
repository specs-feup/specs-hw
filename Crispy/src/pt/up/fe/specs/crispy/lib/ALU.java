/**
 * Copyright 2023 SPeCS.
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

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

public class ALU extends HardwareModule {
    public InputPort A; // porque os imports nao funcionaram
    public InputPort B;
    public InputPort op;
    public OutputPort Y;
    public OutputPort D; // status

    public ALU(int bitwidth) {
        super(ALU.class.getSimpleName());

        A = addInputPort("A", bitwidth);
        B = addInputPort("B", bitwidth);
        op = addInputPort("op", 3);
        Y = addOutputPort("Y", bitwidth);
        D = addOutputPort("D", 4); // {zero, carry, ovf, neg}

        alwayscomb("alublock")._ifelse(op.eq(0b0000))
                .then()._do(Y.nonBlocking(A.add(B)))
                .orElse()._ifelse(op.eq(0b0001))
                .then()._do(Y.nonBlocking(A.sub(B))) // ._ifelse(op.eq(1)).then()._do(Y.nonBlocking(A.sub(B)))

                .orElse()._ifelse(op.eq(0b0010)) // codigo em binario?
                .then()._do(Y.nonBlocking(A.and(B)))

                .orElse()._ifelse(op.eq(0b0011))
                .then()._do(Y.nonBlocking(A.or(B)))

                .orElse()._ifelse(op.eq(0b0100))
                .then()._do(Y.nonBlocking(A.xor(B)));

        // alterar alwayscomb para a ALU
        /*
        alwayscomb("ALUBlock")._ifelse(op.add())
        then()._do(out.nonBlocking(i0))~
            
                // alwayscomb begin
                //    if(op == 1'b0) begin
                //      out = i0??
                //    end
                // end
        
        alwayscomb("ALUBlock")._ifelse(op.sub())
        then()._do(out.nonBlocking(i0))
        
        alwayscomb("ALUBlock")._ifelse(op.incre())
        then()._do(out.nonBlocking(i0))
        
        alwayscomb("ALUBlock")._ifelse(op.decre())
        then()._do(out.nonBlocking(i0))
        
        alwayscomb("ALUBlock")._ifelse(op.and())
        then()._do(out.nonBlocking(i0))
        
        alwayscomb("ALUBlock")._ifelse(op.or())
        then()._do(out.nonBlocking(i0))
        
        .orElse()._do(out.nonBlocking(i1));
        */

        // return ALU
    }
}

// ?? falta fazer alwayscomb
