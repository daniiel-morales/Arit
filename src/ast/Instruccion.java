package ast;

import sym_table.Instancia;
import sym_table.Tabla_Instancias;

public class Instruccion implements NodoAST {
	private final java.util.List<NodoAST> hijos;
	private TYPE type;
	public String log;

	public Instruccion(){
		hijos = new java.util.ArrayList<NodoAST>();
		type = TYPE.SCOPE;
		log = "";
	}

	@Override
	public void add(final Object o) {
		hijos.add((NodoAST)o);
	}

	@Override
	public Object execute(final sym_table.Tabla_Instancias ambito) {
		Object e1;
		final Object e2;
		final Instancia ins;
		int x = 0;

		switch (this.getType()) {
			case ADD:
				return null;
			case SUB:
				return null;
			case MUL:
				return null;
			case DIV:
				return null;
			case POT:
				return null;
			case INC_OP:
				return null;
			case DEC_OP:
				return null;
			case RETURN:
				return null;
			case EXISTS:
				return null;
			case GTHAN:
				return null;
			case GE_OP:
				return null;
			case LTHAN:
				return null;
			case LE_OP:
				return null;
			case EQUAL:
				return null;
			case INVERT:
				return null;
			case AND:
				return null;
			case OR:
				return null;
			case SWITCH:
				return null;
			case IF:
				return null;
			case IF_ELSE:
				return null;
			case TERNARY:
				return null;
			case DECLARE:
				return null;
            case PRINT:
            	return null;
            case SCOPE:
            	x=0;
            	e1=null;
            	while(x < hijos.size()){
                    e1 = hijos.get(x++).execute(ambito);
                    if(e1 != null){
                        if(((NodoAST)e1).getType() == TYPE.ERROR)
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
	public void type(TYPE t) {
		type=t;
	}

	@Override
	public String getValue() {
		return log;
		//METODO NO SOPORTADO
	}

	@Override
	public TYPE getType() {
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
