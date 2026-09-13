package cr.ac.una.est.sistemaalquilercarros.persistencia;

import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO_DATOS = "datos.dat";

    /*
     * Qué hace: Guarda en el archivo datos.dat toda la información contenida en DatosSistema mediante serialización, permitiendo conservar los registros aunque la aplicación sea cerrada
     * Recibe: El objeto DatosSistema que contiene los vehículos, tipos de vehículo, clientes y alquileres que deben guardarse
     * Retorna: No retorna ningún valor
     */
    public void guardar (DatosSistema datos){
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))){
            salida.writeObject(datos);
        }catch (IOException e){
            throw new RuntimeException("No se pudieron guardar los datos",e);
        }
    }

    /*
     * Qué hace: Intenta cargar desde el archivo datos.dat toda la información previamente guardada y, si el archivo todavía no existe, crea una nueva estructura de datos vacía para iniciar el sistema
     * Recibe: No recibe parámetros
     * Retorna: Un objeto DatosSistema con la información recuperada del archivo o un nuevo DatosSistema si todavía no existen datos guardados
     */
    public DatosSistema cargar(){
        File archivo = new File(ARCHIVO_DATOS);

        if(!archivo.exists()){
            return new DatosSistema();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))){
            return (DatosSistema) entrada.readObject();
        }catch(IOException | ClassNotFoundException e){
            throw new RuntimeException("No se pudieron cargar los archivos",e);
        }
    }

}
