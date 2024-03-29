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

package pt.up.specs.cgra;

import org.junit.Test;

import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;
import pt.up.specs.cgra.structure.pes.binary.AdderElement;
import pt.up.specs.cgra.structure.pes.binary.MultiplierElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

public class GenericSpecsCGRATest {

    @Test
    public void testInstantantiateAndView() {
        var CGRAbld = new GenericSpecsCGRA.Builder(2, 2);
        CGRAbld.withHomogeneousPE(new AdderElement());
        CGRAbld.withProcessingElement(new MultiplierElement(), 0, 1);
        var cgra = CGRAbld.build();
        cgra.visualize();
    }

    @Test
    public void testAdderPE() {
        var adder1 = new AdderElement();
        adder1.getPorts().get(0).setPayload(new PEInteger(10));
        adder1.getPorts().get(1).setPayload(new PEInteger(12));
        var r = adder1.execute();
        System.out.println(r.getValue());
    }

    @Test
    public void testBasicCGRAPrototype() {

        /*
         * Build CGRA
         */
        var CGRAbld = new GenericSpecsCGRA.Builder(4, 4);
        CGRAbld.withMemory(3);
        CGRAbld.withHomogeneousPE(new ALUElement());
        CGRAbld.withProcessingElement(new LSElement(), 0, 0);
        CGRAbld.withProcessingElement(new LSElement(8), 0, 1);
        CGRAbld.withProcessingElement(new LSElement(16), 0, 2);
        CGRAbld.withProcessingElement(new LSElement(24), 0, 3);
        var cgra = CGRAbld.build();
        cgra.visualize();

        /*
         * Reset
         */
        cgra.reset();
        for (int i = 0; i < cgra.getLiveinsSize(); i++)
            cgra.writeMemory(new PEInteger(i), new PEInteger(i * i + 1));

        /*      0    1    2    3
         * 
         * 0    LS   LS   LS   LS
         * 1    ADD  ---  ADD  --
         * 2    ---  MUL   --   --
         * 3
         * 
         */

        /*
         * Start feeding instructions
         */
        cgra.execute("set 0 0 1");// LOAD
        cgra.execute("set 0 1 1");// LOAD
        cgra.execute("set 0 2 1");// LOAD
        cgra.execute("set 0 3 1");// LOAD
        cgra.execute("set 1 0 1");// ADD
        cgra.execute("set 1 2 1");// ADD
        cgra.execute("set 2 1 3");// MUL
        cgra.execute("set_io 1 0 0 0 0 1");// ADD 1 0 liga in1 e 2 de pe_src a out de pe1 e pe2
        cgra.execute("set_io 1 2 0 2 0 3");// add 1 2
        cgra.execute("set_io 2 1 1 0 1 2");// mul 2 1

        // defining base address value of 0 for LSU; They already have an offset each of 0, 8, 16, 24
        for (int j = 0; j < 4; j++) {
            cgra.getPE(0, j).getPort(0).setPayload(new PEInteger(0));
            cgra.getPE(0, j).getPort(1).setPayload(new PEInteger(0));
        }

        System.out.println("Addresses set for LSUnits");

        cgra.visualize();

        System.out.println("Initiating first execution steps");

        cgra.step();

        for (int j = 0; j < 4; j++)
            cgra.getPE(0, j).getPort(0).setPayload(new PEInteger(1));

        cgra.step();

        // config
        // fetch
        // execute (as many times as wanted)
        // store
        // end
    }
}
