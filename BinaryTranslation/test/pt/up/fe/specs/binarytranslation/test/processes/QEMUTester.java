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
 
package pt.up.fe.specs.binarytranslation.test.processes;

import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.QEMU;

public class QEMUTester {

    /**
     * Tests if QEMU launches then dies
     * 
     * @param elf
     */
    protected static void testQEMULaunch(ELFProvider elf) {

        try (var qemu = new QEMU(elf.toApplication())) {
            qemu.start();
            System.out.println("waiting...");
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
