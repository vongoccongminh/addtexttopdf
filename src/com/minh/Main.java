package com.minh;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int fontSizeName = 36;

    public static final int fontSizeDistance = 16;

    public static void main(String[] args) throws IOException {

        List<Data> dataList = new ArrayList<>();

        dataList.add(new Data("Nguyễn Văn A", "25km"));
        dataList.add(new Data("TRƯƠNG TRẦN NGÔ", "30km"));
        dataList.add(new Data("Usain Bolt", "100m"));
        dataList.add(new Data("Đỗ Nguyễn Lê Phùng Võ Phạm Bùi", "5km"));

        dataList.forEach(data -> {
            try {
                //Loading an existing document
                PDDocument doc = PDDocument.load(new File("e-certificate_RMS.pdf"));

                //Creating a PDF Document
                PDPage page = doc.getPage(0);

                String dir = "liberation-sans/LiberationSans-Bold.ttf";
                PDType0Font font = PDType0Font.load(doc, new File(dir));

                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true);

                //Begin the Content stream
                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.RED);
                //Setting the font to the Content stream
                contentStream.setFont(font, fontSizeName);
                //Get text name width
                float textNameWidth = font.getStringWidth(data.getName()) / 1000 * fontSizeName;
                //Setting the position for the line
                contentStream.newLineAtOffset(350 - textNameWidth / 2, 210);
                //Adding text in the form of string
                contentStream.showText(data.getName());
                //Ending the content stream
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(font, fontSizeDistance);
                contentStream.setNonStrokingColor(Color.BLUE);
                contentStream.newLineAtOffset(392, 157);
                contentStream.showText(data.getDistance());
                contentStream.endText();

                System.out.println("Content added");

                //Closing the content stream
                contentStream.close();

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
}
