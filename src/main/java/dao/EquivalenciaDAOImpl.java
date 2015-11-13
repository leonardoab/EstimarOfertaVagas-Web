package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Equivalencia;
import dao.Interface.EquivalenciaDAO;

public class EquivalenciaDAOImpl extends HibernateGenericDAO<Equivalencia, Long> implements EquivalenciaDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public ArrayList<Equivalencia> buscarTodasEquivalenciasPorGrade(long codigo){
		
		ArrayList<Equivalencia> equivalencias = new ArrayList<Equivalencia>();
		try {
		em.getTransaction().begin();
    	Query query = em.createQuery("FROM Equivalencia WHERE id_grade = :codigo").setParameter("codigo",  codigo );
    	equivalencias = (ArrayList<Equivalencia>) query.getResultList();
		em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		
		
		
		
		return equivalencias;
    }
	
	
	public Equivalencia buscarPorEquivalencia(long idDisciplinaEsquerda,long idDisciplinaDireita,long idGrade) {
		Equivalencia equivalencia = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Equivalencia WHERE id_disciplina = :iddisciplinaesquerda and id_disciplina_grade = :iddisciplinadireita and id_grade = :idgrade")	;
			query.setParameter("iddisciplinaesquerda", idDisciplinaEsquerda);
			query.setParameter("iddisciplinadireita", idDisciplinaDireita);
			query.setParameter("idgrade", idGrade);
			
			
			equivalencia = (Equivalencia) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return equivalencia;
	}
	
	
	
	

}
