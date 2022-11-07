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
 
package pt.up.specs.cgra.structure.interconnect;

import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class NearestNeighbour extends AInterconnect {

    public NearestNeighbour(SpecsCGRA myparent) {
        super(myparent);
    }

    @Override
    public boolean connectionValid(ProcessingElementPort from, ProcessingElementPort to) {
        var fromPE = from.getPE();
        var toPE = from.getPE();
        var distX = Math.abs(fromPE.getX() - toPE.getX());
        var distY = Math.abs(fromPE.getY() - toPE.getY());

        

        if (distX == 1 && distY == 0)
            return true;

        else if (distX == 0 && distY == 1)
            return true;
        
        else if (distX == 1 && distY == 1)
            return true;

        // anything else
     // diagonal (of any length)
        //if (distX > 0 && distY > 0)
        //    return false;
        else
            return false;
    }
}
