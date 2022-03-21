package Parking;
/**
 * @author Miquel Andreu Rossello Mas
 * @version 1.4
 * Clase de testeo del ParkingMiquelRossello. Aqui se recogen las entradas del menu, se ejecutan los metodos pertinentes
 * de la clase ParkingMiquelRossello y se muestra la informacion del Parking
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ParkingTest_Miquel_Rossello {
    //colores
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";

    Scanner sc = new Scanner(System.in);
    private final ArrayList<String> menu = new ArrayList<>(Arrays.asList(
            "Llenar el parking a partir del archivo",
            "Entra coche",
            "Entra coche discapacitado",
            "Sale coche",
            "Sale coche discapacitado",
            "Guardar listado matriculas al archivo",
            "Mostrar plazas del parking",
            "Salir"
    ));
    private final ParkingMiquelRossello pmr;

    public ParkingTest_Miquel_Rossello(String path, int plazasNoDiscapacitados, int plazasDiscapacitados) {
        this.pmr = new ParkingMiquelRossello(plazasNoDiscapacitados,plazasDiscapacitados);
        this.pmr.leerMatriculas(path);
    }

    public ParkingTest_Miquel_Rossello(int plazasNoDiscapacitados, int plazasDiscapacitados) {
        this.pmr = new ParkingMiquelRossello(plazasNoDiscapacitados, plazasDiscapacitados);
    }

    //muestra las opciones que tiene el usuario, recoge la eleccion y ejecuta el metodo pertinente
    public void recogerEntradaMenu() {
        while (true) {
            System.out.println("\nElija una opcion: ");

            for (int i = 0; i < this.menu.size(); i++) {
                System.out.println("(" + i + ") " + this.menu.get(i));
            }

            int eleccion = sc.nextInt();
            try {
                switch (eleccion) {
                    case 0 -> { //llenar el parking a partir del archivo
                        System.out.println("Introduzca la ruta del archivo");
                        this.pmr.leerMatriculas(this.sc.next());
                    }
                    case 1 -> { //entrar coche normal
                        System.out.println("introduzca la matricula");
                        this.pmr.entraCoche(this.sc.next());
                        if (this.pmr.getPlazasLibres(TipoPlazaParking.NO_DISCAPACITADO)<=this.pmr.getPlazasLibres(TipoPlazaParking.NO_DISCAPACITADO)*0.85) throw new ParkingOcupacion85Exception(TipoPlazaParking.NO_DISCAPACITADO);
                    }

                    case 2 -> { //entrar coche discapacitado
                        System.out.println("introduzca la matricula");
                        this.pmr.entraCocheDiscapacitado(this.sc.next());
                        if (this.pmr.getPlazasLibres(TipoPlazaParking.DISCAPACITADO)<=this.pmr.getPlazasLibres(TipoPlazaParking.DISCAPACITADO)*0.85) throw new ParkingOcupacion85Exception(TipoPlazaParking.DISCAPACITADO);
                    }

                    case 3 -> { //sale coche normal
                        System.out.println("introduzca la matricula");
                        this.pmr.saleCoche(this.sc.next());
                    }
                    case 4 -> { //sale coche discapacitado
                        System.out.println("introduzca la matricula");
                        this.pmr.saleCocheDiscapacitado(this.sc.next());
                    }
                    case 5 -> { //guardar matriculas del parking a un archivo
                        System.out.println("introduzca la ruta al archivo: ");
                        this.pmr.guardarMatriculas(sc.next());
                    }

                    case 6-> { //caso para corroborar el estado de las plazas
                        System.out.println("Leyenda plazas : " +
                                            ANSI_GREEN + "No discapacitado libre " +
                                            ANSI_RED + "No discapacitado ocupado " +
                                            ANSI_BLUE + "Discapacitado libre " +
                                            ANSI_YELLOW + "Discapacitado ocupado \n" + ANSI_RESET);

                        for (int j = 0; j < this.pmr.getPlazas().length; j++) {
                            //plaza para no discapacitados libre
                            if (!this.pmr.getPlazas()[j].isOcupado() && this.pmr.getPlazas()[j].getTipoPlaza()==TipoPlazaParking.NO_DISCAPACITADO){
                                System.out.print(ANSI_GREEN);
                              //plaza para no discapacitados ocupada
                            } else if (this.pmr.getPlazas()[j].isOcupado() && this.pmr.getPlazas()[j].getTipoPlaza()==TipoPlazaParking.NO_DISCAPACITADO){
                                System.out.print(ANSI_RED);
                              //plaza para discapacitados libre
                            } else if (!this.pmr.getPlazas()[j].isOcupado() && this.pmr.getPlazas()[j].getTipoPlaza()==TipoPlazaParking.DISCAPACITADO){
                                System.out.print(ANSI_BLUE);
                              //plaza para discapacitados ocupada
                            } else {
                                System.out.print(ANSI_YELLOW);
                            }


                            System.out.println("Plaza #" + j + " " + this.pmr.getPlazas()[j] + ANSI_RESET);
                        }
                    }

                    case 7 -> {
                        break;
                    }

                    default -> throw new InputIncorrectoException();

                }

                if (eleccion==7){
                    break;
                }

                //mostrar las plazas libres y ocupadas por tipo
                System.out.println(ANSI_GREEN + "Plazas para no discapacitados libres: " + this.pmr.getPlazasLibres(TipoPlazaParking.NO_DISCAPACITADO));
                System.out.println(ANSI_BLUE + "Plazas para discapacitados libres: " + this.pmr.getPlazasLibres(TipoPlazaParking.DISCAPACITADO));

                System.out.println(ANSI_RED + "Plazas para no discapacitados ocupadas: " + this.pmr.getPlazasOcupadas(TipoPlazaParking.NO_DISCAPACITADO));
                System.out.println(ANSI_YELLOW + "Plazas para discapacitados ocupadas: " + this.pmr.getPlazasOcupadas(TipoPlazaParking.DISCAPACITADO) + ANSI_RESET);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
