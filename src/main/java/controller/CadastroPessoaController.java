package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Curso;
import model.Pessoa;
import model.PessoaCurso;

import org.primefaces.event.RowEditEvent;

import dao.CursoDAOImpl;
import dao.PessoaCursoDAOImpl;
import dao.PessoaDAOImpl;
import dao.Interface.CursoDAO;
import dao.Interface.PessoaCursoDAO;
import dao.Interface.PessoaDAO;


@Named
@ViewScoped
public class CadastroPessoaController implements Serializable {



	//========================================================= VARIABLES ==================================================================================//
	
	private static final long serialVersionUID = 1L;

	private PessoaDAO pessoaDAO = new PessoaDAOImpl();
	private CursoDAO cursoDAO = new CursoDAOImpl();
	private PessoaCursoDAO pessoaCursoDAO = new PessoaCursoDAOImpl();

	private Curso curso = new Curso();
	private Pessoa pessoa = new Pessoa();
	private Pessoa pessoaSelecionada = new Pessoa();
	private PessoaCurso pessoaCursoSelecionada = new PessoaCurso();	

	private List<Pessoa> listaPessoa = new ArrayList<Pessoa>();	
	private List<Pessoa> listaPessoaFiltradas ;
	private List<PessoaCurso> listaPessoaCurso = new ArrayList<PessoaCurso>();	

	private boolean lgCodigoCurso = false;
	private boolean lgNomeCurso = false;

	private boolean lgCodigoPessoaCurso = false;
	private boolean lgNomePessoaCurso = false;

	//========================================================= METODOS ==================================================================================//

	@PostConstruct
	public void init() {

		pessoa = new Pessoa();
		listaPessoa = (List<Pessoa>) pessoaDAO.recuperarTodos();
		
	}

	public void onRowEdit(RowEditEvent event) {

		pessoa = (Pessoa) event.getObject();
		pessoa.setNome(pessoa.getNome().toUpperCase());
		pessoa.setCpf(pessoa.getCpf().toUpperCase());
		FacesMessage msg = new FacesMessage("Pessoa Editado!",pessoa.getNome() );	

		FacesContext.getCurrentInstance().addMessage(null, msg);
		pessoaDAO.editar(pessoa);
	}

