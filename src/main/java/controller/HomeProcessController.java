package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Curso;
import dao.CursoDAOImpl;
import dao.Interface.CursoDAO;



@Named 
@ViewScoped
public class HomeProcessController implements Serializable {

	//========================================================= VARIABLES ==================================================================================//

	private static final long serialVersionUID = 1L;

	private CursoDAO cursoDAO = new CursoDAOImpl();

	long idCursoPessoa;
	
	private Curso curso = new Curso();;
	
	//========================================================= METODOS ==================================================================================//

	@Inject
	private UsuarioController usuarioController;

	@PostConstruct
	public void init() {

	
		
		
		
		idCursoPessoa = usuarioController.getPessoa().getIdCurso();
		curso = cursoDAO.recuperarPorId(idCursoPessoa);

	}

	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
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

	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
