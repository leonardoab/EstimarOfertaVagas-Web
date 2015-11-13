package controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.EspectativaDisciplina;
import model.Grade;
import model.GradeDisciplina;
import model.GradeHistorico;
import model.Historico;
import model.arvore.Class;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.Student;
import model.arvore.StudentsHistory;

import org.primefaces.component.datatable.DataTable;

import dao.CursoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.HistoricoDAO;
import estimate.Estimator;


@Named
@ViewScoped
public class VisaoGeralController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private int i;	

	private ImportarArvore importador;
	private Curriculum curriculum;
	private Aluno aluno = new Aluno();
	private Curso curso = new Curso();
	
	private boolean ldGridHistorico = false;

	private int percNrMatriculados = 90  ;
	private int percNrRN = 60 ;
	private int percNrRF = 70;
	private int percNrH = 80;
	private int percNrQH  = 50;

	//0.9f, 0.6f, 0.7f, 0.8f, 0.5f

	private AlunoDAO alunoDAO ;
	private CursoDAO cursoDAO = new CursoDAOImpl();
	private GradeDAO gradeDAO ;
	private HistoricoDAO historicoDAO ;
	private DisciplinaDAO disciplinaDAO ;
	private GradeDisciplinaDAO gradeDisciplinaDAO ;

	private List<String> selectedOptionsObrigatoria = new ArrayList<String>();
	private List<String> selectedOptionsEletiva = new ArrayList<String>();
	private List<String> selectedOptionsOpcional = new ArrayList<String>();

	private List<String> selectedOptionsPeriodo = new ArrayList<String>();
	private List<String> selectedOptionsPeriodoDois = new ArrayList<String>();
	private List<String> selectedOptionsPeriodoTres = new ArrayList<String>();
	private List<String> selectedOptionsSituacao = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoDois = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoTres = new ArrayList<String>();
	private List<String> selectedOptionsSituacaoQuatro = new ArrayList<String>();
	private List<String> listagrade = new ArrayList<String>();


	private ArrayList<String> lista = new ArrayList<String>();
	

	private ArrayList<String> listaDisciplinasDisponiveis = new ArrayList<String>();  
	private ArrayList<String> listaTodasDisciplinasDisponiveis = new ArrayList<String>(); 
	private ArrayList<String> listaTodasDisciplinasSelecionadas = new ArrayList<String>(); 
	private ArrayList<String> listaAlunosSelecionados = new ArrayList<String>(); 
	private ArrayList<String> listaAnosSelecionados = new ArrayList<String>();

	private ArrayList<String> listaAlunosTodos = new ArrayList<String>();
	private ArrayList<String> listaAnoTodos = new ArrayList<String>();

	private List<ColumnModel> columns;
	private List<Aluno> listaAluno= new ArrayList<Aluno>();	
	private ArrayList<Class> classes = new ArrayList<Class>();
	private List<Historico> listaHistorico= new ArrayList<Historico>();
	private List<GradeHistorico> listaGradeHistorico ;
	private List<GradeHistorico> listaGradeHistoricoSelecionadas ;
	private List<EspectativaDisciplina> listaEspectativaDisciplina = new ArrayList<EspectativaDisciplina>();	
	private List<GradeDisciplina> listagradeDisciplina = new ArrayList<GradeDisciplina>();
	//print List(Log) listaLog = new ArrayList<Log>();
	private List<String> listaLog = new ArrayList<String>();

	private List<EspectativaDisciplina> listaEspectativaDisciplinaSelecionada;


	private boolean lgPeriodoUm = false;
	private boolean lgPeriodoCinco = false;
	private boolean lgPeriodoNove = false;
	private boolean lgEletivas = false;


	private Grade grade;
	private EstruturaArvore estruturaArvore;

	@Inject
	private UsuarioController usuarioController;

	private Date d ;
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	@PostConstruct
	public void init() {



		estruturaArvore = EstruturaArvore.getInstance();
		alunoDAO =  estruturaArvore.getAlunoDAO();
		gradeDAO = estruturaArvore.getGradeDAO();
		historicoDAO = estruturaArvore.getHistoricoDAO();
		disciplinaDAO = estruturaArvore.getDisciplinaDAO();
		gradeDisciplinaDAO = estruturaArvore.getGradeDisciplinaDAO();

		curso = cursoDAO.recuperarPorId(usuarioController.getPessoa().getIdCurso());



	}


	//========================================================= METODOS ==================================================================================//


	public void dadosArvore(){

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Inicio dadosArvore");

		for(String codigoGrade :listagrade){

			grade = gradeDAO.buscarPorCodigoGrade(codigoGrade, curso.getId());

			importador = estruturaArvore.recuperarArvore(curso	, grade);


			curriculum = importador.get_cur();
			printTableHeader();

		}
		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Fim dadosArvore");
	}


	public void gerarDados(){

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Inicio gerarDados");

		listaEspectativaDisciplina = new ArrayList<EspectativaDisciplina>();


		classes = new ArrayList<Class>();

		listaDisciplinasDisponiveis = new ArrayList<String>(); 

		columns = new ArrayList<ColumnModel>();

		listaGradeHistorico = new ArrayList<GradeHistorico>();

		listaTodasDisciplinasDisponiveis = new ArrayList<String>();  

		listaAlunosTodos = new ArrayList<String>();  

		listaAnoTodos = new ArrayList<String>();  

		boolean todos = false;
		boolean todosituacao = false;


		if (listagrade == null  || listagrade.size() == 0 ){
			
			ldGridHistorico = false;
			
			return;}

		if(selectedOptionsSituacao.size() == 0 && selectedOptionsSituacaoDois.size() == 0 && selectedOptionsSituacaoTres.size() == 0 &&
				selectedOptionsSituacaoQuatro.size() == 0 ){

			todosituacao = true;

		}



		if (listagrade.size() == 1 && 

				(listaAlunosSelecionados == null || listaAlunosSelecionados.size() == 0) && (listaAnosSelecionados == null || listaAnosSelecionados.size() == 0) && todosituacao){

			todos = true;


		}



		

		for(String codigoGrade :listagrade){

			grade = gradeDAO.buscarPorCodigoGrade(codigoGrade, curso.getId());

			dadosArvore();

			StudentsHistory sh = importador.getSh();

			ArrayList<String> student = new ArrayList<String>();

			student.addAll(sh.getStudents().keySet());

			Collections.sort(student);

			String colorCode = "";	

			for (String stid : student) {

				boolean naoGravar = false;


				Student st = sh.getStudents().get(stid);

				listaAlunosTodos.add(stid);

				if( listaAnoTodos != null && !listaAnoTodos.contains(st.get_ingresso())               ){
					listaAnoTodos.add(st.get_ingresso());
				}

				if (listaAnosSelecionados != null){

					if ( listaAnosSelecionados.size() != 0  ){

						if ( !listaAnosSelecionados.contains(st.get_ingresso())             ){


							continue;

						}

					}
				}


				if (listaAlunosSelecionados != null){

					if ( listaAlunosSelecionados.size() != 0  ){

						if (!listaAlunosSelecionados.contains(stid)){


							continue;

						}

					}
				}

				



				//imprimir opcionais
				String optional = "Opcionais:Aprovado - ";
				String disciplinasOpcionais = "";
				for (Class class1 : st.getClasses(ClassStatus.APPROVED).keySet()) {
					boolean achou = false;

					for (Class classOpcional :classes ) {

						if (classOpcional.getId().equals(class1.getId())){

							achou = true;
							break;
						}
					}
					if (achou == false){
						optional += class1.getId() + "; ";
					}


				}
				if (!optional.equals("Opcionais:Aprovado - "))
					disciplinasOpcionais = disciplinasOpcionais + (optional.substring(0, optional.length()-1)+". ");

				optional = "Matriculado - ";
				for (Class class1 : st.getClasses(ClassStatus.ENROLLED).keySet()) {
					boolean achou = false;

					for (Class classOpcional :classes ) {

						if (classOpcional.getId().equals(class1.getId())){

							achou = true;
							break;
						}
					}

					if (achou == false){
						optional += class1.getId() + "; ";
					}
				}
				if (!optional.equals("Matriculado - "))
					disciplinasOpcionais = disciplinasOpcionais + (optional.substring(0, optional.length()-1)+". ");


				for (Class c1 : classes) {

					int retorno = Estimator.processStudentCourseStatus(c1, st);

					if(listaDisciplinasDisponiveis.contains(c1.getId())){

						if (retorno == 0) 		colorCode = "#0B2161";
						else if (retorno == 1) 	colorCode = "#7777FF";
						else if (retorno == 2)  colorCode = "#FFA500";
						else if (retorno ==3)	colorCode = "#FF0000";
						else if (retorno == 4)	colorCode = "#04B431";
						else if (retorno == 5)	colorCode = "#FA00FF";
						else colorCode = "#FFFFFF"; // Não pode cursar

						if(((selectedOptionsSituacao.contains(colorCode) || selectedOptionsSituacaoDois.contains(colorCode) 
								|| selectedOptionsSituacaoTres.contains(colorCode) || selectedOptionsSituacaoQuatro.contains(colorCode)) == false)
								&& todos == false && todosituacao == false ){

							naoGravar = true;
							break;
						}

						lista.add(colorCode);
						

					}
				}

				if (stid.equals("2675133720000")){
					
					System.out.println();
					
				}
				

				if(!naoGravar){
					listaGradeHistorico.add(new GradeHistorico(lista, disciplinasOpcionais,sh.getStudents().get(stid).getNome(),stid));
					
					
					
				}

				lista = new ArrayList<String>();
				

			}
		}

		int i = 0;
		if (listaGradeHistorico.size() != 0){
			for (i=0;i< listaGradeHistorico.get(0).getHistoricoAluno().size() ;i++){

				for(GradeHistorico gradeHistorico:listaGradeHistorico){

					if (gradeHistorico.getHistoricoAluno().get(i) == "#7777FF") 		listaEspectativaDisciplina.get(i).setNrMatriculados(listaEspectativaDisciplina.get(i).getNrMatriculados() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FFA500") 		listaEspectativaDisciplina.get(i).setNrRN(listaEspectativaDisciplina.get(i).getNrRN() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FF0000") 		listaEspectativaDisciplina.get(i).setNrRF(listaEspectativaDisciplina.get(i).getNrRF() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#04B431") 		listaEspectativaDisciplina.get(i).setNrH(listaEspectativaDisciplina.get(i).getNrH() + 1);
					else if (gradeHistorico.getHistoricoAluno().get(i) == "#FA00FF") 		listaEspectativaDisciplina.get(i).setNrQH(listaEspectativaDisciplina.get(i).getNrQH() + 1);



				}

			}
		}

		pararAqui();

		calculaPercentual();

		if (listaGradeHistorico.size() != 0 ){
			
			ldGridHistorico = true;
		}
		

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Fim gerarDados");

	}

	public void pararAqui(){}


	public void calculaPercentual(){

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Inicio calculaPercentual");

		for (EspectativaDisciplina espectativaDisciplina:listaEspectativaDisciplina){


			espectativaDisciplina.setPercNrMatriculados( espectativaDisciplina.getNrMatriculados() * percNrMatriculados / 100	);
			espectativaDisciplina.setPercNrRN( espectativaDisciplina.getNrRN() * percNrRN / 100	);
			espectativaDisciplina.setPercNrRF( espectativaDisciplina.getNrRF() * percNrRF / 100	);
			espectativaDisciplina.setPercNrH( espectativaDisciplina.getNrH() * percNrH / 100	);
			espectativaDisciplina.setPercNrQH( espectativaDisciplina.getNrQH() * percNrQH / 100	);

			espectativaDisciplina.setNrTotal(espectativaDisciplina.getNrMatriculados() + espectativaDisciplina.getNrRN() + espectativaDisciplina.getNrRF() + 
					espectativaDisciplina.getNrH() + espectativaDisciplina.getNrQH());

			espectativaDisciplina.setPercNrTotal(espectativaDisciplina.getPercNrMatriculados() + espectativaDisciplina.getPercNrRN() + espectativaDisciplina.getPercNrRF()
					+ espectativaDisciplina.getPercNrH() + espectativaDisciplina.getPercNrQH());

		}

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Fim calculaPercentual");


	}


	public void printTableHeader() {

		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Inicio printTableHeader");

		Class[] classe = { };

		if (listagrade.size() == 1){

			ArrayList<Integer> semester = new ArrayList<Integer>();
			semester.addAll(curriculum.getMandatories().keySet());
			Collections.sort(semester);

			columns = new ArrayList<ColumnModel>();
			
			

			//Imprimindo obrigatórias

			i = 0;

			String corDisciplia = "#A9E2F3";


			for (Integer sem : semester) {
				for (Class c1 : curriculum.getMandatories().get(sem).toArray(classe)) {

					listaTodasDisciplinasDisponiveis.add(c1.getId());

					if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){

						if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){

							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(c1);
							columns.add(new ColumnModel(c1.getId(), i, corDisciplia));

							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);

							i++;

							if (corDisciplia.equals("#A9E2F3")){

								corDisciplia = "#CEECF5";
							}

							else {

								corDisciplia = "#A9E2F3";
							}
						}
					}


					else  if (  selectedOptionsPeriodo.size() != 0
							||  selectedOptionsPeriodoDois.size() != 0  
							||  selectedOptionsPeriodoTres.size() != 0 )	{

						Disciplina disciplina = disciplinaDAO.buscarPorCodigoDisciplina(c1.getId());

						GradeDisciplina gradedisciplina = gradeDisciplinaDAO.buscarPorDisciplinaGrade(grade.getId(), disciplina.getId());

						String periodo = String.valueOf(gradedisciplina.getPeriodo());

						if (gradedisciplina.getPeriodo() < 10){

							periodo = "0" + periodo;
						}

						if ( selectedOptionsPeriodo.contains(periodo)  || selectedOptionsPeriodoDois.contains(periodo) || selectedOptionsPeriodoTres.contains(periodo)	){

							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(c1);
							columns.add(new ColumnModel(c1.getId(), i, corDisciplia));


							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);


							i++;

							//listaDisciplinasDisponiveis.

							if (corDisciplia.equals("#A9E2F3")){

								corDisciplia = "#CEECF5";
							}
							else {

								corDisciplia = "#A9E2F3";
							}


						}			
					}
					else if (selectedOptionsEletiva == null || selectedOptionsEletiva.size() == 0){

						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i, corDisciplia));

						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);

						i++;

						//listaDisciplinasDisponiveis.

						if (corDisciplia.equals("#A9E2F3")){

							corDisciplia = "#CEECF5";
						}
						else {

							corDisciplia = "#A9E2F3";
						}


					}
				}
			}

			//Imprimindo eletivas
			for(Class c1 : curriculum.getElectives()) {

				listaTodasDisciplinasDisponiveis.add(c1.getId());


				if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){

					if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){

						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i,"#ccc"));

						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);

						i++;
					}
				}

				else if (selectedOptionsEletiva.contains("Eletivas")	){

					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,"#ccc"));

					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);

					i++;
				}
				else  if ( (selectedOptionsPeriodo == null || selectedOptionsPeriodo.size() == 0) 
						&& (selectedOptionsPeriodoDois == null || selectedOptionsPeriodoDois.size() == 0 ) 
						&& (selectedOptionsPeriodoTres == null || selectedOptionsPeriodoTres.size() == 0) )	{

					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,"#ccc"));

					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);

					i++;

				}
			}


		}

		else if (listagrade.size() > 1){

			i = 0;

			ArrayList<Integer> semester = new ArrayList<Integer>();
			semester.addAll(curriculum.getMandatories().keySet());
			Collections.sort(semester);

			//Imprimindo obrigatórias
			for (Integer sem : semester) {
				for (Class c1 : curriculum.getMandatories().get(sem).toArray(classe)) {

					listaTodasDisciplinasDisponiveis.add(c1.getId());

					if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){

						if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){

							listaDisciplinasDisponiveis.add(c1.getId());
							classes.add(c1);
							columns.add(new ColumnModel(c1.getId(), i,""));

							EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
							espectativaDisciplina.setNomeDisciplina(c1.getId());							
							listaEspectativaDisciplina.add(espectativaDisciplina);

							i++;
						}
					}

					else if(!listaDisciplinasDisponiveis.contains(c1.getId())){

						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i, ""));

						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);


						i++;

					}			
				}
			}

			//Imprimindo eletivas
			for(Class c1 : curriculum.getElectives()) {

				listaTodasDisciplinasDisponiveis.add(c1.getId());

				if(listaTodasDisciplinasSelecionadas != null && listaTodasDisciplinasSelecionadas.size() > 0  ){

					if(listaTodasDisciplinasSelecionadas.contains(c1.getId())){

						listaDisciplinasDisponiveis.add(c1.getId());
						classes.add(c1);
						columns.add(new ColumnModel(c1.getId(), i,""));

						EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
						espectativaDisciplina.setNomeDisciplina(c1.getId());							
						listaEspectativaDisciplina.add(espectativaDisciplina);

						i++;
					}
				}

				else if(!listaDisciplinasDisponiveis.contains(c1.getId())){

					listaDisciplinasDisponiveis.add(c1.getId());
					classes.add(c1);
					columns.add(new ColumnModel(c1.getId(), i,""));

					EspectativaDisciplina espectativaDisciplina = new EspectativaDisciplina();							
					espectativaDisciplina.setNomeDisciplina(c1.getId());							
					listaEspectativaDisciplina.add(espectativaDisciplina);

					i++;

				}
			}

		}


		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Fim printTableHeader");
	}

	public List<String> gradeCodigos(String codigo) {		



		codigo = codigo.toUpperCase();

		List<String> todos = gradeDAO.buscarTodosCodigosGrade(codigo, curso.getId());

		return todos;
	}

	public List<String> nomeDisciplina(String codigo) {

		List<String> listaSelecionadas = new ArrayList<String>();

		for (String disciplina:listaTodasDisciplinasDisponiveis ){

			System.out.println(codigo.toUpperCase());

			if (disciplina.indexOf(codigo.toUpperCase()) >= 0){

				listaSelecionadas.add(disciplina);

			}
		}

		Collections.sort(listaSelecionadas);

		return listaSelecionadas;

	}	


	public List<String> nomeAluno(String codigo) {

		List<String> listaSelecionadas = new ArrayList<String>();

		for (String aluno:listaAlunosTodos ){

			System.out.println(codigo.toUpperCase());

			if (aluno.indexOf(codigo.toUpperCase()) >= 0){

				listaSelecionadas.add(aluno);

			}
		}

		Collections.sort(listaSelecionadas);

		return listaSelecionadas;

	}

	public List<String> alunoAno(String codigo) {

		List<String> listaSelecionadas = new ArrayList<String>();

		for (String alunoAno:listaAnoTodos ){

			System.out.println(codigo.toUpperCase());

			if (alunoAno.indexOf(codigo.toUpperCase()) >= 0){

				listaSelecionadas.add(alunoAno);

			}
		}

		Collections.sort(listaSelecionadas);

		return listaSelecionadas;

	}	

	public void onItemSelectGrade() {

		listaLog = new ArrayList<String>();
		d = GregorianCalendar.getInstance().getTime();  
		listaLog.add(format.format(d).toString() + ":" + "Inicio onItemSelectGrade");

		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:discplinas");
		dataTable.clearInitialState();
		dataTable.reset();

		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:estimativas");
		dataTable.clearInitialState();
		dataTable.reset();


		if (listagrade == null || listagrade.size() == 0 || listagrade.size() == 1){

			lgPeriodoUm = false;
			lgPeriodoCinco = false;
			lgPeriodoNove = false;
			lgEletivas = false;
			
			


		}
		else {



			lgPeriodoUm = true;
			lgPeriodoCinco = true;
			lgPeriodoNove = true;
			lgEletivas = true;




		}

		listaTodasDisciplinasSelecionadas = new ArrayList<String>(); 
		gerarDados();

		for (String log : listaLog){



			System.out.println(log);  

		}
		FacesMessage msg = new FacesMessage("Filtro Aplicado!");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void onItemSelect() {

		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:discplinas");
		dataTable.clearInitialState();
		dataTable.reset();

		dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:estimativas");
		dataTable.clearInitialState();
		dataTable.reset();




		if ((listaTodasDisciplinasSelecionadas == null || listaTodasDisciplinasSelecionadas.size() == 0) && listagrade.size() == 1){
			lgPeriodoUm = false;
			lgPeriodoCinco = false;
			lgPeriodoNove = false;
			lgEletivas = false;


		}
		else{

			lgPeriodoUm = true;
			lgPeriodoCinco = true;
			lgPeriodoNove = true;
			lgEletivas = true;


		}

		gerarDados();

		FacesMessage msg = new FacesMessage("Filtro Aplicado!");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	//========================================================= CLASSE ==================================================================================//

	static public class ColumnModel implements Serializable {  

		private static final long serialVersionUID = 1L;

		private String header;  
		private int mes;  
		private String cor;


		public ColumnModel(String header, int mes,String cor) {  
			this.header = header;  
			this.mes = mes;
			this.cor = cor;

		}  

		public String getHeader() {  
			return header;  
		}  

		public String getCor() {  
			return "background-color:" + cor + ";";  
		}  

		
		public String retornaProperty(GradeHistorico c) {  

			return c.getHistoricoAluno().get(mes);
		}  
		
		
		
		
	}







	//========================================================= GET - SET ==================================================================================//

	public int getI() {
		return i;
	}


	public void setI(int i) {
		this.i = i;
	}


	public ImportarArvore getImportador() {
		return importador;
	}


	public void setImportador(ImportarArvore importador) {
		this.importador = importador;
	}


	public Curriculum getCurriculum() {
		return curriculum;
	}


	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
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





	public List<String> getSelectedOptionsPeriodo() {
		return selectedOptionsPeriodo;
	}


	public void setSelectedOptionsPeriodo(List<String> selectedOptionsPeriodo) {
		this.selectedOptionsPeriodo = selectedOptionsPeriodo;
	}


	public List<String> getSelectedOptionsPeriodoDois() {
		return selectedOptionsPeriodoDois;
	}


	public void setSelectedOptionsPeriodoDois(
			List<String> selectedOptionsPeriodoDois) {
		this.selectedOptionsPeriodoDois = selectedOptionsPeriodoDois;
	}


	public List<String> getSelectedOptionsPeriodoTres() {
		return selectedOptionsPeriodoTres;
	}


	public void setSelectedOptionsPeriodoTres(
			List<String> selectedOptionsPeriodoTres) {
		this.selectedOptionsPeriodoTres = selectedOptionsPeriodoTres;
	}


	public List<String> getSelectedOptionsSituacao() {
		return selectedOptionsSituacao;
	}


	public void setSelectedOptionsSituacao(List<String> selectedOptionsSituacao) {
		this.selectedOptionsSituacao = selectedOptionsSituacao;
	}


	public List<String> getSelectedOptionsSituacaoDois() {
		return selectedOptionsSituacaoDois;
	}


	public void setSelectedOptionsSituacaoDois(
			List<String> selectedOptionsSituacaoDois) {
		this.selectedOptionsSituacaoDois = selectedOptionsSituacaoDois;
	}


	public List<String> getSelectedOptionsSituacaoTres() {
		return selectedOptionsSituacaoTres;
	}


	public void setSelectedOptionsSituacaoTres(
			List<String> selectedOptionsSituacaoTres) {
		this.selectedOptionsSituacaoTres = selectedOptionsSituacaoTres;
	}


	public ArrayList<String> getLista() {
		return lista;
	}


	public void setLista(ArrayList<String> lista) {
		this.lista = lista;
	}






	public List<ColumnModel> getColumns() {
		return columns;
	}


	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}


	public List<GradeHistorico> getListaGradeHistorico() {
		return listaGradeHistorico;
	}


	public void setListaGradeHistorico(List<GradeHistorico> listaGradeHistorico) {
		this.listaGradeHistorico = listaGradeHistorico;
	}


	public List<Aluno> getListaAluno() {
		return listaAluno;
	}


	public void setListaAluno(List<Aluno> listaAluno) {
		this.listaAluno = listaAluno;
	}


	public List<Historico> getListaHistorico() {
		return listaHistorico;
	}


	public void setListaHistorico(List<Historico> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}


	public List<GradeDisciplina> getListagradeDisciplina() {
		return listagradeDisciplina;
	}


	public void setListagradeDisciplina(List<GradeDisciplina> listagradeDisciplina) {
		this.listagradeDisciplina = listagradeDisciplina;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public ArrayList<String> getListaDisciplinasDisponiveis() {
		return listaDisciplinasDisponiveis;
	}


	public void setListaDisciplinasDisponiveis(
			ArrayList<String> listaDisciplinasDisponiveis) {
		this.listaDisciplinasDisponiveis = listaDisciplinasDisponiveis;
	}


	public List<String> getSelectedOptionsObrigatoria() {
		return selectedOptionsObrigatoria;
	}


	public void setSelectedOptionsObrigatoria(
			List<String> selectedOptionsObrigatoria) {
		this.selectedOptionsObrigatoria = selectedOptionsObrigatoria;
	}


	public List<String> getSelectedOptionsEletiva() {
		return selectedOptionsEletiva;
	}


	public void setSelectedOptionsEletiva(List<String> selectedOptionsEletiva) {
		this.selectedOptionsEletiva = selectedOptionsEletiva;
	}


	public List<String> getSelectedOptionsOpcional() {
		return selectedOptionsOpcional;
	}


	public void setSelectedOptionsOpcional(List<String> selectedOptionsOpcional) {
		this.selectedOptionsOpcional = selectedOptionsOpcional;
	}


	public List<String> getSelectedOptionsSituacaoQuatro() {
		return selectedOptionsSituacaoQuatro;
	}


	public void setSelectedOptionsSituacaoQuatro(
			List<String> selectedOptionsSituacaoQuatro) {
		this.selectedOptionsSituacaoQuatro = selectedOptionsSituacaoQuatro;
	}


	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	public UsuarioController getUsuarioController() {
		return usuarioController;
	}


	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}


	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}


	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}


	public GradeDAO getGradeDAO() {
		return gradeDAO;
	}


	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}


	public List<String> getListagrade() {
		return listagrade;
	}


	public void setListagrade(List<String> listagrade) {
		this.listagrade = listagrade;
	}


	public ArrayList<String> getListaTodasDisciplinasDisponiveis() {
		return listaTodasDisciplinasDisponiveis;
	}


	public void setListaTodasDisciplinasDisponiveis(
			ArrayList<String> listaTodasDisciplinasDisponiveis) {
		this.listaTodasDisciplinasDisponiveis = listaTodasDisciplinasDisponiveis;
	}


	public ArrayList<Class> getClasses() {
		return classes;
	}


	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}


	public Grade getGrade() {
		return grade;
	}


	public void setGrade(Grade grade) {
		this.grade = grade;
	}


	public EstruturaArvore getEstruturaArvore() {
		return estruturaArvore;
	}


	public void setEstruturaArvore(EstruturaArvore estruturaArvore) {
		this.estruturaArvore = estruturaArvore;
	}


	public ArrayList<String> getListaTodasDisciplinasSelecionadas() {
		return listaTodasDisciplinasSelecionadas;
	}


	public void setListaTodasDisciplinasSelecionadas(
			ArrayList<String> listaTodasDisciplinasSelecionadas) {
		this.listaTodasDisciplinasSelecionadas = listaTodasDisciplinasSelecionadas;
	}


	public boolean isLgPeriodoUm() {
		return lgPeriodoUm;
	}


	public void setLgPeriodoUm(boolean lgPeriodoUm) {
		this.lgPeriodoUm = lgPeriodoUm;
	}


	public boolean isLgPeriodoCinco() {
		return lgPeriodoCinco;
	}


	public void setLgPeriodoCinco(boolean lgPeriodoCinco) {
		this.lgPeriodoCinco = lgPeriodoCinco;
	}


	public boolean isLgPeriodoNove() {
		return lgPeriodoNove;
	}


	public void setLgPeriodoNove(boolean lgPeriodoNove) {
		this.lgPeriodoNove = lgPeriodoNove;
	}


	public boolean isLgEletivas() {
		return lgEletivas;
	}


	public void setLgEletivas(boolean lgEletivas) {
		this.lgEletivas = lgEletivas;
	}


	public ArrayList<String> getListaAlunosSelecionados() {
		return listaAlunosSelecionados;
	}


	public void setListaAlunosSelecionados(ArrayList<String> listaAlunosSelecionados) {
		this.listaAlunosSelecionados = listaAlunosSelecionados;
	}


	public ArrayList<String> getListaAnosSelecionados() {
		return listaAnosSelecionados;
	}


	public void setListaAnosSelecionados(ArrayList<String> listaAnosSelecionados) {
		this.listaAnosSelecionados = listaAnosSelecionados;
	}


	public ArrayList<String> getListaAlunosTodos() {
		return listaAlunosTodos;
	}


	public void setListaAlunosTodos(ArrayList<String> listaAlunosTodos) {
		this.listaAlunosTodos = listaAlunosTodos;
	}


	public ArrayList<String> getListaAnoTodos() {
		return listaAnoTodos;
	}


	public void setListaAnoTodos(ArrayList<String> listaAnoTodos) {
		this.listaAnoTodos = listaAnoTodos;
	}


	public List<EspectativaDisciplina> getListaEspectativaDisciplina() {
		return listaEspectativaDisciplina;
	}


	public void setListaEspectativaDisciplina(
			List<EspectativaDisciplina> listaEspectativaDisciplina) {
		this.listaEspectativaDisciplina = listaEspectativaDisciplina;
	}


	public int getPercNrMatriculados() {
		return percNrMatriculados;
	}


	public void setPercNrMatriculados(int percNrMatriculados) {
		this.percNrMatriculados = percNrMatriculados;
	}


	public int getPercNrRN() {
		return percNrRN;
	}


	public void setPercNrRN(int percNrRN) {
		this.percNrRN = percNrRN;
	}


	public int getPercNrRF() {
		return percNrRF;
	}


	public void setPercNrRF(int percNrRF) {
		this.percNrRF = percNrRF;
	}


	public int getPercNrH() {
		return percNrH;
	}


	public void setPercNrH(int percNrH) {
		this.percNrH = percNrH;
	}


	public int getPercNrQH() {
		return percNrQH;
	}


	public void setPercNrQH(int percNrQH) {
		this.percNrQH = percNrQH;
	}


	public List<EspectativaDisciplina> getListaEspectativaDisciplinaSelecionada() {
		return listaEspectativaDisciplinaSelecionada;
	}


	public void setListaEspectativaDisciplinaSelecionada(
			List<EspectativaDisciplina> listaEspectativaDisciplinaSelecionada) {
		this.listaEspectativaDisciplinaSelecionada = listaEspectativaDisciplinaSelecionada;
	}


	public List<GradeHistorico> getListaGradeHistoricoSelecionadas() {
		return listaGradeHistoricoSelecionadas;
	}


	public void setListaGradeHistoricoSelecionadas(
			List<GradeHistorico> listaGradeHistoricoSelecionadas) {
		this.listaGradeHistoricoSelecionadas = listaGradeHistoricoSelecionadas;
	}


	public boolean isLdGridHistorico() {
		return ldGridHistorico;
	}


	public void setLdGridHistorico(boolean ldGridHistorico) {
		this.ldGridHistorico = ldGridHistorico;
	}
















}

