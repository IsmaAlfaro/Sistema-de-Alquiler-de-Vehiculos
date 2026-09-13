package cr.ac.una.est.sistemaalquilercarros.repositorio;

import java.util.ArrayList;
import java.util.List;

public class Repositorio <T> {


private final List<T> elementos;

    /*
     * Qué hace: Crea un nuevo repositorio genérico e inicializa una lista vacía que permitirá almacenar elementos de cualquier tipo definido al momento de utilizar la clase
     * Recibe: No recibe parámetros
     * Retorna: No retorna ningún valor
     */
    public Repositorio() {
        elementos = new ArrayList<>();
}

    /*
     * Qué hace: Agrega un nuevo elemento al repositorio y lo incorpora a la lista interna utilizada para almacenar los datos administrados por esta clase
     * Recibe: El elemento de tipo genérico que se desea agregar al repositorio
     * Retorna: No retorna ningún valor
     */
    public void agregar(T elemento) {
    elementos.add(elemento);
}

    /*
     * Qué hace: Intenta eliminar del repositorio el elemento indicado y determina si la eliminación pudo realizarse correctamente
     * Recibe: El elemento de tipo genérico que se desea eliminar del repositorio
     * Retorna: Verdadero si el elemento fue eliminado correctamente o falso si no se encontraba en la lista
     */
    public boolean eliminar(T elemento) {
    return elementos.remove(elemento);
}

    /*
     * Qué hace: Obtiene todos los elementos almacenados actualmente en el repositorio y crea una nueva lista para evitar devolver directamente la colección interna original
     * Recibe: No recibe parámetros
     * Retorna: Una nueva lista que contiene todos los elementos almacenados en el repositorio
     */
    public List<T> obtenerTodos() {
    return new ArrayList<>(elementos);
}

    /*
     * Qué hace: Calcula la cantidad total de elementos que se encuentran almacenados actualmente dentro del repositorio
     * Recibe: No recibe parámetros
     * Retorna: La cantidad de elementos almacenados
     */
    public int cantidad(){
    return elementos.size();
}

    /*
     * Qué hace: Verifica si el repositorio actualmente no contiene ningún elemento almacenado en su lista interna
     * Recibe: No recibe parámetros
     * Retorna: Verdadero si el repositorio está vacío o falso si contiene al menos un elemento
     */
    public boolean estaVacio(){
    return elementos.isEmpty();
}

    /*
     * Qué hace: Elimina todos los elementos almacenados actualmente en el repositorio y los reemplaza completamente por los elementos contenidos en una nueva lista
     * Recibe: La nueva lista de elementos que se desea almacenar en el repositorio
     * Retorna: No retorna ningún valor
     */
    public void reemplazarTodos(List<T> nuevosElementos){
    elementos.clear();
    elementos.addAll(nuevosElementos);
}

}
