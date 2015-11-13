package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import model.integra.AlunoCurso;
import model.integra.AlunoDisciplina;
import model.integra.DisciplinaIntegra;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import dao.CursoDAOImpl;
import dao.Interface.AlunoDAO;
import dao.Interface.CursoDAO;
import dao.Interface.DisciplinaDAO;
import dao.Interface.GradeDAO;
import dao.Interface.HistoricoDAO;



@Named
@ViewScoped
public class ImportaExcel implements Serializable {

	private static final long serialVersionUID = 1L;
	private UploadedFile file;
	private String filename;
	private Path Arquivo;
	private int contImportacao = 0;
	private StreamedContent fileDowload;
	File txt = new File("c:\\Temp\\NaoAtulizados.txt");	

	public void carrega(FileUploadEvent event) throws IOException, BiffException, ParseException {
		Path folder = Paths.get("/Temp");
		filename = event.getFile().getFileName();
		Arquivo = Files.createTempFile(folder, filename, ".xls");
		file = event.getFile();
		try (InputStream input = file.getInputstream()) {
			Files.copy(input, Arquivo, StandardCopyOption.REPLACE_EXISTING);
			//message.messageSelecionada();
		}


		contImportacao++;


	}

	public void importar() throws BiffException, IOException, ParseException {
		
		Workbook workbook = Workbook.getWorkbook(new File(Arquivo.toUri()));

		Sheet sheet = workbook.getSheet(0);
		int linhas = sheet.getRows();

		IntergraControler intergraControler = new IntergraControler();

		
		
		EstruturaArvore estruturaArvore = EstruturaArvore.getInstance();

		CursoDAO cursoDAO = new CursoDAOImpl();	
		AlunoDAO alunoDAO = estruturaArvore.getAlunoDAO();
		DisciplinaDAO disciplinaDAO = estruturaArvore.getDisciplinaDAO();
		GradeDAO gradeDAO = estruturaArvore.getGradeDAO();		
		HistoricoDAO historicoDAO = estruturaArvore.getHistoricoDAO();
		
		historicoDAO.deletaTodos();

		for (int i = 0; i < linhas; i++) {


			AlunoCurso alunoCurso = new AlunoCurso();


			alunoCurso.setCurso(sheet.getCell(0, i).getContents().toString());
			alunoCurso.setMatricula(sheet.getCell(1, i).getContents().toString());
			alunoCurso.setNome(sheet.getCell(2, i).getContents().toString());			
			alunoCurso.setCurriculo(sheet.getCell(3, i).getContents().toString());


			AlunoDisciplina disciplinaList = new AlunoDisciplina() ;

			DisciplinaIntegra disciplinaIntegra = new DisciplinaIntegra() ;

			disciplinaIntegra.setAnoSemestre(sheet.getCell(4, i).getContents().toString());
			disciplinaIntegra.setDisciplina(sheet.getCell(5, i).getContents().toString());
			disciplinaIntegra.setHorasAula(sheet.getCell(8, i).getContents().toString());
			disciplinaIntegra.setNota(sheet.getCell(6, i).getContents().toString());
			disciplinaIntegra.setSituacao(sheet.getCell(7, i).getContents().toString());


			List<DisciplinaIntegra> listaDisciplinasIntegra = new ArrayList<DisciplinaIntegra>();

			listaDisciplinasIntegra.add(disciplinaIntegra);

			disciplinaList.setDisciplina(listaDisciplinasIntegra);

			alunoCurso.setDisciplinas(disciplinaList);
			
			
			

			//intergraControler.importadorDados(alunoCurso,cursoDAO,alunoDAO,disciplinaDAO,historicoDAO,gradeDAO);

			System.out.println(i);

		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Path getArquivo() {
		return Arquivo;
	}

	public void setArquivo(Path arquivo) {
		Arquivo = arquivo;
	}

	public int getContImportacao() {
		return contImportacao;
	}

	public void setContImportacao(int contImportacao) {
		this.contImportacao = contImportacao;
	}

	public StreamedContent getFileDowload() {
		return fileDowload;
	}

	public void setFileDowload(StreamedContent fileDowload) {
		this.fileDowload = fileDowload;
	}

	public File getTxt() {
		return txt;
	}

	public void setTxt(File txt) {
		this.txt = txt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}















}
