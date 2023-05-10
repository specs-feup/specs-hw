/**
 * Copyright 2023 SPeCS.
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

package pt.up.specs.cgra;

import org.junit.Test;

import pt.up.specs.cgra.visualize.AWTExample1;
import pt.up.specs.cgra.visualize.SquareGrid;

public class TestVisualize {

    @Test
    public void test() {
        var awt = new AWTExample1(); // creating a frame.
        awt.setVisible();

        System.out.println("hang");
    }

    @Test
    public void test2() {
        var squareGrid = new SquareGrid();
        System.out.println("hang");
    }
}
