package dao;

import modelo.Reseña;

public interface ReseñaDAO {
    void guardar(Reseña resenia);
    void eliminar(int idResenia);
    boolean existenResenasPorCliente(int clienteID);
}
