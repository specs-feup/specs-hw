package pt.up.specs.cgra;

import javax.swing.JFrame;
import javax.swing.JMenu;

import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.Interconnect_Types;
import pt.up.specs.cgra.structure.Mesh_Types;
import pt.up.specs.cgra.structure.PE_Types;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;
import pt.up.specs.cgra.structure.pes.binary.MultiplierElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.Box;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class GUI_main {

	JFrame frmCgrasim;
	List<ArrayList<JButton>> PEbtnlist;
	List<JTextField> seplist;
	List<Component> rigidseplist;
	List<ArrayList<GridBagConstraints>> gbclist;
	List<ArrayList<Object>> matrixlist;

	private JTextArea txtbox_peinfo;
	protected GenericSpecsCGRA cgra;
	int count_tmp = 0;
	//private static enum type_of_build;



	public GUI_main(int x_cgra, int y_cgra, int memsize, Interconnect_Types IC_T, PE_Types PE_T, Mesh_Types MESH_T, boolean lsu_select) {

		var CGRAbld = new GenericSpecsCGRA.Builder(x_cgra, y_cgra);
		CGRAbld.withMemory(memsize);

		/* switch (type_of_build)
		 * case xxx...
		 * case yyy...
		 */

		switch (MESH_T) {
		case HOMOGENEOUS:
			switch (PE_T) {
			case ALU:
				CGRAbld.withHomogeneousPE(new ALUElement());

				break;
			case LSU:
				CGRAbld.withHomogeneousPE(new LSElement());

				break;
			case MUL:
				CGRAbld.withHomogeneousPE(new MultiplierElement());


				break;

			default:
				CGRAbld.withHomogeneousPE(new ALUElement());


				break;
			}
		case EMPTY:
			break;
		default:
			break;
		}

		switch (IC_T) {
		case NEAREST_NEIGHBOUR:
			CGRAbld.withNearestNeighbourInterconnect();


			break;
		case NEAREST_NEIGHBOUR_DIAGONAL:
			CGRAbld.withNearestNeighbourDiagonalInterconnect();


			break;
		case TOROIDAL:
			CGRAbld.withToroidalNNInterconnect();

			break;

		default:
			CGRAbld.withNearestNeighbourInterconnect();
			break;

		}

		if (lsu_select) CGRAbld.firstRowLSU();

		cgra = CGRAbld.build();

		for (int i = 0; i < cgra.getLocalmemSize(); i++) cgra.writeMemory((Integer) (i), new PEInteger(i*i + 1));

		cgra.reset();

		initialize(x_cgra, y_cgra, IC_T);
	}

	int i = 0;
	int j = 0;

	private void initialize(int x_cgra, int y_cgra, Interconnect_Types IC_T) {



		frmCgrasim = new JFrame();
		frmCgrasim.setTitle("CGRA-SIM");
		frmCgrasim.setBounds(100, 100, 683, 380);
		frmCgrasim.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCgrasim.getContentPane().setLayout(new BorderLayout(0, 0));

		PEbtnlist = new ArrayList<ArrayList<JButton>>();
		seplist = new ArrayList<JTextField>();
		rigidseplist = new ArrayList<Component>();
		gbclist = new ArrayList<ArrayList<GridBagConstraints>>();
		matrixlist = new ArrayList<ArrayList<Object>>();

		JPanel panel = new JPanel();
		frmCgrasim.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[2*x_cgra + 2];
		gbl_panel.rowHeights = new int[2*y_cgra + 2];
		gbl_panel.columnWeights = new double[2*x_cgra + 2];
		gbl_panel.rowWeights = new double[2*y_cgra + 2];
		panel.setLayout(gbl_panel);


		for (int i = 0; i < 2*x_cgra + 2; i++) gbl_panel.columnWidths[i] = 0;
		for (int i = 0; i < 2*y_cgra + 2; i++) gbl_panel.rowHeights[i] = 0;
		gbl_panel.columnWeights[0] = 0.0;
		gbl_panel.columnWeights[2*x_cgra] = 0.0;
		gbl_panel.columnWeights[2*x_cgra + 1] = Double.MIN_VALUE;
		gbl_panel.rowWeights[0] = 0.0;
		gbl_panel.rowWeights[2*y_cgra] = 0.0;
		gbl_panel.rowWeights[2*y_cgra + 1] = Double.MIN_VALUE;
		for (int i = 1; i < 2*x_cgra; i++) gbl_panel.columnWeights[i] = 1.0;
		for (int i = 1; i < 2*y_cgra; i++) gbl_panel.rowWeights[i] = 1.0;


		GridBagConstraints gbtmp = null;
		Component cmptmp = null;

		for (i = 0; i < 2*x_cgra + 1; i++) {
			matrixlist.add(new ArrayList<Object>());
			gbclist.add(new ArrayList<GridBagConstraints>());


			for (j = 0; j < 2*y_cgra + 1; j++) {
				if (j % 2 == 1) PEbtnlist.add(new ArrayList<JButton>());


				//CREATE BUTTONS

				if (i % 2 == 1 && j % 2 == 1) {

					final int x = (i+1)/2 - 1; // Store the X position of the button
					final int y = (j+1)/2 - 1; // Store the Y position of the button

					System.out.printf("button at %d, %d \n", i, j);

					cmptmp = new JButton("Empty");
					((JButton) cmptmp).setText(cgra.getMesh().getProcessingElement(x, y).toString());

					PEbtnlist.get(x).add((JButton) cmptmp);


					((JButton) cmptmp).addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
							getTxtbox_peinfo().setText(cgra.getMesh().getProcessingElement(x, y).printStatus());
						}
					});

				}

				//CREATE SEPARATORS
				else
				{
					switch(IC_T) {
					case NEAREST_NEIGHBOUR:
						if ((i == 0 || i == 2*x_cgra || j == 0 || j == 2*y_cgra) || (i % 2 == 0 && j % 2 == 0))
						{
							cmptmp = Box.createRigidArea(new Dimension(25, 25));
							rigidseplist.add(cmptmp);
						}
						else
						{
							cmptmp = new JTextField();
							((JTextField) cmptmp).setHorizontalAlignment(SwingConstants.CENTER);
							((JTextField) cmptmp).setEditable(false);
							((JTextField) cmptmp).setFont(new Font("Tahoma", Font.BOLD, 16));


							seplist.add((JTextField) cmptmp);

						}

						break;

					case NEAREST_NEIGHBOUR_DIAGONAL:
						if ((i == 0 || i == 2*x_cgra || j == 0 || j == 2*y_cgra))
						{
							cmptmp = Box.createRigidArea(new Dimension(25, 25));
							rigidseplist.add(cmptmp);
						}
						else
						{
							cmptmp = new JTextField();
							((JTextField) cmptmp).setHorizontalAlignment(SwingConstants.CENTER);
							((JTextField) cmptmp).setEditable(false);

							seplist.add((JTextField) cmptmp);
						}
						break;

					case TOROIDAL:
						cmptmp = new JTextField();
						((JTextField) cmptmp).setHorizontalAlignment(SwingConstants.CENTER);
						((JTextField) cmptmp).setEditable(false);

						seplist.add((JTextField) cmptmp);

						break;
					}

					System.out.printf("separator at %d, %d \n", i, j);
				}

				gbtmp = new GridBagConstraints();

				gbtmp.insets = new Insets(0, 0, 5, 5);
				gbtmp.fill = GridBagConstraints.BOTH;

				gbtmp.gridx = i;
				gbtmp.gridy = j;

				panel.add(cmptmp, gbtmp);

				matrixlist.get(i).add(cmptmp);
				gbclist.get(i).add(gbtmp);
			}
		}

		JPanel panel_1 = new JPanel();
		frmCgrasim.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));

		txtbox_peinfo = new JTextArea();
		txtbox_peinfo.setLineWrap(true);
		txtbox_peinfo.setText("Info window...");
		txtbox_peinfo.setEditable(false);
		txtbox_peinfo.setColumns(10);

		JScrollPane scrollPane = new JScrollPane(txtbox_peinfo);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_1.add(scrollPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);

		JButton btn_step = new JButton("Step");
		btn_step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cgra.step();
			}
		});
		panel_2.add(btn_step);

		JButton btn_runstop = new JButton("Run/Stop");
		panel_2.add(btn_runstop);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.NORTH);

		JPanel panel_4 = new JPanel();
		frmCgrasim.getContentPane().add(panel_4, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{14, 0, 0, 0, 0};
		gbl_panel_4.rowHeights = new int[]{40, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);

		JTextArea txtrSendInstructions = new JTextArea();
		txtrSendInstructions.setText("Send instructions...");

		//EVENT LISTENER FOR CLEARING TEXT ON CLICK
		txtrSendInstructions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrSendInstructions.setText("");

			}
		});

		//EVENT LISTENER FOR ENTER KEYPRESS
		txtrSendInstructions.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					txtrSendInstructions.setText(cgra.getInstdec().InstructionParser(txtrSendInstructions.getText()));
					refreshConnects();
				}
			}
		});

		JButton btnNewButton = new JButton("Open File...");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		panel_4.add(btnNewButton, gbc_btnNewButton);
		GridBagConstraints gbc_txtrSendInstructions = new GridBagConstraints();
		gbc_txtrSendInstructions.fill = GridBagConstraints.BOTH;
		gbc_txtrSendInstructions.gridx = 3;
		gbc_txtrSendInstructions.gridy = 0;
		panel_4.add(txtrSendInstructions, gbc_txtrSendInstructions);

		//MENU BAR

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu cgraSettingsMenu = new JMenu("CGRA Settings");
		JMenu loadComputeKernelMenu = new JMenu("Load Compute Kernel");
		JMenu helpMenu = new JMenu("Help");
		JMenu aboutMenu = new JMenu("About");

		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveAsItem = new JMenuItem("Save As...");
		JMenuItem setLocalMemoryItem = new JMenuItem("Set Local CGRA Memory");
		JMenuItem setContextItem = new JMenuItem("Set Context");
		JMenuItem loadContextItem = new JMenuItem("Load Context");
		JMenuItem saveContextToFileItem = new JMenuItem("Save Context...");
		JMenuItem setPEItem = new JMenuItem("Set PE");
		
		JMenuItem setKernel1 = new JMenuItem("Set kernel1");



		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Open" is clicked
				System.out.println("Open clicked");
			}
		});

		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Save As..." is clicked
				System.out.println("Save As... clicked");
			}
		});

		setLocalMemoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Open" is clicked
				System.out.println("Open clicked");
			}
		});

		setContextItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Save As..." is clicked
				System.out.println("Save As... clicked");
			}
		});

		loadContextItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Open" is clicked
				System.out.println("Open clicked");
			}
		});

		saveContextToFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Perform action when "Save As..." is clicked
				System.out.println("Save As... clicked");
			}
		});
		
		setPEItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GUI_setPE setwindow = new GUI_setPE();
							setwindow.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				
			}
		});
		
		setKernel1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});

		fileMenu.add(openItem);
		fileMenu.add(saveAsItem);
		cgraSettingsMenu.add(setPEItem);
		cgraSettingsMenu.add(setLocalMemoryItem);
		cgraSettingsMenu.add(setContextItem);
		cgraSettingsMenu.add(loadContextItem);
		cgraSettingsMenu.add(saveContextToFileItem);
		loadComputeKernelMenu.add(setKernel1);

		menuBar.add(fileMenu);
		menuBar.add(cgraSettingsMenu);
		menuBar.add(loadComputeKernelMenu);
		menuBar.add(helpMenu);
		menuBar.add(aboutMenu);

		frmCgrasim.setJMenuBar(menuBar);

		//frmCgrasim.setSize(400, 400);
		//frmCgrasim.setLayout(null);
		frmCgrasim.setVisible(true);

	}

	public JTextArea getTxtbox_peinfo() {
		return txtbox_peinfo;
	}

	public boolean refreshConnects() {

		//for (List<ProcessingElementPort> a : cgra.getInterconnect().getContext().g )
		for (Map.Entry<ProcessingElementPort,List<ProcessingElementPort>> entry : cgra.getInterconnect().getContext().getConnections().entrySet())
		{
			for (ProcessingElementPort a : entry.getValue())
			{
				int x_src = entry.getKey().getPE().getX();
				int y_src = entry.getKey().getPE().getY();
				int x_dest = a.getPE().getX();
				int y_dest = a.getPE().getY();

				int x_src_gui = x_src*2 + 1;
				int y_src_gui = y_src*2 + 1;

				int x_dif = x_dest - x_src;
				int y_dif = y_dest - y_src;


				JTextField tmpx = (JTextField) matrixlist.get(x_src_gui + x_dif).get(y_src_gui + y_dif);
				if (x_dif == 1 && y_dif == 1)
				{
					tmpx.setText("↘");
				}
				else if (x_dif == 1 && y_dif == 0)
				{
					tmpx.setText("→");
				}
				else if (x_dif == 1 && y_dif == -1)
				{
					tmpx.setText("↗");
				}
				else if (x_dif == 0 && y_dif == -1)
				{
					tmpx.setText("↑");
				}
				else if (x_dif == -1 && y_dif == -1)
				{
					tmpx.setText("↖");
				}
				else if (x_dif == -1 && y_dif == 0)
				{
					tmpx.setText("←");
				}
				else if (x_dif == -1 && y_dif == 1)
				{
					tmpx.setText("↙");
				}
				else if (x_dif == 0 && y_dif == 1)
				{
					tmpx.setText("↓");
				}
				else tmpx.setText("?");
			}
		}
		return false;

	}


	//LOG WINDOW
	/*
	 * consoleTextArea = new JTextArea(20, 40);
        consoleTextArea.setEditable(false);

        // Redirect the System.out output to the console text area
        System.setOut(new PrintStream(new TextAreaOutputStream(consoleTextArea)));

        // Add the console text area to the console frame
        consoleFrame.getContentPane().add(new JScrollPane(consoleTextArea));

        // Set the size and make the console frame visible
        consoleFrame.pack();
        consoleFrame.setVisible(true);
	 */


}
