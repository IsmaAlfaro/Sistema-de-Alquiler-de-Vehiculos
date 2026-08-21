package alquilercarros.modelo;

public class Cliente {
    private final String identificacion;
    private final String nombreCompleto;
    private String telefono;
    private String correo;

    public Cliente(String identificacion, String nombreCompleto, String telefono, String correo) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificacion es obligatoria.");
        }
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        this.identificacion = identificacion.trim();
        this.nombreCompleto = nombreCompleto.trim();
        setTelefono(telefono);
        setCorreo(correo);
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El telefono es obligatorio.");
        }
        this.telefono = telefono.trim();
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        this.correo = correo.trim();
    }

    @Override
    public String toString() {
        return identificacion + " - " + nombreCompleto + " | Telefono: " + telefono
                + " | Correo: " + correo;
    }
}
