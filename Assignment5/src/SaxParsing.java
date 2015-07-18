import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaac on 7/17/2015.
 */
public class SaxParsing extends DefaultHandler {

    private List<String> movieTitles = new ArrayList<>();
    private String movieTitle;

    public static void main(String[] args) {

        SaxParsing test = new SaxParsing();

        System.out.println(test.parseMovieTitles().toString());

    }

    public List<String> parseMovieTitles() {

        try {
            File xmlDocument = Paths.get("src/Theater.xml").toFile();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(xmlDocument, this);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return movieTitles;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attrs) throws SAXException {

        switch (qualifiedName) {
            case "movie":
                movieTitle = attrs.getValue(0);
                break;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

        switch (qualifiedName) {
            case "movie":
                movieTitles.add(movieTitle);
                break;
        }
    }
}
