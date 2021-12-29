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

import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.Register;
import pt.up.fe.specs.crispy.ast.statement.IfStatement;

public class RegisterBank extends HardwareModule {

    public InputPort clk, rst;
    public InputPort addr;
    public InputPort write;
    public InputPort din;
    public OutputPort dout;

    public RegisterBank(int numberRegister, int registerWidth) {
        super(RegisterBank.class.getSimpleName()
                + "_" + numberRegister + "_" + registerWidth + "bits");

        // ports
        clk = addClock();
        rst = addReset();
        addr = addInputPort("addr", (int) (Math.log(numberRegister) / Math.log(2)));
        write = addInputPort("write", 1);
        din = addInputPort("din", registerWidth);
        dout = addOutputPort("dout", registerWidth);

        // internal registers
        var regBank = new ArrayList<Register>();
        for (int i = 0; i < numberRegister; i++)
            regBank.add(addRegister("reg" + i, registerWidth));

        // logic (write block)
        var alwaysblock = alwaysposedge();
        for (int i = 0; i < numberRegister; i++) {
            var addrval = new Immediate(i, addr.getResultWidth());
            var reg = regBank.get(i);
            alwaysblock._ifelse(rst,
                    reg.nonBlocking(0))
                    .orElse(new IfStatement(write.and(addr.eq(addrval)), reg.nonBlocking(din)));

            // TODO: invoking _if inside the orElse not only adds the _if to the else clause, but to the body of the
            // ModuleBlock....

            // CRITICAL FIX
        }

        // logic (read block, just use a Mux!, plus a decoder since the mux is hot bit)
        var decoder = new DecoderNxM(numberRegister);
        var mux = new MuxNto1(numberRegister, numberRegister);

        var decoderoutput = addWire("decoOutput", registerWidth);
        // var deco1 = addInstance(decoder.instantiate("deco1", addr, decoderoutput));
        addInstance(decoder, "deco1", addr, decoderoutput);

        // instantiate(instanceName, connections)
        // TODO: this would allow the method to create an instance inside itself!!
        // how to prevent??

        // how to get output connection wire into and out of instances??
        // see my own implementation of
        // https://github.com/specs-feup/specs-chisel/blob/main/FunctionalUnits/main/pt/inesctec/modules/functionalUnit/InlineApply.scala
        // its basically a functional interface

        addInstance(mux.instantiate("mux1", regBank, decoderoutput, dout));

        // TODO: can I do it like chisel or spinal, where assigning a module to an InputPort
        // basically means that i connect the output of the module to that input port?

        // TODO: re-think the ModuleInstance class so that ports can be accessed by name after
        // instantiation...

        // TODO: maybe make it so that addInstance receives a HardwareModule? and the ModuleInstance class type
        // is created inside the receiving ModuleBlock only;
        // this way, this context (of the constructor / other java body code) can use the HardwareModule class to
        // access the ports??
        // im still not sure if i can make connections between modules using the public port names though...
        // not unless that attribution automatically creates a wire in the host HardwareBlock...

        // TODO: upon emission, the HardwareModule must also emit all files for all HardwareModules it has
        // instantiated within (i.e., I can start the emission only from the toplevel module)

    }
}
