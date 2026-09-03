package cr.ac.una.est.sistemaalquilercarros.repositorio;

import java.util.ArrayList;
import java.util.List;

public class Repositorio <T> {


private final List<T> elementos;

public Repositorio() {
        elementos = new ArrayList<>();
}

public void agregar(T elemento) {
    elementos.add(elemento);
}

public boolean eliminar(T elemento) {
    return elementos.remove(elemento);
}

public List<T> obetenerTodos() {
    return new ArrayList<>(elementos);
}

public int cantidad(){
    return elementos.size();
}

public boolean estaVacio(){
    return elementos.isEmpty();
}

public void remplazarTodos(List<T> nuevosElementos){
    elementos.clear();
    elementos.addAll(nuevosElementos);
}

}
