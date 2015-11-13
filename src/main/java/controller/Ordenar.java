package controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Curso;
import model.Disciplina;
import model.DisciplinaGradeDisciplina;
import model.EventoAce;


public class Ordenar {

	public class EventoAcePeriodo implements Comparator<EventoAce> {
		@Override
		public int compare(EventoAce o1, EventoAce o2) {
			return o1.getPeriodo().compareTo(o2.getPeriodo());
		}
	}

	public class CursoCodigo implements Comparator<Curso> {
		@Override
		public int compare(Curso o1, Curso o2) {
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
	}
	
	public class DisciplinaCodigo implements Comparator<Disciplina> {
	    @Override
	    public int compare(Disciplina o1, Disciplina o2) {
	        return o1.getCodigo().compareTo(o2.getCodigo());
	    }
	}
	
	public class DisciplinaGradeDisciplinaCodigo implements Comparator<DisciplinaGradeDisciplina> {
		@Override
		public int compare(DisciplinaGradeDisciplina o1, DisciplinaGradeDisciplina o2) {
			return o1.getDisciplina().getCodigo().compareTo(o2.getDisciplina().getCodigo());
		}
	}
	
	public class DisciplinaGradeDisciplinaPeriodo implements Comparator<DisciplinaGradeDisciplina> {
		@Override
		public int compare(DisciplinaGradeDisciplina o1, DisciplinaGradeDisciplina o2) {
			return o1.getGradeDisciplina().getPeriodo().compareTo(o2.getGradeDisciplina().getPeriodo());
		}
	}	

	public List<EventoAce> EventoAceOrdenarPeriodo(List<EventoAce> listaEventosAce){

		Collections.sort(listaEventosAce, new EventoAcePeriodo());

		return listaEventosAce;
	}

	public List<Curso> CursoOrdenarCodigo(List<Curso> listaCurso){

		Collections.sort(listaCurso, new CursoCodigo());

		return listaCurso;
	}
	
	public List<Disciplina> DisciplinaOrdenarCodigo(List<Disciplina> listaDisciplina){

		Collections.sort(listaDisciplina, new DisciplinaCodigo());

		return listaDisciplina;
	}
	
	public List<DisciplinaGradeDisciplina> DisciplinaGradeDisciplinaOrdenarCodigo(List<DisciplinaGradeDisciplina> listaDisciplinaGradeDisciplina){

		Collections.sort(listaDisciplinaGradeDisciplina, new DisciplinaGradeDisciplinaCodigo());

		return listaDisciplinaGradeDisciplina;
	}
	
	public List<DisciplinaGradeDisciplina> DisciplinaGradeDisciplinaOrdenarPeriodo(List<DisciplinaGradeDisciplina> listaDisciplinaGradeDisciplina){

		Collections.sort(listaDisciplinaGradeDisciplina, new DisciplinaGradeDisciplinaPeriodo());

		return listaDisciplinaGradeDisciplina;
	}
	

}
