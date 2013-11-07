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
import java.util.Scanner;


public class Progra3 {
	public static ListaSimple listaCodigo = new ListaSimple(); // lista que almacena todo el codigo
	static ListaSimple estatico = new ListaSimple ("Estatico"); // lista que almacena los datos estaticos
	static ListaSimple dinamico = new ListaSimple ("Dinamico"); // lista que almacena los datos dinamicos
	static String resultadoValor;
	static String resultadoTipo;
	
	/*
	 * Funcion principal: lee el archivo que contiene el codigo SML
	 * almacena cada dato del codigo en listaCodigo
	 * luego llama a la funcion evaluar
	 * y por ultimo a la funcion imprimirTablas */
	public static void main(String [] args) throws IOException{
		System.out.println("");
		String ruta;
		Scanner in = new Scanner(System.in);
		System.out.print("Indique el nombre del archivo: \033[1;32m");
		ruta = in.next();
		System.out.print("\033[0m");
		// Archivo que contiene el codigo SML
		//	File archivo = (new File(ruta));
		FileReader fr = new FileReader(ruta);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();
		int pos=0;
		while(linea !=null){
			StringTokenizer dato = new StringTokenizer(linea," ");
			while(dato.hasMoreTokens()){
				Object a = dato.nextToken();
				listaCodigo.Insertar(a, pos);
				pos++;}
			linea = br.readLine();}
		NodosListaSimple aux = listaCodigo.PrimerNodo;
		evaluar(aux,listaCodigo);
		System.out.println("");
		imprimirTablas();
	}
	
	/*funcion evaluar(aux,listaCodigo)
	 * recibe como parametros la lista del codigo que se va a evaluar
	 * y el primer nodo de dicha lista
	 * Esta funcion separa por "funcionalidades" de codigo SML */
	public static void evaluar(NodosListaSimple aux,ListaSimple listaCodigo) throws IOException{
		while(aux!=null){
			// Verifica los datos que hay despues de un val y los separa por let, if o tipo de dato dado
			if(aux.dat.equals("val")){
				aux = aux.siguiente;
				String variable = aux.dat.toString();
				aux = aux.siguiente;
				if(aux.siguiente.dat.equals("let")){
					aux = aux.siguiente;
					int pos = evaluarLet(aux.posicion); // llama a la funcion evaluarLet, para crear una lista con los datos de Let
					estatico.InsertaFinal(variable, resultadoTipo);
					dinamico.InsertaFinal(variable, resultadoValor);
					if(listaCodigo.Largo()==pos+1){ ;break;}
					else{
						while(aux!=null){
							if(aux.posicion==pos){aux = aux.siguiente; break;}
							else aux = aux.siguiente;
						}
					}
					}
				else if(aux.siguiente.dat.equals("if")){
					aux = aux.siguiente;
					int pos = arregloIf(aux.siguiente.posicion); //Llama a la funcion arregloIf para separar los datos por if
					estatico.InsertaFinal(variable, resultadoTipo);
					dinamico.InsertaFinal(variable, resultadoValor);
					if(listaCodigo.Largo()==pos+1){ ;break;}
					else{
						while(aux!=null){
							if(aux.posicion==pos){aux = aux.siguiente; break;}
							else aux = aux.siguiente;
						}
					}
				}
				else{
					String valor = aux.siguiente.dat.toString();
					estatico.InsertaFinal(variable);
					dinamico.InsertaFinal(variable, aux.siguiente.dat.toString());
					try{
						//Verifica si el dato dado es un int
						if (Integer.parseInt(valor) %1 == 0){
							NodosListaSimple aux2 = estatico.PrimerNodo;
							while(aux2!=null){
								if(aux2.dato.equals(variable)){ aux2.tipo = "int";break;}
								else aux2 = aux2.siguiente;
							}
							aux = aux.siguiente;
						}//Fin del if
					}
					catch (NumberFormatException e){
						//llama a la funcion verificarOperacion(), para determinar el tipo de dato dado
						int numVeri = verificarOperacion(valor); 
						if(numVeri == 1);
						else{
							NodosListaSimple aux2 = estatico.PrimerNodo;
							NodosListaSimple aux1 = dinamico.PrimerNodo;
							while(aux2!=null){
								if(aux2.dato.equals(variable)){ aux2.tipo = "int"; aux1 = dinamico.PrimerNodo; break;}
								else{ aux2 = aux2.siguiente;}
							}
							while(aux1!=null){
								if(aux1.dato.equals(variable)){ aux1.tipo = resultadoValor; break;}
								else aux1 = aux1.siguiente;
							}
						}
						aux = aux.siguiente;
					}
				}
			}
			//Si el dato analizado es un let, llama a la funcion evaluarLet
			//y realiza el analisis correspondiente
			else if(aux.dat.equals("let")){
				int pos = evaluarLet(aux.posicion);
				if(listaCodigo.Largo()==pos+1){ ;break;}
				else{
					while(aux!=null){
						if(aux.posicion==pos){aux = aux.siguiente; break;}
						else aux = aux.siguiente;
					}
				}
			}
			//Si el dato analizado es un if, llama a la funcion arregloIf
			//y realiza el analisis correspondiente
			else if(aux.dat.equals("if")){
				int pos = arregloIf(aux.posicion);
			if(listaCodigo.Largo()==pos+1){ ;break;}
			else{
				while(aux!=null){
					if(aux.posicion==pos){aux = aux.siguiente; break;}
					else aux = aux.siguiente;
				}
			}
			}
			else{
				String dato = aux.dat.toString();
				try{
					int numero = Integer.parseInt(dato);
					resultadoValor = String.valueOf(numero);
					resultadoTipo = "int";
				}
				catch (NumberFormatException e){
					int tamanno = dato.length();
					if(tamanno == 1){
						NodosListaSimple a = dinamico.BuscarElemento1(dato);
						NodosListaSimple a1 = estatico.BuscarElemento1(dato);
						if(a!=null && a1!=null){
							resultadoValor = a.tipo;
							resultadoTipo = a1.tipo;
						}
					}
					else{
						int numVeri = verificarOperacion(dato); //verifica el tipo del dato
						
					}
				}
				aux = aux.siguiente;
			}
		}
	}

