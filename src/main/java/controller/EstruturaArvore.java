package controller;

import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Curso;
import model.Grade;
import model.Historico;
import dao.AlunoDAOImpl;
import dao.DisciplinaDAOimpl;
import dao.EquivalenciaDAOImpl;
import dao.EventoAceDAOImpl;
import dao.GradeDAOImpl;
import dao.GradeDisciplinaDAOImpl;
import dao.HistoricoDAOImpl;
import dao.PreRequisitoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.EventoAceDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.HistoricoDAO;
import dao.Interface.PreRequisitoDAO;

public class EstruturaArvore {


	private static EstruturaArvore instancia;

	private AlunoDAO alunoDAO = new AlunoDAOImpl();	
	private GradeDAO gradeDAO = new GradeDAOImpl();
	private HistoricoDAO historicoDAO = new HistoricoDAOImpl();
	private EventoAceDAO eventosAceDAO = new EventoAceDAOImpl();
	private DisciplinaDAO disciplinaDAO = new DisciplinaDAOimpl();
	private PreRequisitoDAO preRequisitoDAO = new PreRequisitoDAOImpl();
	private EquivalenciaDAO equivalenciaDAO = new EquivalenciaDAOImpl();
	private GradeDisciplinaDAO gradeDisciplinaDAO = new GradeDisciplinaDAOImpl();	

	private List<ImportarArvore> todasArvores = new ArrayList<ImportarArvore>();	

	public static synchronized EstruturaArvore getInstance(){

		if (instancia == null){			
			instancia = new EstruturaArvore();
		}		
		return instancia;
	}


	public ImportarArvore recuperarArvore (Curso curso, Grade grade){

		for(ImportarArvore importarArvore:todasArvores){

			if(importarArvore.getCurso().getId() == curso.getId() && importarArvore.getGrade().getId() == grade.getId()){
				return importarArvore;
			}
		}	


		ImportarArvore importador = new ImportarArvore();
		
		importador.importarDisciplinas(grade);
		
		List<Aluno> listaAluno = alunoDAO.buscarTodosAlunoCursoGradeObjeto(curso.getId(), grade.getId());

		for(Aluno aluno:listaAluno){

			List<Historico> listaHistorico = historicoDAO.buscarTodosHistoricosPorMatricula(aluno.getId());
			importador.importarHistorico(listaHistorico);

		}

		importador.setCurso(curso);
		importador.setGrade(grade);
		
		todasArvores.add(importador);
		
		return importador;

	}

	public void removerEstrutura (Curso curso, Grade grade){

		for(ImportarArvore importarArvore:todasArvores){

			if(importarArvore.getCurso().getId() == curso.getId() && importarArvore.getGrade().getId() == grade.getId()){
				todasArvores.remove(importarArvore);
				return;
			}
		}
	}


	public List<ImportarArvore> getTodasArvores() {
		return todasArvores;
	}

	public void setTodasArvores(List<ImportarArvore> todasArvores) {
		this.todasArvores = todasArvores;
	}

	public static EstruturaArvore getInstancia() {
		return instancia;
	}

	public static void setInstancia(EstruturaArvore instancia) {
		EstruturaArvore.instancia = instancia;
	}

	public AlunoDAO getAlunoDAO() {
		return alunoDAO;
	}

	public void setAlunoDAO(AlunoDAO alunoDAO) {
		this.alunoDAO = alunoDAO;
	}

	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public HistoricoDAO getHistoricoDAO() {
		return historicoDAO;
	}

	public void setHistoricoDAO(HistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}

	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
	}

	public GradeDisciplinaDAO getGradeDisciplinaDAO() {
		return gradeDisciplinaDAO;
	}

	public void setGradeDisciplinaDAO(GradeDisciplinaDAO gradeDisciplinaDAO) {
		this.gradeDisciplinaDAO = gradeDisciplinaDAO;
	}

	public PreRequisitoDAO getPreRequisitoDAO() {
		return preRequisitoDAO;
	}

	public void setPreRequisitoDAO(PreRequisitoDAO preRequisitoDAO) {
		this.preRequisitoDAO = preRequisitoDAO;
	}

	public EquivalenciaDAO getEquivalenciaDAO() {
		return equivalenciaDAO;
	}

	public void setEquivalenciaDAO(EquivalenciaDAO equivalenciaDAO) {
		this.equivalenciaDAO = equivalenciaDAO;
	}

	public EventoAceDAO getEventosAceDAO() {
		return eventosAceDAO;
	}

	public void setEventosAceDAO(EventoAceDAO eventosAceDAO) {
		this.eventosAceDAO = eventosAceDAO;
	}
}
