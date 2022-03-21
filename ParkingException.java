package Parking;
/**
 * @author Miquel Andreu Rossello Mas
 * @version 1.2
 * Excepciones personalizadas para el proyecto Parking.
 */

//excepcion padre
public class ParkingException extends Exception{
    //colores
    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN   = "\u001B[36m";
    public static final String ANSI_WHITE  = "\u001B[37m";

    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA" + ANSI_RESET;
    }
}

class MatriculaYaExistenteException extends ParkingException{
    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Matricula ya existente" + ANSI_RESET;
    }
}

class MatriculaInvalidaException extends ParkingException{
    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Matricula incorrecta" + ANSI_RESET;
    }
}

class MatriculaInexistenteException extends ParkingException{
    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Matricula inexistente" + ANSI_RESET;
    }
}

class ParkingLlenoException extends ParkingException{
    TipoPlazaParking tipo;
    public ParkingLlenoException(TipoPlazaParking tipo) {
        super();
        this.tipo = tipo;
    }

    @Override
    public String getMessage() {
        String s = (this.tipo == TipoPlazaParking.NO_DISCAPACITADO) ? "no discapacitados" : "discapacitados";
        return ANSI_RED + "ALERTA=>Parking para " + s + " lleno" +ANSI_RESET;
    }
}

class ParkingOcupacion85Exception extends ParkingException{
    TipoPlazaParking tipo;
    public ParkingOcupacion85Exception(TipoPlazaParking tipo) {
        super();
        this.tipo = tipo;
    }

    @Override
    public String getMessage() {
        String s = (this.tipo == TipoPlazaParking.NO_DISCAPACITADO) ? "no discapacitados" : "discapacitados";
        return ANSI_RED + "ALERTA=>Ocupacion de plazas para " + s + " supera el 85%" +ANSI_RESET;
    }
}

class ParkingDiscapacitadosLlenoException extends ParkingException {
    int numPlaza;

    public ParkingDiscapacitadosLlenoException(int numPlaza) {
        super();
        this.numPlaza = numPlaza;
    }

    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Parking para discapacitados lleno, ha ocupado la plaza normal numero: " + numPlaza + ANSI_RESET;
    }
}

class GarruloException extends ParkingException{
    int numPlaza;

    public GarruloException(int numPlaza) {
        super();
        this.numPlaza = numPlaza;
    }

    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Garrulo detected! Ha ocupado la plaza numero: " + numPlaza + ANSI_RESET;
    }
}

class InputIncorrectoException extends ParkingException{
    @Override
    public String getMessage() {
        return ANSI_RED + "ALERTA=>Debe introducir un numero valido" + ANSI_RESET;
    }
}
