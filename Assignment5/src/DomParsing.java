import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaac on 7/17/2015.
 */
public class DomParsing {

    public static void main(String[] args) {
        DomParsing test = new DomParsing();

        System.out.println(test.parseMovieTitles().toString());
    }

    public List<String> parseMovieTitles() {

        List<String> movieTitles = new ArrayList<>();

        try {
            File xmlDocument = Paths.get("src/Theater.xml").toFile();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlDocument);

            NodeList movieTitlesNode = document.getElementsByTagName("movie");
            for (int i = 0; i < movieTitlesNode.getLength(); i++) {
                Element movieTitleNode = (Element) movieTitlesNode.item(i);
                movieTitles.add(movieTitleNode.getAttribute("name"));
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return movieTitles;
    }
}
