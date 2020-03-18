package ast;

public class Expresion implements NodoAST{
	private Object valor;
	private TYPE type;

	Expresion(Object value, TYPE tipo){
		valor=value;
		type=tipo;
	}

	public Expresion(){

	}
	
	@Override
	public void add(Object o) {
		valor= new Object[]{o};
	}

	@Override
	public Object execute(sym_table.Tabla_Instancias ambito) {
		return this;
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
