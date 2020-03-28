package ast;

import java.awt.Color;
import java.awt.BasicStroke;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ast.NodoAST.TYPE;
import sym_table.*;

public class Operaciones {

    public Object DECLARE(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos) {

        NodoAST exp;
        TYPE i;
        Instancia ins;

        // check if instance not exists
        ins = tabla_simbolos.getInstance(String.valueOf(((Object[])hijos.get(0).getValue())[0]));

        // get value for the instance
        exp = ((NodoAST) hijos.get(1).execute(tabla_simbolos));
        // get type for the new instance
        i = exp.getType();
        if (ins == null) {
            // creates de new instance
            ins = new Instancia("" + ((Object[]) ((NodoAST) hijos.get(0)).getValue())[0], exp.getValue(), i);
            if(tabla_simbolos.addInstance(ins)){
                return null;
            }

            return new Expresion("ERROR>> declaracion <"+ins.getID()+">", TYPE.ERROR);
        }else{
            // UPDATE value to instance
            ins.setValue(exp.getValue());
            ins.setType(exp.getType());
            return null;
        }
    }

    public Object CALL(java.util.List<NodoAST> hijos, Tabla_Instancias ambito){
        NodoAST e1,e2;
        e1 = hijos.get(0);
        String caso = ((Object[])e1.getValue())[0].toString().toLowerCase();
        switch(caso){
            case "c":
                return C(hijos, ambito);
            case "matrix":
                e1 = (NodoAST)hijos.get(1).execute(ambito);
                e2 = (NodoAST)hijos.get(2).execute(ambito);

                return MATRIX(e1, // values
                                e2, // nRows
                                (NodoAST)hijos.get(3).execute(ambito)); //nCol
            case "pie":
                e1 = hijos.get(1);
                e2 = hijos.get(2);
                PIE(e1, e2, hijos.get(3), ambito);
                return null;
            case "barplot":
                e1 = (NodoAST)hijos.get(2).execute(ambito); 
                e2 = (NodoAST)hijos.get(3).execute(ambito); 
                BARPLOT(hijos.get(1), // H
                            ((Object[])e1.getValue())[0], // Xaxis TAG
                            ((Object[])e2.getValue())[0], // Yaxis TAG
                            ((Object[])((NodoAST)hijos.get(4).execute(ambito)).getValue())[0], // Title
                            hijos.get(5),// names
                            ambito);
                return null;
            case "plot":
                e1 = (NodoAST) hijos.get(1).execute(ambito);
                java.util.List<NodoAST> nuevos_hijos = new java.util.ArrayList<NodoAST>();
                nuevos_hijos.add(new Instruccion());
                nuevos_hijos.add(new Expresion(e1.getValue(), e1.getType()));
                e1 = (NodoAST) new Operaciones().C(nuevos_hijos, ambito);
                e2 = (NodoAST) hijos.get(5).execute(ambito);
                if(((Object[])e2.getValue()).length>1)
                    // DIAGRAMA DE DISPERSION
                    PLOT(e1, // MAT
                            (NodoAST)hijos.get(2).execute(ambito), // Xaxis TAG
                            (NodoAST)hijos.get(3).execute(ambito), // Yaxis TAG
                            (NodoAST)hijos.get(4).execute(ambito), // Title
                            e2, // Ylim
                            0); 
                else{
                    // GRAFICA DE LINEA
                    String tipo_plot = (((Object[])((NodoAST)hijos.get(2).execute(ambito)).getValue())[0]).toString();
                    switch(tipo_plot.toLowerCase()){
                        case "p":
                            tipo_plot="0";
                            break;
                        case "i":
                            tipo_plot="1";
                            break;
                        case "o":
                            tipo_plot="2";
                            break;
                        default:
                            return new Expresion("ARIT>> Ese tipo de PLOT no existe", TYPE.ERROR);
                    }
                    PLOT(e1, // V
                            (NodoAST)hijos.get(3).execute(ambito), // Xaxis TAG
                            (NodoAST)hijos.get(4).execute(ambito), // Yaxis TAG
                            (NodoAST)hijos.get(5).execute(ambito), // Title
                            null,
                            Integer.valueOf(tipo_plot));
                }						
                return null;
            case "hist":
                e1 = (NodoAST)hijos.get(2).execute(ambito); 
                e2 = (NodoAST)hijos.get(3).execute(ambito);
                HIST(hijos.get(1), // V
                        ((Object[])e1.getValue())[0], // Xaxis TAG
                        ((Object[])e2.getValue())[0], // Title
                        ambito);
                return null;
            default:
                // TODO: call any function created by the user
                return null;
        }
    }

