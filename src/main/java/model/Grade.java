package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;



@Entity
@SequenceGenerator(name="grade_sequencia", sequenceName="grade_seq", allocationSize=1)
public class Grade {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private String codigo;
	private List<Aluno> grupoAlunos = new ArrayList<Aluno>();
	private List<Equivalencia> grupoEquivalencia = new ArrayList<Equivalencia>();
	private List<GradeDisciplina> grupoGradeDisciplina = new ArrayList<GradeDisciplina>();
	private Curso curso;
	private Long id;
	private int horasAce;
	private int horasOpcionais;
	private int horasEletivas;
	private int numeroMaximoPeriodos;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="grade_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column (name="CODIGO" , nullable = false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
	
	

	
	
	
	
	
	

	@OneToMany(mappedBy = "grade", targetEntity = Aluno.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Aluno> getGrupoAlunos() {
		return grupoAlunos;
	}

	@Column (name="HORASACE" )
	public int getHorasAce() {
		return horasAce;
	}

	public void setHorasAce(int horasAce) {
		this.horasAce = horasAce;
	}

	@Column (name="HORASOPCIONAIS" )
	public int getHorasOpcionais() {
		return horasOpcionais;
	}

	public void setHorasOpcionais(int horasOpcionais) {
		this.horasOpcionais = horasOpcionais;
	}

	@Column (name="HORASELETIVAS" )
	public int getHorasEletivas() {
		return horasEletivas;
	}

	public void setHorasEletivas(int horasEletivas) {
		this.horasEletivas = horasEletivas;
	}

	public int getNumeroMaximoPeriodos() {
		return numeroMaximoPeriodos;
	}

	public void setNumeroMaximoPeriodos(int numeroMaximoPeriodos) {
		this.numeroMaximoPeriodos = numeroMaximoPeriodos;
	}

	public void setGrupoAlunos(List<Aluno> grupoAlunos) {
		this.grupoAlunos = grupoAlunos;
	}

	@ManyToOne
	@JoinColumn(name="ID_CURSO" , referencedColumnName="ID")
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@OneToMany(mappedBy = "grade", targetEntity = Equivalencia.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Equivalencia> getGrupoEquivalencia() {
		return grupoEquivalencia;
	}

	public void setGrupoEquivalencia(List<Equivalencia> grupoEquivalencia) {
		this.grupoEquivalencia = grupoEquivalencia;
	}

	@OneToMany(mappedBy = "grade", targetEntity = GradeDisciplina.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<GradeDisciplina> getGrupoGradeDisciplina() {
		return grupoGradeDisciplina;
	}

	public void setGrupoGradeDisciplina(List<GradeDisciplina> grupoGradeDisciplina) {
		this.grupoGradeDisciplina = grupoGradeDisciplina;
	}

	
}