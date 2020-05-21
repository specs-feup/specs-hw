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

package crisp.accelerator;

import java.util.*;

import crisp.data.Data;
import crisp.datatype.*;

/**
 * This class holds static instantiators for types of SystemVerilog data I expect to use when generating code for the
 * accelerators
 * 
 * @author Nuno
 *
 */
public class AcceleratorDataTypes {

    static public DataType Data32() {
        return new Typedef("data32", new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 }));
    }

    static public DataType FUInputData() {
        var dtype = new DataArray(PrimitiveTypes.logic, new int[] { 32, 3 });
        var map = new ArrayList<Data>();
        map.add(new Data(dtype, "operands"));
        map.add(new Data(PrimitiveTypes.logic, "carrybit"));
        var struct1 = new Struct("fuInputData_t", map);
        return new Typedef("fuInputData", struct1);
    }

    static public DataType FUOutputData() {
        var dtype = new DataArray(AcceleratorDataTypes.Data32(), new int[] { 1, 2 });
        var map = new ArrayList<Data>();
        map.add(new Data(dtype, "results"));
        map.add(new Data(PrimitiveTypes.logic, "carrybit"));
        var struct1 = new Struct("fuOutputData_t", map);
        return new Typedef("fuOutputData", struct1);
    }
}
