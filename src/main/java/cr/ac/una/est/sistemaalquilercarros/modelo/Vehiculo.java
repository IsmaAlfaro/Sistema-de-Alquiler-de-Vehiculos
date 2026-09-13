package cr.ac.una.est.sistemaalquilercarros.modelo;

import java.io.Serializable;

public abstract class Vehiculo implements Serializable {

    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private double tarifaDiaria;
    private EstadoVehiculo estado;
    private TipoVehiculo tipoVehiculo;

    /*
     * Qué hace: Inicializa los datos generales que comparten todos los vehículos del sistema y establece automáticamente su estado inicial como disponible para alquiler
     * Recibe: La placa, marca, modelo, año, tarifa diaria y tipo de vehículo
     * Retorna: No retorna ningún valor
     */
    public Vehiculo(String placa, String marca, String modelo, int anio, double tarifaDiaria, TipoVehiculo tipoVehiculo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tarifaDiaria = tarifaDiaria;
        this.tipoVehiculo = tipoVehiculo;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    /*
     * Qué hace: Obtiene la placa utilizada como identificación principal del vehículo dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La placa del vehículo
     */
    public String getPlaca() {
        return placa;
    }

    /*
     * Qué hace: Modifica la placa utilizada para identificar el vehículo dentro del sistema
     * Recibe: La nueva placa que se desea asignar al vehículo
     * Retorna: No retorna ningún valor
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /*
     * Qué hace: Obtiene la marca fabricante registrada para el vehículo
     * Recibe: No recibe parámetros
     * Retorna: La marca del vehículo
     */
    public String getMarca() {
        return marca;
    }

    /*
     * Qué hace: Modifica la marca fabricante que se encuentra registrada para el vehículo
     * Recibe: La nueva marca que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /*
     * Qué hace: Obtiene el modelo específico que se encuentra registrado para el vehículo
     * Recibe: No recibe parámetros
     * Retorna: El modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /*
     * Qué hace: Modifica el modelo específico que se encuentra registrado para el vehículo
     * Recibe: El nuevo modelo que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /*
     * Qué hace: Obtiene el año correspondiente al vehículo registrado dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: El año del vehículo
     */
    public int getAnio() {
        return anio;
    }

    /*
     * Qué hace: Modifica el año que se encuentra registrado actualmente para el vehículo
     * Recibe: El nuevo año que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /*
     * Qué hace: Obtiene el precio establecido para alquilar el vehículo durante un solo día
     * Recibe: No recibe parámetros
     * Retorna: La tarifa diaria del vehículo
     */
    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    /*
     * Qué hace: Modifica el precio que debe cobrarse por cada día de alquiler del vehículo
     * Recibe: La nueva tarifa diaria que se desea establecer
     * Retorna: No retorna ningún valor
     */
    public void setTarifaDiaria(double tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    /*
     * Qué hace: Obtiene el estado actual del vehículo para determinar si se encuentra disponible o actualmente alquilado
     * Recibe: No recibe parámetros
     * Retorna: El estado actual del vehículo
     */
    public EstadoVehiculo getEstado() {
        return estado;
    }

    /*
     * Qué hace: Modifica el estado actual del vehículo para indicar si está disponible para alquiler o si se encuentra alquilado
     * Recibe: El nuevo estado que se desea asignar al vehículo
     * Retorna: No retorna ningún valor
     */
    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    /*
     * Qué hace: Obtiene el tipo de vehículo asociado con este registro para conocer sus características y categoría correspondiente
     * Recibe: No recibe parámetros
     * Retorna: El tipo de vehículo asociado
     */
    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    /*
     * Qué hace: Modifica el tipo de vehículo asociado con el registro actual
     * Recibe: El nuevo tipo de vehículo que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
}
