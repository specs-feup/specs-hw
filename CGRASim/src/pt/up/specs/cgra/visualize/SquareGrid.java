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

package pt.up.specs.cgra.visualize;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SquareGrid extends JFrame {

    private JPanel gridPanel;

    public SquareGrid() {
        // Set the window title
        setTitle("Square Grid");

        // Create a panel for the grid
        gridPanel = new JPanel(new GridLayout(4, 4));

        // Create and add colored squares to the grid panel
        for (int i = 0; i < 16; i++) {
            JPanel squarePanel = new JPanel();
            squarePanel.setBackground(getRandomColor());
            gridPanel.add(squarePanel);
        }

        // Add the grid panel to the content pane of the window
        getContentPane().add(gridPanel);

        // Set the size of the window and make it visible
        setSize(400, 400);
        setVisible(true);
    }

    private Color getRandomColor() {
        // Generate a random color
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
}
