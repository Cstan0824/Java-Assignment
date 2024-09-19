package project.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
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
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(this.file));


            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, this.htmlString);

            document.close();

            //System.out.println("PDF created and stored temporarily at: " + this.file.getAbsolutePath());
            return true;

        } catch (DocumentException | IOException e) {
            System.out.println("PDF Convert Error: " + e.getMessage());
            return false;
        }
        
    }
    
    public PdfConverter(File _file, PdfTemplate _pdfTemplate) {
        this.file = _file;
        this.htmlString = new StringReader(_pdfTemplate.getContent());
    }
}
