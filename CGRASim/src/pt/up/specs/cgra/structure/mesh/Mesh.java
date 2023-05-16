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

import pt.up.specs.cgra.CGRAUtils;
// github.com/specs-feup/specs-hw
import pt.up.specs.cgra.structure.SpecsCGRA;
import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Helper class to hold the List<List<ProcessingElement>> mesh, reduces some verbosity
 * 
 * @author nuno
 *
 */
public class Mesh {

    private final SpecsCGRA myparent;
    private final int width, height;
    private final List<List<ProcessingElement>> mesh;// array 2d de PEs

    public Mesh(List<List<ProcessingElement>> mesh, SpecsCGRA myparent) {
        this.myparent = myparent;
        this.mesh = mesh;
        this.width = mesh.size();
        this.height = mesh.get(0).size();
        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.height; j++)
                this.mesh.get(i).get(j).setMesh(this);
    }

    private void debug(String str) {
        CGRAUtils.debug(Mesh.class.getSimpleName(), str);
    }

    /*
     * 
     */
    public SpecsCGRA getCGRA() {
        return this.myparent;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void boundCheck(int x, int y) {
        if (x > this.width || y > this.height || x < 0 || y < 0)
            throw new RuntimeException("Mesh: coordinates out of bounds for CGRA mesh");
    }

    public ProcessingElement getProcessingElement(int x, int y) {
        this.boundCheck(x, y);
        return this.mesh.get(x).get(y);
    }

    public ProcessingElement setProcessingElement(int x, int y, ProcessingElement pe) {
        this.boundCheck(x, y);
        if (this.mesh.get(x).set(y, pe) != null) {
            this.debug("PE set sucessfuly: " + pe + " (" + x + ", " + y + ")");

        } else {
            this.debug("PE set failed...");
        }
        return pe;
        // TODO: should return null when fail?
    }

    public boolean execute() {
        this.debug("Execute start...");
        for (var line : this.mesh)
            for (var pe : line)
                pe.execute();

        this.debug("Execute sucessful");
        return true;
    }

    public void reset() {
        for (var row : this.mesh)
            for (var pe : row)
                pe.reset();
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
        sbld.append(str.repeat(this.width));
        return sbld.toString();
    }
}
