package ast;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Graficador 
{
    int contador;

    // Llama todo lo necesario para graficar AST
    public Graficador(NodoAST raiz)
    {
        // Creo una carpeta en /dot
        File folder = new File("." + File.separator +"dot");        
        if(!folder.exists())
            folder.mkdirs();
        
        
        // Rutas para el .dot y la imagen .png
        String ruta_dot = "." + File.separator +"dot"+File.separator+"ast.dot"; 
        String ruta_png = "." + File.separator +"dot"+File.separator+"ast.png"; 
        
        // Arma el contenido del .dot
        this.armar_Cuerpo_dot(raiz, ruta_dot);        
        
        // Genera su imagen .png a partir del archivo .dot
        //this.crearGrafo(ruta_dot, ruta_png);
        
        // Abre rutas de archivo automatico
        this.autoAbrir(ruta_dot);        
    }
        
    // Genera imagenes a partir de archivos .dot
    private void crearGrafo(String ruta_dot, String ruta_png){
        String tParam = "-Tpng";    
        String tOParam = "-o";        
        
        String[] cmd = new String[5]; 
        
        cmd[0] = File.separator +"graphviz_port"+File.separator+"dot.exe";
        cmd[1] = tParam;    
        cmd[2] = ruta_dot;
        cmd[3] = tOParam;   
        cmd[4] = ruta_png;
        Runtime rt = Runtime.getRuntime();
        
        try {
            //Hace la llamada al sistema y ejecuta la variable cmd
            rt.exec( cmd );                                    
        } 
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    // Abre un fichero, archivo, etc. en base a la ruta
    private void autoAbrir(String ruta){
        try{
            File archivo = new File(ruta);
            if(archivo.exists())
                Desktop.getDesktop().open(archivo);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    // Toma la raiz de un NodoAST para crear el .dot
    private void armar_Cuerpo_dot(NodoAST raiz, String ruta_dot){
        contador=0;
        StringBuffer buffer=new StringBuffer();
        buffer.append("\ndigraph G {\r\nnode [style=filled, fontcolor=white];	graph [bgcolor=\"#FFFFFFF\"]; edge [color=grey];\n");        
        this.listarNodos(raiz, buffer);
        this.enlazarNodos(raiz, buffer,0);        
        buffer.append("}");
        this.crearArchivo(ruta_dot, buffer.toString());
    }
    
    // Necesita un NodoAST para listar todos los nodos que existiran en el .dot
    private void listarNodos(NodoAST praiz, StringBuffer buffer)
    {        
        if(praiz.getChild(0)!=null)
            if(!praiz.getValue().equals(""))
                buffer.append("node").append(contador++).append("[color=brown4, shape=house, label=\"").append(praiz.getValue()).append("\"];\n");
            else
                buffer.append("node").append(contador++).append("[color=brown4, shape=house, label=\"").append(praiz.getType()).append("\"];\n");
        else
            buffer.append("node").append(contador++).append("[color=darkolivegreen, shape=egg, label=\"").append(praiz.getValue()).append("\"];\n");
        
        int x=0,y=0;
        NodoAST hijo = praiz.getChild(y++);
        NodoAST padre = praiz;

        while(padre!=null){
            while(hijo!=null){
                listarNodos(hijo,buffer);
                hijo = padre.getChild(y++);
            }
            padre = padre.getChild(x++);
        }
    }    
    
    // Necesita un NodoAST para crear dependencias entre nodos en el .dot
    int x=1;
    private void enlazarNodos(NodoAST praiz, StringBuffer bufffer,int cpadre)
    {
        int y=0;
        NodoAST hijo = praiz.getChild(y++);
        NodoAST padre = praiz;
        
         while(hijo!=null){        
            String relacion="node"+cpadre+"->";
            relacion+="node"+x+";\n";
            bufffer.append(relacion);
            enlazarNodos(hijo, bufffer,x++);
            hijo = padre.getChild(y++);
        }
    } 

    // crea un archivo plano en la ruta y con el contenido recibido
    public synchronized void crearArchivo(String pfichero,String pcontenido)
    {   
        FileWriter archivo = null;
   
        try{
            archivo = new FileWriter(pfichero);

            File a = new File(pfichero);        
            if (a.exists()){
                PrintWriter printwriter = new PrintWriter(archivo);
                printwriter.print(pcontenido);
                printwriter.close();
            }  
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