    public void PRINT(NodoAST e1, Tabla_Instancias ambito){
        int object_type = 3;

        // identify structure of Expresion
        for(Object i : (Object[]) e1.getValue()){
            if(i instanceof Object[]){
                if((Object[]) i instanceof Object[]){
                    // ARRAY
                    object_type = 0;
                    break;
                }else{
                    // MATRIZ
                    object_type = 1;
                }
            }else{
                if(object_type==1){
                    //LISTA
                    object_type = 2;
                    break;
                }
            }
        }
        switch(object_type){
            case 0:
            break;
            case 1:
            break;
            case 2:
            break;
            case 3:
                ambito.forTerminal(""+ java.util.Arrays.toString(
                                                java.util.Arrays.stream(
                                                    (Object[])e1.getValue())
                                                    .map(Object::toString)
                                                    .toArray(String[]::new)));
            break;
        }
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
                    if(i instanceof Object[]){
                        java.util.List<NodoAST> nuevos_hijos = new java.util.ArrayList<NodoAST>();
                        nuevos_hijos.add(new Instruccion());
                        nuevos_hijos.add(new Expresion(i, exp.getType()));
                        i = ((NodoAST)C(nuevos_hijos, tabla_simbolos)).getValue();

                        for(Object iterator : (Object[])i){
                            result_array.add(iterator);
                        }

                    }else
                        result_array.add(i);
            } else {
                for (Object i : (Object[]) exp.getValue()) {
                    if(i instanceof Object[]){
                        java.util.List<NodoAST> nuevos_hijos = new java.util.ArrayList<NodoAST>();
                        nuevos_hijos.add(new Instruccion());
                        nuevos_hijos.add(new Expresion(i, exp.getType()));
                        i = ((NodoAST)C(nuevos_hijos, tabla_simbolos)).getValue();

                        for(Object iterator : (Object[])i){
                            result_array.add(iterator);
                        }
                    }
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
                    dataset.setValue("Desconocido"+i, castTo(valores[i], 0.0));

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

    public void BARPLOT(NodoAST H, Object xlabel, Object ylabel, Object main, NodoAST names, Tabla_Instancias tabla_simbolos){
        Object valores[] = (Object[]) ((NodoAST) H.execute(tabla_simbolos)).getValue();
        Object etiquetas[] = (Object[]) ((NodoAST) names.execute(tabla_simbolos)).getValue();
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        if(etiquetas.length>=valores.length)
            // More or Equal labels per amaunt of labels values
            for(int i=0; i<etiquetas.length; i++)
                if(i<valores.length)
                    dataset.addValue(castTo(valores[i], 0.0), "", ""+etiquetas[i]);
                else
                    dataset.addValue(0, "", ""+etiquetas[i]);
        else
            // More or Equal values per amaunt of labels
            for(int i=0; i<valores.length; i++)
                if(i<etiquetas.length)
                    dataset.addValue(castTo(valores[i], 0.0), "", ""+etiquetas[i]);
                else
                    dataset.addValue(castTo(valores[i], 0.0), "", "Desconocido"+i);

        JFreeChart barChart = ChartFactory.createBarChart(main.toString(), // chart Tittle           
                                                            xlabel.toString(),            
                                                            ylabel.toString(),            
                                                            dataset,          
                                                            PlotOrientation.VERTICAL,           
                                                            false, true, false);

        // New popup Frame for BARPLOT graph
        
        javax.swing.JFrame bar = new javax.swing.JFrame();
        bar.setMinimumSize(new java.awt.Dimension(250, 300));
        bar.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(
                            Graficador.class.getResource("/ide/iconos/boot.png")));
        bar.setTitle("AritIDE - danii_mor");
        bar.setLocationRelativeTo(null);

        bar.setContentPane(new ChartPanel( barChart ));
        bar.setVisible(true);
      
    }

    public void PLOT(NodoAST MAT, NodoAST xlabel, NodoAST ylabel, NodoAST main, NodoAST ylim, int graph_type){
        String title = ""+((Object[])main.getValue())[0];
        String Xlbl = ""+((Object[])xlabel.getValue())[0];
        String Ylbl = ""+((Object[])ylabel.getValue())[0];

        
        final XYSeries values = new XYSeries("");
        int x=1;
        for(Object data : ((Object[])MAT.getValue()))
            values.add(x++, castTo(data, 0.0));       
        
        final XYSeriesCollection dataset = new XYSeriesCollection( );          
        dataset.addSeries( values );          

        JFreeChart xylineChart = ChartFactory.createXYLineChart(title ,
                                                                Xlbl,
                                                                Ylbl,
                                                                dataset,
                                                                PlotOrientation.VERTICAL ,
                                                                    false , true , false);
        final XYPlot ploti = xylineChart.getXYPlot( );
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );

        switch(graph_type){
            case 0:
                renderer.setSeriesStroke( 0 , new BasicStroke(0));
                ploti.setRenderer( renderer );
                break;
            case 1:
                // NO renderer need it
                break;
            case 2:
                renderer.setSeriesStroke( 0 , null);
                ploti.setRenderer( renderer );
                break;
            default:
                throw new UnsupportedOperationException("ARIT>> Ese tipo de PLOT no existe");
        }
        
         


        // New popup Frame for PLOT graph
            
        javax.swing.JFrame plot = new javax.swing.JFrame();
        plot.setMinimumSize(new java.awt.Dimension(250, 300));
        plot.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(
                            Graficador.class.getResource("/ide/iconos/boot.png")));
        plot.setTitle("AritIDE - danii_mor");
        plot.setLocationRelativeTo(null);

