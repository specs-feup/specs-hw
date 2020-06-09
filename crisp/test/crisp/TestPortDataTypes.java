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

import java.util.*;

import org.junit.Test;

import crisp.accelerator.*;
import crisp.data.Data;
import crisp.datatype.*;

public class TestPortDataTypes {

    @Test
    public void test() {

        var dtype1 = new DataArray(PrimitiveTypes.logic, new int[] { 32, 4 });
        var d1 = new Data(dtype1, "data1");
        System.out.println(d1.declare());

        var dtype2 = new DataArray(PrimitiveTypes.integer, new int[] { 32, 4 });
        var d2 = new Data(dtype2, "data2");
        System.out.println(d2.declare());

        var t2 = new Typedef("data32", new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 }));
        var t3 = new DataArray(t2, new int[] { 1, 4 });
        var d3 = new Data(t3, "data3");
        System.out.println(d3.declare());
    }

    @Test
    public void testTypedef() {
        var t2 = new Typedef("data32", new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 }));
        System.out.println(t2.define());
    }

    @Test
    public void testStructWithArrays() {
        var list = new ArrayList<Data>();
        var dtype = new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 });
        list.add(new Data(dtype, "operands"));
        list.add(new Data(dtype, "results"));
        list.add(new Data(PrimitiveTypes.logic, "carrybit"));
        var struct1 = new Struct("fuData", list);
        System.out.println(struct1.define());

        var data1 = new Data(struct1, "st1");
        System.out.println(data1.declare());
    }

    @Test
    public void testArrayOfStruct() {
        var list = new ArrayList<Data>();
        var dtype = new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 });
        list.add(new Data(dtype, "operands"));
        list.add(new Data(dtype, "results"));
        list.add(new Data(PrimitiveTypes.logic, "carrybit"));
        var struct1 = new Struct("fuData", list);
        System.out.println(struct1.define());

        var data1 = new Data(new DataArray(struct1, new int[] { 1, 4 }), "arr1");
        System.out.println(data1.declare());
    }

    @Test
    public void testStrucInStruct() {
        var list = new ArrayList<Data>();
        list.add(new Data(PrimitiveTypes.logic, "abit"));
        var struct1 = new Struct("struct1_t", list);

        var list2 = new ArrayList<Data>();
        list2.add(new Data(struct1, "ts1"));
        list2.add(new Data(struct1, "ts2"));
        list2.add(new Data(PrimitiveTypes.logic, "bbit"));
        var struct2 = new Struct("struct2_t", list2);

        System.out.println(struct1.define());
        System.out.println(struct2.define());
    }

    /*
    @Test
    public void testAdder32() {
    
        var add1 = Adder.newInstance();
        add1.getDefinition();
    
        var add2 = Adder.newInstance(new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 }));
        add2.getDefinition();
    
        var t1 = new Typedef("data32", new DataArray(PrimitiveTypes.logic, new int[] { 32, 1 }));
        var add3 = Adder.newInstance(t1);
        add3.getDefinition();
    }
    */
    @Test
    public void testAcceleratorDatTypes() {
        var testindat = AcceleratorDataTypes.FUInputData();
        System.out.println(testindat.define());

        var testoutdat = AcceleratorDataTypes.FUOutputData();
        System.out.println(testoutdat.define());
    }

}
