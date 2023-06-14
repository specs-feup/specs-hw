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

package pt.up.specs.cgra.structure.mesh;

import java.util.List;
import java.util.stream.Collectors;

import pt.up.specs.cgra.dataypes.PEInteger;
// github.com/specs-feup/specs-hw
import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Helper class to hold the List<List<ProcessingElement>> mesh, reduces some verbosity (GONCALO):array de PEs em
 * list<list<PE>>, e metodo execute e visualize
 * 
 * @author nuno
 *
 */
public class Mesh {

	private final SpecsCGRA myparent;
	private final int x, y;
	private List<List<ProcessingElement>> mesh;// array 2d de PEs


	public Mesh(List<List<ProcessingElement>> mesh, SpecsCGRA myparent) {
		this.myparent = myparent;
		this.mesh = mesh;
		this.x = mesh.size();
		this.y = mesh.get(0).size();
		for (int i = 0; i < this.x; i++)
			for (int j = 0; j < this.y; j++) {
				var pe = this.mesh.get(i).get(j);
				pe.setX(i);
				pe.setY(j);
				pe.setMesh(this);
			}
	}

	/*
	 * 
	 */
	public SpecsCGRA getCGRA() {
		return this.myparent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public ProcessingElement getProcessingElement(int x, int y) {
		if(x > this.x || y > this.y || x < 0 || y < 0)
			throw new RuntimeException("Mesh: coordinates out of bounds for CGRA mesh");

		return this.mesh.get(x).get(y);
	}

	public ProcessingElement setProcessingElement(int x, int y, ProcessingElement pe) {
		if(x > this.x || y > this.y || x < 0 || y < 0)
			throw new RuntimeException("Mesh: coordinates out of bounds for CGRA mesh");

		if (this.mesh.get(x).set(y, pe) != null) {
			pe.setX(x);
			pe.setY(y);
			pe.setMesh(this);
			System.out.printf("PE %d %d set succesfully as %s \n", x, y, pe.toString());
			return pe;

		}

		System.out.printf("PE %d %d set unsuccesfully! \n", x, y);
		return pe;

	}


	public boolean execute() {
		
		System.out.println("\n========== execution step ==========");

		for (var line : this.mesh) {
			for (var pe : line) { 
				pe.execute();
			}
		}

		System.out.println("execution step done succesfully");

		return true;

	}

	public String visualize() {
		var sbld = new StringBuilder();
		var str = "------------------";
		for (var line : this.mesh) {
			sbld.append(str.repeat(line.size()) + "\n");
			for (var pe : line) {
				sbld.append("|  " + pe.toString() + "  |");
			}
			sbld.append("\n");
		}
		sbld.append(str.repeat(this.x));
		return sbld.toString();
	}

	public void reset() {
		for (List<ProcessingElement> row : this.mesh) 
		{
			for (ProcessingElement PE : row)
			{
				PE.setControl(0);
				PE.clearRegisterFile();
			}
		}
	}

	public boolean zero_ports() {

		for (List<ProcessingElement> row : this.mesh) 
		{
			for (ProcessingElement PE : row)
			{
				PE.getPorts().get(0).setPayload(new PEInteger(0));
				PE.getPorts().get(1).setPayload(new PEInteger(0));
				PE.getPorts().get(2).setPayload(new PEInteger(0));


			}
		}

		System.out.println("Ports set to 0s");
		return true;

	}
	
	public List<ProcessingElement> get1Dmesh()
	{
		List<ProcessingElement> flatList = mesh.stream().flatMap(s -> s.stream()).collect(Collectors.toList());
		
		return flatList;
		
	}

	public List<List<ProcessingElement>> get2Dmesh() {
		return mesh;
	}

	public void set2Dmesh(List<List<ProcessingElement>> mesh) {
		this.mesh = mesh;
	}
}
