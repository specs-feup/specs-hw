package pt.up.specs.cgra;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import pt.up.specs.cgra.structure.Interconnect_Types;
import pt.up.specs.cgra.structure.Mesh_Types;
import pt.up.specs.cgra.structure.PE_Types;

import javax.swing.JRadioButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI_config {

	private JFrame frmCgrasimConfigGui;
	public static JTextField txt_xsize;
	public static JTextField txt_ysize;
	public static JTextField txt_memsize;
	public static JComboBox combo_mesh;
	public static JComboBox combo_petype;
	public static JComboBox combo_interconnecttype;
	public static JCheckBox checkbox_lsu;
	public static JButton btn_Exit;
	public static JButton btn_OK;

	static int x_cgra;
	static int y_cgra;
	static int memsize;
	static Interconnect_Types IC_T;
	static PE_Types PE_T;
	static Mesh_Types MESH_T;
	static boolean lsu_select;

	public static void main(String[] args) {

		try {
			// Set the look and feel to match the operating system
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FontUIResource fontUIResource = new FontUIResource(Font.DIALOG, Font.PLAIN, 12);
			setUIFont(fontUIResource);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_config window = new GUI_config();
					window.frmCgrasimConfigGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}


	public GUI_config() {
		initialize();
	}

	private static void setUIFont(FontUIResource fontUIResource) {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontUIResource);
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmCgrasimConfigGui = new JFrame();
		frmCgrasimConfigGui.setTitle("CGRA-SIM Config GUI");
		frmCgrasimConfigGui.setBounds(100, 100, 640, 312);
		frmCgrasimConfigGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frmCgrasimConfigGui.getContentPane().add(panel, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		frmCgrasimConfigGui.getContentPane().add(panel_1, BorderLayout.SOUTH);

		JPanel panel_2 = new JPanel();
		frmCgrasimConfigGui.getContentPane().add(panel_2, BorderLayout.CENTER);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 64, 17, 58, 67, 40, 0, 42, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, -19, 0, 12, 0, 14, 0, 13, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);

		Component rigidArea_top = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_top = new GridBagConstraints();
		gbc_rigidArea_top.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_top.gridx = 0;
		gbc_rigidArea_top.gridy = 0;
		panel_2.add(rigidArea_top, gbc_rigidArea_top);

		JLabel lbl_xsize = new JLabel("X size");
		GridBagConstraints gbc_lbl_xsize = new GridBagConstraints();
		gbc_lbl_xsize.anchor = GridBagConstraints.WEST;
		gbc_lbl_xsize.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_xsize.gridx = 1;
		gbc_lbl_xsize.gridy = 1;
		panel_2.add(lbl_xsize, gbc_lbl_xsize);

		txt_xsize = new JTextField();
		GridBagConstraints gbc_txt_xsize = new GridBagConstraints();
		gbc_txt_xsize.insets = new Insets(0, 0, 5, 5);
		gbc_txt_xsize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_xsize.gridx = 3;
		gbc_txt_xsize.gridy = 1;
		panel_2.add(txt_xsize, gbc_txt_xsize);
		txt_xsize.setColumns(10);

		JLabel lbl_ysize = new JLabel("Y size");
		GridBagConstraints gbc_lbl_ysize = new GridBagConstraints();
		gbc_lbl_ysize.fill = GridBagConstraints.HORIZONTAL;
		gbc_lbl_ysize.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_ysize.gridx = 5;
		gbc_lbl_ysize.gridy = 1;
		panel_2.add(lbl_ysize, gbc_lbl_ysize);

		txt_ysize = new JTextField();
		GridBagConstraints gbc_txt_ysize = new GridBagConstraints();
		gbc_txt_ysize.insets = new Insets(0, 0, 5, 5);
		gbc_txt_ysize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_ysize.gridx = 7;
		gbc_txt_ysize.gridy = 1;
		panel_2.add(txt_ysize, gbc_txt_ysize);
		txt_ysize.setColumns(10);

		JLabel lbl_mesh = new JLabel("Mesh Type");
		GridBagConstraints gbc_lbl_mesh = new GridBagConstraints();
		gbc_lbl_mesh.anchor = GridBagConstraints.WEST;
		gbc_lbl_mesh.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_mesh.gridx = 1;
		gbc_lbl_mesh.gridy = 3;
		panel_2.add(lbl_mesh, gbc_lbl_mesh);

		combo_mesh = new JComboBox();
		combo_mesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (combo_mesh.getSelectedItem() == Mesh_Types.EMPTY)
				{
					combo_petype.setEnabled(false);
				}
				else if (combo_mesh.getSelectedItem() == Mesh_Types.HOMOGENEOUS)
				{
					combo_petype.setEnabled(true);
				}
			}
		});
		combo_mesh.setModel(new DefaultComboBoxModel(Mesh_Types.values()));
		GridBagConstraints gbc_combo_mesh = new GridBagConstraints();
		gbc_combo_mesh.gridwidth = 2;
		gbc_combo_mesh.insets = new Insets(0, 0, 5, 5);
		gbc_combo_mesh.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo_mesh.gridx = 3;
		gbc_combo_mesh.gridy = 3;
		panel_2.add(combo_mesh, gbc_combo_mesh);

		combo_petype = new JComboBox();
		combo_petype.setModel(new DefaultComboBoxModel(PE_Types.values()));
		GridBagConstraints gbc_combo_petype = new GridBagConstraints();
		gbc_combo_petype.gridwidth = 2;
		gbc_combo_petype.insets = new Insets(0, 0, 5, 5);
		gbc_combo_petype.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo_petype.gridx = 6;
		gbc_combo_petype.gridy = 3;
		panel_2.add(combo_petype, gbc_combo_petype);

		JLabel lbl_interconnect = new JLabel("Interconnect Type");
		GridBagConstraints gbc_lbl_interconnect = new GridBagConstraints();
		gbc_lbl_interconnect.anchor = GridBagConstraints.WEST;
		gbc_lbl_interconnect.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_interconnect.gridx = 1;
		gbc_lbl_interconnect.gridy = 5;
		panel_2.add(lbl_interconnect, gbc_lbl_interconnect);

		combo_interconnecttype = new JComboBox();
		combo_interconnecttype.setModel(new DefaultComboBoxModel(Interconnect_Types.values()));
		GridBagConstraints gbc_combo_interconnecttype = new GridBagConstraints();
		gbc_combo_interconnecttype.gridwidth = 2;
		gbc_combo_interconnecttype.insets = new Insets(0, 0, 5, 5);
		gbc_combo_interconnecttype.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo_interconnecttype.gridx = 3;
		gbc_combo_interconnecttype.gridy = 5;
		panel_2.add(combo_interconnecttype, gbc_combo_interconnecttype);

		JLabel lbl_memsize = new JLabel("Memory Size");
		GridBagConstraints gbc_lbl_memsize = new GridBagConstraints();
		gbc_lbl_memsize.anchor = GridBagConstraints.WEST;
		gbc_lbl_memsize.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_memsize.gridx = 1;
		gbc_lbl_memsize.gridy = 7;
		panel_2.add(lbl_memsize, gbc_lbl_memsize);

		txt_memsize = new JTextField();
		GridBagConstraints gbc_txt_memsize = new GridBagConstraints();
		gbc_txt_memsize.insets = new Insets(0, 0, 5, 5);
		gbc_txt_memsize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_memsize.gridx = 3;
		gbc_txt_memsize.gridy = 7;
		panel_2.add(txt_memsize, gbc_txt_memsize);
		txt_memsize.setColumns(10);

		JLabel lbl_LSU = new JLabel("First row of Load-Store Units? ");
		GridBagConstraints gbc_lbl_LSU = new GridBagConstraints();
		gbc_lbl_LSU.anchor = GridBagConstraints.WEST;
		gbc_lbl_LSU.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_LSU.gridx = 1;
		gbc_lbl_LSU.gridy = 9;
		panel_2.add(lbl_LSU, gbc_lbl_LSU);

		checkbox_lsu = new JCheckBox("");
		GridBagConstraints gbc_checkbox_lsu = new GridBagConstraints();
		gbc_checkbox_lsu.fill = GridBagConstraints.HORIZONTAL;
		gbc_checkbox_lsu.insets = new Insets(0, 0, 5, 5);
		gbc_checkbox_lsu.gridx = 3;
		gbc_checkbox_lsu.gridy = 9;
		panel_2.add(checkbox_lsu, gbc_checkbox_lsu);

		btn_OK = new JButton("OK");



		//EVENT OF "OK" BUTTON
		btn_OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					x_cgra = Integer.parseInt(txt_xsize.getText());
					y_cgra = Integer.parseInt(txt_ysize.getText());
					memsize = Integer.parseInt(txt_memsize.getText());

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				MESH_T = (Mesh_Types) combo_mesh.getSelectedItem();
				PE_T = (PE_Types) combo_petype.getSelectedItem();
				IC_T = (Interconnect_Types) combo_interconnecttype.getSelectedItem();
				lsu_select = checkbox_lsu.isSelected();

				if (x_cgra > 0 && y_cgra > 0 && memsize > 0)
				{

					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								GUI_main window2 = new GUI_main(x_cgra, y_cgra, memsize, IC_T, PE_T, MESH_T, lsu_select);
								window2.frmCgrasim.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}); 

		btn_Exit = new JButton("Exit");
		GridBagConstraints gbc_btn_Exit = new GridBagConstraints();
		gbc_btn_Exit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_Exit.insets = new Insets(0, 0, 5, 5);
		gbc_btn_Exit.gridx = 5;
		gbc_btn_Exit.gridy = 9;
		panel_2.add(btn_Exit, gbc_btn_Exit);



		GridBagConstraints gbc_btn_OK = new GridBagConstraints();
		gbc_btn_OK.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_OK.insets = new Insets(0, 0, 5, 5);
		gbc_btn_OK.gridx = 7;
		gbc_btn_OK.gridy = 9;
		panel_2.add(btn_OK, gbc_btn_OK);

		Component rigidArea_bottom_2 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_bottom_2 = new GridBagConstraints();
		gbc_rigidArea_bottom_2.gridx = 8;
		gbc_rigidArea_bottom_2.gridy = 10;
		panel_2.add(rigidArea_bottom_2, gbc_rigidArea_bottom_2);

		JPanel panel_3 = new JPanel();
		frmCgrasimConfigGui.getContentPane().add(panel_3, BorderLayout.WEST);

		JPanel panel_4 = new JPanel();
		frmCgrasimConfigGui.getContentPane().add(panel_4, BorderLayout.EAST);
	}


	public JFrame getFrame() {
		return frmCgrasimConfigGui;
	}


	public void setFrame(JFrame frame) {
		this.frmCgrasimConfigGui = frame;
	}


	/*public JTextField getTxt_xsize() {
		return txt_xsize;
	}




	public void setTxt_xsize(JTextField txt_xsize) {
		this.txt_xsize = txt_xsize;
	}


	public JTextField getTxt_ysize() {
		return txt_ysize;
	}


	public void setTxt_ysize(JTextField txt_ysize) {
		this.txt_ysize = txt_ysize;
	}


	public JTextField getTxt_memsize() {
		return txt_memsize;
	}


	public void setTxt_memsize(JTextField txt_memsize) {
		this.txt_memsize = txt_memsize;
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


	public int getMemsize() {
		return memsize;
	}


	public void setMemsize(int memsize) {
		this.memsize = memsize;
	}


	public Interconnect_Types getIC_T() {
		return IC_T;
	}


	public void setIC_T(Interconnect_Types iC_T) {
		IC_T = iC_T;
	}


	public PE_Types getPE_T() {
		return PE_T;
	}


	public void setPE_T(PE_Types pE_T) {
		PE_T = pE_T;
	}


	public Mesh_Types getMESH_T() {
		return MESH_T;
	}


	public void setMESH_T(Mesh_Types mESH_T) {
		MESH_T = mESH_T;
	}*/

}
