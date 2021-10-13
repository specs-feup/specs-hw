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
 
package org.specs.MicroBlaze.test;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTester {

    @Test
    public void testInstructionToJson() {
        Gson gson = new GsonBuilder().create();

        var addi = MicroBlazeInstruction.newInstance("248", "20c065e8");

        var bytes = gson.toJson(addi).getBytes();

        BufferedOutputStream bw = new BufferedOutputStream(System.out);
        try {
            bw.write(bytes);
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
