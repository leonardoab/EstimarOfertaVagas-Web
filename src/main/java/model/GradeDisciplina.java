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
@SequenceGenerator(name="grade_disciplina_sequencia", sequenceName="grade_disciplina_seq", allocationSize=1)
public class GradeDisciplina {
	// ==========================VARIÃ�VEIS=================================================================================================================//

	private String tipoDisciplina;
	private Long periodo;
	private Disciplina disciplina;
	private Grade grade;
	private List<PreRequisito> preRequisito = new ArrayList<PreRequisito>();
	
	private Long id;

	// ==========================GETTERS_AND_SETTERS======================================================================================================//


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="grade_disciplina_sequencia")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="TIPO_DISCIPLINA")
	public String getTipoDisciplina() {
		return tipoDisciplina;
	}

	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
	}
	@Column(name="PERIODO")
	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	@ManyToOne
	@JoinColumn(name="ID_DISCIPLINA" , referencedColumnName="ID",nullable = false)
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@ManyToOne
	@JoinColumn(name="ID_GRADE" , referencedColumnName="ID",nullable = false)
	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@OneToMany(mappedBy = "gradeDisciplina", targetEntity = PreRequisito.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<PreRequisito> getPreRequisito() {
		return preRequisito;
	}

	public void setPreRequisito(List<PreRequisito> preRequisito) {
		this.preRequisito = preRequisito;
	}
}