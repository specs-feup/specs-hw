/**
 * Copyright 2020 SPeCS.
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

package crisp;

import org.junit.Test;

import crisp.data.Data;
import crisp.datatype.*;

public class TestPortDataTypes {

    @Test
    public void test() {

        var t1 = new Data(PrimitiveTypes.logic, "testdat", 32);
        System.out.println(t1.declare());

        var t2 = new Data(PrimitiveTypes.logic, "testdat", 32, 4);
        System.out.println(t2.declare());
    }

    @Test
    public void testdata32() {

        // var t1 = new Typedef("Data32", new Data("testdat", 32));
        // System.out.println(t1.declare());
    }

}
