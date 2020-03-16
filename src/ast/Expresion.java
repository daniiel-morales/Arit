package ast;

public class Expresion implements NodoAST{
	private Object valor;
	private TYPE type;
	
	@Override
	public void add(Object o) {
		valor=o;
	}

	@Override
	public Object execute(sym_table.Tabla_Instancias ambito) {
		return valor;
	}

	@Override
	public void type(TYPE c) {
		type=c;
	}

	@Override
	public Object getValue() {
		return valor;
	}

	@Override
	public TYPE getType() {
		return type;
	}

	@Override
	public NodoAST getChild(int i) {
		return null;
	}

	@Override
	public int getSize() {
		return -1;
	}
	
}
