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
import pt.up.fe.specs.crispy.ast.expression.operator.Port;
import pt.up.fe.specs.crispy.ast.expression.operator.Register;

public class RegisterBank extends HardwareModule {

    public Port clk, rst;
    public Port addr;
    public Port write;
    public Port din;
    public Port dout;

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
            alwaysblock._ifelse(rst)
                    .then()._do(regBank.get(i).nonBlocking(0))
                    .orElse()
                    ._if(write.and(addr.eq(i)))
                    .then()._do(regBank.get(i).nonBlocking(din));
        }

        // logic (read block, just use a Mux!, plus a decoder since the mux is hot bit)
        var decoderoutput = addWire("decoOutput", registerWidth);
        addInstance(new DecoderNxM(addr.getResultWidth()), addr, decoderoutput);

        var muxoutput = addWire("muxOutput", registerWidth);
        addInstance(new MuxNto1(numberRegister, registerWidth), regBank, decoderoutput, muxoutput);

        // TODO:
        // assign(dout, mux.out);
        // this kind of assignment will require that the "assign" function
        // fetch the parent of "mux.out", fetch its instantiation,
        // and add the connection in the port list

        assign(dout, muxoutput);

        // TODO: method like mux.in.connect(addr);
        // and this method will have to seek the parent ModuleBlock of the HardwareModule
        // find the respective ModuleInstance node, and set the connection in the port list!

        /*var decoder = new DecoderNxM(numberRegister);
        var mux = new MuxNto1(numberRegister, numberRegister);
        
        var decoderoutput = addWire("decoOutput", registerWidth);
        addInstance(decoder.instantiate("deco1", addr, decoderoutput));
        
        var connections = new ArrayList<HardwareOperator>();
        var muxoutput = addWire("muxOutput", registerWidth);
        connections.addAll(regBank);
        connections.add(decoderoutput);
        connections.add(muxoutput);
        addInstance(mux.instantiate("mux1", connections));
        assign(dout, muxoutput);*/

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
