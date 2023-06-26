package com.serethewind.NeoClantech.bankStatement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;

@Slf4j
public class BankStatement {
    /**
     * created a file that will receive generated text.
     * outputStream will allow us move generated text to the pdf file
     */

    private static final String FILE = "C:\\Users\\noah.johnson\\Documents\\Statement\\Sample.pdf";

    public static void generateStatement() throws FileNotFoundException, DocumentException {
        Rectangle rectangle = new Rectangle(PageSize.A4);

        Document document = new Document(rectangle);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("Sample text"));
        document.add(new Chunk("TJA Bank Statement"));
        document.add(new Phrase("I am enjoying this"));
        document.add(new Paragraph(" "));
        Anchor anchor = new Anchor("IAcademy website");
        anchor.setReference("https://www.goal.com");
        document.add(anchor);

        document.add(new Paragraph(" "));

        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Emperor"));
        orderedList.add(new ListItem("Noah"));

        List unOrderedList = new List(List.UNORDERED);
        unOrderedList.add(new ListItem("Testing"));
        unOrderedList.add(new ListItem("Testing2"));

        log.info("populating list");

        String[] firstNamesArray = {"Adeolu", "Oyin", "Noah"};
        String[] lastNamesArray = {"Oduniyi", "Alasoluyi", "Johnson"};

        log.info("setting table to 3 columns");
        PdfPTable table = new PdfPTable(3);
        PdfPCell serialNo = new PdfPCell(new Paragraph("S/N"));
        PdfPCell firstname = new PdfPCell(new Paragraph("Firstname"));
        PdfPCell lastname = new PdfPCell(new Paragraph("Lastname"));

        table.addCell(serialNo);
        table.addCell(firstname);
        table.addCell(lastname);


        for (int i = 0; i<firstNamesArray.length; i++){
            PdfPCell serialNo1 = new PdfPCell(new Paragraph(String.valueOf(i+1)));
            PdfPCell firstName = new PdfPCell(new Paragraph(firstNamesArray[i]));
            PdfPCell lastName = new PdfPCell(new Paragraph(lastNamesArray[i]));

            table.addCell(serialNo1).setBackgroundColor(BaseColor.BLUE);
            table.addCell(firstName);
            table.addCell(lastName);

        }

        document.add(table);
        document.add(orderedList);
        document.add(unOrderedList);
        document.close();

        log.info("File has been generated");
    }

    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        generateStatement();
    }
}
