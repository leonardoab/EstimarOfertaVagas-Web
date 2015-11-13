package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Curso;
import dao.Interface.CursoDAO;

public class CursoDAOImpl extends HibernateGenericDAO<Curso, Long> implements CursoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static CursoDAOImpl instancia;
	
	
	
	public static synchronized CursoDAOImpl getInstance(){

		if (instancia == null){

			instancia = new CursoDAOImpl();

		}
		return instancia;



	}
	
	
	
	

	public Curso buscarPorCodigo(String variavel) {
		Curso curso = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Curso WHERE codigo = :codigo")	.setParameter("codigo", variavel);
			curso = (Curso) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		finally {

		}

		return curso;
	}
	
	public Curso buscarPorNome(String variavel) {
		Curso curso = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Curso WHERE nome = :codigo")	.setParameter("codigo", variavel);
			curso = (Curso) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		finally {

		}

		return curso;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosCodigosCurso(String variavel){


		ArrayList<String> cursosCodigos = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select codigo FROM Curso WHERE codigo like :codigo").setParameter("codigo", "%" + variavel + "%");
			cursosCodigos = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return cursosCodigos;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosNomesCurso(String variavel){


		ArrayList<String> cursosNomes = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select nome FROM Curso WHERE nome like :codigo").setParameter("codigo", "%" + variavel + "%");
			cursosNomes = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return cursosNomes;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Curso> buscarTodosNomesObjetoCurso(String variavel){


		ArrayList<Curso> cursosNomes = new ArrayList<Curso>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Curso WHERE nome like :codigo").setParameter("codigo", "%" + variavel + "%");
			cursosNomes = (ArrayList<Curso>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return cursosNomes;

	}

}
