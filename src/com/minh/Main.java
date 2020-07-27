package com.minh;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int fontSizeName = 14;

    public static final int fontSizeDistance = 16;

    public static void main(String[] args) throws IOException {

        List<Data> dataList = new ArrayList<>();

        dataList.add(new Data("Nguyễn Văn A", "25km"));
        dataList.add(new Data("TRƯƠNG TRẦN NGÔ", "30km"));
        dataList.add(new Data("Usain Bolt", "100m"));

        dataList.forEach(data -> {
            try {
                //Loading an existing document
                PDDocument doc = PDDocument.load(new File("OoPdfFormExample.pdf"));

                //Creating a PDF Document
                PDPage page = doc.getPage(0);

                String dir = "liberation-sans/LiberationSans-Bold.ttf";
                PDType0Font font = PDType0Font.load(doc, new File(dir));

                PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
                PDAcroForm acroForm = documentCatalog.getAcroForm();
                List<PDField> fields = acroForm.getFields();

                for (PDField field : fields) {
                    insert(field, doc, page, "Given Name Text Box", font, data.getName());
                    insert(field, doc, page, "Family Name Text Box", font, data.getDistance());
                }

                //Saving the document
                doc.save(new File("AddText_OP_" + dataList.indexOf(data) + ".pdf"));

                //Closing the document
                doc.close();

            } catch (InvalidPasswordException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    static void insert(PDField field, PDDocument doc, PDPage page, String fieldName, PDType0Font font, String data) throws IOException {
        if (field.getPartialName().equals(fieldName)) {
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true);

            PDRectangle rectangle = field.getWidgets().get(0).getRectangle();

            //Begin the Content stream
            contentStream.beginText();
            contentStream.setNonStrokingColor(Color.RED);
            //Setting the font to the Content stream
            contentStream.setFont(font, fontSizeName);
            //Setting the position for the line
            contentStream.newLineAtOffset(rectangle.getLowerLeftX(), rectangle.getLowerLeftY());
            //Adding text in the form of string
            contentStream.showText(data);
            //Ending the content stream
            contentStream.endText();
            //Closing the content stream
            contentStream.close();
        }
    }
}
