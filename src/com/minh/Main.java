package com.minh;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static final int fontSize = 16;

    public static void main(String[] args) throws IOException {

        List<FileData> dataset = getDataSet();

        // Loading an existing document
        File templateFile = new File("e-certificate_RMS.pdf");
        String outputDir = "./generated";
        final Path outputPath = Files.createDirectories(Paths.get(outputDir));
        dataset.forEach(data -> {
            try {
                generatePDFile(templateFile, data.getTextOptions(), "e-certificate_RMS", outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void generatePDFile(File templateFile, List<TextOption> dataFieldData, String fileNamePrefix, Path outputPath) throws IOException {
        PDDocument doc = PDDocument.load(templateFile);

        PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
        PDAcroForm acroForm = documentCatalog.getAcroForm();
        List<PDField> pdfFields = acroForm.getFields();
        final Map<String, PDField> fieldNameToPDField = pdfFields.stream().collect(Collectors.toMap(PDField::getFullyQualifiedName, f -> f));
        final Map<PDField, TextOption> pdFieldToDataField = dataFieldData.stream().collect(Collectors.toMap(f -> fieldNameToPDField.get(f.getFieldName()), f -> f));

        final String fileNameSuffix = dataFieldData.stream().map(TextOption::getText).collect(Collectors.joining("_"));

        String dir = "fonts/arial.ttf";
        PDType0Font font = PDType0Font.load(doc, new File(dir));

        pdFieldToDataField.forEach((pdField, textOption) -> {
            try {
                addText(pdField, doc, doc.getPage(0), textOption.getFieldName(), font, textOption.getText(), textOption.getTextColor(), textOption.getTextAlign());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        acroForm.flatten();
        doc.save(new File(outputPath.toAbsolutePath() + "/" + String.join("_", fileNamePrefix, fileNameSuffix) + ".pdf"));
        // Closing the document
        doc.close();
    }

    static void addText(PDField field, PDDocument doc, PDPage page, String fieldName, PDType0Font font, String text, Color textColor, String textAlign) throws IOException {
        if (!field.getFullyQualifiedName().equals(fieldName)) {
            return;
        }
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true);

        field.setValue(null);
        field.setReadOnly(true);

        // Begin the Content stream
        contentStream.beginText();
        contentStream.setNonStrokingColor(textColor);
        // Setting the font to the Content stream
        contentStream.setFont(font, fontSize);
        // Get text name width
        float textNameWidth = font.getStringWidth(text) / 1000 * fontSize;

        PDRectangle rectangle = field.getWidgets().get(0).getRectangle();
        float tx = (rectangle.getLowerLeftX() + rectangle.getUpperRightX()) / 2 - textNameWidth / 2;
        float ty = rectangle.getLowerLeftY();
        if (textAlign.equals("left")) {
            tx = rectangle.getLowerLeftX();
            ty = rectangle.getLowerLeftY();
        } else if (textAlign.equals("right")){
            tx = rectangle.getUpperRightX() - textNameWidth;
            ty = rectangle.getLowerLeftY();
        }
        // Setting the position for the line
        contentStream.newLineAtOffset(tx, ty);
        // Adding text in the form of string
        contentStream.showText(text);
        // Ending the content stream
        contentStream.endText();
        // Closing the content stream
        contentStream.close();
    }

    private static List<FileData> getDataSet() {
        Color fullNameColor = new Color(254, 88, 0);
        Color distanceColor = Color.BLACK;

        TextOption user1FullName = new TextOption("fullName", "Lê Bảo Toàn", fullNameColor, "center");
        TextOption user1Distance = new TextOption("distance", "42km", distanceColor, "center");
        TextOption user2FullName = new TextOption("fullName", "Lê Bảo Toàn", fullNameColor, "left");
        TextOption user2Distance = new TextOption("distance", "100km", distanceColor, "center");
        TextOption user3FullName = new TextOption("fullName", "Lê Bảo Toàn", fullNameColor, "right");
        TextOption user3Distance = new TextOption("distance", "45km", distanceColor, "center");

        final FileData user1FileData = new FileData(Arrays.asList(user1FullName, user1Distance));
        final FileData user2FileData = new FileData(Arrays.asList(user2FullName, user2Distance));
        final FileData user3FileData = new FileData(Arrays.asList(user3FullName, user3Distance));

        return Arrays.asList(user1FileData, user2FileData, user3FileData);
    }
}
