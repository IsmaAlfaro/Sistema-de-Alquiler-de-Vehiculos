package alquilercarros.servicio;

import alquilercarros.modelo.Alquiler;
import alquilercarros.modelo.Carro;
import alquilercarros.modelo.Cliente;
import alquilercarros.modelo.EstadoVehiculo;
import alquilercarros.repositorio.Repositorio;

import java.util.ArrayList;
import java.util.List;

public class AlquilerServicio {
    private static final double DEPOSITO_GARANTIA = 50000;
    private Repositorio<Carro> repositorioCarros;
    private Repositorio<Cliente> repositorioClientes;
    private Repositorio<Alquiler> repositorioAlquileres;
    private int siguienteNumeroAlquiler;

    //Inicia los repositorios del sistema
    public AlquilerService() {
        repositorioCarros = new Repositorio<>();
        repositorioClientes = new Repositorio<>();
        repositorioAlquileres = new Repositorio<>();
        siguienteNumeroAlquiler = 1;
}

    //Registra un carro nuevo
    public void registrarCarro(String placa, String marca, String modelo,
                               double tarifaDiaria, int pasajeros, String transmision) {
        validarTexto(placa, "La placa es obligatoria.");
        validarTexto(marca, "La marca es obligatoria.");
        validarTexto(modelo, "El modelo es obligatorio.");
        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero.");
        }
        if (pasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }
        if (buscarCarroPorPlaca(placa) != null) {
            throw new IllegalArgumentException("Ya existe un carro con esa placa.");
        }
        Carro nuevo = new Carro(placa.trim(), marca.trim(), modelo.trim(),
                tarifaDiaria, pasajeros, transmision);
        repositorioCarros.agregar(nuevo);
    }

    //Busca carro por placas
    public Carro buscarCarroPorPlaca(String placa) {
        for (Carro carro : repositorioCarros.obtenerTodos()) {
            if (carro.getPlaca().equalsIgnoreCase(placa.trim())) {
                return carro;
                Página 15
            }
        }
        return null;
    }

    //Modifica un carro que ya existe
    public void modificarCarro(String placaOriginal, String nuevaPlaca,
                               String marca, String modelo, double tarifa,
                               int pasajeros, String transmision) {
        Carro carro = buscarCarroPorPlaca(placaOriginal);
        if (carro == null) {
            throw new IllegalArgumentException("El carro seleccionado no existe.");
        }
        if (carro.getEstado() == EstadoVehiculo.ALQUILADO) {
            throw new IllegalArgumentException("No puede modificar un carro que está alquilado.");
        }
        validarTexto(nuevaPlaca, "La placa es obligatoria.");
        validarTexto(marca, "La marca es obligatoria.");
        validarTexto(modelo, "El modelo es obligatorio.");
        if (tarifa <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        }
        if (pasajeros <= 0) {
            throw new IllegalArgumentException("Los pasajeros deben ser mayores que cero.");
        }
        Carro carroMismaPlaca = buscarCarroPorPlaca(nuevaPlaca);
        if (carroMismaPlaca != null && carroMismaPlaca != carro) {
            throw new IllegalArgumentException("La nueva placa ya pertenece a otro carro.");
        }
        carro.setPlaca(nuevaPlaca.trim());
        carro.setMarca(marca.trim());
        carro.setModelo(modelo.trim());
        carro.setTarifaDiaria(tarifa);
        carro.setCantidadPasajeros(pasajeros);
        carro.setTransmision(transmision);
    }

    //Elimina un carro disponible
    public void eliminarCarro(String placa) {
        Carro carro = buscarCarroPorPlaca(placa);
        if (carro == null) {
            throw new IllegalArgumentException("El carro no existe.");
        }
        if (carro.getEstado() == EstadoVehiculo.ALQUILADO) {
            throw new IllegalArgumentException("No puede eliminar un carro alquilado.");
        }
        repositorioCarros.eliminar(carro);
    }
    /** @return todos los carros. */
    public List<Carro> obtenerCarros() {
        return repositorioCarros.obtenerTodos();
    }
    /** @return solamente los carros disponibles. */
    public List<Carro> obtenerCarrosDisponibles() {
        List<Carro> disponibles = new ArrayList<>();
        for (Carro carro : repositorioCarros.obtenerTodos()) {
            if (carro.getEstado() == EstadoVehiculo.DISPONIBLE) {
                disponibles.add(carro);
            }
        }
        return disponibles;
    }

    //Registra un cliente
    public void registrarCliente(String cedula, String nombre,
                                 String telefono, String correo) {
        validarTexto(cedula, "La cédula es obligatoria.");
        validarTexto(nombre, "El nombre es obligatorio.");
        validarTexto(telefono, "El teléfono es obligatorio.");
        validarTexto(correo, "El correo es obligatorio.");
        if (buscarClientePorCedula(cedula) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con esa cédula.");
        }
        Cliente cliente = new Cliente(cedula.trim(), nombre.trim(),
                telefono.trim(), correo.trim());
        repositorioClientes.agregar(cliente);
    }

    //Busca cliente por cedula
    public Cliente buscarClientePorCedula(String cedula) {
        for (Cliente cliente : repositorioClientes.obtenerTodos()) {
            if (cliente.getCedula().equalsIgnoreCase(cedula.trim())) {
                return cliente;
            }
        }
        return null;
    }

    //Modificar cliente que ya existe
    public void modificarCliente(String cedulaOriginal, String nuevaCedula,
                                 String nombre, String telefono, String correo) {
        Cliente cliente = buscarClientePorCedula(cedulaOriginal);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        validarTexto(nuevaCedula, "La cédula es obligatoria.");
        validarTexto(nombre, "El nombre es obligatorio.");
        validarTexto(telefono, "El teléfono es obligatorio.");
        validarTexto(correo, "El correo es obligatorio.");
        Cliente clienteMismaCedula = buscarClientePorCedula(nuevaCedula);
        if (clienteMismaCedula != null && clienteMismaCedula != cliente) {
            throw new IllegalArgumentException("La nueva cédula ya pertenece a otro cliente.");
        }
        cliente.setCedula(nuevaCedula.trim());
        cliente.setNombre(nombre.trim());
        cliente.setTelefono(telefono.trim());
        cliente.setCorreo(correo.trim());
    }

    //Elimina clientes que no son activos
    public void eliminarCliente(String cedula) {
        Cliente cliente = buscarClientePorCedula(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        for (Alquiler alquiler : obtenerAlquileresActivos()) {
            if (alquiler.getCliente() == cliente) {
                throw new IllegalArgumentException(
                        "No puede eliminar un cliente con un alquiler activo.");
            }
        }
        repositorioClientes.eliminar(cliente);
    }

    //Retorna todos los clientes
    public List<Cliente> obtenerClientes() {
        return repositorioClientes.obtenerTodos();
    }

    //Crea los alquileres y cambia el carro a ALQUILADO
    public Alquiler crearAlquiler(Cliente cliente, Carro carro, int dias) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        }
        if (carro == null) {
            throw new IllegalArgumentException("Debe seleccionar un carro.");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("La cantidad de días debe ser mayor que cero.");
        }
        if (carro.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalArgumentException("El carro no se encuentra disponible.");
        }
        Alquiler alquiler = new Alquiler(siguienteNumeroAlquiler, cliente,
                carro, dias, DEPOSITO_GARANTIA);
        repositorioAlquileres.agregar(alquiler);
        carro.setEstado(EstadoVehiculo.ALQUILADO);
        siguienteNumeroAlquiler++;
        return alquiler;
    }

    //Finaliza un alquiler y deja disponible al carro
    public double devolverAlquiler(Alquiler alquiler, int diasAtraso) {
        if (alquiler == null) {
            throw new IllegalArgumentException("Debe seleccionar un alquiler.");
        }
        if (!alquiler.isActivo()) {
            throw new IllegalArgumentException("El alquiler ya fue devuelto.");
        }
        if (diasAtraso < 0) {
            throw new IllegalArgumentException("Los días de atraso no pueden ser negativos.");
        }
        double multa = alquiler.calcularMulta(diasAtraso);
        alquiler.finalizar();
        alquiler.getCarro().setEstado(EstadoVehiculo.DISPONIBLE);
        return multa;
    }

    //Retorna historial completa de los alquileres
    public List<Alquiler> obtenerAlquileres() {
        return repositorioAlquileres.obtenerTodos();
    }
    /** @return alquileres pendientes de devolución. */
    public List<Alquiler> obtenerAlquileresActivos() {
        List<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : repositorioAlquileres.obtenerTodos()) {
            if (alquiler.isActivo()) {
                activos.add(alquiler);
            }
        }
        return activos;
    }

    public double getDepositoGarantia() {
        return DEPOSITO_GARANTIA;
    }


    private void validarTexto(String texto, String mensaje) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}

