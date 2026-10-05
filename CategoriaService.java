package ni.edu.uam.facturacionapp.Service;

import ni.edu.uam.facturacionapp.Model.Categoria;
import ni.edu.uam.facturacionapp.DAO.CategoriaDao;
import java.sql.SQLException;
import java.util.List;

public class CategoriaService {
    private final CategoriaDao dao = new CategoriaDao();

    public List<Categoria> listar() throws SQLException {
        return dao.listar();
    }

    public void guardar(Categoria e) throws SQLException {
        if (e == null) {
            throw new IllegalArgumentException("No se recibió una categoría válida.");
        }

        e.setNombre(Validacion.texto(e.getNombre(), "Nombre", 100, true));

        if (dao.existeNombre(e.getNombre(), e.getId())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }

        if (e.getId() == null) {
            dao.guardar(e);
        } else {
            dao.actualizar(e);
        }
    }

    public void eliminar(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        if (dao.tieneProductos(id)) {
            throw new IllegalArgumentException(
                    "No puede eliminar la categoría porque tiene productos asociados.");
        }

        dao.eliminar(id);
    }
}
