package sym_table;

import java.util.Stack;
import java.util.LinkedHashMap;

public class Tabla_Instancias {
    private Stack<LinkedHashMap<String, Instancia>> table;
    private String log="";
	
	public Tabla_Instancias(){
        table = new Stack<LinkedHashMap<String, Instancia>>();
        // adds the index 0 for global scope
        addScope();
    }

    public boolean addInstance(Instancia ins){
        // key value identifier for each instance
        String ins_id = ins.getID();

        //check if exists in local scope
        if(table.peek().get(ins_id) == null){
            // adds or update the instance in the table
            table.peek().put(ins_id, ins);
            return true;
        }
        //check if exists in global scope
        else if(table.get(0).get(ins_id) == null){
            // adds or update the instance in the table
            table.get(0).put(ins_id, ins);
            return true;
        }
        return false;
    }

    public Instancia getInstance(String id){
        Instancia ins;
        // searchs in the last scope
        ins=table.peek().get(id);

        // if not exists returns de value of the result search in the global scope
        if(ins==null)
            return table.get(0).get(id);
        
        return ins;
    }

    public void addScope(){
        table.push(new LinkedHashMap<>());
    }

    public void removeScope(){
        table.pop();
    }

    public void forTerminal(String txt){
        log = txt;
    }
    
    public String getTerminal(){
        return log;
    }
}