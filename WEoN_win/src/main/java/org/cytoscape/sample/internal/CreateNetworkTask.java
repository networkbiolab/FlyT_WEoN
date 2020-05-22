package org.cytoscape.sample.internal;

import java.util.Collection;
import java.util.HashMap;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.*;
import java.util.Random;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;

import java.util.List;
import java.util.Map;
import java.util.Hashtable;

public class CreateNetworkTask extends AbstractTask {
	
	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	
	public CreateNetworkTask(final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, final CyNetworkFactory cnf){
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
	}
	
	public void run(TaskMonitor monitor) {
		// Create an empty network
		final CyNetwork myNet = cnf.createNetwork();
		myNet.getRow(myNet).set(CyNetwork.NAME, namingUtil.getSuggestedNetworkTitle("Fly T-WEoN"));
		myNet.getDefaultNodeTable().createColumn("symbol", String.class, false);		
		JFrame frame;
		final JDialog dialog = new JDialog(); 
		dialog.setTitle("Histone Marks");
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

		//random string ID
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        final String ID = salt.toString();
        
        //temp files to write path of files
        final String confFile = System.getProperty("java.io.tmpdir")+"\\conf_PTMs_"+ID;
        final String configComb = System.getProperty("java.io.tmpdir")+"\\conf_comb_"+ID;
        final String refNetFile = System.getProperty("java.io.tmpdir")+"\\refNet_"+ID;
		final String expressionFile = System.getProperty("java.io.tmpdir")+"\\expression_"+ID;
		final String resultsFile = System.getProperty("java.io.tmpdir")+"\\results_"+ID;
		final String dnaseFile = System.getProperty("java.io.tmpdir")+"\\dnase_"+ID;
		final String hmFile = System.getProperty("java.io.tmpdir")+"\\hm_"+ID;
		final String methFile = System.getProperty("java.io.tmpdir")+"\\meth_"+ID;
		final String h3k27me3 = System.getProperty("java.io.tmpdir")+"\\h3k27me3_"+ID;
		final String h3k27ac = System.getProperty("java.io.tmpdir")+"\\h3k27ac_"+ID;
		final String h3k36me2 = System.getProperty("java.io.tmpdir")+"\\h3k36me2_"+ID;
		final String h3k4me = System.getProperty("java.io.tmpdir")+"\\h3k4me_"+ID;
		final String h3k4me2 = System.getProperty("java.io.tmpdir")+"\\h3k4me2_"+ID;
		final String h3k4me3 = System.getProperty("java.io.tmpdir")+"\\h3k4me3_"+ID;
		final String h3k79me2 = System.getProperty("java.io.tmpdir")+"\\h3k79me2_"+ID;
		final String h3k9ac = System.getProperty("java.io.tmpdir")+"\\h3k9ac_"+ID;
		final String h3k9me2 = System.getProperty("java.io.tmpdir")+"\\h3k9me2_"+ID;
		final String h3k9me3 = System.getProperty("java.io.tmpdir")+"\\h3k9me3_"+ID;
		final String h3s10ph = System.getProperty("java.io.tmpdir")+"\\h3s10ph_"+ID;
		final String h4k6ac = System.getProperty("java.io.tmpdir")+"\\h4k6ac_"+ID;
		final String h4k20me3 = System.getProperty("java.io.tmpdir")+"\\h4k20me3_"+ID;
				
		final String[] histoneFiles = {h3k27me3, h3k27ac, h3k36me2, h3k4me, h3k4me2, h3k4me3, h3k79me2, h3k9ac, h3k9me2, h3k9me3, h3s10ph, h4k6ac, h4k20me3};
		final String[] histoneNames = {"H3K27me3", "H3K36me3", "H3K36me2", "H3K4me1", "H3K4me2", "H3K4me3", "H3K79me2", "H3K9ac", "H3K9me2", "H3K9me3", "H3S10ph", "H4K16ac", "H4K20me3"};
		final String[] histoneActv = {"-", "+", "+", "+", "+", "+", "+", "+", "-", "-", "+", "+", "-"};

		frame = new JFrame("Fly T-WEoN - Fly Tool for Weighted Epigenomic Networks");
		frame.setResizable(false);
		frame.setBounds(100, 100, 465, 609);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblRequired = new JLabel("Required");
		lblRequired.setBounds(6, 6, 82, 15);
		frame.getContentPane().add(lblRequired);
		frame.setVisible(true);
		
		//reference network
		JLabel lblNetwork = new JLabel("Network Definition");
		lblNetwork.setBounds(26, 33, 205, 15);
		frame.getContentPane().add(lblNetwork);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String refNet = comboBox_1.getSelectedItem().toString();
				try {
					FileWriter File  = new FileWriter(refNetFile);
					File.write(refNet);
					File.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		comboBox_1.addItem("Distance: 1500nt");
		comboBox_1.addItem("Distance: 2000nt");
		comboBox_1.addItem("Distance: 5000nt");
		comboBox_1.setBounds(230, 28, 205, 25);
		frame.getContentPane().add(comboBox_1);
		
		//expression
		JLabel lblExpresionFile = new JLabel("Expression File");
		lblExpresionFile.setBounds(26, 71, 128, 15);
		frame.getContentPane().add(lblExpresionFile);

		final JLabel lblNoFileSelected = new JLabel("No File Selected");
		lblNoFileSelected.setBounds(230, 93, 205, 15);
		frame.getContentPane().add(lblNoFileSelected);

		
		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(fileChooser);
				try {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String name = fileChooser.getSelectedFile().getName();
					try {
						FileWriter File  = new FileWriter(expressionFile);
						File.write(path);
						File.close();
						lblNoFileSelected.setText(name);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NullPointerException e1) {
		            System.out.println("No file selected");
		        }	
			}
		});

