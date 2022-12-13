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

import pt.up.specs.cgra.dataypes.PEControl.PEMemoryAccess;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.dataypes.PEControl.PEDirection;
import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.NullProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Helper class to hold the List<List<ProcessingElement>> mesh, reduces some verbosity
 * 
 * @author nuno
 *
 */
public class Mesh {

	private final SpecsCGRA myparent;
	private final int x, y;
	private final List<List<ProcessingElement>> mesh;

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
		return this.mesh.get(x).get(y);
	}

	public boolean setConnections()
	{
		for (var line : this.mesh)
			for (var pe : line)
			{
				if(!(pe instanceof NullProcessingElement))
				{
					if (pe.getControl().getMemAccess() != PEMemoryAccess.INITIAL)
					{
						int x_orig = pe.getX();
						int y_orig = pe.getY();
						int x = 0;
						int y = 0;

						PEDirection inputone = pe.getControl().getInputone();
						switch (inputone)
						{
						case E:
							x = x_orig + 1;
							y = y_orig;
							break;
						case NE:
							x = x_orig + 1;
							y = y_orig - 1;
							break;
						case N:
							x = x_orig;
							y = y_orig - 1;
							break;
						case NW:
							x = x_orig - 1;
							y = y_orig - 1;
							break;
						case W:
							x = x_orig - 1;
							y = y_orig;
							break;
						case SW:
							x = x_orig - 1;
							y = y_orig + 1;
							break;
						case S:
							x = x_orig;
							y = y_orig + 1;
							break;
						case SE:
							x = x_orig + 1;
							y = y_orig + 1;
							break;

						case ZERO:
						default:
							x = -1;
							y = -1;
							break;

						}

						if (x >= 0 || y >= 0) 
						{
							System.out.printf("%d, %d \n", x, y);
							this.myparent.getInterconnect().setConnection(this.getProcessingElement(x, y).getPorts().get(2), pe.getPorts().get(0));
							System.out.printf("Connection set between output of %d, %d and input 1 of %d, %d \n", x, y, pe.getX(), pe.getY());
						}
						else System.out.printf("Negative coords, connection unsuccessful for input 1 in %d, %d \n", x_orig, y_orig);

						PEDirection inputtwo = pe.getControl().getInputtwo();
						x_orig = pe.getX();
						y_orig = pe.getY();
						x = 0;
						y = 0;

						switch (inputtwo)
						{
						case E:
							x = x_orig + 1;
							y = y_orig;
							break;
						case NE:
							x = x_orig + 1;
							y = y_orig - 1;
							break;
						case N:
							x = x_orig;
							y = y_orig - 1;
							break;
						case NW:
							x = x_orig - 1;
							y = y_orig - 1;
							break;
						case W:
							x = x_orig - 1;
							y = y_orig;
							break;
						case SW:
							x = x_orig - 1;
							y = y_orig + 1;
							break;
						case S:
							x = x_orig;
							y = y_orig + 1;
							break;
						case SE:
							x = x_orig + 1;
							y = y_orig + 1;
							break;

						case ZERO:
						default:
							x = -1;
							y = -1;
							break;

						}

						if (x >= 0 || y >= 0) 
						{
							this.myparent.getInterconnect().setConnection(this.getProcessingElement(x, y).getPorts().get(2), pe.getPorts().get(0));
							System.out.printf("Connection set between output of %d, %d and input 2 of %d, %d \n", x, y, pe.getX(), pe.getY());

						}
						else System.out.printf("Negative coords, connection unsuccessful for input 2 in %d, %d \n", x_orig, y_orig);
					}
				}
			}
		return true;
	}

	public void fetch(int pos1, int pos2) { //fetch data from generic ram to initial PEs
		int n = 0;
		for (var line : this.mesh)
			for (var pe : line)
				if (pe.getControl().getMemAccess() == PEMemoryAccess.INITIAL)
				{
					if (pe.getControl().getInputone() != PEDirection.ZERO)
					{
						PEData x = pe.getPorts().get(0).setPayload(myparent.getLiveins().read(pos1 + n));
						System.out.printf("payload set: %d on first input port of pe: %d, %d\n", x.getValue(), pe.getX(), pe.getY());
					}

					else pe.getPorts().get(0).setPayload(new PEInteger(0));
					

					if (pe.getControl().getInputtwo() != PEDirection.ZERO)
					{
						PEData y = pe.getPorts().get(1).setPayload(myparent.getLiveins().read(pos2 + n));
						System.out.printf("payload set: %d on second input port of pe: %d, %d\n", y.getValue(), pe.getX(), pe.getY());

					}

					else pe.getPorts().get(1).setPayload(new PEInteger(0));

					n++;
				}
	}

	public void store(int pos) {//store data from final PEs to generic ram
		int n = 0;
		for (var line : this.mesh)
			for (var pe : line)
				if (pe.getControl().getMemAccess() == PEMemoryAccess.FINAL)
				{
					myparent.getLiveouts().write((pos + n), pe.getRegisterFile().get(0));
					n++;
				}
	}

	public void execute() {
		for (var line : this.mesh)
			for (var pe : line)
				pe.execute();
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
}
