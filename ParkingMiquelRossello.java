package Parking;

/**
 * @author Miquel Andreu Rossello Mas
 * @version 1.5
 * ParkingMiquelRossello es la clase gestora del parking. Maneja entradas y salidas de todos los
 * tipos (por entrada manual o desde un archivo) asi como validar las matriculas de dichas entradas
 * y lanzar las debidas excepciones cuando sea necesario.
 */

import java.io.*;
import java.util.ArrayList;

public class ParkingMiquelRossello {
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

    //atributos
    private int plazasNoDiscapacitados; //numero de plazas para no discapacitados
    private int plazasDiscapacitados; //numero de plazas para discapacitados
    private PlazaParking[] plazas; //array de objetos PlazaParking

    public ParkingMiquelRossello(int plazasNoDiscapacitados, int plazasDiscapacitados) {
        this.plazasNoDiscapacitados = plazasNoDiscapacitados;
        this.plazasDiscapacitados = plazasDiscapacitados;
        this.plazas = new PlazaParking[plazasNoDiscapacitados+plazasDiscapacitados];
        this.crearPlazas();
    }

    //llena el array plazas de objetos PlazaParking
    private void crearPlazas(){
        for (int i = 0; i < this.plazas.length; i++){
            //se crean primero las plazas para no discapacitados
            if (i < this.getPlazasNoDiscapacitados()){
                this.plazas[i] = new PlazaParking(TipoPlazaParking.NO_DISCAPACITADO);

            } else { // el resto de plazas son para discapacitados
                this.plazas[i] = new PlazaParking(TipoPlazaParking.DISCAPACITADO);
            }
//            System.out.println(i + " " + plazas[i]); //debug
        }
    }

    //lee las matriculas del archivo pasado por parametro y les asigna un sitio en el parking
    public void leerMatriculas(String path){
        ArrayList<String> matriculas = new ArrayList<>();
        //recoger las matriculas
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String matricula = br.readLine();

            while (matricula!=null){
                //comprobacion del formato de la matricula
                if (this.validarMatricula(matricula)) {
                    matriculas.add(matricula);
                } else {
                    throw new MatriculaInvalidaException();
                }
                matricula = br.readLine();

            }
        } catch (IOException e){
            System.out.println(ANSI_RED + "ALERTA=>Fichero incorrecto o inexistente" + ANSI_RESET);
        } catch (Exception e1){
            System.out.println(e1.getMessage());
        }

