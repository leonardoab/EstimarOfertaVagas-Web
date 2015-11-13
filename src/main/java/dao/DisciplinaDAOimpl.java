package dao;


import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Disciplina;
import dao.Interface.DisciplinaDAO;

public class DisciplinaDAOimpl extends HibernateGenericDAO<Disciplina, Long> implements DisciplinaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosCodigosDisciplina(String variavel){

		ArrayList<String> disciplinasCodigos = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select codigo FROM Disciplina WHERE codigo like :codigo order by codigo").setParameter("codigo", "%" + variavel + "%");
			disciplinasCodigos = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return disciplinasCodigos;

	}

	public Disciplina buscarPorCodigoDisciplina(String variavel) {
		Disciplina disciplina = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Disciplina WHERE codigo = :codigo")	.setParameter("codigo", variavel);
			disciplina = (Disciplina) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return disciplina;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosNomesDisciplina(String variavel){


		ArrayList<String> disciplinasNomes = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select nome FROM Disciplina WHERE nome like :codigo").setParameter("codigo", "%" + variavel + "%");
			disciplinasNomes = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return disciplinasNomes;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Disciplina> buscarTodosNomesDisciplinaObjeto(String variavel){

		ArrayList<Disciplina> disciplinasNomes = new ArrayList<Disciplina>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Disciplina WHERE nome like :codigo order by codigo" ).setParameter("codigo", "%" + variavel + "%");
			disciplinasNomes =  (ArrayList<Disciplina>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return disciplinasNomes;

	}

	public Disciplina buscarPorNomeDisciplina(String variavel) {
		Disciplina disciplina = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Disciplina WHERE nome = :codigo")	.setParameter("codigo", variavel);
			disciplina = (Disciplina) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return disciplina;
	}

}
