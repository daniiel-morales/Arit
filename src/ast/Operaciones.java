package ast;

import ast.NodoAST.TYPE;
import sym_table.*;

public class Operaciones{
    
    public Object DECLARE(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos){

        NodoAST exp;
        TYPE i;
        Instancia ins;

        //check if instance not exists
        ins = tabla_simbolos.getInstance(String.valueOf(hijos.get(0).execute(tabla_simbolos)));

        //get value for the instance
        exp = ((NodoAST)hijos.get(1).execute(tabla_simbolos));
        //get type for the new instance
        i = exp.getType();
        if(ins == null){
            //creates de new instance
            ins = new Instancia(""+((Object[])((NodoAST)hijos.get(0)).getValue())[0], exp.getValue(), i);
            tabla_simbolos.addInstance(ins);

            return null;
        }

        // Agregar UPDATE
        // return null

        return new Expresion("DECLARAR>> Ya existe la variable <"+ins.getID()+">", TYPE.ERROR);
    }

    private Integer castTo(Object exp, int type){
        return Integer.valueOf(String.valueOf(exp));
    }

    private Double castTo(Object exp, double type){
        return Double.valueOf(String.valueOf(exp));
    }

    public NodoAST ADD(NodoAST exp, NodoAST exp2){
        Object vec[] = (Object[]) exp.getValue();
        Object vec2[] = (Object[]) exp2.getValue();
        if (exp == null || exp2 == null || exp.getType() == TYPE.NULL || exp2.getType() == TYPE.NULL)
            exp = null;
        else if ((exp.getType() == TYPE.STRING || exp2.getType() == TYPE.STRING) && (vec.length==1 || vec2.length==1 || vec.length == vec2.length)){
            int x = 0;
            int y = 0;
            int c=0;
            int max_leng = vec.length>vec2.length ? vec.length: vec2.length;
            Object result[]=new Object[max_leng];
            while(c<max_leng){
                if(vec[c] instanceof Object[]){
                    // agregar recorrer listas
                }else{
                    if(x==vec.length)
                        x=0;
                    if(y==vec2.length)
                        y=0;

                    result[c++] = ""+vec[x++]+vec2[y++];
                }
            }
            exp = new Expresion(result, TYPE.STRING); 
        }else if((exp.getType() == TYPE.NUM && exp2.getType() == TYPE.NUM))
            exp = new Expresion(castTo(exp.getValue(),0)+castTo(exp2.getValue(),0), TYPE.NUM);
        else
            //ERROR SUMA
            exp = new Expresion("ADD>> Error al operar <"+exp.getValue()+"> + <"+exp2.getValue()+">", TYPE.ERROR);
        return exp;
    }

/*    IF(hijos:Array<Nodo>, tabla_simbolos:sym_table):any{

        let exp: any, exp2: any
        let i: number = 0

        //execute IFnode condition
        exp = hijos[0].execute(tabla_simbolos)
        exp2 = null;

        if (exp.getType() == TYPES.BOOLEAN) {
            if (exp.getValue()) {
                exp = hijos[1].execute(tabla_simbolos)
                if(exp != null)
                    return exp
                return new Expresion("", TYPES.BREAK)
            } else {
                i = 2
                while (i < hijos.length){
                    //execute else-if
                    exp2 = hijos[i++].execute(tabla_simbolos)
                    
                    if(exp2 != null){
                        //one of all if-else enter successfully
                        //just verify if it has RETURNnode value
                        if(exp2.getType() == TYPES.BREAK)
                            return null
                        return exp2
                    }
                }
                //finish IF-ELSEnode execution
                return null
            }
        } else
            return new Expresion('IF>> condicion <' + exp.getValue() + '> no booleana', TYPES.ERROR)
    }

    ELSE(exp: Expresion):any{
        //returns RETURN value
        if (exp != null)
            return exp
        
        return new Expresion('', TYPES.BREAK)
    }
    
    SWITCH(hijos:Array<Nodo>, tabla_simbolos:sym_table): any{

        let exp: any, exp2: any
        let i: number = 0

        // value for match CASEnode
        exp = hijos[0].execute(tabla_simbolos);
        if (exp.getType() < 6) {
            i = 1;
            while (i < hijos.length) {
                // value for match SWITCHnode value
                exp2 = hijos[i];
                if (exp2.getType() != TYPES.DEFAULT){
                    //NO evaluate CASEnode match value
                    //it has matched in a previous CASEnode without BREAKnode
                    if(exp != true)
                        exp2 = exp2.getChild(0).execute(tabla_simbolos)

                    if (exp == true || exp.getValue() == exp2.getValue()) {
                        exp2 = hijos[i].getChild(1).execute(tabla_simbolos)
                        if (exp2 != null) {
                            if (exp2.getType() == TYPES.BREAK)
                                return null
                            return exp2
                        }
                        // NO break so can execute the next cases without evaluate it
                        exp = true
                    }
                //only for languajes that DEFAULTnode is the final case option
                }else{
                    exp2 = hijos[i].getChild(0).execute(tabla_simbolos)
                    if (exp2 != null) {
                        if (exp2.getType() == TYPES.BREAK)
                            return null
                        return exp2
                    }
                    //exits SWITCH even if exists more CASEnodes of SWITCH
                    break
                }
                i++
            }
        }else
            return new Expresion('SWITCH>> valor <'+exp.getValue()+'> no primitivo', TYPES.ERROR)
        return null
    }
*/

}