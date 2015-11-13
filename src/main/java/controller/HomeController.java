package controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotAuthorizedException;

import model.Aluno;
import model.Curso;
import model.Pessoa;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao;
import br.ufjf.ice.integra3.rs.restclient.RSCursoAlunosDiscSituacao.ServiceVersion;
import br.ufjf.ice.integra3.rs.restclient.model.v2.AlunoCurso;
import br.ufjf.ice.integra3.rs.restclient.model.v2.CursoAlunosSituacaoResponse;
import br.ufjf.ice.integra3.ws.login.interfaces.IWsLogin;
import br.ufjf.ice.integra3.ws.login.interfaces.WsException_Exception;
import br.ufjf.ice.integra3.ws.login.interfaces.WsLoginResponse;
import br.ufjf.ice.integra3.ws.login.interfaces.WsUserInfoResponse;
import br.ufjf.ice.integra3.ws.login.service.WSLogin;
import dao.CursoDAOImpl;
import dao.PessoaDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.HistoricoDAO;
import dao.Interface.PessoaDAO;

@Named
@ViewScoped
public class HomeController implements Serializable{

	private Curso curso = new Curso();
	@Inject
	private UsuarioController usuarioController;

	private static final long serialVersionUID = 1L;
	private String login;
	private String senha;
	private String codigo;
	private String token;

	@PostConstruct
	public void init() {

		CursoDAO cursoDAO = new CursoDAOImpl();
		long idCursoPessoa;
		idCursoPessoa = usuarioController.getPessoa().getIdCurso();
		curso = cursoDAO.recuperarPorId(idCursoPessoa);

	}


	public void chamarTudo() throws IOException {

		String pwd = md5(senha);
		//nao entendi como pegar o TOKEN

		/*try {
			System.out.println("Logando...");
			IWsLogin integra = new WSLogin().getWsLoginServicePort();
			WsLoginResponse user = integra.login(login, pwd, token);

			WsUserInfoResponse infos = integra.getUserInformation(user.getToken()); 

			PessoaDAO pessoaDAO = PessoaDAOImpl.getInstance();

			Pessoa pessoa =  new Pessoa();

			pessoa = pessoaDAO.buscarPorCpfPessoa(infos.getCpf());

			if (pessoa == null){

				int contador = 0;

				for (contador = 0 ; contador < infos.getProfileList().getProfile().size() ; contador ++ )

					if  (infos.getProfileList().getProfile().get(contador).getTipo().equals("cordenador")){

						pessoa =  new Pessoa();
						pessoa.setCpf(infos.getCpf());
						pessoa.setNome(infos.getNome());
						pessoaDAO.persistir(pessoa);
						break;
					}

			}


			System.out.println("Recuperando dados do curso "+codigo+"...");
			RSCursoAlunosDiscSituacao rsClient = new RSCursoAlunosDiscSituacao(user.getToken(), ServiceVersion.V2);



			CursoAlunosSituacaoResponse rsResponse = (CursoAlunosSituacaoResponse) rsClient.get(codigo);
			if (rsResponse.getResponseStatus() != null) 
				throw new Exception (rsResponse.getResponseStatus());

			EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();

			CursoDAO cursoDAO = new CursoDAOImpl();	
			AlunoDAO alunoDAO = estruturaArvore.getAlunoDAO();
			DisciplinaDAO disciplinaDAO = estruturaArvore.getDisciplinaDAO();
			GradeDAO gradeDAO = estruturaArvore.getGradeDAO();		
			HistoricoDAO historicoDAO = estruturaArvore.getHistoricoDAO();

			IntergraControler intergraControler = new IntergraControler();


			codigo = codigo.toUpperCase();

			Curso curso = cursoDAO.buscarPorCodigo(codigo);

			if (curso == null){

				System.out.println("Curso Invalido");
				return;
			}
			else {

				List<Aluno> listaAlunos = alunoDAO.buscarTodosAlunoCursoObjeto(curso.getId());

				for (Aluno aluno:listaAlunos){

					alunoDAO.removePeloId(aluno.getId());

				}

			}

			//deleta historico de todos alunos e os alunos do curso

			for(AlunoCurso alunoCurso : rsResponse.getAluno()) {

				intergraControler.importadorDados(alunoCurso,cursoDAO,alunoDAO,disciplinaDAO,historicoDAO,gradeDAO);

			}

			Date d ;
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

			d = GregorianCalendar.getInstance().getTime();  

			curso.setDataAtualizacao(format.format(d).toString());
			cursoDAO.persistir(curso);

			System.out.println("Finalizado");

		} catch (NotAuthorizedException e) {
			System.out.println("Token inv√°lido");
			e.printStackTrace();			
		} catch (WsException_Exception e) {
			//ImpressÔøΩo de erros
			System.out.println(e.getMessage());
			System.out.println(e.getFaultInfo().getErrorUserMessage());
		} catch (Exception e) {
			//ImpressÔøΩo de erros
			System.out.println("ERRO: usuÔøΩrio sem permissÔøΩo");
			e.printStackTrace();
		}*/



	}

	public static String md5(String input) {

		String md5 = null;

		if(null == input) return null;

		try {
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
