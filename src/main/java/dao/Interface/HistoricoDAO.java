package dao.Interface;



import java.util.ArrayList;

import model.Historico;

public interface HistoricoDAO extends GenericDAO<Historico, Long> {

	public ArrayList<Historico> buscarTodosHistoricosPorMatricula(long codigo);
	public void deletaTodos();
}