package project.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

public class PdfConverter {
    private final File file;
    private StringReader htmlString;

    //setter method
    public void setHtmlString(StringReader _htmlString) {
        this.htmlString = _htmlString;
    }

    public boolean Save() {
        Document document = new Document();

        try {
            // Create a PdfWriter instance
            PdfWriter.getInstance(document, new FileOutputStream(this.file));

            document.open();

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(this.htmlString);

            document.close();

            System.out.println("PDF created and stored temporarily at: " + this.file.getAbsolutePath());
            return true;

        } catch (DocumentException | IOException e) {
            System.out.println("PDF Convert Error: " + e.getMessage());
            return false;
        }
        
    }
    
    public PdfConverter(File _file, StringReader _htmlString) {
        this.file = _file;
        this.htmlString = _htmlString;
    }
}