	/*Funcion arregloIf(pos)
	 * recibe como parametro la posicion de la lista donde se encuentra el If ha analizar, 
	 * Esta funcion, permite realizar una lista con los datos necesarios de un if*/
	static int arregloIf (int pos) throws IOException{
		pos = pos - 1;
		NodosListaSimple aux = listaCodigo.PrimerNodo;
		ListaSimple arreglo = new ListaSimple();
		while(aux!=null){
			if(aux.posicion==pos){
				while(aux!=null){arreglo.Insertar(aux.dat, aux.posicion); aux = aux.siguiente;}}
			else aux = aux.siguiente;
		} int num = EvaluarIf(arreglo); return num;
	}

	/*Funcion EvaluarIf(arreglo)
	 * recibe como parametro el arreglo que contiene todos los datos de un if
	 * Funcion -> separa el if por segmentos en 3 arreglos
	 * e1: contiene los datos que comprender del if hasta el then
	 * e2: contiene los datos que comprenden del then al else
	 * e3: contien los datos que estan despues del else
	 * llama a la funcion evaluar(e1,e2,e3) para determinar los valores del if
	 */
	static int EvaluarIf (ListaSimple arreglo) throws IOException {
		NodosListaSimple exp = arreglo.PrimerNodo;
		
		//arreglos donde se guardan los datos del if
		ArrayList e1= new ArrayList();
		ArrayList e2= new ArrayList();
		ArrayList e3= new ArrayList();
		
		while(exp!=null){
			//Guarda los datos en e1
			if (exp.dat.equals("if")){ exp = exp.siguiente;
				while(!(exp.dat.equals("then"))){ e1.add(exp.dat); exp = exp.siguiente;}}
			//Guarda los datos en e2
			else if (exp.dat.equals("then")){
				 exp = exp.siguiente;
				 if(exp.dat.equals("if")){
					 while(!(exp.dat.equals("else"))){ e2.add(exp.dat); exp = exp.siguiente;}
					 e2.add(exp.dat); exp = exp.siguiente;
					 while(!(exp.dat.equals("else"))){ e2.add(exp.dat); exp = exp.siguiente;}
				 }
				 while(!(exp.dat.equals("else"))){ e2.add(exp.dat); exp = exp.siguiente;}}
			else if(exp.dat.equals("else")){ exp = exp.siguiente;
				if(exp.dat.equals("if")){ e3.add(exp.dat);
					while(!(exp.dat.equals("else"))){ e3.add(exp.dat); exp = exp.siguiente;}e3.add(exp.dat);
				}
				else if(exp.dat.equals("let")){ e3.add(exp.dat);
					while(!(exp.dat.equals("end"))){ e3.add(exp.dat); exp = exp.siguiente;}
					evaluar(e1,e2,e3); return exp.posicion;
				}
				else{ e3.add(exp.dat); evaluar(e1,e2,e3); return exp.posicion; }
			}
			else exp = exp.siguiente;
		}return 0;
	}

