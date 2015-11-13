package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.PreRequisito;
import dao.Interface.PreRequisitoDAO;

public class PreRequisitoDAOImpl extends HibernateGenericDAO<PreRequisito, Long> implements PreRequisitoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PreRequisito> buscarPorTodosCodigoGradeDisc(Long codigo){
		
		ArrayList<PreRequisito> preRequisitos = new ArrayList<PreRequisito>();
		try {
		em.getTransaction().begin();
    	Query query = em.createQuery("FROM PreRequisito WHERE id_grade_disciplina = :codigo").setParameter("codigo",  codigo );
    	preRequisitos = (ArrayList<PreRequisito>) query.getResultList();
		em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}		
		
		return preRequisitos;
    }
	
	
	public Long buscarPorDisciplanaGradeId(Long idGrade ,Long idDisciplina) {
		Long idPrerequisito = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select id FROM PreRequisito WHERE id_grade_disciplina = :idGrade and id_disciplina = :idDisciplina" );
			query.setParameter("idGrade", idGrade);
			query.setParameter("idDisciplina", idDisciplina);
			
			
			idPrerequisito = (Long) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return (long) 0;
		}
		return idPrerequisito;
	}
	
	

}
