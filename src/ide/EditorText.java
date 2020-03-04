package ide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import jsyntaxpane.DefaultSyntaxKit;

/*
 * danii_mor
 */

public class EditorText extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private javax.swing.JLabel lcaret;
    private javax.swing.JScrollPane scroll;
    public javax.swing.JEditorPane txt;

    public EditorText() {
	initComponents();
	DefaultSyntaxKit.initKit();
	txt.setContentType("text/java");
    }

    public EditorText(String type, File file) {
	initComponents();
	DefaultSyntaxKit.initKit();
	txt.setContentType(type);
	cargarArchivo(file);
    }

    private void initComponents() {
	scroll = new javax.swing.JScrollPane();
	txt = new javax.swing.JEditorPane();
	lcaret = new javax.swing.JLabel();

	txt.addCaretListener(new javax.swing.event.CaretListener() {
	    public void caretUpdate(javax.swing.event.CaretEvent evt) {
		try {
		    int caretPos = txt.getCaretPosition();
		    int rowNum = (caretPos == 0) ? 1 : 0;
		    for (int offset = caretPos; offset > 0;) {
			offset = Utilities.getRowStart(txt, offset) - 1;
			rowNum++;
		    }
		    int offset = Utilities.getRowStart(txt, caretPos);
		    int colNum = caretPos - offset + 1;
		    lcaret.setText("Linea: " + rowNum + "       Columna: " + colNum);
		} catch (BadLocationException ex) {
		    Logger.getLogger(AritIDE.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	});

	lcaret.setFont(new java.awt.Font("Tahoma", 1, 12));
	lcaret.setText("Linea: 1       Columna: 1");
	scroll.setViewportView(txt);

	javax.swing.GroupLayout contenedorLayout = new javax.swing.GroupLayout(this);
	this.setLayout(contenedorLayout);
	contenedorLayout.setHorizontalGroup(contenedorLayout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
		.addGroup(contenedorLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(lcaret)));
	contenedorLayout.setVerticalGroup(contenedorLayout
		.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		.addGroup(contenedorLayout.createSequentialGroup()
			.addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lcaret,
				javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private void cargarArchivo(File file) {
	try {
	    BufferedReader bufferreader;
	    bufferreader = new BufferedReader(new FileReader(file.getAbsolutePath()));
	    String linea, contenido = "";
	    // leendo linea a linea
	    while ((linea = bufferreader.readLine()) != null) {
		contenido += linea + "\n";
	    }
	    txt.setText(contenido);
	    bufferreader.close();
	} catch (IOException ex) {
	    Logger.getLogger(EditorText.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
