package parqueadero;

import java.io.*;
import java.util.*;

public class Parqueadero {

    public static Scanner teclado = new Scanner(System.in);
    public static PrintStream out = System.out;

    public static void pausar(String mensage) {
        out.print(mensage + "\nPresione <ENTER> para continuar . . . ");
        teclado.nextLine();
        out.println();
    }

    public static String leer_cadena(String mensaje) {
        out.print(mensaje + ": ");
        return teclado.nextLine();
    }

    public static int leer_entero(String mensaje) {
        try {
            return Integer.parseInt(leer_cadena(mensaje));
        } catch (NumberFormatException e) {
            out.print("N\u00FAmero incorrecto.");
            return leer_entero(mensaje);
        }
    }

    public static String ruta = "estacionamientos.tsv";

    public static void main(String[] args) {
        
        Funcion<Estacionamiento> imprimir = new Funcion<Estacionamiento>() {
            
            public void funcion(Estacionamiento estacionamiento, Object parametros) {
                out.println(estacionamiento);
                int[] contador = (int[]) parametros;
                contador[0]++;
                 
            }           
        }; 
        
        Funcion<Estacionamiento> imprimirEnArchivo = new Funcion<Estacionamiento>() {
            
            public void funcion(Estacionamiento estacionamiento, Object parametros) {
                PrintStream archivo = (PrintStream) parametros;
                archivo.print(estacionamiento.getNumero_de_estacionamiento() + "\t");
                archivo.print(estacionamiento.getPlaca() + "\t");
                archivo.print(estacionamiento.getAnno() + "\t");
                archivo.print(estacionamiento.getMes() + "\t");
                archivo.print(estacionamiento.getDia() + "\t");
                archivo.print(estacionamiento.getHora() + "\t");
                archivo.print(estacionamiento.getMinuto() + "\n");
            }
        };
        
        if(!System.getProperties().get("os.name").equals("Linux") && System.console()!=null)
            try {
                out = new PrintStream(System.out, true, "CP850");
                teclado = new Scanner(System.in, "CP850");
            } catch (UnsupportedEncodingException e) {}
        Vector<Estacionamiento> vector = new Vector<Estacionamiento>();
        int i, n;
        Estacionamiento dato = null, estacionamiento;
        int[] contador = {0};
        int opcion, subopcion;
        String[] campos;
        try {
            Scanner entrada = new Scanner(new FileReader(ruta));
            while (entrada.hasNextLine()) {
                campos = entrada.nextLine().split("\t");
                estacionamiento = new Estacionamiento();
                estacionamiento.setNumero_de_estacionamiento(Integer.parseInt(campos[0]));
                estacionamiento.setPlaca(campos[1]);
                estacionamiento.setAnno(Integer.parseInt(campos[2]));
                estacionamiento.setMes(Integer.parseInt(campos[3]));
                estacionamiento.setDia(Integer.parseInt(campos[4]));
                estacionamiento.setHora(Integer.parseInt(campos[5]));
                estacionamiento.setMinuto(Integer.parseInt(campos[6]));
                vector.add(estacionamiento);
            }
            entrada.close();
        } catch (FileNotFoundException e) {}
        estacionamiento = new Estacionamiento();
        do {
            out.println("MEN\u00DA");
            out.println("1.- Regristro de ingresos");
            out.println("2.- Consultas");
            out.println("3.- Actualizaciones");
            out.println("4.- Registro de salidas");
            out.println("5.- Ordenar registros");
            out.println("6.- Mostrar registros actuales");
            out.println("7.- Salir");
            do {
                opcion = leer_entero ("Seleccione una opci\u00F3n");
                if(opcion<1 || opcion>7)
                    out.println("Opci\u00F3nn no v\u00E1lida.");
            } while (opcion<1 || opcion>7);
            out.println();
            if (vector.isEmpty() && opcion!=1 && opcion!=7) {
                pausar("No hay registros.\n");
                continue;
            }
            if (opcion<5) {
                estacionamiento.setNumero_de_estacionamiento(leer_entero ("Ingrese el número de parqueo"));
                i = vector.indexOf(estacionamiento);
                dato = i<0 ? null : vector.get(i);
                if (dato!=null) {
                    out.println();
                    imprimir.funcion(dato, contador);
                }
            }
            if (opcion==1 && dato!=null)
                out.println("El registro ya existe.");
            else if (opcion>=2 && opcion<=4 && dato==null)
                out.println("\nRegistro no encontrado.");
            else switch (opcion) {
            case 1:
                estacionamiento.setPlaca(leer_cadena ("Ingrese la placa"));
                estacionamiento.setAnno(leer_entero ("Ingrese el año"));
                estacionamiento.setMes(leer_entero ("Ingrese el mes"));
                estacionamiento.setDia(leer_entero ("Ingrese el día"));
                estacionamiento.setHora(leer_entero ("Ingrese la hora"));
                estacionamiento.setMinuto(leer_entero ("Ingrese el minuto"));
                vector.add(estacionamiento);
                estacionamiento = new Estacionamiento();
                out.println("\nRegistro agregado correctamente.");
                break;
            case 3:
                out.println("Men\u00FA de modificaci\u00F3n de campos");
                out.println("1.- Placa");
                out.println("2.- Año");
                out.println("3.- Mes");
                out.println("4.- Dia");
                out.println("5.- Hora");
                out.println("6.- Minuto");
                do {
                    subopcion = leer_entero ("Seleccione un n\u00FAmero de campo a modificar");
                    if (subopcion<1 || subopcion>7)
                        out.println("Opci\u00F3n no v\u00E1lida.");
                } while (subopcion<1 || subopcion>7);
                switch (subopcion) {
                    case 1:
                        dato.setPlaca(leer_cadena ("Ingrese la nueva placa"));
                        break;
                   case 2:
                        dato.setAnno(leer_entero ("Ingrese el nuevo año"));
                        break;
                    case 3:
                        dato.setMes(leer_entero ("Ingrese el nuevo mes"));
                        break;
                    case 4:
                        dato.setDia(leer_entero ("Ingrese el nuevo día"));
                        break;
                    case 5:
                        dato.setHora(leer_entero ("Ingrese la nueva hora"));
                        break;
                    case 6:
                        dato.setMinuto(leer_entero ("Ingrese el nuevo minuto"));
                        break;
                }
                out.println("\nRegistro actualizado correctamente.");
                break;
            case 4:
                vector.remove(dato);
                out.println("Registro borrado correctamente.");
                break;
            case 5:
                Collections.sort(vector);
                out.println("Registros ordenados correctamente.");
                break;
            case 6:
                n = vector.size();
                contador[0] = 0;
                for (i=0; i<n; i++)
                    imprimir.funcion(vector.get(i), contador);
                out.println("Total de registros: " + contador[0] + ".");
                break;
            }
            if (opcion<7 && opcion>=1)
                pausar("");
        } while (opcion!=7);
        try {
            PrintStream salida = new PrintStream(ruta);
            n = vector.size();
            for (i=0; i<n; i++)
                imprimirEnArchivo.funcion(vector.get(i), salida);
            salida.close();
        } catch (FileNotFoundException e) {}
    }
}