	/* Funcion evaluar(a1,a2,a3)
	 * recibe como parametros los arreglos que contiene la informacion de un if
	 * Funcion: determina la condicion del if
	 * ejemplo if x < y, llama a la funcion comparaciones
	 * segun el resultado del if */
	public static void evaluar(ArrayList a1, ArrayList a2, ArrayList a3) throws IOException{
		Iterator<String> exp = a1.iterator();
		//este parte permite separar
		while(exp.hasNext()){
			String dato1 = exp.next(); String dato2 = exp.next();
			if(dato2.equals("<")|dato2.equals(">")|dato2.equals("=")){
				String dato = exp.next(); int num1,num2;
				try{ num1 = Integer.parseInt(dato1); //Determina si el dato es num o no
				//Cuando determina que los dos datos ingresandos son int, llama a la funcion comparaciones
					try{ num2 = Integer.parseInt(dato); comparaciones(num1,num2,dato2,a2,a3);}
					catch(NumberFormatException e2){
						NodosListaSimple nodoAux1 = estatico.BuscarElemento1(dato), nodoAux2 = dinamico.BuscarElemento1(dato);
						if(nodoAux1 != null){ 
							String tipo = nodoAux1.tipo, valor = nodoAux2.tipo;
							num2 = Integer.parseInt(valor);
							comparaciones(num1,num2,dato2,a2,a3);}
						else ;}}
				catch(NumberFormatException e1){
					NodosListaSimple nodoAux = estatico.BuscarElemento1(dato1), nodoAux3 = dinamico.BuscarElemento1(dato1);
					if(nodoAux!=null){
						String tipo = nodoAux.tipo, valor = nodoAux3.tipo;
						try{ num1 = Integer.parseInt(valor);
							try{ num2 = Integer.parseInt(dato); comparaciones(num1,num2,dato2,a2,a3);}
							catch(NumberFormatException e2){
								NodosListaSimple nodoAux1 = estatico.BuscarElemento1(dato), nodoAux2 = dinamico.BuscarElemento1(dato);
								if(nodoAux1 != null){
									tipo = nodoAux1.tipo;
									valor = nodoAux2.tipo;
									num2 = Integer.parseInt(valor);
									comparaciones(num1,num2,dato2,a2,a3);} else;}}
						catch(NumberFormatException e2){;}}	else;}}break;}}

