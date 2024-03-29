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

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.specshw.processes.TxtDump;

public class TxtDumpTest {

    /**
     * 
     */
    @Test
    public void test() {
        var txtfile = BinaryTranslationUtils.getFile("org/specs/BinaryTranslation/specs_cr_text.txt");
        try (var txtDumper = new TxtDump(txtfile)) {
            String line = null;
            while ((line = txtDumper.getLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
