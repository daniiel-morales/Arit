package ast;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import ast.NodoAST.TYPE;
import sym_table.*;

public class Operaciones {

    public Object DECLARE(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos) {

        NodoAST exp;
        TYPE i;
        Instancia ins;

        // check if instance not exists
        ins = tabla_simbolos.getInstance(String.valueOf(hijos.get(0).execute(tabla_simbolos)));

        // get value for the instance
        exp = ((NodoAST) hijos.get(1).execute(tabla_simbolos));
        // get type for the new instance
        i = exp.getType();
        if (ins == null) {
            // creates de new instance
            ins = new Instancia("" + ((Object[]) ((NodoAST) hijos.get(0)).getValue())[0], exp.getValue(), i);
            tabla_simbolos.addInstance(ins);

            return null;
        }

        // Agregar UPDATE
        // return null

        return new Expresion("DECLARAR>> Ya existe la variable <" + ins.getID() + ">", TYPE.ERROR);
    }

    public Object C(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos) {
        NodoAST exp;
        java.util.ArrayList<Object> result_array = new java.util.ArrayList<Object>();
        TYPE type;

        type = ((NodoAST) hijos.get(1).execute(tabla_simbolos)).getType();

        // delete name of function C (concat)
        hijos.remove(0);
        for (NodoAST temp : hijos) {
            exp = (NodoAST) temp.execute(tabla_simbolos);
            if (exp.getType() == type) {
                for (Object i : (Object[]) exp.getValue())
                    result_array.add(i);
            } else {
                for (Object i : (Object[]) exp.getValue()) {
                    switch (type) {
                        case STRING:
                            result_array.add("" + i);
                            break;
                        case FLOAT:
                            result_array.add(castTo(i, 0.0));
                            break;
                        case NUM:
                            result_array.add(castTo(i, 0));
                            break;
                        default:
                            System.err.println("ARIT>> aun no casteable");
                            break;
                    }
                }
            }
        }
        return new Expresion(result_array.toArray(), type);
    }

    public void PIE(NodoAST x, NodoAST label, NodoAST main, Tabla_Instancias tabla_simbolos) {
        Object valores[] = (Object[]) ((NodoAST) x.execute(tabla_simbolos)).getValue();
        Object etiquetas[] = (Object[]) ((NodoAST) label.execute(tabla_simbolos)).getValue();
        String title = ((Object[])((NodoAST) main.execute(tabla_simbolos)).getValue())[0].toString();

        DefaultPieDataset dataset = new DefaultPieDataset();
        if(etiquetas.length>=valores.length)
            // More or Equal labels per amaunt of labels values
            for(int i=0; i<etiquetas.length; i++)
                if(i<valores.length)
                    dataset.setValue(""+etiquetas[i], castTo(valores[i], 0.0));
                else
                    dataset.setValue(""+etiquetas[i], 0);
        else
            // More or Equal values per amaunt of labels
            for(int i=0; i<valores.length; i++)
                if(i<etiquetas.length)
                    dataset.setValue(""+etiquetas[i], castTo(valores[i], 0.0));
                else
                    dataset.setValue("Etiqueta"+i, castTo(valores[i], 0.0));

        JFreeChart chart = ChartFactory.createPieChart(title, // chart title
                dataset, // data
                true, // include legend
                true, false);

        // New popup Frame for PIE graph
        
        javax.swing.JFrame pie = new javax.swing.JFrame();
        pie.setMinimumSize(new java.awt.Dimension(250, 300));
        pie.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(
                            Graficador.class.getResource("/ide/iconos/boot.png")));
        pie.setTitle("AritIDE - danii_mor");
        pie.setLocationRelativeTo(null);

        pie.setContentPane(new ChartPanel( chart ));
        pie.setVisible(true);

        // IF you want the JPG instead

        /*int width = 640; // Width of the image
        int height = 480; // Height of the image
        java.io.File pieChart = new java.io.File("./dot/pie.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);
        } catch (java.io.IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
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