package cr.ac.una.est.sistemaalquilercarros.modelo;

import java.io.Serializable;

public class TipoVehiculo implements Serializable {

    private String nombre;
    private CategoriaVehiculo categoria;
    private boolean requierePasajeros;
    private boolean requiereTraccion;
    private boolean requiereCapacidadCarga;

    /*
     * Qué hace: Crea un nuevo tipo de vehículo y define su nombre, categoría y las características específicas que deberán solicitarse al registrar vehículos pertenecientes a ese tipo
     * Recibe: El nombre, la categoría y los valores que indican si requiere pasajeros, tipo de tracción y capacidad de carga
     * Retorna: No retorna ningún valor
     */
    public TipoVehiculo(String nombre, CategoriaVehiculo categoria, boolean requierePasajeros, boolean requiereTraccion, boolean requiereCapacidadCarga) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.requierePasajeros = requierePasajeros;
        this.requiereTraccion = requiereTraccion;
        this.requiereCapacidadCarga = requiereCapacidadCarga;
    }

    /*
     * Qué hace: Obtiene el nombre utilizado para identificar este tipo de vehículo dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: El nombre del tipo de vehículo
     */
    public String getNombre() {
        return nombre;
    }

    /*
     * Qué hace: Modifica el nombre utilizado para identificar el tipo de vehículo
     * Recibe: El nuevo nombre que se desea asignar al tipo de vehículo
     * Retorna: No retorna ningún valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*
     * Qué hace: Obtiene la categoría general a la que pertenece el tipo de vehículo registrado
     * Recibe: No recibe parámetros
     * Retorna: La categoría del tipo de vehículo
     */
    public CategoriaVehiculo getCategoria() {
        return categoria;
    }

    /*
     * Qué hace: Modifica la categoría general asociada actualmente con el tipo de vehículo
     * Recibe: La nueva categoría que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setCategoria(CategoriaVehiculo categoria) {
        this.categoria = categoria;
    }

    /*
     * Qué hace: Indica si al registrar un vehículo de este tipo es necesario especificar su cantidad de pasajeros
     * Recibe: No recibe parámetros
     * Retorna: Verdadero si requiere cantidad de pasajeros o falso si no la requiere
     */
    public boolean isRequierePasajeros() {
        return requierePasajeros;
    }

    /*
     * Qué hace: Modifica la configuración que determina si este tipo de vehículo necesita registrar una cantidad de pasajeros
     * Recibe: Un valor verdadero o falso indicando si se requiere esta información
     * Retorna: No retorna ningún valor
     */
    public void setRequierePasajeros(boolean requierePasajeros) {
        this.requierePasajeros = requierePasajeros;
    }

    /*
     * Qué hace: Indica si al registrar un vehículo de este tipo es necesario especificar también su tipo de tracción
     * Recibe: No recibe parámetros
     * Retorna: Verdadero si requiere tipo de tracción o falso si no lo requiere
     */
    public boolean isRequiereTraccion() {
        return requiereTraccion;
    }

    /*
     * Qué hace: Modifica la configuración que determina si este tipo de vehículo necesita registrar información sobre su tracción
     * Recibe: Un valor verdadero o falso indicando si se requiere la tracción
     * Retorna: No retorna ningún valor
     */
    public void setRequiereTraccion(boolean requiereTraccion) {
        this.requiereTraccion = requiereTraccion;
    }

    /*
     * Qué hace: Indica si al registrar un vehículo de este tipo es necesario especificar su capacidad máxima de carga
     * Recibe: No recibe parámetros
     * Retorna: Verdadero si requiere capacidad de carga o falso si no la requiere
     */
    public boolean isRequiereCapacidadCarga() {
        return requiereCapacidadCarga;
    }

    /*
     * Qué hace: Modifica la configuración que determina si este tipo de vehículo necesita registrar una capacidad de carga
     * Recibe: Un valor verdadero o falso indicando si se requiere esta información
     * Retorna: No retorna ningún valor
     */
    public void setRequiereCapacidadCarga(boolean requiereCapacidadCarga) {
        this.requiereCapacidadCarga = requiereCapacidadCarga;
    }

    /*
     * Qué hace: Genera la representación en texto del tipo de vehículo utilizando su nombre para que pueda mostrarse fácilmente en componentes de la interfaz como los ComboBox
     * Recibe: No recibe parámetros
     * Retorna: El nombre del tipo de vehículo
     */
    @Override
    public String toString() {
        return nombre;
    }
}
