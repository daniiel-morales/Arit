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
		NodoAST e1;
		final NodoAST e2;
		final Instancia ins;
		int x = 0;

		switch (this.getType()) {
			case ADD:
				e1 = (NodoAST) hijos.get(0).execute(ambito);
				e2 = (NodoAST) hijos.get(1).execute(ambito);
				
				return new Operaciones().ADD(e1, e2);
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
			case BREAK:
				return this;
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
				e1 = (NodoAST)hijos.get(0).execute(ambito);
				e2 = (NodoAST)hijos.get(1).execute(ambito);
				return new Operaciones().EQUAL(e1, e2, ambito);
			case INVERT:
				return null;
			case AND:
				return null;
			case OR:
				return null;
			case SWITCH:
				return new Operaciones().SWITCH(hijos, ambito);
			case IF:
				return new Operaciones().IF(hijos, ambito); 
            case ELSE:
                // execute ELSEnode statements 
				e1 = (NodoAST)hijos.get(0).execute(ambito);
				
				// returns RETURN value
				if (e1 != null)
					return e1;
					
				// finishs IF statement
       			return new Expresion("BREAK", TYPE.BREAK);
			case TERNARY:
				return null;
			case WHILE:
				e1 = (NodoAST) hijos.get(1);
				e2 = (NodoAST) hijos.get(0);
				return new Operaciones().DO(e1, //statement
											e2, //boolean_statement
											ambito); 
			case DO:
				e1 = (NodoAST) hijos.get(0);
				e2 = (NodoAST) hijos.get(1);
				return new Operaciones().DO(e1, //statement
											e2, //boolean_statement
											ambito); 
			case FOR:
				e1 = (NodoAST) hijos.get(2).execute(ambito);
				e2 = (NodoAST) hijos.get(0);
				return new Operaciones().FOR(e2, //statement
												(NodoAST) hijos.get(1), // iterator
												e1, // array
												ambito);
			case CALL:
				return new Operaciones().CALL(hijos, ambito);
			case DECLARE:
				new ast.Operaciones().DECLARE(hijos, ambito);
				return null;
			case PRINT:
				e1 = (NodoAST) hijos.get(0).execute(ambito);
				new Operaciones().PRINT(e1, ambito);
            	return null;
            case SCOPE:
            	x=0;
            	e1=null;
            	while(x < hijos.size()){
                    e1 = (NodoAST) hijos.get(x++).execute(ambito);
                    if(e1 != null){
                        if(e1.getType() == TYPE.ERROR)
                            //ADD exp to SYM_TABLE Array<Node_types_error>
                            break;
                        return e1;
                    }
				}
				return null;
            default:
				ambito.forTerminal("ERROR - Nodo aun no definido\n");
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
