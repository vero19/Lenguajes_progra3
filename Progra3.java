import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class Progra3 {
	static ArrayList listaDatos = new ArrayList();
	public static void main(String [] args) throws IOException{
		File archivo = (new File("codigo.txt"));
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();
		
		while(linea !=null){
			StringTokenizer dato = new StringTokenizer(linea," ");
			while(dato.hasMoreTokens()){
				listaDatos.add(dato.nextToken());
	        }
			linea = br.readLine();
		}
		analisis();
	}
	
	public static void analisis() throws IOException{
		Iterator<String> nombre = listaDatos.iterator();
		FileWriter fichero1 = (new FileWriter("estatico.txt",true));
		FileWriter fichero2 = (new FileWriter("dinamico.txt",true));
		while(nombre.hasNext()){
			String informacion = nombre.next();
			if(informacion.equals("val")){
				String inf = nombre.next();
				PrintWriter p = new PrintWriter(fichero1);
		        p.print(inf);
				PrintWriter p2 = new PrintWriter(fichero2);
		        p2.print(inf);
			}
			else if(informacion.equals("=")){
				String inf = nombre.next();
				int numero;
				if(inf.equals("let"))
					System.out.println("YOOOO");
				else if(inf.equals("if"))
					System.out.println("Jose Daniel");
				else if(Integer.parseInt(inf)==0){
					System.out.println("FABIANA");
					numero = Integer.parseInt(inf);
				}
				else
					System.out.println("Josue");
				PrintWriter p = new PrintWriter(fichero2);
		        p.print(" = " + inf + "\n");
			}
		}
		fichero1.close();
		fichero2.close();
	}
}
