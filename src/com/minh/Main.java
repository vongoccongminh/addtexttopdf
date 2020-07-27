package com.minh;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int fontSizeName = 14;

    public static final int fontSizeDistance = 16;

    public static void main(String[] args) throws IOException {

        List<Data> dataList = new ArrayList<>();

        dataList.add(new Data("Lê Bảo Toàn", "100km"));
        dataList.add(new Data("Lê Bảo Toàn", "42km"));
//        dataList.add(new Data("Usain Bolt", "100m"));

        dataList.forEach(data -> {
            try {
                //Loading an existing document
                PDDocument doc = PDDocument.load(new File("e-certificate_RMS.pdf"));

                //Creating a PDF Document
                PDPage page = doc.getPage(0);

                String dir = "liberation-sans/LiberationSans-Bold.ttf";
                PDType0Font font = PDType0Font.load(doc, new File(dir));

                String dirArial = "arial.ttf";

                PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
                PDAcroForm acroForm = documentCatalog.getAcroForm();
                List<PDField> fields = acroForm.getFields();

                PDFont formFont = PDType0Font.load(doc, new FileInputStream(dirArial), false); // check that the font has what you need; ARIALUNI.TTF is good but huge
                PDResources res = acroForm.getDefaultResources(); // could be null, if so, then create it with the setter
                String fontName = res.add(formFont).getName();
                String defaultAppearanceString = "/" + fontName + " 0 Tf 0 g"; // adjust to replace existing font name

                for (PDField field : fields) {
                    PDTextField textField = (PDTextField) field;
                    textField.setDefaultAppearance(defaultAppearanceString);

                    textField.setValue(data.getName());
                    textField.setReadOnly(true);

//                    insert(field, doc, page, "fullName",  font, data.getName());
//                    insert(field, doc, page, "distance",  font, data.getDistance());
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
