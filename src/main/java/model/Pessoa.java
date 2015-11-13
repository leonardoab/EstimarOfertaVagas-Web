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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name="pessoa_sequencia", sequenceName="pessoa_seq", allocationSize=1) 
public class Pessoa {	
	
	private Long id;
	private String cpf;
	private String nome;
	private Long idCurso;
	private List<PessoaCurso> pessoaCurso = new ArrayList<PessoaCurso>();	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="pessoa_sequencia")  
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column (name="CPF", unique = true, nullable = false)
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}	
	
	@Column (name="NOME", unique = true, nullable = false)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	@OneToMany(mappedBy = "pessoa", targetEntity = PessoaCurso.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<PessoaCurso> getPessoaCurso() {
		return pessoaCurso;
	}
	
	public void setPessoaCurso(List<PessoaCurso> pessoaCurso) {
		this.pessoaCurso = pessoaCurso;
	}
	
	public Long getIdCurso() {
		return idCurso;
	}
	
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
}