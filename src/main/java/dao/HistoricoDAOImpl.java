package dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import model.Historico;
import dao.Interface.HistoricoDAO;

public class HistoricoDAOImpl extends HibernateGenericDAO<Historico, Long> implements HistoricoDAO, Serializable {

	private static final long serialVersionUID = 1L;


	@SuppressWarnings("unchecked")
	public ArrayList<Historico> buscarTodosHistoricosPorMatricula(long codigo){

		ArrayList<Historico> historicos = new ArrayList<Historico>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Historico WHERE id_matricula = :codigo").setParameter("codigo",  codigo );
			historicos = (ArrayList<Historico>) query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}

		return historicos;
	}


	public void deletaTodos(){


		try {
			em.getTransaction().begin();
			Query query = em.createQuery("delete FROM Historico ");
			int rowCount = query.executeUpdate();
			System.out.println("Rows affected: " + rowCount);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			

		}


	}


}
