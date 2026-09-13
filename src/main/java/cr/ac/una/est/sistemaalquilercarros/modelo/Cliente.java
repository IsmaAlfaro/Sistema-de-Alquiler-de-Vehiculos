package cr.ac.una.est.sistemaalquilercarros.modelo;

import java.io.Serializable;

public class Cliente implements Serializable {

    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String correo;

    /*
     * Qué hace: Crea un nuevo cliente y almacena toda la información principal necesaria para identificarlo y utilizarlo posteriormente dentro de los alquileres
     * Recibe: La cédula, el nombre completo, el número de teléfono y el correo electrónico del cliente
     * Retorna: No retorna ningún valor
     */
    public Cliente(String cedula, String nombreCompleto, String telefono, String correo) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
    }

    /*
     * Qué hace: Obtiene la cédula registrada para identificar al cliente dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La cédula del cliente
     */
    public String getCedula() {
        return cedula;
    }

    /*
     * Qué hace: Modifica la cédula almacenada actualmente para el cliente
     * Recibe: La nueva cédula que se desea asignar al cliente
     * Retorna: No retorna ningún valor
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /*
     * Qué hace: Obtiene el nombre completo que se encuentra registrado para el cliente
     * Recibe: No recibe parámetros
     * Retorna: El nombre completo del cliente
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /*
     * Qué hace: Modifica el nombre completo que se encuentra registrado para el cliente
     * Recibe: El nuevo nombre completo que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /*
     * Qué hace: Obtiene el número de teléfono registrado como información de contacto del cliente
     * Recibe: No recibe parámetros
     * Retorna: El número de teléfono del cliente
     */
    public String getTelefono() {
        return telefono;
    }

    /*
     * Qué hace: Modifica el número de teléfono utilizado como información de contacto del cliente
     * Recibe: El nuevo número de teléfono que se desea registrar
     * Retorna: No retorna ningún valor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /*
     * Qué hace: Obtiene la dirección de correo electrónico registrada como información de contacto del cliente
     * Recibe: No recibe parámetros
     * Retorna: El correo electrónico del cliente
     */
    public String getCorreo() {
        return correo;
    }

    /*
     * Qué hace: Modifica la dirección de correo electrónico registrada para el cliente
     * Recibe: El nuevo correo electrónico que se desea asignar
     * Retorna: No retorna ningún valor
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /*
     * Qué hace: Genera una representación en texto del cliente combinando su cédula y su nombre completo para facilitar su identificación en la interfaz
     * Recibe: No recibe parámetros
     * Retorna: Una cadena de texto con la cédula y el nombre completo del cliente
     */
    @Override
    public String toString() {
        return cedula + " - " + nombreCompleto;
    }
}