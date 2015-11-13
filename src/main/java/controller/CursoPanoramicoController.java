package controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Aluno;
import model.Grade;
import model.Historico;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;

import dao.AlunoDAOImpl;

@Named
@ViewScoped
public class CursoPanoramicoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BubbleChartModel bubbleChart = new BubbleChartModel();
	private AlunoDAOImpl alunoDAO = new AlunoDAOImpl();
	private Grade grade;
	
	@PostConstruct
	public void init()
	{
		
		getBubbleChart().setAnimate(true);
		getBubbleChart().setZoom(true);
		
		//ArrayList<Aluno> alunos = alunoDAO.buscarTodosAlunoCursoGradeObjeto(grade.getCurso().getId(), grade.getId());
		
		//for(Aluno a : alunos)
		Aluno a = alunoDAO.buscarPorMatricula("2679133773");
		//{
			int horas = 0;
			long horasgrade = grade.getHorasAce() + grade.getHorasEletivas() + grade.getHorasOpcionais();
			for(Historico h : a.getGrupoHistorico())
			{
				horas += Integer.getInteger(h.getDisciplina().getCargaHoraria());
			}
			
			bubbleChart.add(new BubbleChartSeries(a.getMatricula(), 5 , horas, horas/(int)horasgrade));
			
		//}
			
	}
	
	//Intercepta clique em cada bola
	public void ItemSelect(ItemSelectEvent event)
	{
		System.out.println(event.getItemIndex() + " "+ event.getSeriesIndex());
	}

	public BubbleChartModel getBubbleChart() {
		return bubbleChart;
	}

	public void setBubbleChart(BubbleChartModel bubbleChart) {
		this.bubbleChart = bubbleChart;
	}
	
	
}
