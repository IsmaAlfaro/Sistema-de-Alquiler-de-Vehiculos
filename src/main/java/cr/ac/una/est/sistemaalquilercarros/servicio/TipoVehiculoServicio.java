package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.repositorio.TipoVehiculoRepositorio;

import java.util.List;

public class TipoVehiculoServicio {

    private final TipoVehiculoRepositorio tipoVehiculoRepositorio;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los tipos de vehículo y establece el repositorio que se utilizará para realizar las operaciones de almacenamiento y consulta
     * Recibe: El repositorio encargado de administrar los tipos de vehículo
     * Retorna: No retorna ningún valor
     */
    public TipoVehiculoServicio(TipoVehiculoRepositorio tipoVehiculoRepositorio) {

        this.tipoVehiculoRepositorio = tipoVehiculoRepositorio;

    }

    /*
     * Qué hace: Registra un nuevo tipo de vehículo después de validar sus datos y comprobar que no exista otro tipo registrado con el mismo nombre
     * Recibe: El objeto TipoVehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    public void registrarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) != null) {

            throw new IllegalArgumentException("Ya existe un tipo de vehículo con ese nombre.");

        }

        tipoVehiculoRepositorio.agregar(tipo);

    }

    /*
     * Qué hace: Busca dentro del repositorio un tipo de vehículo utilizando su nombre como criterio de identificación
     * Recibe: El nombre del tipo de vehículo que se desea buscar
     * Retorna: El tipo de vehículo encontrado o null si no existe uno con ese nombre
     */
    public TipoVehiculo buscarPorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {

            return null;

        }

        return tipoVehiculoRepositorio.buscarPorNombre(nombre.trim());

    }

    /*
     * Qué hace: Obtiene todos los tipos de vehículo que se encuentran registrados actualmente dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de tipos de vehículo registrados
     */
    public List<TipoVehiculo> obtenerTodos() {

        return tipoVehiculoRepositorio.obtenerTodos();

    }

    /*
     * Qué hace: Modifica los datos de un tipo de vehículo existente después de validar su información y comprobar que se encuentre registrado en el sistema
     * Recibe: El objeto TipoVehiculo que contiene los datos que se desean modificar
     * Retorna: No retorna ningún valor
     */
    public void modificarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) == null) {

            throw new IllegalArgumentException("El tipo de vehículo no existe.");

        }

        tipoVehiculoRepositorio.modificar(tipo);

    }

    /*
     * Qué hace: Busca un tipo de vehículo por su nombre y, si existe, solicita su eliminación mediante el repositorio correspondiente
     * Recibe: El nombre del tipo de vehículo que se desea eliminar
     * Retorna: No retorna ningún valor
     */
    public void eliminarTipo(String nombre) {

        TipoVehiculo tipo = buscarPorNombre(nombre);

        if (tipo == null) {

            throw new IllegalArgumentException("El tipo de vehículo no existe.");

        }

        tipoVehiculoRepositorio.eliminar(nombre);

    }

    /*
     * Qué hace: Verifica que el tipo de vehículo proporcionado sea válido, comprobando que exista, que tenga un nombre definido y que tenga asignada una categoría
     * Recibe: El objeto TipoVehiculo cuyos datos se desean validar
     * Retorna: No retorna ningún valor
     */
    private void validarTipo(TipoVehiculo tipo) {

        if (tipo == null) {

            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo.");

        }

        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException("El nombre del tipo es obligatorio.");

        }

        if (tipo.getCategoria() == null) {

            throw new IllegalArgumentException("La categoría del vehículo es obligatoria.");

        }

    }

}
