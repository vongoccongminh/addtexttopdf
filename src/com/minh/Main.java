package com.minh;

import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Data> dataList = new ArrayList<>();

        dataList.add(new Data("Nguyễn Văn A", "25 km"));
        dataList.add(new Data("TRƯƠNG TRẦN NGÔ", "30 km"));
        dataList.add(new Data("Usain Bolt", "100 m"));

        dataList.forEach(data -> {
            try {
                //Loading an existing document
                PDDocument doc = PDDocument.load(new File("e-certificate_RMS.pdf"));

                //Creating a PDF Document
                PDPage page = doc.getPage(0);

//                System.out.println("height : " + page.getMediaBox().getHeight());

//                System.out.println("with : " + page.getMediaBox().getWidth());


                String dir = "liberation-sans/LiberationSans-Bold.ttf";
                PDType0Font font = PDType0Font.load(doc, new File(dir));

                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false, true);

                contentStream.setNonStrokingColor(Color.RED);

                //Begin the Content stream
                contentStream.beginText();

                //Setting the font to the Content stream
                contentStream.setFont(font, 36);

                //Setting the position for the line
                contentStream.newLineAtOffset(270, 210);

                //Adding text in the form of string
                contentStream.showText(data.getName());

//                contentStream.newLine();
//                contentStream.setFont(font, 36);
//                contentStream.setNonStrokingColor(Color.BLUE);
//                contentStream.newLineAtOffset(0, 0);
//                contentStream.showText(data.getDistance());

                //Ending the content stream
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
