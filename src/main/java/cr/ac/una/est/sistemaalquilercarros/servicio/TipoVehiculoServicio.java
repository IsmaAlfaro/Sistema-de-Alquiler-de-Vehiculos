package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.CategoriaVehiculo;
import cr.ac.una.est.sistemaalquilercarros.modelo.TipoVehiculo;
import cr.ac.una.est.sistemaalquilercarros.persistencia.DatosSistema;
import cr.ac.una.est.sistemaalquilercarros.persistencia.PersistenciaDatos;
import cr.ac.una.est.sistemaalquilercarros.repositorio.Repositorio;

import java.util.List;

public class TipoVehiculoServicio {

    private final Repositorio<TipoVehiculo> repositorioTipos;
    private final DatosSistema datosSistema;
    private final PersistenciaDatos persistencia;

    /*
     * Qué hace: Inicializa el servicio encargado de administrar los tipos de vehículo, carga los tipos previamente almacenados y, si todavía no existe ninguno, crea automáticamente los tipos iniciales definidos por el sistema
     * Recibe: El objeto DatosSistema y el servicio encargado de guardar la información
     * Retorna: No retorna ningún valor
     */
    public TipoVehiculoServicio(DatosSistema datosSistema, PersistenciaDatos persistencia) {

        this.datosSistema = datosSistema;
        this.persistencia = persistencia;

        repositorioTipos = new Repositorio<>();

        if (datosSistema.getTiposVehiculo() != null) {
            repositorioTipos.reemplazarTodos(
                    datosSistema.getTiposVehiculo()
            );
        }

        if (repositorioTipos.estaVacio()) {
            crearTiposIniciales();
        }
    }

    /*
     * Qué hace: Registra un nuevo tipo de vehículo después de validar sus datos y comprobar que no exista otro tipo registrado con el mismo nombre
     * Recibe: El objeto TipoVehiculo que se desea registrar
     * Retorna: No retorna ningún valor
     */
    public void registrarTipo(TipoVehiculo tipo) {

        validarTipo(tipo);

        if (buscarPorNombre(tipo.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un tipo de vehículo con ese nombre");
        }

        repositorioTipos.agregar(tipo);

        guardarCambios();
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

        for (TipoVehiculo tipo : repositorioTipos.obtenerTodos()) {

            if (tipo.getNombre().equalsIgnoreCase(nombre.trim())) {
                return tipo;
            }
        }

        return null;
    }

    /*
     * Qué hace: Obtiene todos los tipos de vehículo que se encuentran registrados actualmente dentro del sistema
     * Recibe: No recibe parámetros
     * Retorna: La lista completa de tipos de vehículo registrados
     */
    public List<TipoVehiculo> obtenerTodos() {
        return repositorioTipos.obtenerTodos();
    }

    /*
     * Qué hace: Busca un tipo de vehículo por su nombre y, si existe, lo elimina del repositorio y guarda posteriormente los cambios realizados
     * Recibe: El nombre del tipo de vehículo que se desea eliminar
     * Retorna: No retorna ningún valor
     */
    public void eliminarTipo(String nombre) {

        TipoVehiculo tipo = buscarPorNombre(nombre);

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no existe");
        }

        repositorioTipos.eliminar(tipo);

        guardarCambios();
    }

    /*
     * Qué hace: Verifica que el tipo de vehículo proporcionado sea válido, comprobando que exista, que tenga un nombre definido y que tenga asignada una categoría
     * Recibe: El objeto TipoVehiculo cuyos datos se desean validar
     * Retorna: No retorna ningún valor
     */
    private void validarTipo(TipoVehiculo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo");
        }

        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo es obligatorio");
        }

        if (tipo.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría del vehículo es obligatoria");}}

    /*
     * Qué hace: Crea y registra automáticamente los tipos de vehículo iniciales del sistema, incluyendo Sedán, SUV, Minivan, CUV, Deportivo y Pickup, con sus respectivas categorías y características requeridas
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void crearTiposIniciales() {

        repositorioTipos.agregar(new TipoVehiculo("Sedán", CategoriaVehiculo.PASAJEROS, true, false, false));

        repositorioTipos.agregar(new TipoVehiculo("SUV", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Minivan", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("CUV", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Deportivo", CategoriaVehiculo.PASAJEROS, true, true, false));

        repositorioTipos.agregar(new TipoVehiculo("Pickup", CategoriaVehiculo.CARGA, false, true, true));

        guardarCambios();
    }

    /*
     * Qué hace: Actualiza en DatosSistema la lista actual de tipos de vehículo y posteriormente guarda toda la información mediante el mecanismo de persistencia
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    private void guardarCambios() {

        datosSistema.setTiposVehiculo(repositorioTipos.obtenerTodos());

        persistencia.guardar(datosSistema);
    }
}