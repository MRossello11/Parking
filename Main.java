package Parking;
/**
 * @author Miquel Andreu Rossello Mas
 * @version 1.2
 * Clase ejecutable del proyecto donde se inicializan los valores iniciales necesarios.
 */
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ParkingTest_Miquel_Rossello p = inicio();
        p.recogerEntradaMenu();
    }

    private static ParkingTest_Miquel_Rossello inicio(){
        String path = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Quiere cargar las matriculas del archivo? [S/N]");
        String cargaMatriculas = sc.next();

        if (cargaMatriculas.equalsIgnoreCase("S")){
            System.out.println("Introduzca la ruta al archivo: ");
            path = sc.next();
        }

        System.out.println("Introduzca el numero de plazas para no discapacitados: ");
        int plazasNoDiscapacitados = sc.nextInt();

        System.out.println("Introduzca el numero de plazas para discapacitados: ");
        int plazasDiscapacitados = sc.nextInt();

        if (path!=null) {
            return new ParkingTest_Miquel_Rossello(path, plazasNoDiscapacitados, plazasDiscapacitados);
        } else {
            return new ParkingTest_Miquel_Rossello(plazasNoDiscapacitados, plazasDiscapacitados);
        }
    }
}
