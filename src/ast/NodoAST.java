package ast;

public interface NodoAST {
	public void add(Object o);
	public Object execute(sym_table.Tabla_Instancias ambito);
	public void type(TYPE t);
	public Object getValue();
	public TYPE getType();
	public NodoAST getChild(int i);
	public int getSize();
	public enum TYPE{
		//leaf
		ID, 
		NUM, 
		CHAR,
		BOOLEAN, 
		STRING,
		NULL,
		ERROR, 
		BREAK,
		//root
		SWITCH,
		CASE,
		DEFAULT,
		DECLARE,
		IF, 
		ELSE, 
		IF_ELSE,
		PRINT,
		ADD,
		SUB,
		MUL,
		DIV,
		POT,
		MOD,
		SCOPE,
		INC_OP,
		DEC_OP,
		RETURN,
		EXISTS,
		GTHAN,
		GE_OP,
		LTHAN,
		LE_OP,
		EQUAL,
		INVERT,
		AND,
		OR,
		TERNARY,
		WHILE,
		DO,
		CONTINUE,
		FLOAT,
		FUNCTION,
		NOEQUAL,
		CALL;
	}
}
