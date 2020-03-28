package ast;

public class Expresion implements NodoAST{
	private Object valor;
	private TYPE type;

	public Expresion(Object value, TYPE tipo){
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
		if(type == TYPE.ID){
			sym_table.Instancia id = ambito.getInstance(((Object[])valor)[0].toString());
			return new Expresion(id.getValue(), id.getType());
		}
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