        plot.setContentPane(new ChartPanel( xylineChart ));
        plot.pack( );

        plot.setVisible(true);
        
    }

    public void HIST(NodoAST V, Object xlabel, Object main, Tabla_Instancias tabla_simbolos){
        java.util.List<Object> valores =  new java.util.ArrayList<Object>(java.util.Arrays.asList((Object[]) ((NodoAST) V.execute(tabla_simbolos)).getValue()));
       
        HistogramDataset dataset = new HistogramDataset();        
        double valores_casteados[] = new double[valores.size()];
        int i=0;
        for(Object v : valores){
            valores_casteados[i++] = castTo(v, 0.0);
        }
        dataset.addSeries("Histogram",valores_casteados,valores.size()%2==1?(valores.size()+1)/2:valores.size()/2);  
       JFreeChart chart = ChartFactory.createHistogram(main.toString(), // chart Tittle           
                                                        xlabel.toString(),            
                                                        "Frecuencia",            
                                                        dataset,          
                                                        PlotOrientation.VERTICAL,           
                                                        false, true, false);

        // New popup Frame for HIST graph
        
        javax.swing.JFrame hist = new javax.swing.JFrame();
        hist.setMinimumSize(new java.awt.Dimension(250, 300));
        hist.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(
                            Graficador.class.getResource("/ide/iconos/boot.png")));
        hist.setTitle("AritIDE - danii_mor");
        hist.setLocationRelativeTo(null);

        hist.setContentPane(new ChartPanel( chart ));
        hist.setVisible(true);

    }
    
    public Object MATRIX(NodoAST data, NodoAST nrow, NodoAST ncol){
        int rows = castTo(((Object[])nrow.getValue())[0],0);
        int cols = castTo(((Object[])ncol.getValue())[0],0);

        Object values[] = (Object[]) data.getValue();
        java.util.List<Object> matriz = new java.util.ArrayList<Object>();
        java.util.List<Object> columns;

        int c =0;
        for (int i = 0; i < cols; i++) {
            columns = new java.util.ArrayList<Object>();
            for (int j = 0; j < rows; j++) {
                if(c == values.length)
                    c=0;
                columns.add(values[c++]);
            }
            matriz.add(columns.toArray());
        }
        return new Expresion(matriz.toArray(), TYPE.NUM);
    }

    private Integer castTo(Object exp, int type){
        return Integer.valueOf(String.valueOf(exp));
    }

    private Double castTo(Object exp, double type){
        return Double.valueOf(String.valueOf(exp));
    }

    private Boolean castTo(Object exp, boolean type){
        return Boolean.parseBoolean(String.valueOf(exp));
    }

    public Object FOR(NodoAST statement, NodoAST iterator, NodoAST array, Tabla_Instancias tabla_simbolos){
        // make lineal all structures
        java.util.List<NodoAST> nuevos_hijos = new java.util.ArrayList<NodoAST>();
        nuevos_hijos.add(new Instruccion());
        nuevos_hijos.add(new Expresion(array.getValue(), array.getType()));
        Object array_maped[] = (Object[])((NodoAST)C(nuevos_hijos, tabla_simbolos)).getValue();

        for(Object i : array_maped){
            // extracts value and adds to the iterator DECLARE Instruction
            NodoAST declare_iterator = new Instruccion();
            declare_iterator.type(TYPE.DECLARE);
            declare_iterator.add(iterator.getChild(0));
            declare_iterator.add(new Expresion(new Object[]{i}, array.getType()));
            declare_iterator.execute(tabla_simbolos);

            // executes statement SCOPE
            i = statement.execute(tabla_simbolos);
            if(i != null){
                if(((NodoAST)i).getType() == TYPE.ERROR)
                    //ADD exp to SYM_TABLE Array<Node_types_error>
                    break;
                return i;
            }
        }
        return null;
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
            exp = new Expresion(new Object[]{castTo(vec[0],0)+castTo(vec2[0],0)}, TYPE.NUM);
        else
            //ERROR SUMA
            exp = new Expresion("ADD>> Error al operar <"+exp.getValue()+"> + <"+exp2.getValue()+">", TYPE.ERROR);
        return exp;
    }

    public Object EQUAL(NodoAST e1, NodoAST e2, Tabla_Instancias ambito){
        int x =0;
        if(e1.getType() == e2.getType()){
            x = ((Object[])e1.getValue()).length;
            if( x == ((Object[])e2.getValue()).length){
                if(x>1){
                    java.util.List<NodoAST> nuevos_hijos = new java.util.ArrayList<NodoAST>();
                    nuevos_hijos.add(new Instruccion());
                    nuevos_hijos.add(e1);
                    Object izq[] = (Object[])((NodoAST)new Operaciones().C(nuevos_hijos, ambito)).getValue();
                
                    nuevos_hijos = new java.util.ArrayList<NodoAST>();
                    nuevos_hijos.add(new Instruccion());
                    nuevos_hijos.add(e2);
                    Object der[] = (Object[])((NodoAST)new Operaciones().C(nuevos_hijos, ambito)).getValue();
                    
                    for (int i = 0; i < der.length; i++)
                        if(izq[i]!=der[i])
                            return new Expresion(new Object[]{false},TYPE.BOOLEAN);

                    return new Expresion(new Object[]{true},TYPE.BOOLEAN);
                }
                else if(((Object[])e1.getValue())[0].equals(((Object[])e2.getValue())[0]))
                    return new Expresion(new Object[]{true},TYPE.BOOLEAN);
                
            }
        }
        return new Expresion(new Object[]{false},TYPE.BOOLEAN);
    }

    public Object IF(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos){

        Object exp, exp2;
        int i = 0;

        //execute IFnode condition
        exp = hijos.get(0).execute(tabla_simbolos);
        exp2 = null;

        if (((NodoAST)exp).getType() == TYPE.BOOLEAN) {
            if (castTo(((Object[])((NodoAST)exp).getValue())[0], true)) {
                exp = hijos.get(1).execute(tabla_simbolos);
                if(exp != null)
                    return exp;
                return new Expresion("", TYPE.BREAK);
            } else {
                i = 2;
                while (i < hijos.size()){
                    //execute else-if
                    exp2 = hijos.get(i++).execute(tabla_simbolos);
                    
                    if(exp2 != null){
                        //one of all if-else enter successfully
                        //just verify if it has RETURNnode value
                        if(((NodoAST)exp2).getType() == TYPE.BREAK)
                            return null;
                        return exp2;
                    }
                }
                //finish IF-ELSEnode execution
                return null;
            }
        } else
            return new Expresion("IF>> condicion <" + ((NodoAST)exp).getValue() + "> no booleana", TYPE.ERROR);
    }
    
    public Object SWITCH(java.util.List<NodoAST> hijos, Tabla_Instancias tabla_simbolos){

        Object exp, exp2;
        int i = 0;

        // value for match CASEnode
        exp = hijos.get(0).execute(tabla_simbolos);
        if (((NodoAST)exp).getType().ordinal() < 6) {
            i = 1;
            while (i < hijos.size()) {
                // value for match SWITCHnode value
                exp2 = hijos.get(i);
                if (((NodoAST)exp2).getType() != TYPE.DEFAULT){
                    //NO evaluate CASEnode match value
                    //it has matched in a previous CASEnode without BREAKnode
                    if(castTo(exp,true) != true)
                        exp2 = ((NodoAST)exp2).getChild(0).execute(tabla_simbolos);

                    if (castTo(exp,true) == true || ((Object[])((NodoAST)exp).getValue())[0] == ((Object[])((NodoAST)exp2).getValue())[0]) {
                        //Executes CASEnode SCOPE
                        exp2 = hijos.get(i).getChild(1).execute(tabla_simbolos);
                        if (exp2 != null) {
                            if (((NodoAST)exp2).getType() == TYPE.BREAK)
                                return null;
                            return exp2;
                        }
                        // NO break so can execute the next cases without evaluate it
                        exp = true;
                    }
                //only for languajes that DEFAULTnode is the final case option
                }else{
                    exp2 = hijos.get(i).getChild(0).execute(tabla_simbolos);
                    if (exp2 != null) {
                        if (((NodoAST)exp2).getType() == TYPE.BREAK)
                            return null;
                        return exp2;
                    }
                    //exits SWITCH even if exists more CASEnodes of SWITCH
                    break;
                }
                i++;
            }
        }else
            return new Expresion("SWITCH>> valor <"+((NodoAST)exp).getValue()+"> no primitivo", TYPE.ERROR);
        return null;
    }
}