package ide;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AritIDE {

	public JFrame frmAritIDE;
	private ArrayList<String> rutas = new ArrayList<>();
	private JTabbedPane jTabs;

	/**
	 * danii_mor
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AritIDE window = new AritIDE();
					window.frmAritIDE.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AritIDE() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAritIDE = new JFrame();
		frmAritIDE.setMinimumSize(new Dimension(250, 300));
		frmAritIDE
				.setIconImage(Toolkit.getDefaultToolkit().getImage(AritIDE.class.getResource("/ide/iconos/boot.png")));
		frmAritIDE.setTitle("AritIDE - danii_mor");
		frmAritIDE.setBounds(100, 100, 450, 300);
		frmAritIDE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmAritIDE.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("Archivo");
		menuBar.add(mnFile);

		JPanel addTab = new JPanel();

		JTextArea terminal_txt = new JTextArea();
		terminal_txt.setBackground(Color.BLACK);
		terminal_txt.setForeground(Color.WHITE);
		terminal_txt.append(">> Bienvenido a AritIDE - danii_mor \n");
		terminal_txt.setEditable(false);
		javax.swing.JScrollPane terminal = new javax.swing.JScrollPane(terminal_txt);

		jTabs = new JTabbedPane(JTabbedPane.TOP);

		jTabs.addTab("+", addTab);
		addTab.getAccessibleContext().setAccessibleName("");

		rutas.add("");
		jTabs.add(new EditorText(), "Archivo1", 0);
		jTabs.setSelectedIndex(0);

		jTabs.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int selectedTab = jTabs.getSelectedIndex();
				if ((jTabs.getTabCount() - 1) == selectedTab) {
					rutas.add("");

					jTabs.add(new EditorText(), "Archivo" + (selectedTab + 1), selectedTab);
					jTabs.setSelectedIndex(selectedTab);
				}
			}
		});

		JMenuItem open = new JMenuItem("Abrir");
		open.setIcon(new ImageIcon(AritIDE.class.getResource("/ide/iconos/document-open.png")));
		mnFile.add(open);
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// Codigo para abrir archivo

				JFileChooser open = new JFileChooser();
				open.setDialogTitle("AritIDE - danii_mor");

				// open.setFileFilter(new FileNameExtensionFilter("Seleccionar archivo",
				// ".arit"));

				if (open.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {

						int position = jTabs.getComponentCount() - 1;

						rutas.add(open.getSelectedFile().getCanonicalPath());

						jTabs.add(new EditorText("", open.getSelectedFile()), open.getSelectedFile().getName(),
								position);
						jTabs.setSelectedIndex(position);

					} catch (IOException e) {
						terminal_txt.append(String.valueOf(e));
					}
				}
			}

		});

		JMenuItem guardar = new JMenuItem("Guardar");
		guardar.setIcon(new ImageIcon(AritIDE.class.getResource("/ide/iconos/kazam-countdown.png")));
		mnFile.add(guardar);
		guardar.addActionListener(new ActionListener() {

			// Codigo para guardar

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = rutas.get(jTabs.getSelectedIndex());
				if (file.equals("")) {
					// Se envia a guardar como
					terminal_txt.append(
							saveLike(((EditorText) jTabs.getComponent(jTabs.getSelectedIndex() + 1)).txt.getText()));
				} else {
					try (java.io.FileWriter fw = new java.io.FileWriter(file)) {

						fw.write(((EditorText) jTabs.getComponent(jTabs.getSelectedIndex() + 1)).txt.getText());
						fw.close();

					} catch (IOException e) {
						terminal_txt.append(String.valueOf(e) + "\n");
					}
				}
			}

		});

		JMenuItem guardarComo = new JMenuItem("Guardar como");
		guardarComo.setIcon(new ImageIcon(AritIDE.class.getResource("/ide/iconos/kazam-recording.png")));
		mnFile.add(guardarComo);
		guardarComo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				terminal_txt.append(
						saveLike(((EditorText) jTabs.getComponent(jTabs.getSelectedIndex() + 1)).txt.getText()));

			}
		});

		JMenu mnRun = new JMenu("Analisis");
		menuBar.add(mnRun);

		JMenuItem ejecutar = new JMenuItem("Ejecutar");
		ejecutar.setIcon(new javax.swing.ImageIcon(AritIDE.class.getResource("/ide/iconos/kt-start.png"))); // NOI18N
		ejecutar.setText("Ejecutar");
		ejecutar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Codigo para ejecutar

				java.io.Reader reader = new java.io.StringReader(
						((EditorText) jTabs.getComponentAt(jTabs.getSelectedIndex())).txt.getText());
				syntax.LexicoAscendente lexer = new syntax.LexicoAscendente(reader);
				syntax.SintacticoAscendente parser = new syntax.SintacticoAscendente(lexer);

				try {
					parser.parse();
					new ast.Graficador(parser.root);
				} catch (Exception e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
	    }
	});
	mnRun.add(ejecutar);

	JMenu mnNewMenu = new JMenu("Reportes");
	mnNewMenu.setIcon(new ImageIcon(AritIDE.class.getResource("/ide/iconos/ajuda-sobre.png")));
	mnRun.add(mnNewMenu);

	JMenuItem mntmReportes = new JMenuItem("Errores");
	mntmReportes.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	});
	mnNewMenu.add(mntmReportes);

	JMenuItem mntmNewMenuItem = new JMenuItem("Simbolos");
	mntmNewMenuItem.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	});
	mnNewMenu.add(mntmNewMenuItem);

	JMenuItem mntmNewMenuItem_1 = new JMenuItem("AST");
	mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	});
	mnNewMenu.add(mntmNewMenuItem_1);

	JMenuItem mntmNewMenuItem_2 = new JMenuItem("Graficas");
	mntmNewMenuItem_2.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	});
	mntmNewMenuItem_2.setIcon(new ImageIcon(AritIDE.class.getResource("/ide/iconos/kazam-countdown.png")));
	mnRun.add(mntmNewMenuItem_2);

	JMenu mnHelp = new JMenu("Ayuda");
	menuBar.add(mnHelp);

	JMenuItem mntmdaniimor = new JMenuItem("v1.0.0");
	mnHelp.add(mntmdaniimor);

	JMenuItem menuItem = new JMenuItem("201314810");
	mnHelp.add(menuItem);

	GroupLayout groupLayout = new GroupLayout(frmAritIDE.getContentPane());
	groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
		.createSequentialGroup().addContainerGap()
		.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
			.addComponent(terminal, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
			.addComponent(jTabs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
		.addContainerGap()));
	groupLayout
		.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap()
					.addComponent(jTabs, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED).addComponent(terminal,
						GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addContainerGap()));

	frmAritIDE.getContentPane().setLayout(groupLayout);
    }

    // Codigo para guardar como

    private String saveLike(String txt) {
	JFileChooser chooser = new JFileChooser();
	int seleccion = chooser.showSaveDialog(null);
	if (seleccion == JFileChooser.APPROVE_OPTION) {
	    try (java.io.FileWriter fw = new java.io.FileWriter(chooser.getSelectedFile())) {
		fw.write(txt);
		fw.close();
		
		int posicion = jTabs.getSelectedIndex();
		rutas.add(posicion, chooser.getSelectedFile().getCanonicalPath());
		jTabs.setTitleAt(posicion, chooser.getSelectedFile().getName());
		jTabs.repaint();
	    } catch (IOException e) {
		return String.valueOf(e) + "\n";
	    }
	}
	return "";
    }
}
