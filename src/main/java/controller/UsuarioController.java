package controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Curso;
import model.Pessoa;
import model.PessoaCurso;
import dao.CursoDAOImpl;
import dao.PessoaCursoDAOImpl;
import dao.PessoaDAOImpl;
import dao.Interface.CursoDAO;
import dao.Interface.PessoaCursoDAO;
import dao.Interface.PessoaDAO;


@Named
@SessionScoped 
public class UsuarioController implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String login;
	private String senha;
	
	private boolean lgimportador = false;
	private boolean lgperfil = false;

	private Pessoa pessoa = new Pessoa();
	private Curso curso = new Curso();

	private PessoaDAO pessoaDAO = new PessoaDAOImpl();
	private CursoDAO cursoDAO = new CursoDAOImpl();	
	private PessoaCursoDAO pessoaCursoDAO = new PessoaCursoDAOImpl();

	private List<PessoaCurso> listaPessoaCurso = new ArrayList<PessoaCurso>();	
	
	private ArrayList<String> grupos = new ArrayList<String>();

	private String cursoSelecionado;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;	
	
	@PostConstruct
    public void init() {		
		pessoa.setNome(login);		
	}

	public void login() throws ServletException, IOException {

		if(validarLogin()){

			RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
			dispatcher.forward(request, response);
			facesContext.responseComplete();}
	}

	public boolean validarLogin() {

		pessoa = pessoaDAO.buscarPorCpfPessoa(login.toUpperCase());

		if (pessoa == null){

			return false;

		}

		listaPessoaCurso = pessoaCursoDAO.buscarTodasPessoaCursoPorPessoa(pessoa.getId());

		grupos = new ArrayList<String>();
		
			for(PessoaCurso pessoaCurso : listaPessoaCurso ){
				if(pessoa.getIdCurso() == null){
				pessoa.setIdCurso(pessoaCurso.getCurso().getId());	
				}

				grupos.add( pessoaCurso.getCurso().getCodigo() );			
		}

		curso = cursoDAO.recuperarPorId(pessoa.getIdCurso());

		if (curso != null){
		cursoSelecionado = curso.getCodigo() ;
		}		

		if (pessoa.getCpf().toUpperCase().equals("ADMIN")){
			
			lgimportador = true;
			lgperfil = true;			
		}

		return true;
	}

	public void GerarConfirmacao() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
				"Usuário/Senha inválidos", "Mensagem de erro completa");
		context.addMessage("destinoErro", msg);
	}

	public void GerarErroConexao() {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
				"Falha ao conectar com SIGA",
				"Mensagem de erro completa");
		context.addMessage("destinoErro", msg);
	}

	public void mudarCurso(){

		curso = cursoDAO.buscarPorCodigo(cursoSelecionado);
		pessoa.setIdCurso(curso.getId());
				
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isLgimportador() {
		return lgimportador;
	}

	public void setLgimportador(boolean lgimportador) {
		this.lgimportador = lgimportador;
	}

	public boolean isLgperfil() {
		return lgperfil;
	}

	public void setLgperfil(boolean lgperfil) {
		this.lgperfil = lgperfil;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	public CursoDAO getCursoDAO() {
		return cursoDAO;
	}

	public void setCursoDAO(CursoDAO cursoDAO) {
		this.cursoDAO = cursoDAO;
	}

	public PessoaCursoDAO getPessoaCursoDAO() {
		return pessoaCursoDAO;
	}

	public void setPessoaCursoDAO(PessoaCursoDAO pessoaCursoDAO) {
		this.pessoaCursoDAO = pessoaCursoDAO;
	}

	public List<PessoaCurso> getListaPessoaCurso() {
		return listaPessoaCurso;
	}

	public void setListaPessoaCurso(List<PessoaCurso> listaPessoaCurso) {
		this.listaPessoaCurso = listaPessoaCurso;
	}

	public ArrayList<String> getGrupos() {
		return grupos;
	}

	public void setGrupos(ArrayList<String> grupos) {
		this.grupos = grupos;
	}

	public String getCursoSelecionado() {
		return cursoSelecionado;
	}

	public void setCursoSelecionado(String cursoSelecionado) {
		this.cursoSelecionado = cursoSelecionado;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
