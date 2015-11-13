package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aluno;
import model.Curso;
import model.DisciplinaPlanejamento;
import model.Grade;
import model.arvore.Class;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;
import dao.CursoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.GradeDAO;
import dao.Interface.HistoricoDAO;
import estimate.StudentCoursePlan;



@Named
@ViewScoped
public class PlanejamentoFormaturaController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private boolean lgMatriculaAluno = false;
	private boolean lgNomeAluno  = false;
	private boolean lgSelecionado  = false;	
	private boolean lgCampoHrsPeriodo  = true;	

	private Aluno aluno = new Aluno();
	private DisciplinaPlanejamento disciplinaSelecionada = new DisciplinaPlanejamento();

	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
	private List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();

	private boolean lgTabelaUm = false;
	private boolean lgTabelaDois = false;
	private boolean lgTabelaTres = false;
	private boolean lgTabelaQuatro = false;
	private boolean lgTabelaCinco = false;
	private boolean lgTabelaSeis = false;
	private boolean lgTabelaSete = false;
	private boolean lgTabelaOito = false;
	private boolean lgTabelaNove = false;
	private boolean lgTabelaDez = false;
	private boolean lgTabelaOnze = false;
	private boolean lgTabelaDoze = false;



	private List< String > listaCargaHorariaPeriodo = new ArrayList<String>();

	private int horasEletivasConcluidas;
	private int horasOpcionaisConcluidas;
	private int horasObrigatoriasConcluidas;
	private int horasObrigatorias;
	private int horasFaltamEletivas ;
	private int horasFaltamOpcionais ;
	private int contadorPorPeriodo;

	private Student st;

	private Integer qtdHorasPeriodo;


	private Curriculum curriculum;
	private Curriculum curriculumAluno;

	private float ira;
	private int periodo;

	private AlunoDAO alunoDAO ;


	private GradeDAO gradeDAO ;
	private CursoDAO cursoDAO = new CursoDAOImpl();
	private HistoricoDAO historicoDAO ;

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
		gradeDAO = estruturaArvore.getGradeDAO();
		historicoDAO = estruturaArvore.getHistoricoDAO();

		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);

		aluno.setGrade(grade);

		qtdHorasPeriodo= 300;
		
		CursoDAO cursoDAO = new CursoDAOImpl();
		long idCursoPessoa;
		idCursoPessoa = usuarioController.getPessoa().getIdCurso();
		curso = cursoDAO.recuperarPorId(idCursoPessoa);

	}

	public String bordaPeriodo(int variavel){


		if (aluno.getGrade().getCodigo() != null){

			if (periodo + variavel > aluno.getGrade().getNumeroMaximoPeriodos()){

				return "border-style: solid; border-color: red;";

			}
		}

		return "";
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


		return i + 1;
	}

	public String periodo(int numero){

		Calendar now = Calendar.getInstance();

		int ano = now.get(Calendar.YEAR);
		int mes = now.get(Calendar.MONTH) + 1;
		int i;
		String periodo = "";

		for (i=0;i<numero;i++){

			if(mes >= 1 && mes <= 6){

				periodo = "1 - " + ano;
			}
			else {

				periodo = "3 - " + ano;
				ano++;
			}

			mes = mes + 6;

			if (mes > 12){

				mes = mes - 12;

			}
		}

		return periodo;
	}

	public boolean moverDireita(int posicao,DisciplinaPlanejamento disciplinaSelecionadaIntera ){

		if (disciplinaSelecionadaIntera == null){

			disciplinaSelecionadaIntera = disciplinaSelecionada;
		}

		boolean funcionou = true;

		for(int i: curriculumAluno.getMandatories().keySet())
		{
			for(Class c: curriculumAluno.getMandatories().get(i))
			{
				if(c.getId() == disciplinaSelecionadaIntera.getCodigo()){

					if ( ((i+1) % 2) == ((posicao + 1) % 2)    ){

						disciplinaSelecionadaIntera.setCor("black");

					}
					else {

						disciplinaSelecionadaIntera.setCor("orange");

					}
				}

				for(Class cl: c.getPrerequisite()){

					if(cl.getId() == disciplinaSelecionadaIntera.getCodigo()){


						DisciplinaPlanejamento preRequisitoPlanejamento = new DisciplinaPlanejamento();

						preRequisitoPlanejamento.setCodigo(c.getId());
						preRequisitoPlanejamento.setCargaHoraria(c.getWorkload());

						for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao + 1) ){

							if (disciplinaPrincipal.getCodigo() == preRequisitoPlanejamento.getCodigo()){


								funcionou = moverDireita (posicao + 1,disciplinaPrincipal);
								break;

							}
						}
					}
				}
			}
		}

		if (recuperarLista(posicao + 1) == null){

			FacesMessage msg = new FacesMessage("Não é possível realizar esta ação,verifique os pré-requisitos ou a própria disciplina!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return false;
		}

		if (funcionou == false){

			return false;

		}

		if (funcionou == true){
			recuperarLista(posicao).remove(disciplinaSelecionadaIntera);

			if (recuperarLista(posicao + 1).size() == 0){

				recuperarLg(posicao + 1, true);
			}

			recuperarLista(posicao + 1).add(disciplinaSelecionadaIntera);
		}

		return true;
	}

	public boolean moverEsquerda(int posicao,DisciplinaPlanejamento disciplinaSelecionadaIntera ){

		if (disciplinaSelecionadaIntera == null){

			disciplinaSelecionadaIntera = disciplinaSelecionada;

		}

		boolean funcionou = true;

		for(int i: curriculumAluno.getMandatories().keySet())
		{
			for(Class c: curriculumAluno.getMandatories().get(i))
			{

				if(c.getId() == disciplinaSelecionadaIntera.getCodigo()){

					if ( ((i+1) % 2) == ((posicao - 1) % 2)    ){

						disciplinaSelecionadaIntera.setCor("black");

					}
					else {

						disciplinaSelecionadaIntera.setCor("orange");

					}
				}

				if (c.getId() == disciplinaSelecionadaIntera.getCodigo()){

					for(Class cl: c.getPrerequisite()){


						for(DisciplinaPlanejamento disciplinaPrincipal :recuperarLista(posicao - 1) ){

							if (disciplinaPrincipal.getCodigo() == cl.getId()){

								funcionou = moverEsquerda (posicao - 1,disciplinaPrincipal);
								break;
							}
						}
					}
				}
			}
		}

		if (recuperarLista(posicao - 1) == null){

			FacesMessage msg = new FacesMessage("Não é possível realizar esta ação,verifique os pré-requisitos ou a própria disciplina!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return false;
		}

		if (funcionou == false){

			return false;

		}


		if (funcionou == true){
			recuperarLista(posicao).remove(disciplinaSelecionadaIntera);
			recuperarLista(posicao - 1).add(disciplinaSelecionadaIntera);
			
			
			recuperarLg(posicao - 1, true);
			
			for(int x = 12; x >= posicao; x-- ){

				if (recuperarLista(x).size() == 0){

					recuperarLg(x, false);

				}

				else {
					break;
				}


			}

		}

		return true;

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

	public void onItemSelectAluno() {

		lgTabelaUm = true;

		lgMatriculaAluno = true;
		lgNomeAluno = true;	
		lgCampoHrsPeriodo = false;

		listaCargaHorariaPeriodo = new ArrayList<String>();

		aluno = alunoDAO.buscarPorMatricula(aluno.getMatricula());

		Grade gradeTeste = gradeDAO.buscarPorCodigoGrade(aluno.getGrade().getCodigo(), aluno.getCurso().getId());

		aluno.setGrade(gradeTeste);

		importador = estruturaArvore.recuperarArvore(aluno.getCurso(), aluno.getGrade());

		StudentsHistory sh = importador.getSh();
		st = sh.getStudents().get(aluno.getMatricula());
		ira = st.getIRA();
		aluno.setPeriodoIngresso(Integer.toString(st.getFirstSemester()));

		periodo = periodoCorrente(Integer.toString(st.getFirstSemester()));

		curriculum = importador.get_cur();		

		if (aluno.getGrade().getHorasEletivas() == 0 || aluno.getGrade().getHorasOpcionais() == 0){

			FacesMessage msg = new FacesMessage("Grade nao cadastrada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		gerarExpectativa();

	}


	public void gerarExpectativa(){

		listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();

		lgTabelaUm = false;
		lgTabelaDois = false;
		lgTabelaTres = false;
		lgTabelaQuatro = false;
		lgTabelaCinco = false;
		lgTabelaSeis = false;
		lgTabelaSete = false;
		lgTabelaOito = false;
		lgTabelaNove = false;
		lgTabelaDez = false;
		lgTabelaOnze = false;
		lgTabelaDoze = false;
		
		gerarDadosAluno(st,curriculum);


		horasFaltamEletivas =     aluno.getGrade().getHorasEletivas() - horasEletivasConcluidas ;
		horasFaltamOpcionais =   aluno.getGrade().getHorasOpcionais() - horasOpcionaisConcluidas;

		StudentCoursePlan g = new StudentCoursePlan(st, curriculum, qtdHorasPeriodo);

		curriculumAluno = g.generate();

		

		int ultimoPeriodoPreenchido = 0;

		for(int i : curriculumAluno.getMandatories().keySet())
		{
			System.out.println( i + ": ");

			contadorPorPeriodo = 0;

			for(Class cl: curriculumAluno.getMandatories().get(i))
			{

				contadorPorPeriodo =  contadorPorPeriodo + cl.getWorkload();				
				DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();
				disciplinaPlanejamento.setCargaHoraria(cl.getWorkload());
				disciplinaPlanejamento.setCodigo(cl.getId());

				recuperarLista(i+1).add(disciplinaPlanejamento);
				recuperarLg(i+1, true);

			}

			gerarEletivasObrigatorias (recuperarLista(i+1));
			ultimoPeriodoPreenchido = i;
			listaCargaHorariaPeriodo.add(Integer.toString(contadorPorPeriodo));

		}



		while(horasFaltamEletivas > 0 || horasFaltamOpcionais > 0){

			contadorPorPeriodo = 0;
			ultimoPeriodoPreenchido = ultimoPeriodoPreenchido + 1;
			recuperarLg(ultimoPeriodoPreenchido+ 1, true);
			gerarEletivasObrigatorias (recuperarLista(ultimoPeriodoPreenchido+1));
			listaCargaHorariaPeriodo.add(Integer.toString(contadorPorPeriodo));

		}		
	}


	public String cargaHorariaPeriodo(int periodo){

		if (listaCargaHorariaPeriodo.size() == 0) {

			return "";
		}

		return listaCargaHorariaPeriodo.get(periodo);
	}

	public void gerarEletivasObrigatorias (List<DisciplinaPlanejamento> listaDisciplinas){

		boolean continuar = true; 

		while(continuar){

			if ( (qtdHorasPeriodo - contadorPorPeriodo) > 0){

				if (horasFaltamEletivas >= 60 && (qtdHorasPeriodo - contadorPorPeriodo)>= 60){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(60);
					disciplinaPlanejamento.setCodigo("ELETIVA" );

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamEletivas = horasFaltamEletivas - 60;
					contadorPorPeriodo = contadorPorPeriodo + 60;

				}

				else if (horasFaltamOpcionais >= 60 && (qtdHorasPeriodo - contadorPorPeriodo) >= 60){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(60);
					disciplinaPlanejamento.setCodigo("OPCIONAL" );

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamOpcionais = horasFaltamOpcionais - 60;
					contadorPorPeriodo = contadorPorPeriodo + 60;

				}

				else if (horasFaltamEletivas >= 30 && (qtdHorasPeriodo - contadorPorPeriodo) >= 30){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(30);
					disciplinaPlanejamento.setCodigo("ELETIVA");

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamEletivas = horasFaltamEletivas - 30;
					contadorPorPeriodo = contadorPorPeriodo + 30;
				}

				else if (horasFaltamEletivas >= 30 && (qtdHorasPeriodo - contadorPorPeriodo) >= 30){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(30);
					disciplinaPlanejamento.setCodigo("ELETIVA");

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamEletivas = horasFaltamEletivas - 30;
					contadorPorPeriodo = contadorPorPeriodo + 30;
				}

				else  if (horasFaltamEletivas > 0 && (qtdHorasPeriodo - contadorPorPeriodo) >= horasFaltamEletivas){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(horasFaltamEletivas);
					disciplinaPlanejamento.setCodigo("ELETIVA");

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamEletivas = 0;
					contadorPorPeriodo = contadorPorPeriodo + horasFaltamEletivas;
				}

				else if (horasFaltamOpcionais > 0 && (qtdHorasPeriodo - contadorPorPeriodo) >= horasFaltamOpcionais){

					DisciplinaPlanejamento disciplinaPlanejamento = new DisciplinaPlanejamento();

					disciplinaPlanejamento.setCargaHoraria(horasFaltamOpcionais);
					disciplinaPlanejamento.setCodigo("OPCIONAL");

					listaDisciplinas.add(disciplinaPlanejamento);

					horasFaltamOpcionais = 0;
					contadorPorPeriodo = contadorPorPeriodo + horasFaltamOpcionais;

				}

				else if ((qtdHorasPeriodo - contadorPorPeriodo) < horasFaltamOpcionais){

					continuar = false;
				}


				else if (horasFaltamOpcionais <= 0 && horasFaltamEletivas <= 0){

					continuar = false;
				}
			} 

			else {

				continuar = false;

			}
		}
	}

	public List<DisciplinaPlanejamento> recuperarLista(int i){

		if(i== 1) return listaDisciplinaSelecionadas;
		if(i== 2) return listaDisciplinaSelecionadasDois;
		if(i== 3) return listaDisciplinaSelecionadasTres;
		if(i== 4) return listaDisciplinaSelecionadasQuatro;
		if(i== 5) return listaDisciplinaSelecionadasCinco;
		if(i== 6) return listaDisciplinaSelecionadasSeis;
		if(i== 7) return listaDisciplinaSelecionadasSete;
		if(i== 8) return listaDisciplinaSelecionadasOito;
		if(i== 9) return listaDisciplinaSelecionadasNove;
		if(i== 10) return listaDisciplinaSelecionadasDez;
		if(i== 11) return listaDisciplinaSelecionadasOnze;
		if(i== 12) return listaDisciplinaSelecionadasDoze;
		return null;

	}

	public void recuperarLg(int i,boolean valor){

		if(i== 1)  lgTabelaUm = valor;
		if(i== 2)  lgTabelaDois = valor;
		if(i== 3)  lgTabelaTres = valor;
		if(i== 4)  lgTabelaQuatro = valor;
		if(i== 5)  lgTabelaCinco = valor;
		if(i== 6)  lgTabelaSeis = valor;
		if(i== 7)  lgTabelaSete = valor;
		if(i== 8)  lgTabelaOito = valor;
		if(i== 9)  lgTabelaNove = valor;
		if(i== 10)  lgTabelaDez = valor;
		if(i== 11)  lgTabelaOnze = valor;
		if(i== 12)  lgTabelaDoze = valor;


	}

	public void limpaAluno(){

		aluno = new Aluno();
		lgMatriculaAluno = false;
		lgNomeAluno  = false;
		ira = 0;
		periodo = 0;	
		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasEletivasConcluidas = 0;

		listaDisciplinaSelecionadas = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDois = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasTres = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasQuatro = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasCinco = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSeis = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasSete = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOito = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasNove = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDez = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasOnze = new ArrayList<DisciplinaPlanejamento>();
		listaDisciplinaSelecionadasDoze = new ArrayList<DisciplinaPlanejamento>();
		
		lgTabelaUm = false;
		lgTabelaDois = false;
		lgTabelaTres = false;
		lgTabelaQuatro = false;
		lgTabelaCinco = false;
		lgTabelaSeis = false;
		lgTabelaSete = false;
		lgTabelaOito = false;
		lgTabelaNove = false;
		lgTabelaDez = false;
		lgTabelaOnze = false;
		lgTabelaDoze = false;

		Grade grade = new Grade();
		grade.setHorasEletivas(0);
		grade.setHorasOpcionais(0);

		lgCampoHrsPeriodo = true;
		aluno.setGrade(grade);

	}

	public void gerarDadosAluno(Student st, Curriculum cur)
	{
		HashMap<Class, ArrayList<String[]>> aprovado;

		horasObrigatorias = 0;
		horasObrigatoriasConcluidas = 0;
		horasOpcionaisConcluidas = 0;
		horasEletivasConcluidas = 0;

		aprovado = new HashMap<Class, ArrayList<String[]>>(st.getClasses(ClassStatus.APPROVED)); 
		TreeSet<String> naocompletado = new TreeSet<String>();

		for(int i: cur.getMandatories().keySet())
		{
			for(Class c: cur.getMandatories().get(i))
			{
				horasObrigatorias = horasObrigatorias + c.getWorkload();

				if(!aprovado.containsKey(c)) {
					naocompletado.add(c.getId());

				}
				else{

					horasObrigatoriasConcluidas = horasObrigatoriasConcluidas + c.getWorkload();

					//System.out.println(" bgcolor="+ (pos ? "#A9E2F3" : "#CEECF5") +"><td align=center>"+ i + "</td><td align=center> " + c.getId() + "</td><td align=center>APROVADO em " + classdata[0]+ "</td>");
					aprovado.remove(c);
				}
			}	
		}

		int creditos = 0;

		for(Class c: cur.getElectives())
		{
			if(aprovado.containsKey(c))	{
				creditos += c.getWorkload();
				aprovado.remove(c);
			} 	
		}

		horasEletivasConcluidas = creditos;

		creditos = 0;

		Set<Class> ap = aprovado.keySet();
		Iterator<Class> i = ap.iterator();
		while(i.hasNext())
		{
			Class c = i.next();

			creditos += c.getWorkload();
		}

		horasOpcionaisConcluidas = creditos;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}

	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}


	public void setIra(float ira) {
		this.ira = ira;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public ImportarArvore getImportador() {
		return importador;
	}

	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}

	public boolean isLgSelecionado() {
		return lgSelecionado;
	}

	public void setLgSelecionado(boolean lgSelecionado) {
		this.lgSelecionado = lgSelecionado;
	}

	public int getHorasEletivasConcluidas() {
		return horasEletivasConcluidas;
	}

	public void setHorasEletivasConcluidas(int horasEletivasConcluidas) {
		this.horasEletivasConcluidas = horasEletivasConcluidas;
	}

	public int getHorasOpcionaisConcluidas() {
		return horasOpcionaisConcluidas;
	}

	public void setHorasOpcionaisConcluidas(int horasOpcionaisConcluidas) {
		this.horasOpcionaisConcluidas = horasOpcionaisConcluidas;
	}

	public int getHorasObrigatoriasConcluidas() {
		return horasObrigatoriasConcluidas;
	}

	public void setHorasObrigatoriasConcluidas(int horasObrigatoriasConcluidas) {
		this.horasObrigatoriasConcluidas = horasObrigatoriasConcluidas;
	}

	public int getHorasObrigatorias() {
		return horasObrigatorias;
	}

	public void setHorasObrigatorias(int horasObrigatorias) {
		this.horasObrigatorias = horasObrigatorias;
	}

	public DisciplinaPlanejamento getDisciplinaSelecionada() {
		return disciplinaSelecionada;
	}

	public void setDisciplinaSelecionada(
			DisciplinaPlanejamento disciplinaSelecionada) {
		this.disciplinaSelecionada = disciplinaSelecionada;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadas() {
		return listaDisciplinaSelecionadas;
	}

	public void setListaDisciplinaSelecionadas(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadas) {
		this.listaDisciplinaSelecionadas = listaDisciplinaSelecionadas;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDois() {
		return listaDisciplinaSelecionadasDois;
	}

	public void setListaDisciplinaSelecionadasDois(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDois) {
		this.listaDisciplinaSelecionadasDois = listaDisciplinaSelecionadasDois;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasTres() {
		return listaDisciplinaSelecionadasTres;
	}

	public void setListaDisciplinaSelecionadasTres(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasTres) {
		this.listaDisciplinaSelecionadasTres = listaDisciplinaSelecionadasTres;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasQuatro() {
		return listaDisciplinaSelecionadasQuatro;
	}

	public void setListaDisciplinaSelecionadasQuatro(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasQuatro) {
		this.listaDisciplinaSelecionadasQuatro = listaDisciplinaSelecionadasQuatro;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasCinco() {
		return listaDisciplinaSelecionadasCinco;
	}

	public void setListaDisciplinaSelecionadasCinco(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasCinco) {
		this.listaDisciplinaSelecionadasCinco = listaDisciplinaSelecionadasCinco;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasSeis() {
		return listaDisciplinaSelecionadasSeis;
	}

	public void setListaDisciplinaSelecionadasSeis(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSeis) {
		this.listaDisciplinaSelecionadasSeis = listaDisciplinaSelecionadasSeis;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasSete() {
		return listaDisciplinaSelecionadasSete;
	}

	public void setListaDisciplinaSelecionadasSete(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasSete) {
		this.listaDisciplinaSelecionadasSete = listaDisciplinaSelecionadasSete;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasOito() {
		return listaDisciplinaSelecionadasOito;
	}

	public void setListaDisciplinaSelecionadasOito(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOito) {
		this.listaDisciplinaSelecionadasOito = listaDisciplinaSelecionadasOito;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasNove() {
		return listaDisciplinaSelecionadasNove;
	}

	public void setListaDisciplinaSelecionadasNove(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasNove) {
		this.listaDisciplinaSelecionadasNove = listaDisciplinaSelecionadasNove;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDez() {
		return listaDisciplinaSelecionadasDez;
	}

	public void setListaDisciplinaSelecionadasDez(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDez) {
		this.listaDisciplinaSelecionadasDez = listaDisciplinaSelecionadasDez;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasOnze() {
		return listaDisciplinaSelecionadasOnze;
	}

	public void setListaDisciplinaSelecionadasOnze(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasOnze) {
		this.listaDisciplinaSelecionadasOnze = listaDisciplinaSelecionadasOnze;
	}

	public List<DisciplinaPlanejamento> getListaDisciplinaSelecionadasDoze() {
		return listaDisciplinaSelecionadasDoze;
	}

	public void setListaDisciplinaSelecionadasDoze(
			List<DisciplinaPlanejamento> listaDisciplinaSelecionadasDoze) {
		this.listaDisciplinaSelecionadasDoze = listaDisciplinaSelecionadasDoze;
	}

	public HistoricoDAO getHistoricoDAO() {
		return historicoDAO;
	}

	public void setHistoricoDAO(HistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}

	public float getIra() {
		return ira;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public List<String> getListaCargaHorariaPeriodo() {
		return listaCargaHorariaPeriodo;
	}

	public void setListaCargaHorariaPeriodo(List<String> listaCargaHorariaPeriodo) {
		this.listaCargaHorariaPeriodo = listaCargaHorariaPeriodo;
	}

	public int getHorasFaltamEletivas() {
		return horasFaltamEletivas;
	}

	public void setHorasFaltamEletivas(int horasFaltamEletivas) {
		this.horasFaltamEletivas = horasFaltamEletivas;
	}

	public int getHorasFaltamOpcionais() {
		return horasFaltamOpcionais;
	}

	public void setHorasFaltamOpcionais(int horasFaltamOpcionais) {
		this.horasFaltamOpcionais = horasFaltamOpcionais;
	}

	public Curriculum getCurriculumAluno() {
		return curriculumAluno;
	}

	public void setCurriculumAluno(Curriculum curriculumAluno) {
		this.curriculumAluno = curriculumAluno;
	}

	public int getContadorPorPeriodo() {
		return contadorPorPeriodo;
	}

	public void setContadorPorPeriodo(int contadorPorPeriodo) {
		this.contadorPorPeriodo = contadorPorPeriodo;
	}

	public Integer getQtdHorasPeriodo() {
		return qtdHorasPeriodo;
	}

	public void setQtdHorasPeriodo(Integer qtdHorasPeriodo) {
		this.qtdHorasPeriodo = qtdHorasPeriodo;
	}

	public boolean isLgCampoHrsPeriodo() {
		return lgCampoHrsPeriodo;
	}

	public void setLgCampoHrsPeriodo(boolean lgCampoHrsPeriodo) {
		this.lgCampoHrsPeriodo = lgCampoHrsPeriodo;
	}

	public Student getSt() {
		return st;
	}

	public void setSt(Student st) {
		this.st = st;
	}

	public boolean isLgTabelaUm() {
		return lgTabelaUm;
	}

	public void setLgTabelaUm(boolean lgTabelaUm) {
		this.lgTabelaUm = lgTabelaUm;
	}

	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}

	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}

	public boolean isLgTabelaDois() {
		return lgTabelaDois;
	}

	public void setLgTabelaDois(boolean lgTabelaDois) {
		this.lgTabelaDois = lgTabelaDois;
	}

	public boolean isLgTabelaTres() {
		return lgTabelaTres;
	}

	public void setLgTabelaTres(boolean lgTabelaTres) {
		this.lgTabelaTres = lgTabelaTres;
	}

	public boolean isLgTabelaQuatro() {
		return lgTabelaQuatro;
	}

	public void setLgTabelaQuatro(boolean lgTabelaQuatro) {
		this.lgTabelaQuatro = lgTabelaQuatro;
	}

	public boolean isLgTabelaCinco() {
		return lgTabelaCinco;
	}

	public void setLgTabelaCinco(boolean lgTabelaCinco) {
		this.lgTabelaCinco = lgTabelaCinco;
	}

	public boolean isLgTabelaSeis() {
		return lgTabelaSeis;
	}

	public void setLgTabelaSeis(boolean lgTabelaSeis) {
		this.lgTabelaSeis = lgTabelaSeis;
	}

	public boolean isLgTabelaSete() {
		return lgTabelaSete;
	}

	public void setLgTabelaSete(boolean lgTabelaSete) {
		this.lgTabelaSete = lgTabelaSete;
	}

	public boolean isLgTabelaOito() {
		return lgTabelaOito;
	}

	public void setLgTabelaOito(boolean lgTabelaOito) {
		this.lgTabelaOito = lgTabelaOito;
	}

	public boolean isLgTabelaNove() {
		return lgTabelaNove;
	}

	public void setLgTabelaNove(boolean lgTabelaNove) {
		this.lgTabelaNove = lgTabelaNove;
	}

	public boolean isLgTabelaDez() {
		return lgTabelaDez;
	}

	public void setLgTabelaDez(boolean lgTabelaDez) {
		this.lgTabelaDez = lgTabelaDez;
	}

	public boolean isLgTabelaOnze() {
		return lgTabelaOnze;
	}

	public void setLgTabelaOnze(boolean lgTabelaOnze) {
		this.lgTabelaOnze = lgTabelaOnze;
	}

	public boolean isLgTabelaDoze() {
		return lgTabelaDoze;
	}

	public void setLgTabelaDoze(boolean lgTabelaDoze) {
		this.lgTabelaDoze = lgTabelaDoze;
	}


}