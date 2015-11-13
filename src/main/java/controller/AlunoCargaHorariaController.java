package controller;

import java.io.Serializable;
import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import dao.AlunoDAOImpl;
import model.Aluno;
import model.Disciplina;
//import model.Grade;
import model.GradeDisciplina;
import model.Historico;

import org.primefaces.model.chart.PieChartModel;

@Named
@ViewScoped
public class AlunoCargaHorariaController implements Serializable {

	private static final long serialVersionUID = 1L;
	private PieChartModel chart;
	//private Aluno aluno;
	
	@PostConstruct
	public void init()
	{
		chart = new PieChartModel();
		
		Aluno aluno = new AlunoDAOImpl().buscarPorMatricula("2679133773");
		
		
		HashMap<String, Disciplina> concluido = new HashMap<String, Disciplina>();
		
		
		for(Historico h: aluno.getGrupoHistorico())
		{
			if("Aprovado".equals(h.getStatusDisciplina()) || "Dispensado".equals(h.getStatusDisciplina()))
			{
				concluido.put(h.getDisciplina().getCodigo(), h.getDisciplina());
				System.out.println(h.getDisciplina().getCodigo());
			}
		}
		
		int horas_aoe = 0;
		int horas_e = 0;
		int horas_o = 0;

	for(GradeDisciplina d: aluno.getGrade().getGrupoGradeDisciplina())
	{
		if(concluido.containsKey(d.getDisciplina().getCodigo()))
		{
			System.out.println(d.getDisciplina().getCargaHoraria());
			
			if("Obrigatória".equals(d.getTipoDisciplina()))
				horas_aoe += Integer.parseInt(d.getDisciplina().getCargaHoraria());
			else if("Eletiva".equals(d.getTipoDisciplina()))
				horas_e += Integer.parseInt(d.getDisciplina().getCargaHoraria());
			else
				horas_o += Integer.parseInt(d.getDisciplina().getCargaHoraria());
		}
	}
		
		if(horas_e > aluno.getGrade().getHorasEletivas())
		{
			horas_o += horas_e - aluno.getGrade().getHorasEletivas();
			horas_e = aluno.getGrade().getHorasEletivas();
		}
		
		chart.set("AOE Completadas", horas_aoe);
		chart.set("AOE Restantes", aluno.getGrade().getHorasAce());
		chart.set("Eletivas Completadas", horas_e);
		chart.set("Eletivas Restantes", aluno.getGrade().getHorasEletivas() - horas_e);
		chart.set("Opcionais Completadas", horas_o);
		chart.set("Opcionais Restantes", aluno.getGrade().getHorasOpcionais() - horas_o);
		chart.setShowDataLabels(true);
		chart.setLegendPosition("se");
		chart.setSeriesColors("0000CC, 3399FF, 9900CC, CC66FF, 006600, 66FF99");
	}
	
	public PieChartModel getChart()
	{
		return chart;
	}

}
