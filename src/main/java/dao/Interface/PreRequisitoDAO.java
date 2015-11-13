package dao.Interface;



import java.util.ArrayList;

import model.PreRequisito;

public interface PreRequisitoDAO extends GenericDAO<PreRequisito, Long> {

	public ArrayList<PreRequisito> buscarPorTodosCodigoGradeDisc(Long codigo);
	public Long buscarPorDisciplanaGradeId(Long idGrade ,Long idDisciplina) ;
	
}