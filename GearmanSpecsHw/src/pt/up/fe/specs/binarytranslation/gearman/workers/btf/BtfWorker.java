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

package pt.up.fe.specs.binarytranslation.gearman.workers.btf;

import java.util.HashMap;
import java.util.Map;

import org.gearman.GearmanFunctionCallback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pt.up.fe.specs.binarytranslation.gearman.SpecsHwWorker;

public class BtfWorker extends SpecsHwWorker {

    @Override
    public byte[] workInternal(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        String dataString = new String(data);
        System.out.println("RECEIVED DATA:\n" + dataString);
        
        Gson gson = new GsonBuilder().create();

        var input = gson.fromJson(dataString, Map.class);
        System.out.println("INPUT OBJECT:\n" + input);
        System.out.println("OLA: " + input.get("ola").getClass());
        
 
        var outputMap = new HashMap<>();
        outputMap.put("aString", "HEY");
        outputMap.put("anInt", 2);        
//        return gson.toJson(new BtfOutput("gello", true, 10)).getBytes();        
        return gson.toJson(outputMap).getBytes();        
    }

    @Override
    protected byte[] getErrorOutput(String message) {
        var gson = new GsonBuilder().create();
        return gson.toJson(new BtfOutput("error: " + message, false, 10)).getBytes();   
    }

}
