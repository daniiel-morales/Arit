package sym_table;

public class Instancia {
	private String identificador;
	private Object valor;
	private int tipo;
	
	public Instancia(){
	}
	
	public Instancia(String id, Object value, char type){
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
	public int getType(){
		return tipo;
	}
	public void setID(String id){
		identificador=id;
	}
	public void setValue(Object o){
		valor=o;
	}
	public void setType(char c){
		tipo=c;
	}
}