interface Funcion<T extends Comparable<T>> {
    void funcion(T dato, Object parametros);
}

class Estacionamiento implements Comparable<Estacionamiento> {

    private int numero_de_estacionamiento;
    private String placa;
    
    private int anno;
    private int mes;
    private int dia;
    private int hora;
    private int minuto;
    
    public boolean equals(Object estacionamiento) {
        return this==estacionamiento || (estacionamiento instanceof Estacionamiento && numero_de_estacionamiento==((Estacionamiento)estacionamiento).numero_de_estacionamiento);
    }

    public int compareTo(Estacionamiento estacionamiento) {
        return numero_de_estacionamiento - estacionamiento.numero_de_estacionamiento;
    }
    
    public String toString() {
        return
            "numero de estacionamiento: " + numero_de_estacionamiento + "\n" +
            "placa                    : " + placa + "\n" +
            "anno                     : " + anno + "\n" +
            "mes                      : " + mes + "\n" +
            "dia                      : " + dia + "\n" +
            "hora                     : " + hora + "\n" +
            "minuto                   : " + minuto + "\n";
    }

    public int getNumero_de_estacionamiento() {
        return numero_de_estacionamiento;
    }
    
    public void setNumero_de_estacionamiento(int numero_de_estacionamiento) {
        this.numero_de_estacionamiento = numero_de_estacionamiento;
    }

    public String getPlaca() {
        return placa;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public int getAnno() {
        return anno;
    }
    
    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getMes() {
        return mes;
    }
    
    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }
    
    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHora() {
        return hora;
    }
    
    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }
    
    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
}