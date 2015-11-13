package dao.Interface;



import java.util.ArrayList;

import model.PessoaCurso;

public interface PessoaCursoDAO extends GenericDAO<PessoaCurso, Long> {
	public ArrayList<PessoaCurso> buscarTodasPessoaCursoPorPessoa(long codigo);
	
}