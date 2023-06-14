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

package pt.up.specs.cgra.structure.context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort;

public class Context {

	/*
	 * contexts are final, and are applied to the interconnect by iterating
	 * over the entire map of source ports to destination ports
	 */
	private final Map<ProcessingElementPort, List<ProcessingElementPort>> connections;
	private String name;

	public Context(Map<ProcessingElementPort, List<ProcessingElementPort>> connections) {
		this.connections = connections;
	}

	/*public Context(int contextID, GenericDFG dfg)
    {
    	this.contextID = contextID;
    	this.connections = makeConnections(dfg);
    }*/

	/* private static Map<ProcessingElementPort, List<ProcessingElementPort>> makeConnections(GenericDFG dfg) 
    {

    	return null;
    	//return tmp_con;
    } */

	public Map<ProcessingElementPort, List<ProcessingElementPort>> getConnections() {
		return connections;
	}

	public Optional<List<ProcessingElementPort>> getDestinationsOf(ProcessingElementPort source) {
		return Optional.of(this.connections.get(source));
	}
	
	public boolean saveToFile(String name) {
		try (FileOutputStream fos = new FileOutputStream(String.format(name + ".ctxt"));
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(connections);
			System.out.println("Map saved to file successfully");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Map<ProcessingElementPort, List<ProcessingElementPort>> readFile(String x) {
		try (FileInputStream fis = new FileInputStream(x);
	             ObjectInputStream ois = new ObjectInputStream(fis)) {
			Map<ProcessingElementPort, List<ProcessingElementPort>> map = (Map<ProcessingElementPort, List<ProcessingElementPort>>) ois.readObject();
	            System.out.println("Map loaded from file successfully");
	           return map;
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	            return null;
	        }
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
