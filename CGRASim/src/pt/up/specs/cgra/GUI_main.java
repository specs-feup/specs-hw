package pt.up.specs.cgra;

import javax.swing.JFrame;
import javax.swing.JMenu;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.Interconnect_Types;
import pt.up.specs.cgra.structure.Mesh_Types;
import pt.up.specs.cgra.structure.PE_Types;
import pt.up.specs.cgra.structure.context.Context;
import pt.up.specs.cgra.structure.memory.GenericMemory;
import pt.up.specs.cgra.structure.pes.EmptyPE;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;
import pt.up.specs.cgra.structure.pes.binary.MultiplierElement;
import pt.up.specs.cgra.structure.pes.loadstore.LSElement;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;



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
	int x_cgra, y_cgra;
	int memsize;
	Interconnect_Types IC_T; 
	PE_Types PE_T; 
	Mesh_Types MESH_T; 
	boolean lsu_select;
	GUI_setPE setwindow;

	public GUI_main(int x_cgra, int y_cgra, int memsize, Interconnect_Types IC_T, PE_Types PE_T, Mesh_Types MESH_T, boolean lsu_select) {

		var CGRAbld = new GenericSpecsCGRA.Builder(x_cgra, y_cgra);
		this.x_cgra = x_cgra;
		this.y_cgra = y_cgra;
		this.memsize = memsize;
		this.IC_T = IC_T;
		this.PE_T = PE_T;
		this.MESH_T = MESH_T;
		this.lsu_select = lsu_select;

		CGRAbld.withMemory(memsize);

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

			case EMPTY:
			default:
				CGRAbld.withHomogeneousPE(new EmptyPE());
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

		setwindow = new GUI_setPE(this, cgra, x_cgra, y_cgra);
		setwindow.frame.setVisible(false);
	}

	int i = 0;
	int j = 0;

	private void initialize(int x_cgra, int y_cgra, Interconnect_Types IC_T) {

		frmCgrasim = new JFrame();
		frmCgrasim.setTitle("CGRA-SIM");
		frmCgrasim.setBounds(100, 100, 200 + 100*x_cgra, 50 + 100*y_cgra);
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

					ProcessingElement pex = cgra.getMesh().getProcessingElement(x, y);

					System.out.printf("button at %d, %d \n", i, j);

					cmptmp = new JButton("Empty");
					((JButton) cmptmp).setText(String.format("<html> %s<br>%d </html>", pex.toString(), pex.getRegisterFile().get(0).getValue().intValue()));

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
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().endsWith(".txt");
					}

					public String getDescription() {
						return "Text files... (*.txt)";
					}
				};

				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					cgra.getInstdec().parseFile(selectedFile);
				}
				System.out.println("Save As... clicked");

			}
		});
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
		JMenu viewMenu = new JMenu("View");
		JMenu cgraSettingsMenu = new JMenu("CGRA Settings");
		JMenu setComputeKernelMenu = new JMenu("Set Compute Kernel");
		JMenu helpMenu = new JMenu("Help");
		JMenu aboutMenu = new JMenu("About");

		JMenuItem openItem = new JMenuItem("Load Simulator Preset...");
		JMenuItem saveAsItem = new JMenuItem("Save As...");
		JMenuItem loadLocalMemoryItem = new JMenuItem("Load Local CGRA Memory...");
		JMenuItem setContextItem = new JMenuItem("Set Context");
		JMenuItem loadContextItem = new JMenuItem("Load Context...");
		JMenuItem saveContextToFileItem = new JMenuItem("Save Context...");
		JMenuItem setPEItem = new JMenuItem("Set PE");
		JMenuItem viewRegistersWindow = new JCheckBoxMenuItem("Registers Window");
		JMenuItem viewConfigWindow = new JCheckBoxMenuItem("Configuration window");


		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().endsWith(".cpreset");
					}

					public String getDescription() {
						return "CGRA-SIM Preset files... (*.cpreset)";
					}
				};

				fileChooser.setFileFilter((javax.swing.filechooser.FileFilter) filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					readAllFile(selectedFile);
				}
				System.out.println("Open clicked");
			}
		});

		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().endsWith(".cpreset");
					}

					public String getDescription() {
						return "CGRA-SIM Preset files... (*.cpreset)";
					}
				};

				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					saveAllToFile(selectedFile);
				}
				System.out.println("Save As... clicked");
			}
		});

		loadLocalMemoryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						// Allow directories and files with the desired extension
						return file.isDirectory() || file.getName().endsWith(".clocalmem");
					}

					public String getDescription() {
						return "CGRA-SIM Local Memory files... (*.clocalmem)";
					}
				};

				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					readLocalMemoryFile(selectedFile);
				}
				System.out.println("Open clicked");
			}
		});

		loadContextItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().endsWith(".cctxt");
					}

					public String getDescription() {
						return "CGRA-SIM Context files... (*.cctxt)";
					}
				};

				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					readContextFile(selectedFile);
				}
				System.out.println("Open clicked");
			}
		});

		saveContextToFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileFilter filter = new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().endsWith(".cctxt");
					}

					public String getDescription() {
						return "CGRA-SIM Context files... (*.cctxt)";
					}
				};

				fileChooser.setFileFilter(filter);

				int result = fileChooser.showOpenDialog(frmCgrasim);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					saveContextToFile(selectedFile);
				}
				System.out.println("Save As... clicked");
			}
		});

		setPEItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							setwindow.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		setComputeKernelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		fileMenu.add(openItem);
		fileMenu.add(saveAsItem);
		cgraSettingsMenu.add(setPEItem);
		cgraSettingsMenu.add(loadLocalMemoryItem);
		cgraSettingsMenu.add(setContextItem);
		cgraSettingsMenu.add(loadContextItem);
		cgraSettingsMenu.add(saveContextToFileItem);

		menuBar.add(fileMenu);
		menuBar.add(cgraSettingsMenu);
		menuBar.add(setComputeKernelMenu);
		menuBar.add(helpMenu);
		menuBar.add(aboutMenu);

		frmCgrasim.setJMenuBar(menuBar);
		frmCgrasim.setVisible(true);

	}

	public JTextArea getTxtbox_peinfo() {
		return txtbox_peinfo;
	}

	public boolean refreshConnects() {

		//for (List<ProcessingElementPort> a : cgra.getInterconnect().getContext().g )
		for (Map.Entry<ProcessingElementPort,List<ProcessingElementPort>> entry : cgra.getInterconnect().makeContext().getConnections().entrySet())
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

	public boolean refreshButtons() {
		int i = 0;
		int j = 0;
		for (ArrayList<JButton> btnlist : PEbtnlist)
		{
			for (JButton a : btnlist)
			{
				ProcessingElement x = cgra.getMesh().getProcessingElement(i, j);
				a.setText(String.format("%s \n %d", x.toString(), x.getRegisterFile().get(0).getValue().intValue()));
				j++;
			}

			i++;
			j = 0;
		}
		return true;
	}

	public JFrame getFrmCgrasim() {
		return frmCgrasim;
	}

	public void setFrmCgrasim(JFrame frmCgrasim) {
		this.frmCgrasim = frmCgrasim;
	}

	public List<ArrayList<JButton>> getPEbtnlist() {
		return PEbtnlist;
	}

	public void setPEbtnlist(List<ArrayList<JButton>> pEbtnlist) {
		PEbtnlist = pEbtnlist;
	}

	public List<JTextField> getSeplist() {
		return seplist;
	}

	public void setSeplist(List<JTextField> seplist) {
		this.seplist = seplist;
	}

	public List<Component> getRigidseplist() {
		return rigidseplist;
	}

	public void setRigidseplist(List<Component> rigidseplist) {
		this.rigidseplist = rigidseplist;
	}

	public List<ArrayList<GridBagConstraints>> getGbclist() {
		return gbclist;
	}

	public void setGbclist(List<ArrayList<GridBagConstraints>> gbclist) {
		this.gbclist = gbclist;
	}

	public List<ArrayList<Object>> getMatrixlist() {
		return matrixlist;
	}

	public void setMatrixlist(List<ArrayList<Object>> matrixlist) {
		this.matrixlist = matrixlist;
	}

	public GenericSpecsCGRA getCgra() {
		return cgra;
	}

	public void setCgra(GenericSpecsCGRA cgra) {
		this.cgra = cgra;
	}

	public int getX_cgra() {
		return x_cgra;
	}

	public void setX_cgra(int x_cgra) {
		this.x_cgra = x_cgra;
	}

	public int getY_cgra() {
		return y_cgra;
	}

	public void setY_cgra(int y_cgra) {
		this.y_cgra = y_cgra;
	}

	protected void saveAllToFile(File file) {

		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

			objectOut.writeObject(x_cgra);
			objectOut.writeObject(y_cgra);
			objectOut.writeObject(memsize);
			objectOut.writeObject(IC_T);
			objectOut.writeObject(PE_T);
			objectOut.writeObject(MESH_T);
			objectOut.writeObject(lsu_select);
			objectOut.writeObject(cgra.getContext_memory());
			objectOut.writeObject(cgra.getMesh().get2Dmesh());
			objectOut.writeObject(cgra.getInterconnect().makeContext());

			objectOut.close();
			fileOut.close();
			System.out.println("Objects saved to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void readAllFile(File file) {
		ArrayList<Context> context_memory = null;
		int selected_context = 0;
		List<List<ProcessingElement>> mesh2d = null;
		Context tmp_ctxt = null;

		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			this.x_cgra = (int) objectIn.readObject();
			this.y_cgra = (int) objectIn.readObject();
			this.memsize = (int) objectIn.readObject();
			this.IC_T = (Interconnect_Types) objectIn.readObject();
			this.PE_T = (PE_Types) objectIn.readObject();
			this.MESH_T = (Mesh_Types) objectIn.readObject();
			this.lsu_select = (boolean) objectIn.readObject();
			context_memory = (ArrayList<Context>) objectIn.readObject();
			mesh2d = (List<List<ProcessingElement>>) objectIn.readObject();
			tmp_ctxt = (Context) objectIn.readObject();

			objectIn.close();
			fileIn.close();

			System.out.println("Retrieved objects from file:");

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		var CGRAbld = new GenericSpecsCGRA.Builder(x_cgra, y_cgra);

		CGRAbld.withMemory(memsize);

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

			case EMPTY:
			default:
				CGRAbld.withHomogeneousPE(new EmptyPE());
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

		cgra.setContext_memory(context_memory);
		cgra.setSelected_context(selected_context);

		for (int i = 0; i < cgra.getLocalmemSize(); i++) cgra.writeMemory((Integer) (i), new PEInteger(i*i + 1));

		cgra.reset();

		cgra.getMesh().set2Dmesh(mesh2d);
		cgra.saveContext(tmp_ctxt);

		initialize(x_cgra, y_cgra, IC_T);

		setwindow = new GUI_setPE(this, cgra, x_cgra, y_cgra);
		setwindow.frame.setVisible(false);

	}

	protected void readLocalMemoryFile(File file) {
		GenericMemory localMemory = null;

		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			localMemory = (GenericMemory) objectIn.readObject();
			cgra.setMemory(localMemory);
			objectIn.close();
			fileIn.close();

			System.out.println("Retrieved objects from file:");

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void readContextFile(File file) {
		Context ctxt = null;

		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			ctxt = (Context) objectIn.readObject();
			cgra.saveContext(ctxt);
			objectIn.close();
			fileIn.close();

			System.out.println("Retrieved objects from file:");

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void saveContextToFile(File file) {

		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

			objectOut.writeObject(cgra.getInterconnect().makeContext());
			objectOut.close();
			fileOut.close();
			System.out.println("Objects saved to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