		btnSelectFile.setBounds(230, 65, 205, 27);
		frame.getContentPane().add(btnSelectFile);
		
		//result folder
		JLabel lblResultFolder = new JLabel("Result Folder");
		lblResultFolder.setBounds(26, 126, 128, 15);
		frame.getContentPane().add(lblResultFolder);
	
		final JLabel lblNoFolderSelected = new JLabel("No Folder Selected");
		lblNoFolderSelected.setBounds(230, 148, 205, 15);
		frame.getContentPane().add(lblNoFolderSelected);
		
		JButton btnSelectFolder = new JButton("Select Folder");
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
			    fileChooser.setCurrentDirectory(new java.io.File("."));
				//fileChooser.showOpenDialog(fileChooser);

			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	try {
			    		String path = fileChooser.getSelectedFile().getAbsolutePath();
			    		String name = fileChooser.getSelectedFile().getName();
						FileWriter File  = new FileWriter(resultsFile);
						File.write(path);
						File.close();
						lblNoFolderSelected.setText(name);
				        System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        
			    } else {
			    	System.out.println("No Selection ");
			    }
			}
		});
		btnSelectFolder.setBounds(230, 120, 205, 27);
		frame.getContentPane().add(btnSelectFolder);
		
////////////////////////////////////////////////////////////////////////////////////////////////////
		// SEPARATOR
		JSeparator separator = new JSeparator();
		separator.setBounds(16, 195, 419, 16);
		frame.getContentPane().add(separator);
		
		JLabel lblOptionals = new JLabel("Optional");
		lblOptionals.setBounds(6, 211, 121, 15);
		frame.getContentPane().add(lblOptionals);
		
		//dnase
		JLabel lblDnase = new JLabel("DNase File");
		lblDnase.setBounds(26, 252, 128, 15);
		frame.getContentPane().add(lblDnase);
		
		final JLabel lblNoFileSelected_1 = new JLabel("No File Selected");
		lblNoFileSelected_1.setBounds(230, 274, 205, 15);
		frame.getContentPane().add(lblNoFileSelected_1);
		
		JButton btnNewButton = new JButton("Select File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(fileChooser);
				try {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String name = fileChooser.getSelectedFile().getName();
					try {
						FileWriter File  = new FileWriter(dnaseFile);
						File.write(path);
						File.close();
						lblNoFileSelected_1.setText(name);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NullPointerException e1) {
		            System.out.println("No file selected");
		        }
			}
		});
		btnNewButton.setBounds(230, 246, 205, 27);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblDnaseFilter = new JLabel("DNase Score");
		lblDnaseFilter.setBounds(26, 303, 175, 15);
		frame.getContentPane().add(lblDnaseFilter);
		
		final JTextField textField = new JTextField();
		textField.setText("0");
		textField.setBounds(230, 301, 205, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblHistoneMarks = new JLabel("Histone Mark Path Files");
		lblHistoneMarks.setBounds(26, 345, 187, 15);
		frame.getContentPane().add(lblHistoneMarks);

		JButton btnNewButton_1 = new JButton("Select Files");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel contentPanel = new JPanel();
				dialog.setVisible(true);
				dialog.setBounds(100, 100, 546, 505);
				dialog.getContentPane().setLayout(new BorderLayout());
				contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
				contentPanel.setLayout(null);

				JLabel dialoglblHereYouCan = new JLabel("Histone marks: all modifications shown below are non mandatory,");
				dialoglblHereYouCan.setBounds(12, 12, 534, 15);
				contentPanel.add(dialoglblHereYouCan);
				
				JLabel dialoglblStep = new JLabel("Fly T-WeON will run using each of the marks for which you loaded a file.");
				dialoglblStep.setBounds(12, 28, 522, 15);
				contentPanel.add(dialoglblStep);
				JLabel dialoglblHkme = new JLabel("H3K27me3");
				dialoglblHkme.setBounds(55, 55, 123, 15);
				contentPanel.add(dialoglblHkme);
				final JLabel dialoglblNoFileSelected = new JLabel("No File Selected");

				JButton dialogbtnSelectFile = new JButton("Select File");
				dialogbtnSelectFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//h3k27me3
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k27me3);
								File.write(path);
								File.close();
								dialoglblNoFileSelected.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile.setBounds(196, 50, 117, 25);
				
				dialoglblNoFileSelected.setBounds(331, 55, 203, 15);
				contentPanel.add(dialogbtnSelectFile);
				contentPanel.add(dialoglblNoFileSelected);
				
				JLabel dialoglblHkac = new JLabel("H3K36me3");
				dialoglblHkac.setBounds(55, 82, 123, 15);
				contentPanel.add(dialoglblHkac);

				JButton dialogbtnSelectFile_1 = new JButton("Select File");
				final JLabel dialoglblNoFileSelected_1 = new JLabel("No File Selected");
				dialogbtnSelectFile_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//h3k27ac
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k27ac);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_1.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_1.setBounds(196, 77, 117, 25);
				contentPanel.add(dialogbtnSelectFile_1);
			
				dialoglblNoFileSelected_1.setBounds(331, 82, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_1);
				
				JLabel dialoglblHkme_1 = new JLabel("H3K36me2");
				final JLabel dialoglblNoFileSelected_2 = new JLabel("No File Selected");
				dialoglblHkme_1.setBounds(55, 111, 123, 15);
				contentPanel.add(dialoglblHkme_1);

				JButton dialogbtnSelectFile_2 = new JButton("Select File");
				dialogbtnSelectFile_2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k36me2");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k36me2);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_2.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
						
					}
				});
				dialogbtnSelectFile_2.setBounds(196, 106, 117, 25);
				contentPanel.add(dialogbtnSelectFile_2);
				
				
				dialoglblNoFileSelected_2.setBounds(331, 111, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_2);
				
				JLabel dialoglblHkme_2 = new JLabel("H3K4me");
				final JLabel dialoglblNoFileSelected_3 = new JLabel("No File Selected");
				dialoglblHkme_2.setBounds(55, 138, 123, 15);
				contentPanel.add(dialoglblHkme_2);
				
				JButton dialogbtnSelectFile_3 = new JButton("Select File");
				dialogbtnSelectFile_3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("H3K4me1");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k4me);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_3.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_3.setBounds(196, 133, 117, 25);
				contentPanel.add(dialogbtnSelectFile_3);
				
				
				dialoglblNoFileSelected_3.setBounds(331, 138, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_3);
				
				JLabel dialoglblHkme_3 = new JLabel("H3K4me2");
				final JLabel dialoglblNoFileSelected_4 = new JLabel("No File Selected");
				dialoglblHkme_3.setBounds(55, 165, 123, 15);
				contentPanel.add(dialoglblHkme_3);
				
				JButton dialogbtnSelectFile_4 = new JButton("Select File");
				dialogbtnSelectFile_4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k4me2");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k4me2);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_4.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_4.setBounds(196, 160, 117, 25);
				contentPanel.add(dialogbtnSelectFile_4);
				
				
				dialoglblNoFileSelected_4.setBounds(331, 165, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_4);
				
				JLabel dialoglblHkme_4 = new JLabel("H3K4me3");
				final JLabel dialoglblNoFileSelected_5 = new JLabel("No File Selected");
				dialoglblHkme_4.setBounds(55, 192, 123, 15);
				contentPanel.add(dialoglblHkme_4);
				
				JButton dialogbtnSelectFile_5 = new JButton("Select File");
				dialogbtnSelectFile_5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k4me3");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k4me3);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_5.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_5.setBounds(196, 187, 117, 25);
				contentPanel.add(dialogbtnSelectFile_5);
				
				dialoglblNoFileSelected_5.setBounds(331, 192, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_5);
				
				JLabel dialoglblHkme_5 = new JLabel("H3K79me2");
				final JLabel dialoglblNoFileSelected_6 = new JLabel("No File Selected");
				dialoglblHkme_5.setBounds(55, 219, 123, 15);
				contentPanel.add(dialoglblHkme_5);
				
				JButton dialogbtnSelectFile_6 = new JButton("Select File");
				dialogbtnSelectFile_6.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k79me2");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k79me2);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_6.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_6.setBounds(196, 214, 117, 25);
				contentPanel.add(dialogbtnSelectFile_6);
				
				dialoglblNoFileSelected_6.setBounds(331, 219, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_6);
				
				JLabel dialoglblHkac_1 = new JLabel("H3K9ac");
				final JLabel dialoglblNoFileSelected_7 = new JLabel("No File Selected");
				dialoglblHkac_1.setBounds(55, 246, 123, 15);
				contentPanel.add(dialoglblHkac_1);
				
				JButton dialogbtnSelectFile_7 = new JButton("Select File");
				dialogbtnSelectFile_7.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("H3K9ac");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k9ac);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_7.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_7.setBounds(196, 241, 117, 25);
				contentPanel.add(dialogbtnSelectFile_7);
				
				//JLabel dialoglblNoFileSelected_7 = new JLabel("No File Selected");
				dialoglblNoFileSelected_7.setBounds(331, 246, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_7);
				
				JLabel dialoglblHkme_6 = new JLabel("H3K9me2");
				final JLabel dialoglblNoFileSelected_8 = new JLabel("No File Selected");
				dialoglblHkme_6.setBounds(55, 273, 123, 15);
				contentPanel.add(dialoglblHkme_6);
				
				JButton dialogbtnSelectFile_8 = new JButton("Select File");
				dialogbtnSelectFile_8.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k9me2");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k9me2);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_8.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_8.setBounds(196, 268, 117, 25);
				contentPanel.add(dialogbtnSelectFile_8);
				
				dialoglblNoFileSelected_8.setBounds(331, 273, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_8);
				
				JLabel dialoglblHkme_7 = new JLabel("H3K9me3");
				final JLabel dialoglblNoFileSelected_9 = new JLabel("No File Selected");
				dialoglblHkme_7.setBounds(55, 300, 123, 15);
				contentPanel.add(dialoglblHkme_7);
				
				JButton dialogbtnSelectFile_9 = new JButton("Select File");
				dialogbtnSelectFile_9.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3k9me3");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3k9me3);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_9.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_9.setBounds(196, 295, 117, 25);
				contentPanel.add(dialogbtnSelectFile_9);
				
				
				dialoglblNoFileSelected_9.setBounds(331, 300, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_9);
				
				JLabel dialoglblHsph = new JLabel("H3S10ph");
				final JLabel dialoglblNoFileSelected_10 = new JLabel("No File Selected");
				dialoglblHsph.setBounds(55, 327, 123, 15);
				contentPanel.add(dialoglblHsph);
				
				JButton dialogbtnSelectFile_10 = new JButton("Select File");
				dialogbtnSelectFile_10.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h3s10ph");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h3s10ph);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_10.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_10.setBounds(196, 322, 117, 25);
				contentPanel.add(dialogbtnSelectFile_10);
				
				
				dialoglblNoFileSelected_10.setBounds(331, 327, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_10);
				
				JLabel dialoglblHkac_2 = new JLabel("H4K16ac");
				final JLabel dialoglblNoFileSelected_11 = new JLabel("No File Selected");
				dialoglblHkac_2.setBounds(55, 356, 123, 15);
				contentPanel.add(dialoglblHkac_2);
				
				JButton dialogbtnSelectFile_11 = new JButton("Select File");
				dialogbtnSelectFile_11.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h4k16ac");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h4k6ac);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_11.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_11.setBounds(196, 351, 117, 25);
				contentPanel.add(dialogbtnSelectFile_11);
				
				
				dialoglblNoFileSelected_11.setBounds(331, 356, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_11);
				
				JLabel dialoglblHkme_8 = new JLabel("H4K20me3");
				final JLabel dialoglblNoFileSelected_12 = new JLabel("No File Selected");
				dialoglblHkme_8.setBounds(55, 385, 123, 15);
				contentPanel.add(dialoglblHkme_8);
				
				JButton dialogbtnSelectFile_12 = new JButton("Select File");
				dialogbtnSelectFile_12.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//System.out.println("h4k20me3");
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.showOpenDialog(fileChooser);
						try {
							String path = fileChooser.getSelectedFile().getAbsolutePath();
							String name = fileChooser.getSelectedFile().getName();
							try {
								FileWriter File  = new FileWriter(h4k20me3);
								File.write(path);
								File.close();
								dialoglblNoFileSelected_12.setText(name);
							}catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NullPointerException e1) {
				            System.out.println("No file selected");
				        }
					}
				});
				dialogbtnSelectFile_12.setBounds(196, 380, 117, 25);
				contentPanel.add(dialogbtnSelectFile_12);
				
				
				dialoglblNoFileSelected_12.setBounds(331, 385, 203, 15);
				contentPanel.add(dialoglblNoFileSelected_12);
				
				JButton dialogbtnReturnToThe = new JButton("Return to the main window");
				dialogbtnReturnToThe.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dialog.setVisible(false);
					}
				});
				dialogbtnReturnToThe.setBounds(12, 438, 511, 25);
				contentPanel.add(dialogbtnReturnToThe);
			}
		});

		btnNewButton_1.setBounds(230, 339, 205, 27);
		frame.getContentPane().add(btnNewButton_1);
		
		//methylation
		final JLabel lblNoFileSelected_3 = new JLabel("No File Selected");
		lblNoFileSelected_3.setBounds(230, 419, 205, 15);
		frame.getContentPane().add(lblNoFileSelected_3);
		
		JLabel lblMethylation = new JLabel("Methylation File");
		lblMethylation.setBounds(26, 397, 175, 15);
		frame.getContentPane().add(lblMethylation);
		
		JButton btnSelectFile_1 = new JButton("Select File");
		btnSelectFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(fileChooser);
				try {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String name = fileChooser.getSelectedFile().getName();
					try {
						FileWriter File  = new FileWriter(methFile);
						File.write(path);
						File.close();
						lblNoFileSelected_3.setText(name);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NullPointerException e1) {
		            System.out.println("No file selected");
		        }
			}
		});
		btnSelectFile_1.setBounds(230, 391, 205, 27);
		frame.getContentPane().add(btnSelectFile_1);

		JLabel lblMethylationScore = new JLabel("Methylation Score");
		lblMethylationScore.setBounds(26, 449, 175, 15);
		frame.getContentPane().add(lblMethylationScore);
		
		final JTextField textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(230, 447, 205, 25);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		//run weon button
		JButton btnRunWeon = new JButton("Run Fly T-WEoN");
		btnRunWeon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//checking if all files with path saved exists
				final JPanel panel = new JPanel();
				
				//doing final histone file
				String text = "";
				for (int i = 0; i < histoneFiles.length; i++) {
					File f = new File(histoneFiles[i]);
					if(f.exists()) { 
						try {
							BufferedReader br = new BufferedReader(new FileReader(histoneFiles[i]));
							String st; 
							while ((st = br.readLine()) != null)
							{
								text +="\""+histoneNames[i]+"\t"+histoneActv[i]+"\tpromotor\t"+st+"\",\n";
							} 
							br.close();
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
				}
				
				if(text.length() != 0) {
					text = text.substring(0, text.length()-2)+"\n";
					try {
						FileWriter File  = new FileWriter(hmFile);
						File.write(text);
						File.close();
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				//expression
				File f = new File(expressionFile);
				if(f.exists()) { 
				    System.out.println("pass");
				} else {
					JOptionPane.showMessageDialog(panel, "You need to provide us a expression file.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//result folder
				File f1 = new File(resultsFile);
				if(f1.exists()) { 
				    System.out.println("pass");
				} else {
					JOptionPane.showMessageDialog(panel, "You need to provide us a result folder.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				//create file 
				final String configFile = System.getProperty("java.io.tmpdir")+"\\config_"+ID;
				File file = new File(configFile);
				 
				//Write Content
				FileWriter writer;
				try {
					writer = new FileWriter(file);
					writer.write("#CONFIG_FILE\n");
					//checking for PTM file
					File f2 = new File(hmFile);
					if(f2.exists()) { 
						writer.write("PTMs_filter = Y\n");
						writer.write("comb_PTMs_filter = N\n");
					} else {
						writer.write("PTMs_filter = N\n");
						writer.write("comb_PTMs_filter = N\n");
					}
					File f3 = new File(dnaseFile);
					if(f3.exists()) { 
						writer.write("DNAse_hyper = Y\n");
					} else {
						writer.write("DNAse_hyper = N\n");
					}
					File f4 = new File(methFile);
					if(f4.exists()) { 
						writer.write("Meth = Y\n");
					} else {
						writer.write("Meth = N\n");
					}
					writer.write("TF_express_filter = Y\n");
					writer.write("miRNAs_express_filter = Y\n");
					writer.write("GENEs_file = C:\\WEoN_FlyT\\example_data\\dmel-all-filtered-r6.32-GENES.gff\n");
					

					writer.write("config_PTMs = "+confFile+"\n");
					if(f2.exists()) { 
						try {
							BufferedReader br1 = new BufferedReader(new FileReader(hmFile));
							String st; 
							while ((st = br1.readLine()) != null)
							{
								writer.write(st+"\n");
							} 
							br1.close();
								
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						
						writer.write("config_comb_PTMs = "+configComb+"\n");
						writer.write("#comb_PTM	open(+)/close(-)\n");

						writer.write("\"H3K4me2;H3K9ac;H3K14ac	+\",\n");
						writer.write("\"H3K4me3;H3K4me2;H3K9ac	+\",\n");
						writer.write("\"H3K4me3;H3K9ac;H3K27ac	+\",\n");
						writer.write("\"H3K27me3;H3K9me3	-\",\n");
						writer.write("\"H3K4me2;H3K4me3;H3K36me2;H3K36me3	+\",\n");
						writer.write("\"H3K27me3;H4K20me3	-\",\n");
						writer.write("\"H3K9me2;H3K9me3	-\",\n");
						writer.write("\"H3K27me2;H3K27me3	-\",\n");
						writer.write("\"H3K4me3;H3K9ac	+\",\n");
					}
					File f5 = new File(expressionFile);
					if(f5.exists()) {
						BufferedReader br1 = null;
						FileReader fr1 = null;
						try {
							//reading current network
							fr1 = new FileReader(expressionFile);
							
							br1 = new BufferedReader(fr1);
							String sCurrentLine;
							while ((sCurrentLine = br1.readLine()) != null) {
								writer.write("RPKM_file = "+sCurrentLine+"\n");
							}
							br1.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					File f6 = new File(dnaseFile);
					if(f6.exists()) {
						
						BufferedReader br2 = null;
						FileReader fr2 = null;
						try {
							//reading current network
							fr2 = new FileReader(dnaseFile);
							br2 = new BufferedReader(fr2);
							String sCurrentLine;
							while ((sCurrentLine = br2.readLine()) != null) {
								writer.write("DNAse_file = "+sCurrentLine+"\n");
								writer.write("score_dnase = "+textField.getText()+"\n");
							}
							br2.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					File f7 = new File(methFile);
					if(f7.exists()) {	
						BufferedReader br3 = null;
						FileReader fr3 = null;
						try {
							//reading current network
							fr3 = new FileReader(methFile);
							br3 = new BufferedReader(fr3);
							String sCurrentLine;
							while ((sCurrentLine = br3.readLine()) != null) {
								writer.write("Meth_file = "+sCurrentLine+"\n");
								writer.write("porc_meth = "+textField_1.getText()+"\n");
							}
							br3.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					writer.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				
				//doing a system call perl main.pl -r config -n red ref(1500) -o out
   				String outputFolder = "";
       			try {
       				File f111 = new File(resultsFile);
					if(f111.exists()) { 
						try {
							BufferedReader br = new BufferedReader(new FileReader(resultsFile));
							String st; 
							while ((st = br.readLine()) != null)
							{
								outputFolder += st;
							} 
							br.close();
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
					
       				String net = (String) comboBox_1.getSelectedItem();
       				String netToUse = "RefNet_"+net.replaceAll("Distance: ","").replace("nt","")+"_gffENCODE_chips_6.32.tsv";// "out_TFBS_network_1500.tsv";
       				JOptionPane.showMessageDialog(panel, "Running Fly T-WEoN. This could take several time, please be patient",  "Info", JOptionPane.INFORMATION_MESSAGE);
       				
       				File dir = new File(System.getProperty("java.io.tmpdir"));
					Process p = Runtime.getRuntime().exec("perl C:\\WEoN_FlyT\\main.pl -r "+System.getProperty("java.io.tmpdir")+"config_"+ID+" -n C:\\WEoN_FlyT\\"+netToUse+" -o "+outputFolder, null, dir);

					p.waitFor();


				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(panel, "Can not run Fly T-WEoN",  "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
       			catch (InterruptedException e1) {
					// TODO Auto-generated catch block
       				JOptionPane.showMessageDialog(panel, "bad execution of Fly T-WEoN",  "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
				String line = null;
				String fileName = outputFolder+"\\scored_GRN.tsv";
				try {
					//creating a new table
					
					//doing dictionary for gene symbol
					Map<String, String> gene2symbol = new HashMap<String, String>();
					BufferedReader br = new BufferedReader(new FileReader("C:\\WEoN_FlyT\\WEoN_FlyT\\gene2symbol.tsv"));
					try {
						 String lineDict = br.readLine();
						 
						 while (lineDict != null) {
							 try {
								 String[] arrOfStr = lineDict.replaceAll("\n", "").split("\t");
								 try {
									 gene2symbol.put(arrOfStr[0], arrOfStr[1]);	
								 } catch (Exception eN2) {
									 ;
								 }
								  
							 }catch (Exception eN1) {
								 ;
							 }
							 
							 lineDict = br.readLine();
						 }
					 } finally {
						 br.close();
					 }	
		            // FileReader reads text files in the default encoding.
		            FileReader fileReader = new FileReader(fileName);

		            // Always wrap FileReader in BufferedReader.
		            BufferedReader bufferedReader = new BufferedReader(fileReader);
		            
		            Map<String, Long> nodesSUID = new HashMap<String, Long>();
		            while((line = bufferedReader.readLine()) != null) {
		            	String [] data = line.replaceAll("\n", "").split("\t");
		            	// add a node to the network
		            	CyNode node1 = null;
		            	CyNode node2 = null;

		            	if(nodesSUID.get(data[0]) == null) {
		            		//getNetwork().getRow(e.getNetwork()).get("name", String.class)
		            		node1 = myNet.addNode();
		            		
		            		String symbol = gene2symbol.get(data[0]);
				            if (symbol == null) {
				            	symbol = "";
				            }
				            myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", data[0]);
			            	myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("symbol", symbol);	

				            //myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("symbol", symbol);
		            		nodesSUID.put(data[0], node1.getSUID());
		            		
		            	} else {
		            		node1 = myNet.getNode(nodesSUID.get(data[0]));
		            	}
		            	
		            	if(nodesSUID.get(data[1]) == null) {
		            		//getNetwork().getRow(e.getNetwork()).get("name", String.class)
		            		node2 = myNet.addNode();
		            		
		            		String symbol = gene2symbol.get(data[1]);
				            if (symbol == null) {
				            	symbol = "";
				            }
				            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", data[1]);
			            	myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("symbol", symbol);	

				            
		            		nodesSUID.put(data[1], node2.getSUID());
		            	} else {
		            		node2 = myNet.getNode(nodesSUID.get(data[1]));
		            	}
		        		// Add an edge
		            	CyEdge edge = myNet.addEdge(node1, node2, true);
		            	myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", data[0]+" (regulates) "+ data[1]);	
	        			myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("interaction", data[2]);	
		            }   
		            netMgr.addNetwork(myNet);
		            
		            // Always close files.
		            bufferedReader.close();
		            JOptionPane.showMessageDialog(panel, "Your network has been loaded.", "Info", JOptionPane.INFORMATION_MESSAGE);
		        }
		        catch(FileNotFoundException ex) {
		        	JOptionPane.showMessageDialog(panel, "Error, Fly T-WEoN can not generate your condition specific network",  "Error", JOptionPane.ERROR_MESSAGE);
		            System.out.println(
		                "Unable to open file '" + 
		                fileName + "'");                
		        }
		        catch(IOException ex) {
		        	JOptionPane.showMessageDialog(panel, "Error, Fly T-WEoN can not read your condition specific network",  "Error", JOptionPane.ERROR_MESSAGE);
		            System.out.println(
		                "Error reading file '" 
		                + fileName + "'");                  
		            // Or we could just do this: 
		            // ex.printStackTrace();
		        }
				
				
				
			}
		});
		btnRunWeon.setBounds(12, 494, 436, 43);
		frame.getContentPane().add(btnRunWeon);
				
		//menu
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mntmUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(new URI("https://weon.readthedocs.io/en/latest/"));
					} catch(IOException e1) {
						System.out.println("error loading manual");
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		mnHelp.add(mntmUserManual);
		frame.repaint();
	}
}
