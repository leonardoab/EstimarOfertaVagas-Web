package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Grade;
import dao.Interface.GradeDAO;

public class GradeDAOImpl extends HibernateGenericDAO<Grade, Long> implements GradeDAO, Serializable {

	private static final long serialVersionUID = 1L;


	@SuppressWarnings("unchecked")
	public ArrayList<String> buscarTodosCodigosGrade(String variavel,long idCurso){


		ArrayList<String> gradesCodigos = new ArrayList<String>();

		try {
			em.getTransaction().begin();
			Query query = em.createQuery("Select codigo FROM Grade WHERE codigo like :codigo and id_curso = :idcurso");
			query.setParameter("codigo", "%" + variavel + "%");
			query.setParameter("idcurso",  idCurso );			
			gradesCodigos = (ArrayList<String>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			
			return null;
		}	

		return gradesCodigos;

	}


	public Grade buscarPorCodigoGrade(String variavel,long idCurso) {
		Grade grade = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM model.Grade WHERE codigo = :codigo and id_curso = :idcurso");	
			query.setParameter("codigo", variavel);
			query.setParameter("idcurso", idCurso);
			grade = (Grade) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return grade;
	}


















}
