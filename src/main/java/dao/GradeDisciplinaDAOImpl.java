package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.GradeDisciplina;
import dao.Interface.GradeDisciplinaDAO;

public class GradeDisciplinaDAOImpl extends HibernateGenericDAO<GradeDisciplina, Long> implements GradeDisciplinaDAO, Serializable {

	private static final long serialVersionUID = 1L;


	@SuppressWarnings("unchecked")
	public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGrade(long codigo){

		ArrayList<GradeDisciplina> gradeDisciplinas = new ArrayList<GradeDisciplina>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_grade = :codigo order by periodo").setParameter("codigo",  codigo );
			gradeDisciplinas = (ArrayList<GradeDisciplina>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {

			return null;
		}




		return gradeDisciplinas;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<GradeDisciplina> buscarTodasGradeDisciplinaPorGradeId(long idGrade){

		ArrayList<GradeDisciplina> gradeDisciplinas = new ArrayList<GradeDisciplina>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_grade = :idGrade order by periodo");
			query.setParameter("idGrade",  idGrade );
			
			gradeDisciplinas = (ArrayList<GradeDisciplina>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {

			return null;
		}




		return gradeDisciplinas;
	}



	public GradeDisciplina buscarPorPK(String tipo,Long periodo,Long idGrade,Long idDisciplina) {
		GradeDisciplina gradeDisciplina = null;
		try {
			em.getTransaction().begin();
			Query query = (em.createQuery("FROM GradeDisciplina WHERE tipo = :tipo and periodo = :periodo").setParameter("tipo", tipo)).setParameter("periodo", periodo);
			gradeDisciplina = (GradeDisciplina) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {

			return null;
		}
		return gradeDisciplina;
	}

	public GradeDisciplina buscarPorDisciplinaGrade(Long idGrade,Long idDisciplina) {
		GradeDisciplina gradeDisciplina = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_disciplina = :idDisciplina and id_grade = :idGrade");
			query.setParameter("idGrade", idGrade);
			query.setParameter("idDisciplina", idDisciplina);
			gradeDisciplina = (GradeDisciplina) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {

			return null;
		}
		return gradeDisciplina;
	}



	@SuppressWarnings("unchecked")
	public ArrayList<GradeDisciplina> buscarPorTipoGrade(Long idGrade,String tipo){

		ArrayList<GradeDisciplina> gradeDisciplinas = new ArrayList<GradeDisciplina>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM GradeDisciplina WHERE id_grade = :idGrade and tipo_disciplina = :tipo");
			query.setParameter("idGrade", idGrade);
			query.setParameter("tipo", tipo);
			gradeDisciplinas = (ArrayList<GradeDisciplina>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {

			return null;
		}




		return gradeDisciplinas;
	}




}
