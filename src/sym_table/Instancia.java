package sym_table;

import ast.NodoAST.TYPE;

public class Instancia {
	private String identificador;
	private Object valor;
	private TYPE tipo;
	
	public Instancia(){
	}
	
	public Instancia(String id, Object value, TYPE type){
		identificador=id;
		valor=value;
		tipo=type;
	}
	
	public String getID(){
		return identificador;
	}
	public Object getValue(){
		return valor;
	}
	public TYPE getType(){
		return tipo;
	}
	public void setID(String id){
		identificador=id;
	}
	public void setValue(Object o){
		valor=o;
	}
	public void setType(TYPE c){
		tipo=c;
	}
}
