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

package pt.up.fe.specs.crispy.lib;

import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.declaration.ArrayDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

public class RAMemory extends HardwareModule {

    public final InputPort clk = addClock(), rst = addReset();
    public final InputPort ren = addInputPort("ren", 1), wen = addInputPort("wen", 1);
    public final InputPort addr;
    public final InputPort dataIn;
    public final OutputPort dataOut;

    public RAMemory(int bytes, int byteWidth) {
        super("RAMemory_" + bytes + "_w" + byteWidth);

        // ports
        addr = addInputPort("addr", (int) (bytes));
        dataIn = addInputPort("dataIn", byteWidth);
        dataOut = addOutputPort("dataOut", byteWidth);

        // ram
        var ram = addArray(ArrayDeclaration.ofRegisters("ram", byteWidth * 8, bytes));

        // read cycle
        alwaysposedge()._if(wen).then()._do(ram.nonBlocking(dataIn));

        // write cycle
        var addr00 = addWire("addr00", addr.getWidth());
        assign(addr00.idx(addr00.getWidth(), 2), addr.idx(addr.getWidth(), 2));
        assign(addr00.idx(1, 0), 0);
        alwaysposedge()._if(ren).then()._do(dataOut.nonBlocking(ram.idx(addr00)));
    }
}
