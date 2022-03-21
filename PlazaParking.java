package Parking;
/**
 * @author Miquel Andreu Rossello Mas
 * @version 1.1
 * PlazaParking son los objetos que conforman al parking. Se describen por su ocupacion, tipo de plaza
 * y la matricula del coche que contienen.
 */
public class PlazaParking {
   private boolean ocupado; //describe si la plaza esta ocupada (true) o no (false)
   private TipoPlazaParking tipoPlaza; //tipo de plaza (DISCAPACITADO|NO_DISCAPACITADO)
   private String coche; //matricula del coche que ocupa la plaza

    public PlazaParking() {

    }

    public PlazaParking(TipoPlazaParking tipoPlaza) {
        this.ocupado = false;
        this.tipoPlaza = tipoPlaza;
        this.coche = "";
    }

    @Override
    public String toString() {
        return this.coche;
    }

    //getter & setters
    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public TipoPlazaParking getTipoPlaza() {
        return tipoPlaza;
    }

    public void setTipoPlaza(TipoPlazaParking tipoPlaza) {
        this.tipoPlaza = tipoPlaza;
    }

    public String getCoche() {
        return coche;
    }

    public void setCoche(String coche) {
        this.coche = coche;
    }
}
