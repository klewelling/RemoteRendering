import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Paths;

/**
 * Created by Isaac on 7/17/2015.
 */
public class XsltTransforming {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        transformInfo();
        System.out.println("File saved as Theater.html");
    }

    public static void transformInfo() throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter("Theater.html", "UTF-8");
        try {
            File xmlDocument = Paths.get("src/Theater.xml").toFile();
            File stylesheet = Paths.get("src/Theater.xsl").toFile();

            TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);

            Transformer transformer = factory.newTransformer(new StreamSource(stylesheet));
            transformer.transform(new StreamSource(xmlDocument), new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