	/*Funcion comparaciones(num1,num2,comp,e2,e3
	 * los parametros son: los dos numero a comparar, el operando de comparacion
	 * dos arreglos de la condiciones del if
	 * Funcion que compara la condicion del if, y determinar si evaluar la parte del then o del else*/
	public static void comparaciones(int num1, int num2, String comp, ArrayList e2, ArrayList e3) throws IOException{
		if(comp.equals("<")){
			if(num1 < num2){ evaluarThen(num1,num2,e2);}
			else evaluarElse(num1,num2,e3); }
		else if(comp.equals(">")){
			if(num1>num2) evaluarThen(num1,num2,e2);
			else evaluarElse(num1,num2,e3);}
		else{
			if(num1==num2) evaluarThen(num1,num2,e2);
			else evaluarElse(num1,num2,e3);}
	}

	/*Funcion evaluarThen(num1,num2,e2)
	 * Parametros: los dos numeros del if, y el arreglo que contiene la 
	 * condicion then del if*/
	public static void evaluarThen(int num1,int num2, ArrayList e2) throws IOException{
		Iterator<String> datos = e2.iterator();
		String nombre = datos.next();
		if(e2.size() == 1){
			if(nombre.length()==1){
				try{ int a = Integer.parseInt(nombre); resultadoValor = String.valueOf(a);
					resultadoTipo = "int";}
				catch(NumberFormatException e){
					NodosListaSimple nodoAux1 = estatico.BuscarElemento1(nombre), nodoAux2 = dinamico.BuscarElemento1(nombre);
					if(nodoAux1 != null){
						String tipo = nodoAux1.tipo, valor = nodoAux2.tipo;
						resultadoValor = valor; resultadoTipo = tipo; }	else ; } }
			else{
				try{  int a = Integer.parseInt(nombre); resultadoValor = String.valueOf(a);
					resultadoTipo = "int";}
				catch(NumberFormatException e){/*System.out.println("funcion de josue"); //javax.script}*/}}}
		else{
			if(nombre.equals("let")) System.out.println("funcion vero");
			else if(nombre.equals("if")){
				int tamanno = e2.size();
				int pos = 1;
				ListaSimple lista = new ListaSimple();
				lista.Insertar(nombre, 0);
				while(pos!=tamanno){lista.Insertar(datos.next(), pos); pos++;}
				EvaluarIf(lista);}
			else{
				try{int numero = Integer.parseInt(nombre);
					resultadoValor = String.valueOf(numero);resultadoTipo = "int";}
				catch (NumberFormatException e){
					int tamanno = nombre.length();
					if(tamanno == 1){
						NodosListaSimple a = dinamico.BuscarElemento1(nombre), a1 = estatico.BuscarElemento1(nombre);
						if(a!=null && a1!=null){
							resultadoValor = a.tipo;
							resultadoTipo = a1.tipo;}}
					else{int numVeri = verificarOperacion(nombre);}}}}}

	/*Funcion evaluarElse(num1,num2,e2)
	 * Parametros: los dos numeros del if, y el arreglo que contiene la 
	 * condicion else del if*/
	public static void evaluarElse(int num1,int num2,ArrayList e3) throws IOException{
		Iterator<String> datos = e3.iterator();
		String nombre = datos.next();
		if(e3.size() == 1){
			if(nombre.length()==1){
				try{
					int a = Integer.parseInt(nombre);
					resultadoValor = String.valueOf(a);
					resultadoTipo = "int";
				}
				catch(NumberFormatException e){
					NodosListaSimple nodoAux1 = estatico.BuscarElemento1(nombre), nodoAux2 = dinamico.BuscarElemento1(nombre);
					if(nodoAux1 != null){
						String tipo = nodoAux1.tipo, valor = nodoAux2.tipo;
						resultadoValor = valor; resultadoTipo = tipo;} else ;}}
			else{
				try{ int a = Integer.parseInt(nombre); resultadoValor = String.valueOf(a); resultadoTipo = "int";}
				catch(NumberFormatException e){ int num = verificarOperacion(nombre);}  /*javax.script}*/}}
		else{
			if(nombre.equals("let")) System.out.println("funcion vero");
			else if(nombre.equals("if")){
				int tamanno = e3.size(), pos = 1;
				ListaSimple lista = new ListaSimple();
				while(pos!=tamanno){lista.Insertar(datos.next(), pos);pos++;}EvaluarIf(lista);}
			else{
				try{int numero = Integer.parseInt(nombre);resultadoValor = String.valueOf(numero);resultadoTipo = "int";}
				catch (NumberFormatException e){int tamanno = nombre.length();
					if(tamanno == 1){NodosListaSimple a = dinamico.BuscarElemento1(nombre), a1 = estatico.BuscarElemento1(nombre);
						if(a!=null && a1!=null){resultadoValor = a.tipo;resultadoTipo = a1.tipo;}}
					else{int numVeri = verificarOperacion(nombre);}}}}}
	
