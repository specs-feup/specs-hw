/**
 *  Copyright 2020 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.elfsimulator.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;
import org.specs.MicroBlaze.stream.*;

import java.io.File;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.binarytranslation.*;

public class TestSimulator {

    private File openFile() {

        // static
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/matmul.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/innerprod.txt");
        File fd = SpecsIo.resourceCopy("org/specs/elfsimulator/microblaze/cholesky.txt");
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/innerprod.txt");

        // dynamic
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky_trace.txt");
        System.out.print(fd.exists());
        fd.deleteOnExit();
        return fd;
    }
    
    @Test
    void loadMicroBlazeElfTest() {
       MicroBlazeElfStream stream = new MicroBlazeElfStream(openFile());
       var newinst = stream.nextInstruction();
       while (newinst != null) {
           System.out.print(newinst + "\n");
           newinst = stream.nextInstruction();
       }
    }
    
    @Test
    void loadRiscvElfTest() {
         
        //fail("Not yet implemented");
    }

}
