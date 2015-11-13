/*ATUALIZAÇÕES*/
/*DATA = 15/05/2016   MÉTODO = completeText 					ANALISTA = Tliner Friaça Castro			*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/
/*DATA = 			  MÉTODO = completeText 					ANALISTA = 								*/


package dao;



import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dao.Interface.GenericDAO;



public class HibernateGenericDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {
	
//========================================================= VARIABLES ==================================================================================//
	
	 EntityManagerFactory emf = Persistence.createEntityManagerFactory("EstimarOfertasVagasWeb");
	 EntityManager em = emf.createEntityManager();
	
//======================================================================================================================================================//
//============================================================ METHODS =================================================================================//
		
	public void persistir(T entity) {
		try {		
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

	}

	public void editar(T entity) {
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
			
		} catch (Exception e) {

		}

	}

	public void remover(T entity) {
		try {
			em.getTransaction().begin();
			em.remove(em.merge(entity));
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Erro ao remover");
		}
	}

	public void removePeloId(ID id) {
		try {
			T entity = recuperarPorId(id);
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public T recuperarPorId(ID id) {
		try {
			em.getTransaction().begin();
			T obj = em.find(recuperarTipoClasse(), id);
			em.getTransaction().commit();
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public Collection<T> recuperarTodos() {
		try {
			em.getTransaction().begin();
			@SuppressWarnings("unchecked")
			List<T> list = em.createQuery("FROM " + recuperarTipoClasse().getName()).getResultList();
			em.getTransaction().commit();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public Query criarConsultaPersonalizada(String query, Object... parameters) {
		Query q = em.createQuery(query);

		for (int i = 1; i <= parameters.length; i++) {
			q.setParameter(i, parameters[i]);
		}

		return q;
	}

	@SuppressWarnings("unchecked")
	private Class<T> recuperarTipoClasse() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
//======================================================================================================================================================//
	
}
