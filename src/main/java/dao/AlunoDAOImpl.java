package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Aluno;
import dao.Interface.AlunoDAO;

public class AlunoDAOImpl extends HibernateGenericDAO<Aluno, Long> implements AlunoDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public Aluno buscarPorMatricula(String variavel) {
		Aluno aluno = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Aluno WHERE matricula = :codigo");	
			query.setParameter("codigo", variavel);
			aluno = (Aluno) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return aluno;
	}
	
	public Aluno buscarPorNome(String variavel) {
		Aluno aluno = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Aluno WHERE nome = :codigo")	.setParameter("codigo", variavel);
			aluno = (Aluno) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return aluno;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosAlunoMatricula(String variavel,Long idcurso){


		ArrayList<String> alunoMatricula = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select matricula FROM Aluno WHERE matricula like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);
			alunoMatricula = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return alunoMatricula;

	}
	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosAlunoNome(String variavel,Long idcurso){


		ArrayList<String> alunoNome = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select nome FROM Aluno WHERE nome like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);
			alunoNome = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return alunoNome;

	}
	@SuppressWarnings("unchecked")
	public ArrayList<Aluno> buscarTodosAlunoNomeObjeto(String variavel,Long idcurso){


		ArrayList<Aluno> alunoNome = new ArrayList<Aluno>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Aluno WHERE nome like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso", idcurso);
			alunoNome = (ArrayList<Aluno>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}	

		return alunoNome;

	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Aluno> buscarTodosAlunoCursoGradeObjeto(Long idCurso,Long idGrade ){


		ArrayList<Aluno> alunoNome = new ArrayList<Aluno>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Aluno WHERE id_curso = :idCurso and id_grade = :idGrade ");
			query.setParameter("idCurso",  idCurso );
			query.setParameter("idGrade",  idGrade );
			alunoNome = (ArrayList<Aluno>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			
			return null;
		}	

		return alunoNome;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Aluno> buscarTodosAlunoCursoObjeto(Long idCurso){


		ArrayList<Aluno> alunoNome = new ArrayList<Aluno>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Aluno WHERE id_curso = :idCurso  ");
			query.setParameter("idCurso",  idCurso );
			alunoNome = (ArrayList<Aluno>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			
			return null;
		}	

		return alunoNome;

	}
	
	
	

}
