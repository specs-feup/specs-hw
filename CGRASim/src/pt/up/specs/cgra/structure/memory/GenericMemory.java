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

package pt.up.specs.cgra.structure.memory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;

public class GenericMemory implements Memory {

	private int memsize;
	private HashMap<Integer, PEData> mem;

	public GenericMemory(int memsize) {
		this.memsize = memsize;
		this.mem = new HashMap<Integer, PEData>(memsize);

		//this.mem_new = Collections.synchronizedMap(mem);

		/*for (int i = 0; i < this.memsize; i++)
            this.mem_new.put(Integer.valueOf(i), new PEInteger(0));*/

	}

	@Override
	public PEData read(Integer addr) {

		// if not exist put zero and then read
		if(!this.mem.containsKey(addr)) {
			System.out.printf("Key does not exist at addr %d! returning 0... \n", addr.intValue());
			this.write(addr, new PEInteger(0)); 
			}

		return this.mem.get(addr);
	}

	@Override
	public boolean write(Integer addr, PEData data) {
		if (addr.intValue() > this.memsize) {
			System.out.printf("Error writing to memory - addr exceeds memsize... \n");
			return false; 
		}
		this.mem.put(addr, data);
		return true;
	}

	public int getSize() {
		return this.memsize;
	}
}
