package repositorio;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio genérico encargado de almacenar objetos en memoria.
 *
 * @param <T> tipo de objeto que será almacenado.
 */
public class Repositorio<T> {

    private List<T> elementos;

    /**
     * Inicializa el repositorio con una lista vacía.
     *
     * @return no retorna ningún valor porque es un constructor.
     */
    public Repositorio() {
        elementos = new ArrayList<>();
    }

    /**
     * Agrega un elemento al repositorio.
     *
     * @param elemento objeto que será almacenado.
     * @return no retorna ningún valor.
     */
    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    /**
     * Elimina un elemento del repositorio.
     *
     * @param elemento objeto que se desea eliminar.
     * @return true si se eliminó correctamente o false si no se encontró.
     */
    public boolean eliminar(T elemento) {
        return elementos.remove(elemento);
    }

    /**
     * Obtiene todos los elementos almacenados.
     *
     * @return una copia de la lista de elementos.
     */
    public List<T> obtenerTodos() {
        return new ArrayList<>(elementos);
    }

    /**
     * Obtiene la cantidad de elementos almacenados.
     *
     * @return cantidad de elementos.
     */
    public int cantidad() {
        return elementos.size();
    }
}