	/*Funcion evaluarLet(pos)
	 * Parametros: pos -> posicion en la listaCodigo del Let que se va a evaluar
	 * Funcion: separa el Let en dos lista:
	 * 1.ListaVariables: guarda todos los datos desde el let hasta el in
	 * 2.ListaAsignaciones: guarda todos los datos desde el in hasta el end*/
	static int evaluarLet(int pos) throws IOException{
		NodosListaSimple aux = listaCodigo.PrimerNodo;
		ListaSimple listaLet = new ListaSimple();
		while(aux!=null){
			if(aux.posicion==pos){
				while(aux!=null){ listaLet.Insertar(aux.dat, aux.posicion); aux = aux.siguiente;}
				break;}else aux = aux.siguiente;}
		NodosListaSimple auxLet = listaLet.PrimerNodo;
		ListaSimple listaVariables = new ListaSimple(), listaAsignaciones = new ListaSimple();
		while(auxLet!=null){
			if (auxLet.dat.equals("let")){
				auxLet = auxLet.siguiente; int posic = 0;
				while(!(auxLet.dat.equals("in"))){ Object a = auxLet.dat;
					if(a.equals("let")){
						while(!(a.equals("end"))){
							listaVariables.Insertar(a, auxLet.posicion);
							auxLet = auxLet.siguiente; a = auxLet.dat;}}
					listaVariables.Insertar(a,auxLet.posicion); posic++;
					auxLet = auxLet.siguiente;}}
			else if (auxLet.dat.equals("in")){auxLet = auxLet.siguiente;
				int posic = 0;
				while(!(auxLet.dat.equals("end"))){
					Object a = auxLet.dat;
					listaAsignaciones.Insertar(a,auxLet.posicion);
					posic++;
					auxLet = auxLet.siguiente;
				 }	}
			else if (auxLet.dat.equals("end")){
				NodosListaSimple aux1 = listaVariables.PrimerNodo;
				NodosListaSimple aux2 = listaAsignaciones.PrimerNodo;
				evaluar(aux1,listaVariables);
				evaluar(aux2,listaAsignaciones);
				return auxLet.posicion;}
		}
		return auxLet.posicion;
	}
	
	public static void validar_tipo(){
		NodosListaSimple veri = dinamico.PrimerNodo;
		NodosListaSimple guar = estatico.PrimerNodo;
		while (veri!=null && guar!=null){
			if (veri.tipo.equals("true") || veri.tipo.equals("false")){
				//guar.Igual = "->";
				guar.tipo = "Boolean";
				guar = guar.siguiente;
				veri = veri.siguiente;
			}//Fin del if
			/*guar = guar.siguiente;
			veri = veri.siguiente;*/
			else{
				try{
					if (Integer.parseInt(veri.tipo) %1 == 0){
						//guar.Igual = "->";
						guar.tipo = "int";
						guar = guar.siguiente;
						veri = veri.siguiente;
					}//Fin del if		
				}//Fin del try
				catch (NumberFormatException e){
					//guar.Igual = "->";
					guar.tipo = definir(veri.tipo);
					guar = guar.siguiente;
					veri = veri.siguiente;
				}//Fin del catch
			}//Fin del else
		}//Fin del while
	}//Fin de valida_tipo

