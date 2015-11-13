package dao.Interface;



import model.Pessoa;

public interface PessoaDAO extends GenericDAO<Pessoa, Long> {

	public Pessoa buscarPorNomePessoa(String variavel);
	public Pessoa buscarPorCpfPessoa(String variavel);
	
}