	public void onRowCancel(RowEditEvent event) {

		FacesMessage msg = new FacesMessage("Edição Cancelada!", ((Pessoa) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void incluirPessoa(){

		if (pessoa.getCpf() == null || pessoa.getCpf().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Matricula!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (pessoa.getNome() == null || pessoa.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (curso.getNome() == null || curso.getNome().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Nome Curso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (curso.getCodigo() == null || curso.getCodigo().equals("")){
			FacesMessage msg = new FacesMessage("Preencha o campo Código Curso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (pessoa.getCpf() != null && pessoa.getNome() != null && curso.getCodigo() != null && curso.getNome() != null  ){	

			pessoa.setNome(pessoa.getNome().toUpperCase());
			pessoa.setCpf(pessoa.getCpf().toUpperCase());		

			Pessoa pessoaAuxiliar = new Pessoa();

			pessoaAuxiliar = pessoaDAO.buscarPorCpfPessoa(pessoa.getCpf());
			if(pessoaAuxiliar == null){
				pessoaAuxiliar = pessoaDAO.buscarPorNomePessoa(pessoa.getNome());
			}

			if(pessoaAuxiliar == null){
				pessoaDAO.persistir(pessoa);

				listaPessoa.add(pessoa);

				PessoaCurso pessoaCurso = new PessoaCurso();
				pessoaCurso.setCurso(curso);
				pessoaCurso.setPessoa(pessoa);

				pessoaCursoDAO.persistir(pessoaCurso);

			}
			else {
				FacesMessage msg = new FacesMessage("Pessoa já cadastrada!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

			pessoa = new Pessoa();
			curso = new Curso();
			lgCodigoCurso = false;
			lgNomeCurso = false;

		}

	}


	public void incluiPessoaCurso(){

		PessoaCurso pessoaCurso = new PessoaCurso();
		pessoaCurso.setCurso(curso);
		pessoaCurso.setPessoa(pessoaSelecionada);

		if (curso == null ){
			FacesMessage msg = new FacesMessage("Curso Inválido!");
			FacesContext.getCurrentInstance().addMessage(null, msg);	
			return;
		}
		pessoaCursoDAO.persistir(pessoaCurso);

		listaPessoaCurso.add(pessoaCurso);

		curso = new Curso();

		lgCodigoPessoaCurso = false;
		lgNomePessoaCurso = false;
	}


	public void limpaPessoa(){

		pessoa = new Pessoa();
		curso = new Curso();

		lgCodigoCurso = false;
		lgNomeCurso = false;

	}


	public void limpaPessoaCurso(){

		curso = new Curso();

		lgCodigoPessoaCurso = false;
		lgNomePessoaCurso = false;

	}


	public void deletarPessoa(){

		pessoaDAO.removePeloId(pessoaSelecionada.getId());
		listaPessoa.clear();
		List<Pessoa> listaPessoaAuxiliar = new ArrayList<Pessoa>();	
		listaPessoaAuxiliar = (List<Pessoa>) pessoaDAO.recuperarTodos();
		listaPessoa.clear();

		for(Pessoa p:listaPessoaAuxiliar){

			listaPessoa.add(p);

		}		
	}

	public List<String> cursoCodigos(String codigo) {		

		codigo = codigo.toUpperCase();

		List<String> todos = cursoDAO.buscarTodosCodigosCurso(codigo);

		return todos;	

	}

	public List<Curso> cursoNomes(String codigo) {		

		codigo = codigo.toUpperCase();

		List<Curso> todos = cursoDAO.buscarTodosNomesObjetoCurso(codigo);

		return todos;	

	}


	public void onItemSelectCodigoCurso() {

		curso = cursoDAO.buscarPorCodigo(curso.getCodigo());

		lgCodigoCurso = true;
		lgNomeCurso = true;

	}

	public void onItemSelectNomeCurso() {

		lgCodigoCurso = true;
		lgNomeCurso = true;

	}

	public void onItemSelectCodigoPessoaCurso() {

		curso = cursoDAO.buscarPorCodigo(curso.getCodigo());
		
		lgCodigoPessoaCurso = true;
		lgNomePessoaCurso = true;

	}

	public void onItemSelectNomePessoaCurso() {

		curso = cursoDAO.buscarPorNome(curso.getNome());

		lgCodigoPessoaCurso = true;
		lgNomePessoaCurso = true;

	}

	public void carregaPessoaCurso(){

		listaPessoaCurso = new ArrayList<PessoaCurso>();

		ArrayList<PessoaCurso> todos = pessoaCursoDAO.buscarTodasPessoaCursoPorPessoa(pessoaSelecionada.getId());
		while(!todos.isEmpty()){  

			listaPessoaCurso.add(todos.remove(0));
		}

	}

	public void deletarPessoaCurso(){

		pessoaCursoDAO.removePeloId(pessoaCursoSelecionada.getId());

		List<PessoaCurso> listaPessoaCursoAuxiliar = (List<PessoaCurso>) pessoaCursoDAO.buscarTodasPessoaCursoPorPessoa(pessoaSelecionada.getId());

		listaPessoaCurso.clear();

		for(PessoaCurso p:listaPessoaCursoAuxiliar){

			listaPessoaCurso.add(p);

		}
	}

	//========================================================= GET - SET ==================================================================================//


	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public List<Pessoa> getListaPessoaFiltradas() {
		return listaPessoaFiltradas;
	}

	public void setListaPessoaFiltradas(List<Pessoa> listaPessoaFiltradas) {
		this.listaPessoaFiltradas = listaPessoaFiltradas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Pessoa getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(Pessoa pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
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

	public boolean isLgCodigoCurso() {
		return lgCodigoCurso;
	}

	public void setLgCodigoCurso(boolean lgCodigoCurso) {
		this.lgCodigoCurso = lgCodigoCurso;
	}

	public boolean isLgNomeCurso() {
		return lgNomeCurso;
	}

	public void setLgNomeCurso(boolean lgNomeCurso) {
		this.lgNomeCurso = lgNomeCurso;
	}

	public PessoaCursoDAO getPessoaCursoDAO() {
		return pessoaCursoDAO;
	}

	public void setPessoaCursoDAO(PessoaCursoDAO pessoaCursoDAO) {
		this.pessoaCursoDAO = pessoaCursoDAO;
	}

	public boolean isLgCodigoPessoaCurso() {
		return lgCodigoPessoaCurso;
	}

	public void setLgCodigoPessoaCurso(boolean lgCodigoPessoaCurso) {
		this.lgCodigoPessoaCurso = lgCodigoPessoaCurso;
	}

	public boolean isLgNomePessoaCurso() {
		return lgNomePessoaCurso;
	}

	public void setLgNomePessoaCurso(boolean lgNomePessoaCurso) {
		this.lgNomePessoaCurso = lgNomePessoaCurso;
	}

	public List<PessoaCurso> getListaPessoaCurso() {
		return listaPessoaCurso;
	}

	public void setListaPessoaCurso(List<PessoaCurso> listaPessoaCurso) {
		this.listaPessoaCurso = listaPessoaCurso;
	}

	public PessoaCurso getPessoaCursoSelecionada() {
		return pessoaCursoSelecionada;
	}

	public void setPessoaCursoSelecionada(PessoaCurso pessoaCursoSelecionada) {
		this.pessoaCursoSelecionada = pessoaCursoSelecionada;
	}
}

