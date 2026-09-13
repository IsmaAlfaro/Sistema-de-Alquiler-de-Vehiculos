package cr.ac.una.est.sistemaalquilercarros.modelo;

public class VehiculoCarga extends Vehiculo {

    private double capacidadCarga;
    private String tipoTraccion;

    /*
     * Qué hace: Crea un vehículo destinado a carga utilizando los datos generales heredados de Vehiculo y agregando la capacidad de carga y el tipo de tracción propios de esta categoría
     * Recibe: La placa, marca, modelo, año, tarifa diaria, tipo de vehículo, capacidad de carga y tipo de tracción
     * Retorna: No retorna ningún valor
     */
    public VehiculoCarga(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo, double capacidadCarga, String tipoTraccion) {
        super(placa, marca, modelo, anio, tarifaDiaria, tipoVehiculo);

        this.capacidadCarga = capacidadCarga;
        this.tipoTraccion = tipoTraccion;
    }

    /*
     * Qué hace: Obtiene la capacidad máxima de carga que se encuentra registrada para este vehículo
     * Recibe: No recibe parámetros
     * Retorna: La capacidad de carga del vehículo
     */
    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    /*
     * Qué hace: Modifica la capacidad máxima de carga registrada actualmente para este vehículo
     * Recibe: La nueva capacidad de carga que se desea establecer
     * Retorna: No retorna ningún valor
     */
    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    /*
     * Qué hace: Obtiene el tipo de tracción que se encuentra registrado para el vehículo de carga
     * Recibe: No recibe parámetros
     * Retorna: El tipo de tracción del vehículo
     */
    public String getTipoTraccion() {
        return tipoTraccion;
    }

    /*
     * Qué hace: Modifica el tipo de tracción que se encuentra registrado para el vehículo de carga
     * Recibe: El nuevo tipo de tracción que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setTipoTraccion(String tipoTraccion) {
        this.tipoTraccion = tipoTraccion;
    }

    /*
     * Qué hace: Genera una representación resumida del vehículo de carga utilizando su placa, marca y modelo para facilitar su identificación dentro de la interfaz
     * Recibe: No recibe parámetros
     * Retorna: Una cadena de texto con la placa, marca y modelo del vehículo
     */
    @Override
    public String toString() {
        return getPlaca() + " - " + getMarca() + " - " + getModelo();
    }
}