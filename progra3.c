//Inclucion de librerias necesarias para el programa
#include <stdio.h>

#define MAX 500

main(){
	printf("------------- BIENVENIDO (A) ------------- \n\n");
	FILE *archivo=NULL;
	char* nom_archivo = "codigo.txt";
	char lectura[MAX],lect[MAX] ;
		
	archivo = fopen(nom_archivo, "r"); //Abre el archivo para leerlo

	while(!feof(archivo)){
		fscanf(archivo,"%s",lectura); // Lee palabra por palabra
		printf("%s \n",lectura);
	}
	fclose(archivo); // Cierra el archivo
}
