package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.PessoaCurso;
import dao.Interface.PessoaCursoDAO;

public class PessoaCursoDAOImpl extends HibernateGenericDAO<PessoaCurso, Long> implements PessoaCursoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public ArrayList<PessoaCurso> buscarTodasPessoaCursoPorPessoa(long codigo){
		
		ArrayList<PessoaCurso> pessoaCurso = new ArrayList<PessoaCurso>();
		try {
		em.getTransaction().begin();
    	Query query = em.createQuery("FROM PessoaCurso WHERE id_pessoa = :codigo").setParameter("codigo",  codigo );
    	pessoaCurso = (ArrayList<PessoaCurso>) query.getResultList();
		em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		
		
		
		
		return pessoaCurso;
    }
	
	
	
	

}
