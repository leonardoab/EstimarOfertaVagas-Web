package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Disciplina;
import org.primefaces.event.RowEditEvent;
import dao.Interface.DisciplinaDAO;


@Named
@ViewScoped
public class CadastroDisciplinasController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private DisciplinaDAO disciplinaDAO;

	private Ordenar ordenar = new Ordenar();	
	private Disciplina disciplina = new Disciplina();

	private List<Disciplina> listaDisciplinasFiltradas ;
	private List<Disciplina> listaDisciplinas = new ArrayList<Disciplina>();	
	
	private EstruturaArvore estruturaArvore;	

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {

		estruturaArvore = EstruturaArvore.getInstance();

		disciplinaDAO = estruturaArvore.getDisciplinaDAO();

		listaDisciplinas = (List<Disciplina>) disciplinaDAO.recuperarTodos();

		ordenar.DisciplinaOrdenarCodigo(listaDisciplinas);		

	}

	public void onRowEdit(RowEditEvent event) {     

		FacesMessage msg = new FacesMessage("DIsciplina Editada!",((Disciplina) event.getObject()).getNome() );
		FacesContext.getCurrentInstance().addMessage(null, msg);
		disciplinaDAO.editar(((Disciplina) event.getObject()));
	}

	public void onRowCancel(RowEditEvent event) {


		FacesMessage msg = new FacesMessage("Edição Cancelada!", ((Disciplina) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void incluirDisciplina(){

		if (disciplina.getCodigo() == null || disciplina.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Código!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getNome() == null || disciplina.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getCargaHoraria() == null || disciplina.getCargaHoraria().equals("")){
			FacesMessage msg = new FacesMessage("Preecha o campo Carga Horária!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		if (disciplina.getCargaHoraria() != null  && disciplina.getCodigo() != null && disciplina.getNome() != null){	

			disciplina.setNome(disciplina.getNome().toUpperCase());
			disciplina.setCodigo(disciplina.getCodigo().toUpperCase());		
			disciplinaDAO.persistir(disciplina);
			listaDisciplinas.add(disciplina);

			ordenar.DisciplinaOrdenarCodigo(listaDisciplinas);

			disciplina = new Disciplina();
		}
	}

	public void limpaDisciplina(){
		
		disciplina = new Disciplina();

	}

	//========================================================= GET - SET ==================================================================================//


	public DisciplinaDAO getDisciplinaDAO() {
		return disciplinaDAO;
	}

	public void setDisciplinaDAO(DisciplinaDAO disciplinaDAO) {
		this.disciplinaDAO = disciplinaDAO;
	}

	public List<Disciplina> getListaDisciplinas() {
		return listaDisciplinas;
	}

	public void setListaDisciplinas(List<Disciplina> listaDisciplinas) {
		this.listaDisciplinas = listaDisciplinas;
	}

	public List<Disciplina> getListaDisciplinasFiltradas() {
		return listaDisciplinasFiltradas;
	}

	public void setListaDisciplinasFiltradas(
			List<Disciplina> listaDisciplinasFiltradas) {
		this.listaDisciplinasFiltradas = listaDisciplinasFiltradas;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

