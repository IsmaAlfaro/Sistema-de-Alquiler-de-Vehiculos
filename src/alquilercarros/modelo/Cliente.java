package alquilercarros.modelo;

public class Cliente {
    private final String documento;
    private final String nombreCompleto;
    private String telefono;
    private String correo;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento es obligatorio.");
        }
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        this.documento = documento.trim();
        this.nombreCompleto = nombreCompleto.trim();
        setTelefono(telefono);
        setCorreo(correo);
    }

    public String getDocumento() {
        return documento;
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
        return documento + " - " + nombreCompleto + " | Telefono: " + telefono
                + " | Correo: " + correo;
    }
}
