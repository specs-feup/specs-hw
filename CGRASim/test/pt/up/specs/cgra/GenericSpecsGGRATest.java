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
import pt.up.specs.cgra.structure.pes.ALUElement;
import pt.up.specs.cgra.structure.pes.MultiplierElement;

public class GenericSpecsGGRATest {

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
    
    @Test
    public void testBasicCGRAPrototype() {
        var CGRAbld = new GenericSpecsCGRA.Builder(3, 3, 8);
        CGRAbld.withHomogeneousPE(new ALUElement());
        var cgra = CGRAbld.build();
        cgra.visualize();
        
        
        
    }
}
