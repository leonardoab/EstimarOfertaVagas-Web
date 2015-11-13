package controller;

import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.Grade;
import model.Historico;
import br.ufjf.ice.integra3.rs.restclient.model.v2.AlunoCurso;
import model.integra.DisciplinaIntegra;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.HistoricoDAO;

public class IntergraControler {

	

	public void importadorDados (AlunoCurso alunoCurso,CursoDAO cursoDAO,AlunoDAO alunoDAO,DisciplinaDAO disciplinaDAO,HistoricoDAO historicoDAO,GradeDAO gradeDAO){


		Curso curso = new Curso();
		
		if ( cursoDAO.buscarPorCodigo(alunoCurso.getCurso()) == null){	
			curso.setCodigo(alunoCurso.getCurso());
			cursoDAO.persistir(curso);
		}
		else {
			curso = cursoDAO.buscarPorCodigo(alunoCurso.getCurso());
		}
		
		
		Grade grade = gradeDAO.buscarPorCodigoGrade(alunoCurso.getCurriculo(), curso.getId());
		
		if (grade == null){
			
			
			grade = new Grade();
			grade.setCodigo(alunoCurso.getCurriculo());
			grade.setCurso(curso);
			gradeDAO.persistir(grade);
			
			
		}
		//aluno.setPeriodoIngresso(alunoCurso.getCurriculo());
		

		Aluno aluno = new Aluno();
		
		aluno = alunoDAO.buscarPorMatricula(alunoCurso.getMatricula());

		if ( aluno == null){
			
			
			aluno = new Aluno();
			aluno.setMatricula(alunoCurso.getMatricula());
			aluno.setNome(alunoCurso.getNome());
			aluno.setGrade(grade);
			aluno.setCurso(curso);
			alunoDAO.persistir(aluno);

		} 
		

		


		for (br.ufjf.ice.integra3.rs.restclient.model.v2.Disciplina disciplinaIntegra : alunoCurso.getDisciplinas().getDisciplina()) {

			Disciplina disciplina = new Disciplina();

			if ( disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina()) == null){
				disciplina.setCodigo(disciplinaIntegra.getDisciplina());
				disciplina.setCargaHoraria(disciplinaIntegra.getHorasAula());
				disciplinaDAO.persistir(disciplina);

			}
			else {
				
				disciplina = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaIntegra.getDisciplina());

			}

			Historico historico = new Historico();
			historico.setAluno(aluno);
			historico.setDisciplina(disciplina);
			historico.setNota(disciplinaIntegra.getNota());
			historico.setSemestreCursado(disciplinaIntegra.getAnoSemestre());
			historico.setStatusDisciplina(disciplinaIntegra.getSituacao());

			historicoDAO.persistir(historico); 	

		}
	}

}