	public static int verificarOperacion(String valor) throws IOException{
		ListaSimple listaOperaciones = new ListaSimple();
		int largo = valor.length();
		int pos = 0;
		String unificar = "";
		while(pos!=largo){
			String caracter = String.valueOf(valor.charAt(pos));
			if(caracter.equals("*")|caracter.equals("+")|caracter.equals("-")|caracter.equals("/")){
				listaOperaciones.InsertaFinal(unificar, caracter);
				unificar = "";
				pos++;}
			else{unificar = unificar + caracter; pos++;}}
		listaOperaciones.InsertaFinal(unificar,null);
		if(listaOperaciones.Largo()==1){letra(unificar);validar_tipo();return 1;}
		else{calcular(listaOperaciones);return 2;}
	}
	
	public static void calcular(ListaSimple lista){
		NodosListaSimple aux = lista.PrimerNodo;
		int resultado = 0;
		while(aux!=null){
			String dato1,oper,dato2;
			dato1 = aux.dato;
			oper = aux.tipo;
			dato2 = aux.siguiente.dato;
			resultado = operaciones(dato1,oper,dato2);
			aux.siguiente.dato = String.valueOf(resultado);
			aux = aux.siguiente;
			if(aux.tipo==null) break;
		}
		resultadoValor = String.valueOf(resultado);
		resultadoTipo = "int";
		
	}
	public static int resolver(int num1,int num2,String oper){
		int resultado;
		if(oper.equals("+")) resultado = num1 + num2;
		else if(oper.equals("-")) resultado = num1 - num2;
		else if(oper.equals("*")) resultado = num1 * num2;
		else resultado = num1 / num2;
		return resultado;
	}
	
	public static int operaciones(String dato1,String oper,String dato2){
		int num1,num2;
		try{
			num1 = Integer.parseInt(dato1);
			try{num2 = Integer.parseInt(dato2); int result = resolver(num1,num2,oper);return result;}
			catch(NumberFormatException e1){
				NodosListaSimple aux = dinamico.BuscarElemento1(dato2);
				if(aux!=null){
					try{num2 = Integer.parseInt(aux.tipo);int result = resolver(num1,num2,oper);return result;}
					catch(NumberFormatException e3){;}
					}
				}
		}
		catch(NumberFormatException e2){
			NodosListaSimple aux = dinamico.BuscarElemento1(dato1);
			if(aux!=null){
				try{
					num1 = Integer.parseInt(aux.tipo);
					try{num2 = Integer.parseInt(dato2);int result = resolver(num1,num2,oper);return result;}
					catch(NumberFormatException e1){
						aux = dinamico.BuscarElemento1(dato2);
						if(aux!=null){
							try{num2 = Integer.parseInt(aux.tipo);int result = resolver(num1,num2,oper);return result;}
							catch(NumberFormatException e3){;}
							}
						}
					}
				catch(NumberFormatException e){;}
			}
		}
		return 0;
	}
	
	/*  Metodo que verifica si una varible esta en el programa para cambiarlo por el 
    valor correspondiente... Ej: val x = y se cambia por x = 6 ya que
    habia una expresion que indica que y = 6 
   */
	public static void letra (String info) throws IOException{	
		//Se verifica que la lista no se encuentre vacia
		if (dinamico.VaciaLista()){
			System.out.println("Lista vacia");
		}//Fin del if
		else{
			//Se realizan las busquedas para realizar el cambio del valor en caso que sea necesario
			NodosListaSimple aux = dinamico.PrimerNodo;
			NodosListaSimple guia = dinamico.PrimerNodo;
			while (aux!=null){
				String var = aux.dato;
				if (var.equals(info)){
					while (guia != null){
						if (guia.tipo.equals(info)){
							guia.tipo = aux.tipo;
							break;
						}//Fin del if
						else{
							guia = guia.siguiente;
						}//Fin del else
					}//Fin del while interno
					aux.tipo = aux.tipo;
					break;
				}//Fin del if 
				else{
					aux = aux.siguiente;
				}//Fin del else
			}//Fin del while
		}//Fin del else
		
	}//Fin del metodo letras