        //entrada de los coches
        for (String m:matriculas) {
            try {
                //se comprueba si la matricula ya existe en el parking
                if (this.comprobarMatriculaExistente(m)){
                    throw new MatriculaYaExistenteException();
                }
                //se comprueba el tipo de matricula y se realiza la entrada adecuada
                if (m.startsWith("0")){
                    int numPlaza = this.entraCocheDiscapacitado(m);
                } else {
                    int numPlaza = this.entraCoche(m);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //entrada de un coche normal al parking
    public int entraCoche(String matricula)throws Exception{
        //Excepciones
        //para cuando el parking este lleno
        if (this.getPlazasLibres(TipoPlazaParking.NO_DISCAPACITADO)==0) throw new ParkingLlenoException(TipoPlazaParking.NO_DISCAPACITADO);
        //si la matricula es para discapacitados o no tiene el formato valido
        if (matricula.startsWith("0")||!this.validarMatricula(matricula)) throw new MatriculaInvalidaException();
        //si la matricula ya existe
        if (comprobarMatriculaExistente(matricula)) throw new MatriculaYaExistenteException();

        int numPlaza = 0; //numero de plaza que se le va a asignar al coche

        //para los no discapacitados podemos saltar las plazas para discapacitados
        //aun asi, se comprobara que esta no sea de discapacitados
        for (int i = 0; i<this.plazas.length-this.getPlazasDiscapacitados();i++){
            //se comprueba que la plaza no este ocupada y sea para no discapacitados
            if (!this.plazas[i].isOcupado() && this.plazas[i].getTipoPlaza()==TipoPlazaParking.NO_DISCAPACITADO){
                this.plazas[i].setCoche(matricula);
                this.plazas[i].setOcupado(true);
                numPlaza = i;
                break;
            }
        }
        return numPlaza;
    }

    //entra un coche para discapacitados
    public int entraCocheDiscapacitado(String matricula)throws Exception{
        //Excepciones

        //en caso de que salte la excepcion, el metodo puede seguirse ejecutando ya que puede que haya plazas libres para no discapacitados
        try {
            //control de que el parking no esta lleno
            if (this.getPlazasLibres(TipoPlazaParking.DISCAPACITADO)==0) throw new ParkingLlenoException(TipoPlazaParking.DISCAPACITADO);
        } catch (ParkingLlenoException e) {
            e.getMessage();
        }
        //control de validez de matricula y si esta existe
        if (!this.validarMatricula(matricula)) throw new MatriculaInvalidaException();
        if (comprobarMatriculaExistente(matricula)) throw new MatriculaYaExistenteException();

        int numPlaza = 0;
        boolean garrulo = matricula.startsWith("1"); //variable para detectar garrulos

        //en el caso de que hayan plazas para discapacitados
        if (this.getPlazasLibres(TipoPlazaParking.DISCAPACITADO)>0) {
            for (int i = this.getPlazasNoDiscapacitados(); i < this.plazas.length; i++) {
                if (!this.plazas[i].isOcupado() && this.plazas[i].getTipoPlaza()==TipoPlazaParking.DISCAPACITADO){
                    this.plazas[i].setCoche(matricula);
                    this.plazas[i].setOcupado(true);
                    numPlaza = i;
                    break;
                }
            }
        } else {
            //al no haber plazas para discapacitados libres, se le busca una plaza entre los no discapacitados
            for (int i = 0; i < this.getPlazasNoDiscapacitados(); i++) {
                if (!this.plazas[i].isOcupado()) {
                    if (this.plazas[i].getTipoPlaza() == TipoPlazaParking.NO_DISCAPACITADO) { //si la plaza es para no discapacitados y se aparca un discapacitado, se le considerara no discapacitado
                        matricula = matricula.replaceFirst("0", "1");
                        this.plazas[i].setCoche(matricula);
                        this.plazas[i].setOcupado(true);
                        throw new ParkingDiscapacitadosLlenoException(i);
                    }
                    numPlaza = i;
                    break;
                }
            }
        }

        //control de garrulos
        if (garrulo) throw new GarruloException(numPlaza);

        return numPlaza;
    }

    //salida de un coche normal
    public void saleCoche(String matricula)throws Exception{
        //en caso de que se intente sacar un coche de discapacitado
        if (matricula.startsWith("0")) throw new MatriculaInvalidaException();

        //un coche no discapacitado no deberia encontrarse en la zona de discapacitados asi que solo
        //se le buscara en la zona de no discapacitados
        for (int i = 0; i < this.getPlazasNoDiscapacitados(); i++) {
            //si se encuentra el coche, se vacia la plaza
            if (this.plazas[i].getCoche().equals(matricula)) {
                this.plazas[i].setCoche("");
                this.plazas[i].setOcupado(false);
                return;
            }
        }
        //si no se encuentra el coche se lanza una excepcion
        throw new MatriculaInexistenteException();
    }

    //salida de un coche para discapacitados del parking
    public void saleCocheDiscapacitado(String matricula)throws Exception{
        //en caso de que se intente sacar un coche de no discapacitado
//        if (matricula.startsWith("1")) throw new MatriculaInvalidaException(); //desactivado para poder sacar a los garrulos

        //si el coche se considera discapacitado, este se le buscara por la zona de discapacitados (ya que es el unico sitio donde puede estar)
        for (int i = this.getPlazasNoDiscapacitados(); i < this.plazas.length; i++) {
            if (this.plazas[i].getCoche().equals(matricula)) {
                //cuando se encuentra la matricula, se resetea la plaza
                this.plazas[i].setCoche("");
                this.plazas[i].setOcupado(false);
                return;
            }
        }
        //en caso de no encontrase la matricula, se lanzaria una excepcion
        throw new MatriculaInexistenteException();
    }

    //escribe las matriculas existentes en el parking en un archivo
    public void guardarMatriculas(String path)throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        //se escriben todas las matriculas mientras no sean vacias
        for (PlazaParking p: this.plazas) {
            if (!p.getCoche().equals("")){
                bw.write(p.getCoche()+"\n");
            }
        }
        bw.close(); //cierre del buffer de escritura
    }

    //comprueba si la matricula ya existe en el parking (true=existe, false=no existe)
    private boolean comprobarMatriculaExistente(String m){
        for (PlazaParking plaza : this.plazas) {
            if (plaza.getCoche().equals(m)) {
                return true;
            }
        }
        return false;
    }

    //valida el formato de una matricula (true=valido, false=no valido)
    private boolean validarMatricula(String m){
        return m.matches("[0-1][0-9]{4}[A-Z]{3}");
    }

    //getters y setters
    public int getPlazasOcupadas(TipoPlazaParking tipo){

        int contadorPlazas = 0;
        //se cuentan las plazas por tipo que contengan un coche
        for (PlazaParking plaza : this.plazas) {
            if (plaza.isOcupado() && plaza.getTipoPlaza() == tipo) {
                contadorPlazas++;
            }
        }
        return contadorPlazas;
    }

    public int getPlazasLibres(TipoPlazaParking tipo){
        if (tipo==TipoPlazaParking.DISCAPACITADO){
            return this.getPlazasDiscapacitados()-this.getPlazasOcupadas(tipo);
        } else {
            return this.getPlazasNoDiscapacitados() - this.getPlazasOcupadas(tipo);
        }
    }

    public int getPlazasNoDiscapacitados() {
        return plazasNoDiscapacitados;
    }

    public void setPlazasNoDiscapacitados(int plazasNoDiscapacitados) {
        this.plazasNoDiscapacitados = plazasNoDiscapacitados;
    }

    public int getPlazasDiscapacitados() {
        return plazasDiscapacitados;
    }

    public void setPlazasDiscapacitados(int plazasDiscapacitados) {
        this.plazasDiscapacitados = plazasDiscapacitados;
    }

    public PlazaParking[] getPlazas() {
        return plazas;
    }

    public void setPlazas(PlazaParking[] plazas) {
        this.plazas = plazas;
    }
}
