package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aluno;
import model.Curso;
import model.Historico;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;

import org.primefaces.component.datatable.DataTable;

import dao.CursoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.HistoricoDAO;



@Named
@ViewScoped
public class HistoricoController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//
	
	private static final long serialVersionUID = 1L;
	private boolean lgMatriculaAluno = false;
	private boolean lgNomeAluno  = false;
	
	private Aluno aluno = new Aluno();
	
	private AlunoDAO alunoDAO ;
	private HistoricoDAO historicoDAO ;
	
	private List<Historico> listaHistorico = new ArrayList<Historico>();
	private List<Historico> listaHistoricoFiltrada ;

	private String classeEscolhida;

	private Float ira;
	private Integer periodo;
	
	private Curriculum curriculum;
	private ImportarArvore importador;
	private EstruturaArvore estruturaArvore;
	private Curso curso = new Curso();
	
	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;
	
	@PostConstruct
	public void init() {
		
		estruturaArvore = EstruturaArvore.getInstance();
		alunoDAO =  estruturaArvore.getAlunoDAO();
		historicoDAO = estruturaArvore.getHistoricoDAO();	
		
		CursoDAO cursoDAO = new CursoDAOImpl();
		long idCursoPessoa;
		idCursoPessoa = usuarioController.getPessoa().getIdCurso();
		curso = cursoDAO.recuperarPorId(idCursoPessoa);
		
	}	
	
	public List<String> alunoMatricula(String codigo) {		

		codigo = codigo.toUpperCase();

		List<String> todos = alunoDAO.buscarTodosAlunoMatricula(codigo,curso.getId());

		return todos;
	}	
	
	public List<Aluno> alunoNome(String codigo) {		

		codigo = codigo.toUpperCase();

		List<Aluno> todos = alunoDAO.buscarTodosAlunoNomeObjeto(codigo,curso.getId());
		
		return todos;
	}
	
	public void onItemSelectMatriculaAluno() {		

		lgMatriculaAluno = true;
		lgNomeAluno = true;	
		
		aluno = alunoDAO.buscarPorMatricula(aluno.getMatricula());
		
		listaHistorico = historicoDAO.buscarTodosHistoricosPorMatricula(aluno.getId());		

		importador = estruturaArvore.recuperarArvore(aluno.getCurso(), aluno.getGrade());

		StudentsHistory sh = importador.getSh();
		Student st = sh.getStudents().get(aluno.getMatricula());
		ira = st.getIRA();
		aluno.setPeriodoIngresso(Integer.toString(st.getFirstSemester()));
		
		periodo = periodoCorrente(Integer.toString(st.getFirstSemester()));	
		
	}	
	
	public void limpaAluno()  {
		
		aluno = new Aluno();
		lgMatriculaAluno = false;
		lgNomeAluno  = false;
		ira = (float) 0;
		periodo = 0;
		
		listaHistorico = new ArrayList<Historico>();		
		
		 DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridHistorico");			
		 dataTable.clearInitialState();		 
		 dataTable.reset();
	}
	
	
	public int periodoCorrente(String ingresso){


		Calendar now = Calendar.getInstance();

		int anoAtual = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int i = 0;
		int periodoAtual = 0;

		if(mes >= 1 && mes <= 6){

			periodoAtual = 1;
		}
		else {

			periodoAtual = 3;

		}

		int anoIngresso = Integer.parseInt(ingresso.substring(0, 4));
		int periodoIngresso = Integer.parseInt(ingresso.substring(4, 5));

		while( anoIngresso != anoAtual || periodoAtual != periodoIngresso  ){

			i++;

			if (periodoIngresso == 3){

				anoIngresso++;
				periodoIngresso = 1;
			}
			else{

				periodoIngresso = 3;

			}

		}


		return i;
	}
	
	//========================================================= GET - SET ==================================================================================//

	public boolean isLgMatriculaAluno() {
		return lgMatriculaAluno;
	}

	public void setLgMatriculaAluno(boolean lgMatriculaAluno) {
		this.lgMatriculaAluno = lgMatriculaAluno;
	}

	public boolean isLgNomeAluno() {
		return lgNomeAluno;
	}

	public void setLgNomeAluno(boolean lgNomeAluno) {
		this.lgNomeAluno = lgNomeAluno;
	}

	public Aluno getAluno() {
		return aluno;
	}	

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public AlunoDAO getAlunoDAO() {
		return alunoDAO;
	}


	public void setAlunoDAO(AlunoDAO alunoDAO) {
		this.alunoDAO = alunoDAO;
	}

	public HistoricoDAO getHistoricoDAO() {
		return historicoDAO;
	}

	public void setHistoricoDAO(HistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}

	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}

	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getClasseEscolhida() {
		return classeEscolhida;
	}

	public void setClasseEscolhida(String classeEscolhida) {
		this.classeEscolhida = classeEscolhida;
	}	

	public Float getIra() {
		return ira;
	}

	public void setIra(Float ira) {
		this.ira = ira;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public ImportarArvore getImportador() {
		return importador;
	}

	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public List<Historico> getListaHistoricoFiltrada() {
		return listaHistoricoFiltrada;
	}

	public void setListaHistoricoFiltrada(List<Historico> listaHistoricoFiltrada) {
		this.listaHistoricoFiltrada = listaHistoricoFiltrada;
	}
}