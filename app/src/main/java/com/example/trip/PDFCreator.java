package com.example.trip;

import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PDFCreator {

    public static void createPdf(String content, String fileName) {
        // Create a new Document
        Document document = new Document();

        try {
            // Set the path and filename of the output PDF file
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName +".pdf";

            // Create a new PdfWriter instance
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the Document for writing
            document.open();

            // Add content to the Document
            document.add(new Paragraph(content));

            // Close the Document
            document.close();


        } catch (Exception e) {
            // Handle any exceptions that occur
            e.printStackTrace();
        }
    }
}
