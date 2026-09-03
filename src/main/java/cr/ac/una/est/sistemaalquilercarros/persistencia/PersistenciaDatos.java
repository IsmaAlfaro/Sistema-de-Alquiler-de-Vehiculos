package cr.ac.una.est.sistemaalquilercarros.persistencia;

import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO_DATOS = "datos.dat";

    public void guardar (DatosSistema datos){
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))){
            salida.writeObject(datos);
        }catch (IOException e){
            throw new RuntimeException("No se pudieron guardar los datos",e);
        }
    }

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
