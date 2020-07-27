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
                generatePDFile(templateFile, data.getFieldData(), "e-certificate_RMS", outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void generatePDFile(File templateFile, List<FieldData> dataFieldData, String fileNamePrefix, Path outputPath) throws IOException {
        PDDocument doc = PDDocument.load(templateFile);

        PDDocumentCatalog documentCatalog = doc.getDocumentCatalog();
        PDAcroForm acroForm = documentCatalog.getAcroForm();
        List<PDField> pdfFields = acroForm.getFields();
        final Map<String, PDField> fieldNameToPDField = pdfFields.stream().collect(Collectors.toMap(PDField::getFullyQualifiedName, f -> f));
        final Map<PDField, FieldData> pdFieldToDataField = dataFieldData.stream().collect(Collectors.toMap(f -> fieldNameToPDField.get(f.getName()), f -> f));

        final String fileNameSuffix = dataFieldData.stream().map(FieldData::getText).collect(Collectors.joining("_"));

        String dir = "fonts/arial.ttf";
        PDType0Font font = PDType0Font.load(doc, new File(dir));

        pdFieldToDataField.forEach((pdField, fieldData) -> {
            try {
                addText(pdField, doc, doc.getPage(0), fieldData.getName(), font, fieldData.getText(), fieldData.getTextColor());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        doc.save(new File(outputPath.toAbsolutePath() + "/" + String.join("_", fileNamePrefix, fileNameSuffix) + ".pdf"));
        // Closing the document
        doc.close();
    }

    static void addText(PDField field, PDDocument doc, PDPage page, String fieldName, PDType0Font font, String text, Color textColor) throws IOException {
        if (field.getPartialName().equals(fieldName)) {
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true);

            PDRectangle rectangle = field.getWidgets().get(0).getRectangle();

            //Begin the Content stream
            contentStream.beginText();
            contentStream.setNonStrokingColor(textColor);
            //Setting the font to the Content stream
            contentStream.setFont(font, fontSize);
            //Setting the position for the line
            contentStream.newLineAtOffset(rectangle.getLowerLeftX(), rectangle.getLowerLeftY());
            //Adding text in the form of string
            contentStream.showText(text);
            //Ending the content stream
            contentStream.endText();
            //Closing the content stream
            contentStream.close();
        }
    }

    private static List<FileData> getDataSet() {
        Color fullNameColor = new Color(254, 88, 0);
        Color distanceColor = Color.BLACK;

        FieldData user1FullName = new FieldData("fullName", "Lê Bảo Toàn", fullNameColor, "center");
        FieldData user1Distance = new FieldData("distance", "42km", distanceColor, "left");
        FieldData user2FullName = new FieldData("fullName", "Lê Bảo Toàn", fullNameColor, "center");
        FieldData user2Distance = new FieldData("distance", "100km", distanceColor, "left");

        final FileData user1FileData = new FileData(Arrays.asList(user1FullName, user1Distance));
        final FileData user2FileData = new FileData(Arrays.asList(user2FullName, user2Distance));

        return Arrays.asList(user1FileData, user2FileData);
    }
}
