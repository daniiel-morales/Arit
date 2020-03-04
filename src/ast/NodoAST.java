package ast;

public interface NodoAST {
	public void add(NodoAST n);
	public Object execute(NodoAST n);
	public void value(Object lex);
	public void type(char c);
	public Object getValue();
	public char getType();
	public NodoAST getChild(int i);
	public int getSize();
}
