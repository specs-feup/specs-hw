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

import pt.up.specs.cgra.controlDeprecated.PEControlALU;
import pt.up.specs.cgra.controlDeprecated.PEControl.PEDirection;
import pt.up.specs.cgra.controlDeprecated.PEControl.PEMemoryAccess;
import pt.up.specs.cgra.controlDeprecated.PEControlALU.ALU_OP;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.ALUElement;
import pt.up.specs.cgra.structure.pes.MultiplierElement;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

public class GenericSpecsCGRATest {
/*
    @Test
    public void testInstantantiateAndView() {
        var CGRAbld = new GenericSpecsCGRA.Builder(2, 2, 8);
        CGRAbld.withHomogeneousPE(new ALUElement());
        CGRAbld.withProcessingElement(new MultiplierElement(), 0, 1);
        var cgra = CGRAbld.build();
        cgra.visualize();
    }

    @Test
    public void testAdderPE() {
        var adder1 = new ALUElement();

        adder1.getPorts().get(0).setPayload(new PEInteger(10));
        adder1.getPorts().get(1).setPayload(new PEInteger(12));
        var r = adder1.execute();
        System.out.println(r.getValue());
    }
    */
    @Test
    public void testBasicCGRAPrototype() {
        var CGRAbld = new GenericSpecsCGRA.Builder(3, 3, 8);
        CGRAbld.withHomogeneousPE(new ALUElement());
        var cgra = CGRAbld.build();
        cgra.visualize();
        
        ProcessingElement pe1 = cgra.getMesh().getProcessingElement(0, 0);
        pe1.setControl(new PEControlALU(pe1, PEMemoryAccess.INITIAL, ALU_OP.PASSL, PEDirection.N, PEDirection.ZERO));
        pe1.printStatus();
        
        pe1 = cgra.getMesh().getProcessingElement(0, 1);
        pe1.setControl(new PEControlALU(pe1, PEMemoryAccess.INITIAL, ALU_OP.PASSL, PEDirection.N, PEDirection.ZERO));
        pe1.printStatus();

        pe1 = cgra.getMesh().getProcessingElement(1, 0);
        pe1.setControl(new PEControlALU(pe1, PEMemoryAccess.NONE, ALU_OP.ADD, PEDirection.N, PEDirection.NE));
        pe1.printStatus();

        pe1 = cgra.getMesh().getProcessingElement(1, 1);
        pe1.setControl(new PEControlALU(pe1, PEMemoryAccess.FINAL, ALU_OP.PASSL, PEDirection.W, PEDirection.E));
        pe1.printStatus();
        
        cgra.getLiveins().write(0, new PEInteger(2));

        cgra.getLiveins().write(1, new PEInteger(3));

        
        for (int i = 2; i<8; i++)
        {
            cgra.getLiveins().write(i, new PEInteger(0));
        }

        cgra.fetch(0, 5);

        cgra.setConnections();
        
        cgra.execute();

        
        //config
        //fetch
        //execute (as many times as wanted)
        //store
        //end
        
    }
}
