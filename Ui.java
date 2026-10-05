package ni.edu.uam.facturacionapp.Controller;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
public final class Ui {
 private Ui() {}
 public static <T> void ejecutar(Pane root, Callable<T> trabajo, Consumer<T> listo, Consumer<Throwable> error) {
  root.setDisable(true);
  Task<T> task=new Task<>() { @Override protected T call() throws Exception { return trabajo.call(); } };
  task.setOnSucceeded(e->{ root.setDisable(false); listo.accept(task.getValue()); });
  task.setOnFailed(e->{ root.setDisable(false); error.accept(task.getException()); });
  Thread hilo=new Thread(task,"consulta-catalogos"); hilo.setDaemon(true); hilo.start();
 }
 public static String mensaje(Throwable e) {
  if(e instanceof SQLException sql) {
   String estado=sql.getSQLState();
   if("23505".equals(estado)) return "El código o dato único ya está registrado. Utiliza otro valor.";
   if("23503".equals(estado)) return "No puede eliminar la categoría porque tiene productos asociados.";
   if(estado!=null && estado.startsWith("08")) return "No se pudo conectar con la base de datos. Revisa el servidor y la configuración de conexión.";
   if("28P01".equals(estado)) return "Usuario o contraseña de la base de datos incorrectos.";
   if("42P01".equals(estado)) return "Faltan tablas. Ejecuta el script de estructura de la base de datos.";
   return "No se completó la operación en la base de datos (estado "+estado+"). Revisa la configuración y las tablas.";
  }
  return e.getMessage()==null?"No se pudo completar la operación.":e.getMessage();
 }
 public static void advertencia(String encabezado, String mensaje) {
  Alert a=new Alert(Alert.AlertType.WARNING);
  a.setTitle("Validación");
  a.setHeaderText(encabezado);
  a.setContentText(mensaje);
  a.showAndWait();
 }
 public static void error(Throwable e) {
  Alert a=new Alert(Alert.AlertType.ERROR);a.setTitle("Revisa la operación");a.setHeaderText("No se guardaron cambios");a.setContentText(mensaje(e));a.showAndWait();
 }
}
