package dao;

import java.io.Serializable;

import javax.persistence.Query;

import model.Pessoa;
import dao.Interface.PessoaDAO;

public class PessoaDAOImpl extends HibernateGenericDAO<Pessoa, Long> implements PessoaDAO, Serializable {
	
	
private static PessoaDAOImpl instancia;
	
	
	
	public static synchronized PessoaDAOImpl getInstance(){

		if (instancia == null){

			instancia = new PessoaDAOImpl();

		}
		return instancia;



	}
	
	
	

	private static final long serialVersionUID = 1L;
	
	public Pessoa buscarPorNomePessoa(String variavel) {
		Pessoa pessoa = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Pessoa WHERE nome = :codigo")	.setParameter("codigo", variavel);
			pessoa = (Pessoa) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return pessoa;
	}
	
	
	
	public Pessoa buscarPorCpfPessoa(String variavel) {
		Pessoa pessoa = null;
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("FROM Pessoa WHERE cpf = :codigo")	.setParameter("codigo", variavel);
			pessoa = (Pessoa) query.getSingleResult();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().commit();
			return null;
		}
		return pessoa;
	}
	
	
	
	
	
	

}
