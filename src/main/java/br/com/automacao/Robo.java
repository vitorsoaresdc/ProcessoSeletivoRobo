package br.com.automacao;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static br.com.automacao.Navegador.AcessaNavegador;
import static br.com.automacao.Log.logs;

public class Robo {
    public static void main(String[] args) throws IOException {

        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        //AcessaNavegador();

        // Acessando o PDF
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        File[] pdfFiles = downloadsPath.toFile().listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null || pdfFiles.length == 0) {
            logs("PDF não encontrado!");
            System.out.println("Nenhum arquivo PDF encontrado no diretório de downloads.");
            return;
        }
        Arrays.sort(pdfFiles, Comparator.comparingLong(File::lastModified).reversed());
        File ultimoDownloadFeito = pdfFiles[0];

        // pega o numero da pagina e titulo
        try (PDDocument document = PDDocument.load(ultimoDownloadFeito)) {
            logs("Pegando titulo e número da pagina");
            PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
            System.out.println(outline);

            if (outline != null) {
                logs("Criando planilha Excel");
                // Cria um arquivo Excel e uma planilha
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Planilha");

                // Chama o método para preencher a lista de títulos
                List<String> titles = new ArrayList<>();

                processOutlineItem(outline.getFirstChild(), document, 0, sheet, titles);

                logs("Criando linhas e células da planilha");
                // Cria uma nova linha
                Row row = sheet.createRow(0);

                for (int i = 0; i < titles.size(); i++) {
                    String title = titles.get(i);

                    Cell cell1 = row.createCell(0);
                    cell1.setCellValue(title);
                    Cell cell2 = row.createCell(1);
                    cell2.setCellValue("pagina");
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(data + " " + hora);

                    // Incrementa o índice da linha
                    row = sheet.createRow(i + 1);
                }

                logs("Criando arquivo de saída planilha.xlsx");
                String filePath = System.getProperty("user.home") + "/Downloads/planilha.xlsx";
                FileOutputStream fileOut = new FileOutputStream(filePath);
                workbook.write(fileOut);
                fileOut.close();
            }
        }
    }
    private static void processOutlineItem(PDOutlineItem item, PDDocument document, int level, Sheet sheet, List<String> titles) throws IOException {
        if (item == null) {
            return;
        }
        int pageNumber = document.getPages().indexOf(item.findDestinationPage(document)) + 1;
        String title = item.getTitle();

        System.out.print(pageNumber+" ");
        System.out.println(title);

        titles.add(title);

        processOutlineItem(item.getFirstChild(), document, level + 1, sheet, titles);
        processOutlineItem(item.getNextSibling(), document, level, sheet, titles);
    }

}

