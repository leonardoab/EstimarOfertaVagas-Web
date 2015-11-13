package controller;

import java.util.List;

import model.Curso;
import model.Equivalencia;
import model.Grade;
import model.GradeDisciplina;
import model.Historico;
import model.PreRequisito;
import model.arvore.Class;
import model.arvore.ClassFactory;
import model.arvore.ClassStatus;
import model.arvore.Curriculum;
import model.arvore.StudentsHistory;
import dao.Interface.EquivalenciaDAO;
import dao.Interface.GradeDisciplinaDAO;
import dao.Interface.PreRequisitoDAO;



public class ImportarArvore {
	
	private Curso curso;
	private Grade grade;
	private EstruturaArvore estruturaArvore;
	private boolean lgProcessadoGrade = false;
	private boolean lgProcessadoHistorico = false;	
	private StudentsHistory sh = new StudentsHistory();
	private Curriculum _cur = new Curriculum();
	

	public void importarHistorico(List<Historico> listaHistorico){

		for (Historico historico:listaHistorico){
			ClassStatus status;
			String classStatus = historico.getStatusDisciplina();

			if (classStatus.equals("Matriculado")) status = ClassStatus.ENROLLED;
			else if (classStatus.equals("Aprovado") || classStatus.equals("Dispensado")) status = ClassStatus.APPROVED;
			else if (classStatus.equals("Rep Nota")) status = ClassStatus.REPROVED_GRADE;
			else if (classStatus.equals("Rep Freq")) status = ClassStatus.REPROVED_FREQUENCY;
			else continue; 

			sh.add(historico.getAluno().getMatricula(), historico.getAluno().getNome(), historico.getAluno().getPeriodoIngresso(), historico.getSemestreCursado(), historico.getDisciplina().getCodigo(), status, historico.getNota(), historico.getDisciplina().getCargaHoraria());
		}		
	}

	public void importarDisciplinas(Grade grade){

		estruturaArvore = EstruturaArvore.getInstance();		
		
		PreRequisitoDAO preRequisitoDAO = estruturaArvore.getPreRequisitoDAO();
		EquivalenciaDAO equivalenciaDAO = estruturaArvore.getEquivalenciaDAO();
		GradeDisciplinaDAO gradeDisciplinaDAO = estruturaArvore.getGradeDisciplinaDAO();
		
		List<GradeDisciplina> listaGradeDisciplina = gradeDisciplinaDAO.buscarTodasGradeDisciplinaPorGradeId(grade.getId());

		for(GradeDisciplina gradeDisciplina : listaGradeDisciplina){

			List<PreRequisito> listaPreRequisito = preRequisitoDAO.buscarPorTodosCodigoGradeDisc(gradeDisciplina.getId());

			if (gradeDisciplina.getTipoDisciplina().equals("Obrigatoria") ){

				String semester = String.valueOf(gradeDisciplina.getPeriodo());
				String _class = gradeDisciplina.getDisciplina().getCodigo();
				Class c = ClassFactory.getClass(_class,true,gradeDisciplina.getDisciplina().getCargaHoraria());
				_cur.addMandatoryClass(Integer.valueOf(semester), c);

				for (PreRequisito prerequisito:listaPreRequisito){

					String prerequisite = prerequisito.getDisciplina().getCodigo(); 
					Class pre = ClassFactory.getClass(prerequisite,false,prerequisito.getDisciplina().getCargaHoraria());
					c.addPrerequisite(pre);
				}
				c.setWorkload(Integer.valueOf(gradeDisciplina.getDisciplina().getCargaHoraria()));
			}

			else {

				Class c = ClassFactory.getClass(gradeDisciplina.getDisciplina().getCodigo(),false,gradeDisciplina.getDisciplina().getCargaHoraria());
				_cur.addElectiveClass(c);

				for (PreRequisito prerequisito:listaPreRequisito){

					String prerequisite = prerequisito.getDisciplina().getCodigo(); 
					Class pre = ClassFactory.getClass(prerequisite,false,gradeDisciplina.getDisciplina().getCargaHoraria());
					c.addPrerequisite(pre);
				}

				c.setWorkload(Integer.valueOf(gradeDisciplina.getDisciplina().getCargaHoraria()));
			}
		}

		List<Equivalencia> listaEquivalencia = equivalenciaDAO.buscarTodasEquivalenciasPorGrade(grade.getId());

		for(Equivalencia equivalencia : listaEquivalencia){

			String idDaGrade = equivalencia.getDisciplinaGrade().getCodigo();
			String idNaoDaGrade = equivalencia.getDisciplinaEquivalente().getCodigo();

			Class c = null;
			if (!ClassFactory.contains(idDaGrade)
					&& ClassFactory.contains(idNaoDaGrade)) {
				String aux = idNaoDaGrade;
				idNaoDaGrade = idDaGrade;
				idDaGrade = aux;
			}

			//VERIFICAR
			// TODO há um erro abaixo. Se já tem 2 disciplinas, tem que ver qual
						// é a obrigatória/eletiva, caso contrário se apareceu 2x a
						// equivalência, então dá erro. Ex: MAT114->MAT157 e MAT114->MAT156
						else if (ClassFactory.contains(idDaGrade)
								&& ClassFactory.contains(idNaoDaGrade))
							// throw new
							// IOException("Equivalência de duas disciplinas já existentes na grade: "
							// + idDaGrade + " <-> " + idNaoDaGrade);
							System.out.println("Equivalência de duas disciplinas já existentes na grade: "
									+ idDaGrade + " <-> " + idNaoDaGrade);

						else if (!ClassFactory.contains(idDaGrade)
								&& !ClassFactory.contains(idNaoDaGrade))
							System.out.println("Equivalência de duas disciplinas não existentes na grade: "
									+ idDaGrade + " <-> " + idNaoDaGrade);


			c = ClassFactory.getClass(idDaGrade,false,equivalencia.getDisciplinaGrade().getCargaHoraria());
			ClassFactory.addClass(idNaoDaGrade, c);

		}		
	}


	public Grade getGrade() {
		return grade;
	}
	
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public boolean isLgProcessadoHistorico() {
		return lgProcessadoHistorico;
	}

	public void setLgProcessadoHistorico(boolean lgProcessadoHistorico) {
		this.lgProcessadoHistorico = lgProcessadoHistorico;
	}

	public boolean isLgProcessadoGrade() {
		return lgProcessadoGrade;
	}

	public void setLgProcessadoGrade(boolean lgProcessadoGrade) {
		this.lgProcessadoGrade = lgProcessadoGrade;
	}

	public StudentsHistory getSh() {
		return sh;
	}

	public void setSh(StudentsHistory sh) {
		this.sh = sh;
	}

	public Curriculum get_cur() {
		return _cur;
	}

	public void set_cur(Curriculum _cur) {
		this._cur = _cur;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
}
