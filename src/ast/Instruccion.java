package ast;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;

import sym_table.Instancia;
import sym_table.Tabla_Instancias;

public class Instruccion implements NodoAST {
	private final java.util.List<NodoAST> hijos;
	private int type;
	public String log;

	public Instruccion(){
		hijos = new java.util.ArrayList<NodoAST>();
		type = TYPE.SCOPE.ordinal();
		log = "";
	}

	@Override
	public void add(Object o) {
		hijos.add((NodoAST)o);
	}

	@Override
	public Object execute(sym_table.Tabla_Instancias ambito) {
		Object e1, e2;
		Instancia ins;
		int x = 0;

		switch (this.getType()) {
			case TYPE.ADD.ordinal():
				return null;
			case TYPE.SUB.ordinal():
				return null;
			case TYPE.MUL.ordinal():
				return null;
			case TYPE.DIV.ordinal():
				return null;
			case TYPE.POT.ordinal():
				return null;
			case TYPE.INC_OP.ordinal():
				return null;
			case TYPE.DEC_OP.ordinal():
				return null;
			case 2013:
				// Obtener valor de arreglo de objetos
				return null;
			case TYPE.RETURN.ordinal():
				return null;
			case TYPE.EXISTS.ordinal():
				return null;
			case TYPE.GTHAN.ordinal():
				return null;
			case TYPE.GE_OP.ordinal():
				return null;
			case TYPE.LTHAN.ordinal():
				return null;
			case TYPE.LE_OP.ordinal():
				return null;
			case TYPE.EQUAL.ordinal():
				return null;
			case TYPE.INVERT.ordinal():
				return null;
			case TYPE.AND.ordinal():
				return null;
			case TYPE.OR.ordinal():
				return null;
			case TYPE.SWITCH.ordinal():
				return null;
			case TYPE.IF.ordinal():
				return null;
			case TYPE.IF_ELSE.ordinal():
				return null;
			case TYPE.TERNARY.ordinal():
				return null;
			case TYPE.DECLARE.ordinal():
				return null;
            case TYPE.PRINT.ordinal():
            	return null;
            case TYPE.SCOPE.ordinal():
            	x=0;
            	e1=null;
            	while(x < hijos.size()){
                    e1 = hijos.get(x++).execute(ambito);
                    if(e1 != null){
                        if(((NodoAST)e1).getType() == TYPE.ERROR.ordinal())
                            //ADD exp to SYM_TABLE Array<Node_types_error>
                            break;
                        return e1;
                    }
                }
            default:
            	log+="ERROR - Nodo aun no definido\n";
            	return null;
        }
	}
	
	public int getSize() {
		return hijos.size();
	}

	@Override
	public void type(int t) {
		type=t;
	}

	@Override
	public String getValue() {
		return log;
		//METODO NO SOPORTADO
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public NodoAST getChild(final int i) {
		if(i<hijos.size())
			return hijos.get(i);
		else
			return null;
	}
}
