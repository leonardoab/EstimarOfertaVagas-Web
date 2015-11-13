

package dao.Interface;



import java.util.ArrayList;

import model.EventoAce;


public interface EventoAceDAO extends GenericDAO<EventoAce, Long> {

	public ArrayList<EventoAce> buscarPorAluno(long codigo);
	
	
}