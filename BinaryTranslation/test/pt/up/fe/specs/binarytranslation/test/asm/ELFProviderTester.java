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
 
package pt.up.fe.specs.binarytranslation.test.asm;

import java.util.List;

import pt.up.fe.specs.binarytranslation.elf.ELFProvider;

public class ELFProviderTester {

    /*
     * 
     */
    protected static void testStartStopAddrReading(List<Class<? extends ELFProvider>> listOfELFProviders) {

        for (var elfset : listOfELFProviders) {
            var elfs = elfset.getEnumConstants();
            System.out.println(elfset.getSimpleName());
            for (var elf : elfs) {
                var app = elf.toApplication();
                System.out.print(elf.getELFName() + ": ");
                System.out.print("0x" + Long.toHexString(app.getKernelStart()) + " - ");
                System.out.println("0x" + Long.toHexString(app.getKernelStop()));
            }
        }
    }
}
