package com.kawa.kinch;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The GUI.
 * @author Kawa
 *
 */
public class Window extends JFrame {

	/**
	 * The version number.
	 */
	private static String version = "1.0b11";

	private static final long serialVersionUID = 2052283588480241620L;
	private JCheckBox clean;
	private JTextArea console;
	private JButton fileChoose;
	private JCheckBox jCheckBox1;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JPanel jPanel1;
	private JProgressBar jProgressBar1;
	private JScrollPane jScrollPane1;
	private JTextField jTextField1;
	private JTextField jTextField2;
	private JButton outputChoose;
	private JCheckBox relative;
	private JButton start;
	private int checks = 0;

	public static String outputFileLocation;

	/**
	 * Creates new form Window
	 */
	public Window() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {

		this.setTitle("SoCalKinch v" + version);

		jPanel1 = new JPanel();
		jLabel1 = new JLabel();
		jTextField1 = new JTextField();
		fileChoose = new JButton();
		jScrollPane1 = new JScrollPane();
		console = new JTextArea();
		jProgressBar1 = new JProgressBar();
		start = new JButton();
		clean = new JCheckBox();
		relative = new JCheckBox();
		jLabel2 = new JLabel();
		jTextField2 = new JTextField();
		outputChoose = new JButton();
		jCheckBox1 = new JCheckBox();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setText("WCA IDs File:");

		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		fileChoose.setText("Browse...");
		fileChoose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fileChooseActionPerformed(evt);
			}
		});

		console.setEditable(false);
		console.setColumns(20);
		console.setRows(5);
		jScrollPane1.setViewportView(console);

		jProgressBar1.setStringPainted(true);

		start.setText("Start!");
		start.setEnabled(false);
		start.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startActionPerformed(evt);
			}
		});

		clean.setText("Clean after finish?");
		clean.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cleanActionPerformed(evt);
			}
		});

		relative.setSelected(true);
		relative.setText("Relative?");
		relative.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				relativeActionPerformed(evt);
			}
		});

		jLabel2.setText("Output:");

		jTextField2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField2ActionPerformed(evt);
			}
		});

		outputChoose.setText("Browse...");
		outputChoose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				outputChooseActionPerformed(evt);
			}
		});

		jCheckBox1.setText("Default Location?");
		jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBox1ActionPerformed(evt);
			}
		});

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
		.setHorizontalGroup(jPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel1Layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								jPanel1Layout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
										.addComponent(
												jScrollPane1)
												.addComponent(
														start,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addComponent(
																jProgressBar1,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
																.addGroup(
																		jPanel1Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel1Layout
																				.createParallelGroup(
																						GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								jLabel2)
																								.addComponent(
																										jLabel1))
																										.addPreferredGap(
																												LayoutStyle.ComponentPlacement.RELATED)
																												.addGroup(
																														jPanel1Layout
																														.createParallelGroup(
																																GroupLayout.Alignment.LEADING)
																																.addComponent(
																																		jTextField1)
																																		.addComponent(
																																				jTextField2))
																																				.addPreferredGap(
																																						LayoutStyle.ComponentPlacement.RELATED)
																																						.addGroup(
																																								jPanel1Layout
																																								.createParallelGroup(
																																										GroupLayout.Alignment.LEADING,
																																										false)
																																										.addComponent(
																																												fileChoose,
																																												GroupLayout.DEFAULT_SIZE,
																																												GroupLayout.DEFAULT_SIZE,
																																												Short.MAX_VALUE)
																																												.addComponent(
																																														outputChoose,
																																														GroupLayout.DEFAULT_SIZE,
																																														GroupLayout.DEFAULT_SIZE,
																																														Short.MAX_VALUE)))
																																														.addGroup(
																																																GroupLayout.Alignment.TRAILING,
																																																jPanel1Layout
																																																.createSequentialGroup()
																																																.addGap(0,
																																																		401,
																																																		Short.MAX_VALUE)
																																																		.addGroup(
																																																				jPanel1Layout
																																																				.createParallelGroup(
																																																						GroupLayout.Alignment.LEADING)
																																																						.addGroup(
																																																								GroupLayout.Alignment.TRAILING,
																																																								jPanel1Layout
																																																								.createSequentialGroup()
																																																								.addComponent(
																																																										relative)
																																																										.addPreferredGap(
																																																												LayoutStyle.ComponentPlacement.RELATED)
																																																												.addComponent(
																																																														clean))
																																																														.addComponent(
																																																																jCheckBox1,
																																																																GroupLayout.Alignment.TRAILING))))
																																																																.addContainerGap()));
		jPanel1Layout
		.setVerticalGroup(jPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel1Layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								jPanel1Layout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(
												jTextField1,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
												.addComponent(
														fileChoose))
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		jPanel1Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																				.addComponent(jLabel2)
																				.addComponent(
																						jTextField2,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								outputChoose))
																								.addPreferredGap(
																										LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(jCheckBox1)
																										.addPreferredGap(
																												LayoutStyle.ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																												.addComponent(jScrollPane1,
																														GroupLayout.PREFERRED_SIZE,
																														201, GroupLayout.PREFERRED_SIZE)
																														.addPreferredGap(
																																LayoutStyle.ComponentPlacement.UNRELATED)
																																.addGroup(
																																		jPanel1Layout
																																		.createParallelGroup(
																																				GroupLayout.Alignment.BASELINE)
																																				.addComponent(clean)
																																				.addComponent(relative))
																																				.addPreferredGap(
																																						LayoutStyle.ComponentPlacement.UNRELATED)
																																						.addComponent(jProgressBar1,
																																								GroupLayout.PREFERRED_SIZE,
																																								GroupLayout.DEFAULT_SIZE,
																																								GroupLayout.PREFERRED_SIZE)
																																								.addPreferredGap(
																																										LayoutStyle.ComponentPlacement.RELATED)
																																										.addComponent(start).addContainerGap()));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPanel1,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPanel1,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack();

		System.out.println("Displaying GUI for SoCalKinch v" + version);
		System.out
		.println("CSV Parsing and Tabulating provided by OpenCSV 3.8, under the Apache 2.0 license");
	}

	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private void fileChooseActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = chooser.showOpenDialog(jPanel1);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				jTextField1.setText(chooser.getSelectedFile()
						.getCanonicalPath());
				checks++;
				if (checks == 2)
					start.setEnabled(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void cleanActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private void startActionPerformed(java.awt.event.ActionEvent evt) {
		Main main = new Main(jTextField1.getText(), console,
					jProgressBar1, relative.isSelected(),
					clean.isSelected(), new File(outputFileLocation
							+ File.separator + "kinch.csv"));
		
		if (start.getText().equals("Start!")) 
		{
			new Thread(main).start();
			start.setEnabled(false);
		}
	}

	private void relativeActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private void outputChooseActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(1);
		int returnVal = chooser.showDialog(this, null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				jTextField2.setText(chooser.getSelectedFile()
						.getCanonicalPath() + File.separator + "kinch.csv");
				outputFileLocation = chooser.getSelectedFile().getCanonicalPath();
				checks++;
				if (checks == 2)
					start.setEnabled(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
		if (jCheckBox1.isSelected()) {
			try {
				jTextField2
				.setText(new File("." + File.separator + "kinch.csv")
				.getCanonicalPath());
				jTextField2.setEnabled(false);
				outputChoose.setEnabled(false);
				checks++;
				if (checks == 2)
					start.setEnabled(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			jTextField2.setText("");
			jTextField2.setEnabled(true);
			outputChoose.setEnabled(true);
			checks--;
			if (checks != 2)
				start.setEnabled(false);
		}
	}

	/**
	 * Displays the window.
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Window().setVisible(true);
			}
		});
	}
}