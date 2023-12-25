package com.mindhub.homeBanking.models;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PDFExporter {

    private List<Transaction> transactionList;
    private String number;
    private String name;
    private String lastName;

    public PDFExporter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.number = transactionList.get(0).getAccount().getNumber();
        this.name = transactionList.get(0).getAccount().getClient().getName();
        this.lastName = transactionList.get(0).getAccount().getClient().getLastName();
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell= new PdfPCell();
        cell.setBackgroundColor(Color.darkGray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("DATE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("DESCRIPTION", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TYPE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("AMOUNT", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("BALANCE", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        Font fontData= FontFactory.getFont(FontFactory.COURIER);
        for(Transaction transaction: transactionList){
            table.addCell(new PdfPCell(new Phrase(transaction.getDate().toLocalDate().toString(), fontData)));
            table.addCell(new PdfPCell(new Phrase(transaction.getDescription(), fontData)));
            table.addCell(new PdfPCell(new Phrase(transaction.getType().toString(), fontData)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(transaction.getAmount()))));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(transaction.getCurrentBalance()))));
        }
    }
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        com.lowagie.text.Image logo = com.lowagie.text.Image.getInstance("https://i.ibb.co/WpMCJGB/logo.png");
        logo.setAbsolutePosition(40, 777);
        logo.scalePercent(30);

        document.add(logo);

        font.setSize(30);
        font.setColor(Color.black);

        Paragraph titulo = new Paragraph("Horizon Bank", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreDocumento = new Paragraph("Statement of Account", font);
        nombreDocumento.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreCliente = new Paragraph("Client: " + lastName + " " + name);
        nombreCliente.setSpacingBefore(25);
        Paragraph numeroCuenta = new Paragraph("Account number: " + number);
        LocalDate localDate = LocalDate.now();
        Paragraph fecha = new Paragraph("Date: " + localDate);

        document.add(titulo);
        document.add(nombreDocumento);
        document.add(nombreCliente);
        document.add(numeroCuenta);
        document.add(fecha);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 5.0f, 1.25f, 1.5f, 1.5f});
        table.setSpacingBefore(25);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }
}