	public static void resolverIn(ListaSimple in) throws IOException{
		NodosListaSimple auxIn = in.PrimerNodo;
		if(auxIn.dat.equals("if")){ 
			int a = arregloIf(auxIn.posicion - 1); 
			if(in.Largo()==a+1){ ;}
			else{ while(auxIn!=null){
				if(auxIn.posicion==a){break;}
				else auxIn = auxIn.siguiente;}}}}
	
	public static String definir(String dato){
		char pc= ';';
		if (dato.charAt(dato.length()-1)==pc);{
			StringTokenizer tokn = new StringTokenizer(dato,";");
			dato = tokn.nextToken();
		}
		try {
			//Verifico si es un int
			Integer.parseInt(dato);
			return "int";
		} catch (NumberFormatException nfe){
			//Verifico si es un bool
			if (dato.equals("true")||dato.equals("false")){
				return "boolean";}
			//Verifico si es un char
			else if(dato.charAt(0)=='#'){
				return "char";}
			//Verifico si es una tupla
			else if(dato.charAt(0)=='('){
				dato= eliminarChar(dato,'(',')');
				StringTokenizer tupla = new StringTokenizer(dato,",");
				String obtenido= tupla.nextToken();
				if(obtenido.indexOf("[")==0){
					String aux=tupla.nextToken();
					while(aux.indexOf("]")<0){
						obtenido=obtenido+","+aux;
						aux= tupla.nextToken();}
				}
				obtenido=definir(obtenido);
				while(tupla.hasMoreTokens()){
					String aux2=tupla.nextToken();
					if(aux2.indexOf("[")==0){
						String aux=tupla.nextToken();
						while(aux.indexOf("]")<0){
							aux2=aux2+","+aux;
							aux= tupla.nextToken();}
					}
					obtenido+="*"+definir(aux2);
				}
				return obtenido;
					}
			//Verifico si es una lista
			else if(dato.charAt(0)=='['){
				dato= eliminarChar(dato,'[',']');
				StringTokenizer lista = new StringTokenizer(dato,",");
				String result = definir(lista.nextToken());
				result = "list "+result;
				return result;}
			else if(dato.indexOf("let")==0){
				
				return "let";//Funcion de Vero que retorna un String
			}
			else if(dato.indexOf("if")==0){
				return "if";//Funcion de Jose que retorna un String
			}
			//Verifico si es un string
			else{ //(dato.charAt(0)=='"')
				return "string";}
			
		}
	}

	public static String eliminarChar(String linea,char caract1, char caract2){
		String nuevo="";
		for(int i=0; i<linea.length(); i++){
			if(linea.charAt(i)!=caract1&& linea.charAt(i)!=caract2)
				nuevo+= linea.charAt(i);
		}
		return nuevo;
	}
	
	public static void imprimirTablas(){
		System.out.println("\033[1;32m---- Tabla ambiente estatico ----");
		System.out.print("\033[0m");
		NodosListaSimple aux = estatico.PrimerNodo;
		NodosListaSimple aux1 = dinamico.PrimerNodo;
		while(aux!=null){
			System.out.println(" 	\033[1;33m"+aux.dato+" \033[0m--> "+aux.tipo);
			aux = aux.siguiente;
		}
		System.out.println(" ");
		System.out.println("\033[1;32m---- Tabla ambiente dinamico ----\033[0m");
		while(aux1!=null){
			System.out.println(" 	\033[1;33m"+aux1.dato+" \033[0m--> "+aux1.tipo);
			aux1 = aux1.siguiente;
		}
	}
}

