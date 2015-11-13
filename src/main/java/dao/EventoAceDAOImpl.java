

package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.EventoAce;
import dao.Interface.EventoAceDAO;

public class EventoAceDAOImpl extends HibernateGenericDAO<EventoAce, Long> implements EventoAceDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public ArrayList<EventoAce> buscarPorAluno(long codigo){
		
		ArrayList<EventoAce> eventosAce = new ArrayList<EventoAce>();
		try {
		em.getTransaction().begin();
    	Query query = em.createQuery("FROM EventoAce WHERE id_aluno = :codigo order by periodo").setParameter("codigo",  codigo );
    	eventosAce = (ArrayList<EventoAce>) query.getResultList();
		em.getTransaction().commit();
		} catch (Exception e) {
			
			em.getTransaction().commit();
			return null;
			
		}
	
		
		return eventosAce;
    }


}
