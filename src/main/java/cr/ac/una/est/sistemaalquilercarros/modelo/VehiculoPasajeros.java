package cr.ac.una.est.sistemaalquilercarros.modelo;

public class VehiculoPasajeros extends Vehiculo {

    private int cantidadPasajeros;
    private String tipoTraccion;

    /*
     * Qué hace: Crea un vehículo destinado al transporte de pasajeros utilizando los datos generales heredados de Vehiculo y agregando la cantidad de pasajeros y el tipo de tracción
     * Recibe: La placa, marca, modelo, año, tarifa diaria, tipo de vehículo, cantidad de pasajeros y tipo de tracción
     * Retorna: No retorna ningún valor
     */
    public VehiculoPasajeros(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo, int cantidadPasajeros, String tipoTraccion) {
        super(placa, marca, modelo, anio, tarifaDiaria, tipoVehiculo);

        this.cantidadPasajeros = cantidadPasajeros;
        this.tipoTraccion = tipoTraccion;
    }

    /*
     * Qué hace: Obtiene la cantidad máxima de pasajeros que se encuentra registrada para este vehículo
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de pasajeros permitidos en el vehículo
     */
    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    /*
     * Qué hace: Modifica la cantidad de pasajeros que se encuentra registrada actualmente para este vehículo
     * Recibe: La nueva cantidad de pasajeros que se desea establecer
     * Retorna: No retorna ningún valor
     */
    public void setCantidadPasajeros(int cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
    }

    /*
     * Qué hace: Obtiene el tipo de tracción que se encuentra registrado para el vehículo de pasajeros
     * Recibe: No recibe parámetros
     * Retorna: El tipo de tracción del vehículo
     */
    public String getTipoTraccion() {
        return tipoTraccion;
    }

    /*
     * Qué hace: Modifica el tipo de tracción que se encuentra registrado para el vehículo de pasajeros
     * Recibe: El nuevo tipo de tracción que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setTipoTraccion(String tipoTraccion) {
        this.tipoTraccion = tipoTraccion;
    }

    /*
     * Qué hace: Genera una representación resumida del vehículo de pasajeros utilizando su placa, marca y modelo para facilitar su identificación dentro de la interfaz
     * Recibe: No recibe parámetros
     * Retorna: Una cadena de texto con la placa, marca y modelo del vehículo
     */
    @Override
    public String toString() {
        return getPlaca() + " - " + getMarca() + " - " + getModelo();
    }
}