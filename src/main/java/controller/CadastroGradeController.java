package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Curso;
import model.Disciplina;
import model.DisciplinaGradeDisciplina;
import model.Equivalencia;
import model.Grade;
import model.GradeDisciplina;
import model.PreRequisito;

import org.primefaces.component.datatable.DataTable;

import dao.CursoDAOImpl;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.PreRequisitoDAO;



@Named 
@ViewScoped
public class CadastroGradeController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private boolean lgHorasAoe = true;
	private boolean lgMaxPeriodo = true;
	private boolean lgExcluirGrade = true;
	private boolean lgIncluirGrade = true;	
	private boolean lgCodigoGrade = false;
	private boolean lgHorasEletivas = true;
	private boolean lgNomeDisciplina = true;	
	private boolean lgTipoDisciplina = true;
	private boolean lgHorasOpcionais  = true;		
	private boolean lgCodigoDisciplina = true;	
	private boolean lgLimparDisciplina = true;
	private boolean lgIncluirDisciplina = true;
	private boolean lgPeriodoDisciplina = true;
	private boolean lgLimparEquivalencia = true;
	private boolean lgIncluirEquivalencia = true;
	private boolean lgCargaHorariaDisciplina = true;
	private boolean lgNomeDisciplinaEquivalenciaUm = true;
	private boolean lgNomeDisciplinaEquivalenciaDois = true;
	private boolean lgCodigoDisciplinaEquivalenciaUm = true;
	private boolean lgCodigoDisciplinaEquivalenciaDois = true;
	
	private List<Equivalencia> listaEquivalenciaSelecionada ;
	private List<Equivalencia> listaEquivalencia = new ArrayList<Equivalencia>();	
	private List<DisciplinaGradeDisciplina> listaEletivasSelecionada ;
	private List<DisciplinaGradeDisciplina> listaObrigatoriasSelecionada ;
	private List<DisciplinaGradeDisciplina> listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();
	private List<DisciplinaGradeDisciplina> listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();		
	private List<PreRequisito> listaPreRequisitos = new ArrayList<PreRequisito>();	
	
	private Grade grade = new Grade();	
	private Curso curso = new Curso();
	private Ordenar ordenar = new Ordenar();
	private Disciplina disciplina = new Disciplina();	
	private Disciplina disciplinaPre = new Disciplina();
	private Disciplina disciplinaNova = new Disciplina();
	private Disciplina disciplinaEquivalenciaUm = new Disciplina();
	private Disciplina disciplinaEquivalenciaDois = new Disciplina();
	private GradeDisciplina gradeDisciplina = new GradeDisciplina();	
	private PreRequisito linhaSelecionadaPreRequisto = new PreRequisito();
	private Equivalencia linhaSelecionadaEquivalencia = new Equivalencia();
	private DisciplinaGradeDisciplina linhaSelecionada = new DisciplinaGradeDisciplina();	

	private GradeDAO gradeDAO ;	
	private DisciplinaDAO disciplinaDAO ;
	private PreRequisitoDAO preRequisitoDAO ;
	private EquivalenciaDAO equivalenciaDAO ;
	private GradeDisciplinaDAO gradeDisciplinaDAO ;
	
	private CursoDAO cursoDAO = new CursoDAOImpl();

	long idCursoPessoa;
	private String tipoPre;
	private EstruturaArvore estruturaArvore;

	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {

		estruturaArvore = EstruturaArvore.getInstance();
		
		gradeDAO = estruturaArvore.getGradeDAO();
		disciplinaDAO = estruturaArvore.getDisciplinaDAO();
		equivalenciaDAO = estruturaArvore.getEquivalenciaDAO();
		preRequisitoDAO =  estruturaArvore.getPreRequisitoDAO();		
		gradeDisciplinaDAO = estruturaArvore.getGradeDisciplinaDAO();
		
		
		
		idCursoPessoa = usuarioController.getPessoa().getIdCurso();
		curso = cursoDAO.recuperarPorId(idCursoPessoa);

	}
	public List<String> gradeCodigos(String codigo) {	

		List<String> todos = gradeDAO.buscarTodosCodigosGrade(codigo,idCursoPessoa);

		return todos;
	}

	public void buscarGrade(){

		Grade gradeAuxiliar = new Grade();

		disciplina = new Disciplina();

		gradeDisciplina = new GradeDisciplina();

		gradeAuxiliar = gradeDAO.buscarPorCodigoGrade(grade.getCodigo(),idCursoPessoa);

		if (gradeAuxiliar != null) {

			grade = gradeAuxiliar;

			FacesMessage msg = new FacesMessage("Grade encontrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			lgCodigoGrade = true;

			lgHorasEletivas = false;
			lgHorasOpcionais  = false;
			lgHorasAoe = false;
			lgMaxPeriodo = false;


			lgCodigoDisciplina = false;
			lgNomeDisciplina = false;	

			lgCargaHorariaDisciplina = false;
			lgTipoDisciplina = false;
			lgIncluirDisciplina = false;
			lgLimparDisciplina = false;

			lgCodigoDisciplinaEquivalenciaUm = false;
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaDois = false;

			lgIncluirEquivalencia = false;
			lgLimparEquivalencia = false;

			lgExcluirGrade = false;

			atualizaGrids();

		}

		else{		

			if(grade.getCodigo() != null){
				grade.setCurso(curso);
				grade.setHorasAce( 0);
				grade.setHorasEletivas( 0);
				grade.setHorasOpcionais( 0);
				gradeDAO.persistir(grade);

				FacesMessage msg = new FacesMessage("Nova grade cadastrada!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
			else {

				FacesMessage msg = new FacesMessage("Preencha o campo Código Grade!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				return;
			}

			lgCodigoGrade = true;
			lgHorasEletivas = false;
			lgHorasOpcionais  = false;
			lgHorasAoe = false;
			lgMaxPeriodo = false;

			lgCodigoDisciplina = false;
			lgNomeDisciplina = false;	

			lgCargaHorariaDisciplina = false;
			lgTipoDisciplina = false;
			lgIncluirDisciplina = false;
			lgLimparDisciplina = false;

			lgCodigoDisciplinaEquivalenciaUm = false;
			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaDois = false;

			lgIncluirEquivalencia = false;
			lgLimparEquivalencia = false;

			lgExcluirGrade = false;
		}
	}

	public void excluirGrade(){

		gradeDAO.removePeloId(grade.getId());
		limpaGrade();

	}

	public void limpaGrade(){

		grade = new Grade();
		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();
		listaEquivalencia = new ArrayList<Equivalencia>();

		lgCodigoGrade = false;
		lgHorasEletivas = true;
		lgHorasOpcionais  = true;
		lgHorasAoe = true;
		lgMaxPeriodo = true;
		lgIncluirGrade = true;

		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgPeriodoDisciplina = true;
		lgCargaHorariaDisciplina = true;
		lgTipoDisciplina = true;
		lgIncluirDisciplina = true;
		lgLimparDisciplina = true;


		lgCodigoDisciplinaEquivalenciaUm = true;
		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaDois = true;

		lgIncluirEquivalencia = true;
		lgLimparEquivalencia = true;

		lgExcluirGrade = true;

		listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();
		listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();
		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridObrigatorias");
		dataTable.clearInitialState();
		dataTable.reset();
		
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEletivas");
		dataTable.clearInitialState();
		dataTable.reset();
		
		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principalForm:gridEquivalencias");
		dataTable.clearInitialState();
		dataTable.reset();		
		
	}



	public void alterarGrade(){

		gradeDAO.editar(grade);
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);

	}

	public void incluiGrade(){

		if (grade.getHorasAce() == 0 ){
			FacesMessage msg = new FacesMessage("Preencha o campo Horas AOE!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (grade.getHorasEletivas() == 0){
			FacesMessage msg = new FacesMessage("Preencha o campo Horas Eletiva!");
			FacesContext.getCurrentInstance().addMessage(null, msg);	
			return;

		}

		if (grade.getHorasOpcionais() == 0){
			FacesMessage msg = new FacesMessage("Preencha o campo Horas Opcionais!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		grade.setCurso(curso);

		if (grade.getId() == 0){
			
			gradeDAO.persistir(grade);
		}

		else {

			gradeDAO.editar(grade);

		}
		
		lgHorasEletivas = true;
		lgHorasOpcionais  = true;
		lgHorasAoe = true;
		lgIncluirGrade = true;
		lgMaxPeriodo = true;

		lgCodigoDisciplina = false;
		lgNomeDisciplina = false;	
		lgPeriodoDisciplina = false;
		lgCargaHorariaDisciplina = false;
		lgTipoDisciplina = false;
		lgIncluirDisciplina = false;
		lgLimparDisciplina = false;

		lgCodigoDisciplinaEquivalenciaUm = false;
		lgCodigoDisciplinaEquivalenciaDois = false;
		lgNomeDisciplinaEquivalenciaUm = false;
		lgNomeDisciplinaEquivalenciaDois = false;

		lgIncluirEquivalencia = false;
		lgLimparEquivalencia = false;

	}
	
	public void alteraCampoPeriodo(){

		if(gradeDisciplina.getTipoDisciplina().equals("Obrigatoria")){

			lgPeriodoDisciplina = false;
		}
		else{
			
			lgPeriodoDisciplina = true;

		}

	}

	public List<String> disciplinaCodigos(String codigo) {	

		codigo = codigo.toUpperCase();

		List<String> todos = disciplinaDAO.buscarTodosCodigosDisciplina(codigo);

		return todos;
	}

	public List<Disciplina> disciplinaNomes(String codigo) {	

		codigo = codigo.toUpperCase();

		List<Disciplina> todos = disciplinaDAO.buscarTodosNomesDisciplinaObjeto(codigo);

		return todos;
	}

	public List<Disciplina> disciplinaNomesEquivalencia(String codigo) {	

		List<Disciplina> listaNomesSelecionados = new ArrayList<Disciplina>();

		List<DisciplinaGradeDisciplina> newList = new ArrayList<DisciplinaGradeDisciplina>(listaObrigatorias);

		newList.addAll(listaEletivas);

		for (DisciplinaGradeDisciplina disciplina:newList ){

			if (disciplina.getDisciplina().getNome().indexOf(codigo.toUpperCase()) >= 0){

				listaNomesSelecionados.add(disciplina.getDisciplina());

			}
		}		
		
		codigo = codigo.toUpperCase();

		return listaNomesSelecionados;
	}

	public List<Disciplina> disciplinaCodigoEquivalencia(String codigo) {	

		List<Disciplina> listaNomesSelecionados = new ArrayList<Disciplina>();

		List<DisciplinaGradeDisciplina> newList = new ArrayList<DisciplinaGradeDisciplina>(listaObrigatorias);

		newList.addAll(listaEletivas);

		for (DisciplinaGradeDisciplina disciplina:newList ){

			if (disciplina.getDisciplina().getCodigo().indexOf(codigo.toUpperCase()) >= 0){

				listaNomesSelecionados.add(disciplina.getDisciplina());

			}

		}

		codigo = codigo.toUpperCase();

		return listaNomesSelecionados;
	}
	
	public void limparDisciplina(){

		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();

		lgCodigoDisciplina = false;
		lgNomeDisciplina = false;	
		lgCargaHorariaDisciplina = false;

	}
	
public void incluirDisciplinaNova(){
		
		disciplinaNova.setCodigo(disciplinaNova.getCodigo().toUpperCase());
		disciplinaNova.setNome(disciplinaNova.getNome().toUpperCase());

		Disciplina disciplinaExiste = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaNova.getCodigo());

		if (disciplinaNova.getCodigo() == ""){

			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplinaNova.getNome() == ""){

			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if (disciplinaNova.getCargaHoraria() == ""){

			FacesMessage msg = new FacesMessage("Preencha o campo Carga Horária!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplinaExiste != null){

			FacesMessage msg = new FacesMessage("Disciplina já existe");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;

		}
		else {

			disciplinaDAO.persistir(disciplinaNova);
			FacesMessage msg = new FacesMessage("Disciplina cadastrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaNova =  new Disciplina();

		}
	}

	public void onItemSelectCodigoDisciplina() {


		disciplina = disciplinaDAO.buscarPorCodigoDisciplina(disciplina.getCodigo());

		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgCargaHorariaDisciplina = true;
	}

	public void onItemSelectCodigoNome() {


		Disciplina disciplinaAux = disciplinaDAO.buscarPorCodigoDisciplina(disciplina.getCodigo());

		if (disciplinaAux != null){

			disciplina = disciplinaAux;
		}

		lgCodigoDisciplina = true;
	}

	public void onItemSelectNomeDisciplina() {
		
		lgCodigoDisciplina = true;
		lgNomeDisciplina = true;	
		lgCargaHorariaDisciplina = true;	
	}

	public void onItemSelectCodigoDisciplinaEquivalenciaUm() {

		lgCodigoDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaUm = true;	
	}

	public void onItemSelectNomeDisciplinaEquivalenciaUm() {

		lgCodigoDisciplinaEquivalenciaUm = true;
		lgNomeDisciplinaEquivalenciaUm = true;	
	}

	public void onItemSelectCodigoDisciplinaEquivalenciaDois() {

		disciplinaEquivalenciaDois = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaEquivalenciaDois.getCodigo());

		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaDois = true;	
	}

	public void onItemSelectNomeDisciplinaEquivalenciaDois() {

		lgCodigoDisciplinaEquivalenciaDois = true;
		lgNomeDisciplinaEquivalenciaDois = true;	
	}

	public void incluirGradeDisciplina(){

		if (disciplina.getNome() == null){

			disciplina.setNome("");

		}

		if(disciplina.getCodigo().equals("")){

			FacesMessage msg = new FacesMessage("Preencha o campo Código Disciplina !");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getId() == null){

			if (disciplina.getNome() == null || disciplina.getNome().equals("")){

				FacesMessage msg = new FacesMessage("Preencha o campo Nome Disciplina !");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}

		if(gradeDisciplina.getTipoDisciplina() == ""){

			FacesMessage msg = new FacesMessage("Preencha o campo Tipo Disciplina!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if(gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") && gradeDisciplina.getPeriodo() == 0){

			FacesMessage msg = new FacesMessage("Preencha o campo Período!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
		}

		disciplina.setNome(disciplina.getNome().toUpperCase());
		disciplina.setCodigo(disciplina.getCodigo().toUpperCase());
		disciplinaDAO.persistir(disciplina);
		
		gradeDisciplina.setDisciplina(disciplina);
		gradeDisciplina.setGrade(grade);	

		List<GradeDisciplina> todos = gradeDisciplinaDAO.buscarTodasGradeDisciplinaPorGrade(grade.getId());

		for (GradeDisciplina g:todos){

			if(g.getDisciplina().getId() == disciplina.getId()){

				FacesMessage msg = new FacesMessage("Disciplina já cadastrada nesta grade!");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				return;
			}
		}
		
		gradeDisciplinaDAO.persistir(gradeDisciplina);	

		DisciplinaGradeDisciplina disciplinaGradeDisciplina  = new DisciplinaGradeDisciplina();

		disciplinaGradeDisciplina.setDisciplina(disciplina);

		disciplinaGradeDisciplina.setGradeDisciplina(gradeDisciplina);	

		if (gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") ){
			listaObrigatorias.add(disciplinaGradeDisciplina);

		}
		
		else {
			listaEletivas.add(disciplinaGradeDisciplina);

		}

		ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaObrigatorias);
		ordenar.DisciplinaGradeDisciplinaOrdenarPeriodo(listaObrigatorias);
		//
		
		ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaEletivas);
		
		disciplina = new Disciplina();
		gradeDisciplina = new GradeDisciplina();

		lgCodigoDisciplina = false;
		lgNomeDisciplina = false;	
		lgCargaHorariaDisciplina = false;	
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);

	}	

	public void deletarGradeDisciplina(){

		gradeDisciplinaDAO.removePeloId(linhaSelecionada.getGradeDisciplina().getId());
		
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);
		
		atualizaGrids();
	}
	
	public void atualizaGrids(){

		listaEquivalencia = new ArrayList<Equivalencia>();			
		listaEletivas = new ArrayList<DisciplinaGradeDisciplina>();
		listaObrigatorias = new ArrayList<DisciplinaGradeDisciplina>();
		listaEquivalencia = equivalenciaDAO.buscarTodasEquivalenciasPorGrade(grade.getId());		

		ArrayList<GradeDisciplina> todos = gradeDisciplinaDAO.buscarTodasGradeDisciplinaPorGrade(grade.getId());

		while(!todos.isEmpty()){ 

			DisciplinaGradeDisciplina disciplinaGradeDisciplina  = new DisciplinaGradeDisciplina();
			disciplinaGradeDisciplina.setDisciplina(disciplinaDAO.recuperarPorId(todos.get(0).getDisciplina().getId()));
			disciplinaGradeDisciplina.setGradeDisciplina(todos.get(0));	

			if (todos.get(0).getTipoDisciplina().equals("Obrigatoria")){

				listaObrigatorias.add(disciplinaGradeDisciplina);

			}
			else{

				listaEletivas.add(disciplinaGradeDisciplina);

			}
			
			ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaObrigatorias);
			ordenar.DisciplinaGradeDisciplinaOrdenarPeriodo(listaObrigatorias);
			
			
			ordenar.DisciplinaGradeDisciplinaOrdenarCodigo(listaEletivas);

			todos.remove(0);
		}
	}

	public void incluiPreRequisitos (){

		PreRequisito preRequisito  = new PreRequisito();

		preRequisito  = new PreRequisito();

		disciplinaPre = disciplinaDAO.buscarPorCodigoDisciplina(disciplinaPre.getCodigo());

		preRequisito.setDisciplina(disciplinaPre);

		preRequisito.setGradeDisciplina(linhaSelecionada.getGradeDisciplina());

		preRequisito.setTipo(tipoPre);
		
		if ( disciplinaPre == null || disciplinaPre.getCodigo() == "" ){
			
			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
			
			
		}
		
		if (tipoPre == ""){
			
			FacesMessage msg = new FacesMessage("Selecione o Tipo de Pré-Requsito!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		long idPreprocurado = preRequisitoDAO.buscarPorDisciplanaGradeId(linhaSelecionada.getGradeDisciplina().getId(), disciplinaPre.getId());

		if(idPreprocurado != 0){

			FacesMessage msg = new FacesMessage("Pre-Requisito já cadastrado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaPre = new Disciplina();
			tipoPre = "";
			return;
		}


		if(disciplinaPre.getId() == linhaSelecionada.getDisciplina().getId()){

			FacesMessage msg = new FacesMessage("Não é possível incluir como pré-requisito a própria disciplina!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			disciplinaPre = new Disciplina();
			tipoPre = "";
			return;
		}

		preRequisitoDAO.persistir(preRequisito);

		listaPreRequisitos.add(preRequisito);

		disciplinaPre = new Disciplina();

		tipoPre = "";
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);

	}

	public void carregaPreRequisitos(){

		listaPreRequisitos = new ArrayList<PreRequisito>();

		ArrayList<PreRequisito> todos = preRequisitoDAO.buscarPorTodosCodigoGradeDisc(linhaSelecionada.getGradeDisciplina().getId());
		while(!todos.isEmpty()){  

			listaPreRequisitos.add(todos.remove(0));
		}
	}

	public void deletarPreRequisito(){

		preRequisitoDAO.removePeloId(linhaSelecionadaPreRequisto.getId());

		ArrayList<PreRequisito> todos = preRequisitoDAO.buscarPorTodosCodigoGradeDisc(linhaSelecionada.getGradeDisciplina().getId());

		listaPreRequisitos.clear();

		for(PreRequisito p:todos){

			listaPreRequisitos.add(p);

		}
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);
		
		
	}

	public void incluiEquivalencia(){

		Equivalencia equivalencia = new Equivalencia();

		equivalencia.setDisciplinaGrade(disciplinaEquivalenciaUm);

		equivalencia.setDisciplinaEquivalente(disciplinaEquivalenciaDois);

		equivalencia.setGrade(grade);

		Equivalencia equivalenciaAuxiliar;

		equivalenciaAuxiliar = equivalenciaDAO.buscarPorEquivalencia(disciplinaEquivalenciaUm.getId(), disciplinaEquivalenciaDois.getId(), grade.getId());

		if (equivalenciaAuxiliar == null){

			equivalenciaAuxiliar = equivalenciaDAO.buscarPorEquivalencia( disciplinaEquivalenciaDois.getId(),disciplinaEquivalenciaUm.getId(), grade.getId());
		}


		if (equivalenciaAuxiliar != null){
			FacesMessage msg = new FacesMessage("Equivalencia já existe!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			disciplinaEquivalenciaUm = new Disciplina();

			disciplinaEquivalenciaDois = new Disciplina();

			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaDois = false;	
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaUm = false;	

			return;
		}

		if(disciplinaEquivalenciaDois.getId() == disciplinaEquivalenciaUm.getId()){


			FacesMessage msg = new FacesMessage("Não é possível incluir equivalencia da própria disciplina!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			disciplinaEquivalenciaUm = new Disciplina();

			disciplinaEquivalenciaDois = new Disciplina();

			lgCodigoDisciplinaEquivalenciaDois = false;
			lgNomeDisciplinaEquivalenciaDois = false;	
			lgCodigoDisciplinaEquivalenciaUm = false;
			lgNomeDisciplinaEquivalenciaUm = false;	

			return;
		}

		equivalenciaDAO.persistir(equivalencia);	

		listaEquivalencia.add(equivalencia);

		disciplinaEquivalenciaUm = new Disciplina();

		disciplinaEquivalenciaDois = new Disciplina();

		lgCodigoDisciplinaEquivalenciaDois = false;
		lgNomeDisciplinaEquivalenciaDois = false;	
		lgCodigoDisciplinaEquivalenciaUm = false;
		lgNomeDisciplinaEquivalenciaUm = false;	
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);

	}


	

	public void limpaEquivalencia(){

		disciplinaEquivalenciaUm = new Disciplina();

		disciplinaEquivalenciaDois = new Disciplina();

		lgCodigoDisciplinaEquivalenciaDois = false;
		lgNomeDisciplinaEquivalenciaDois = false;	
		lgCodigoDisciplinaEquivalenciaUm = false;
		lgNomeDisciplinaEquivalenciaUm = false;	

	}

	public void deletarEquivalencia(){		

		equivalenciaDAO.removePeloId(linhaSelecionadaEquivalencia.getId());
		
		
		
		estruturaArvore.removerEstrutura(curso, grade);
		
		atualizaGrids();		

	}

	//========================================================= GET - SET ==================================================================================//

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public boolean isLgHorasEletivas() {
		return lgHorasEletivas;
	}

	public void setLgHorasEletivas(boolean lgHorasEletivas) {
		this.lgHorasEletivas = lgHorasEletivas;
	}

	public boolean isLgHorasOpcionais() {
		return lgHorasOpcionais;
	}

	public void setLgHorasOpcionais(boolean lgHorasOpcionais) {
		this.lgHorasOpcionais = lgHorasOpcionais;
	}

	public boolean isLgHorasAoe() {
		return lgHorasAoe;
	}

	public void setLgHorasAoe(boolean lgHorasAoe) {
		this.lgHorasAoe = lgHorasAoe;
	}

	public boolean isLgIncluirGrade() {
		return lgIncluirGrade;
	}

	public void setLgIncluirGrade(boolean lgIncluirGrade) {
		this.lgIncluirGrade = lgIncluirGrade;
	}

	public boolean isLgCodigoGrade() {
		return lgCodigoGrade;
	}

	public void setLgCodigoGrade(boolean lgCodigoGrade) {
		this.lgCodigoGrade = lgCodigoGrade;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public boolean isLgCodigoDisciplina() {
		return lgCodigoDisciplina;
	}

	public void setLgCodigoDisciplina(boolean lgCodigoDisciplina) {
		this.lgCodigoDisciplina = lgCodigoDisciplina;
	}

	public boolean isLgNomeDisciplina() {
		return lgNomeDisciplina;
	}

	public void setLgNomeDisciplina(boolean lgNomeDisciplina) {
		this.lgNomeDisciplina = lgNomeDisciplina;
	}

	public boolean isLgPeriodoDisciplina() {
		return lgPeriodoDisciplina;
	}

	public void setLgPeriodoDisciplina(boolean lgPeriodoDisciplina) {
		this.lgPeriodoDisciplina = lgPeriodoDisciplina;
	}

	public boolean isLgCargaHorariaDisciplina() {
		return lgCargaHorariaDisciplina;
	}

	public void setLgCargaHorariaDisciplina(boolean lgCargaHorariaDisciplina) {
		this.lgCargaHorariaDisciplina = lgCargaHorariaDisciplina;
	}

	public boolean isLgTipoDisciplina() {
		return lgTipoDisciplina;
	}

	public void setLgTipoDisciplina(boolean lgTipoDisciplina) {
		this.lgTipoDisciplina = lgTipoDisciplina;
	}

	public GradeDisciplina getGradeDisciplina() {
		return gradeDisciplina;
	}

	public void setGradeDisciplina(GradeDisciplina gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isLgIncluirDisciplina() {
		return lgIncluirDisciplina;
	}

	public void setLgIncluirDisciplina(boolean lgIncluirDisciplina) {
		this.lgIncluirDisciplina = lgIncluirDisciplina;
	}

	public boolean isLgLimparDisciplina() {
		return lgLimparDisciplina;
	}

	public void setLgLimparDisciplina(boolean lgLimparDisciplina) {
		this.lgLimparDisciplina = lgLimparDisciplina;
	}

	public List<DisciplinaGradeDisciplina> getListaObrigatorias() {
		return listaObrigatorias;
	}

	public void setListaObrigatorias(
			List<DisciplinaGradeDisciplina> listaObrigatorias) {
		this.listaObrigatorias = listaObrigatorias;
	}

	public List<DisciplinaGradeDisciplina> getListaEletivas() {
		return listaEletivas;
	}

	public void setListaEletivas(List<DisciplinaGradeDisciplina> listaEletivas) {
		this.listaEletivas = listaEletivas;
	}

	public DisciplinaGradeDisciplina getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public void setLinhaSelecionada(DisciplinaGradeDisciplina linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public Disciplina getDisciplinaPre() {
		return disciplinaPre;
	}

	public void setDisciplinaPre(Disciplina disciplinaPre) {
		this.disciplinaPre = disciplinaPre;
	}

	public List<PreRequisito> getListaPreRequisitos() {
		return listaPreRequisitos;
	}

	public void setListaPreRequisitos(List<PreRequisito> listaPreRequisitos) {
		this.listaPreRequisitos = listaPreRequisitos;
	}

	public List<Equivalencia> getListaEquivalencia() {
		return listaEquivalencia;
	}

	public void setListaEquivalencia(List<Equivalencia> listaEquivalencia) {
		this.listaEquivalencia = listaEquivalencia;
	}

	public Disciplina getDisciplinaEquivalenciaUm() {
		return disciplinaEquivalenciaUm;
	}

	public void setDisciplinaEquivalenciaUm(Disciplina disciplinaEquivalenciaUm) {
		this.disciplinaEquivalenciaUm = disciplinaEquivalenciaUm;
	}

	public Disciplina getDisciplinaEquivalenciaDois() {
		return disciplinaEquivalenciaDois;
	}

	public void setDisciplinaEquivalenciaDois(Disciplina disciplinaEquivalenciaDois) {
		this.disciplinaEquivalenciaDois = disciplinaEquivalenciaDois;
	}

	public boolean isLgCodigoDisciplinaEquivalenciaUm() {
		return lgCodigoDisciplinaEquivalenciaUm;
	}

	public void setLgCodigoDisciplinaEquivalenciaUm(
			boolean lgCodigoDisciplinaEquivalenciaUm) {
		this.lgCodigoDisciplinaEquivalenciaUm = lgCodigoDisciplinaEquivalenciaUm;
	}

	public boolean isLgCodigoDisciplinaEquivalenciaDois() {
		return lgCodigoDisciplinaEquivalenciaDois;
	}

	public void setLgCodigoDisciplinaEquivalenciaDois(
			boolean lgCodigoDisciplinaEquivalenciaDois) {
		this.lgCodigoDisciplinaEquivalenciaDois = lgCodigoDisciplinaEquivalenciaDois;
	}

	public boolean isLgNomeDisciplinaEquivalenciaUm() {
		return lgNomeDisciplinaEquivalenciaUm;
	}

	public void setLgNomeDisciplinaEquivalenciaUm(
			boolean lgNomeDisciplinaEquivalenciaUm) {
		this.lgNomeDisciplinaEquivalenciaUm = lgNomeDisciplinaEquivalenciaUm;
	}

	public boolean isLgNomeDisciplinaEquivalenciaDois() {
		return lgNomeDisciplinaEquivalenciaDois;
	}

	public void setLgNomeDisciplinaEquivalenciaDois(
			boolean lgNomeDisciplinaEquivalenciaDois) {
		this.lgNomeDisciplinaEquivalenciaDois = lgNomeDisciplinaEquivalenciaDois;
	}

	public boolean isLgIncluirEquivalencia() {
		return lgIncluirEquivalencia;
	}

	public void setLgIncluirEquivalencia(boolean lgIncluirEquivalencia) {
		this.lgIncluirEquivalencia = lgIncluirEquivalencia;
	}

	public boolean isLgLimparEquivalencia() {
		return lgLimparEquivalencia;
	}

	public void setLgLimparEquivalencia(boolean lgLimparEquivalencia) {
		this.lgLimparEquivalencia = lgLimparEquivalencia;
	}

	public PreRequisito getLinhaSelecionadaPreRequisto() {
		return linhaSelecionadaPreRequisto;
	}

	public void setLinhaSelecionadaPreRequisto(
			PreRequisito linhaSelecionadaPreRequisto) {
		this.linhaSelecionadaPreRequisto = linhaSelecionadaPreRequisto;
	}

	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public Equivalencia getLinhaSelecionadaEquivalencia() {
		return linhaSelecionadaEquivalencia;
	}

	public void setLinhaSelecionadaEquivalencia(
			Equivalencia linhaSelecionadaEquivalencia) {
		this.linhaSelecionadaEquivalencia = linhaSelecionadaEquivalencia;
	}

	public GradeDisciplinaDAO getGradeDisciplinaDAO() {
		return gradeDisciplinaDAO;
	}

	public void setGradeDisciplinaDAO(GradeDisciplinaDAO gradeDisciplinaDAO) {
		this.gradeDisciplinaDAO = gradeDisciplinaDAO;
	}

	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
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

	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	public long getIdCursoPessoa() {
		return idCursoPessoa;
	}
	
	public void setIdCursoPessoa(long idCursoPessoa) {
		this.idCursoPessoa = idCursoPessoa;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}

	public String getTipoPre() {
		return tipoPre;
	}

	public void setTipoPre(String tipoPre) {
		this.tipoPre = tipoPre;
	}

	public boolean isLgExcluirGrade() {
		return lgExcluirGrade;
	}

	public void setLgExcluirGrade(boolean lgExcluirGrade) {
		this.lgExcluirGrade = lgExcluirGrade;
	}

	public boolean isLgMaxPeriodo() {
		return lgMaxPeriodo;
	}

	public void setLgMaxPeriodo(boolean lgMaxPeriodo) {
		this.lgMaxPeriodo = lgMaxPeriodo;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public Disciplina getDisciplinaNova() {
		return disciplinaNova;
	}

	public void setDisciplinaNova(Disciplina disciplinaNova) {
		this.disciplinaNova = disciplinaNova;
	}

	public List<Equivalencia> getListaEquivalenciaSelecionada() {
		return listaEquivalenciaSelecionada;
	}

	public void setListaEquivalenciaSelecionada(
			List<Equivalencia> listaEquivalenciaSelecionada) {
		this.listaEquivalenciaSelecionada = listaEquivalenciaSelecionada;
	}

	public List<DisciplinaGradeDisciplina> getListaObrigatoriasSelecionada() {
		return listaObrigatoriasSelecionada;
	}

	public void setListaObrigatoriasSelecionada(
			List<DisciplinaGradeDisciplina> listaObrigatoriasSelecionada) {
		this.listaObrigatoriasSelecionada = listaObrigatoriasSelecionada;
	}

	public List<DisciplinaGradeDisciplina> getListaEletivasSelecionada() {
		return listaEletivasSelecionada;
	}

	public void setListaEletivasSelecionada(
			List<DisciplinaGradeDisciplina> listaEletivasSelecionada) {
		this.listaEletivasSelecionada = listaEletivasSelecionada;
	}
}
