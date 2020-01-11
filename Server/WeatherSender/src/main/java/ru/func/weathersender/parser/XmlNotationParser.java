package ru.func.weathersender.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.func.weathersender.entity.Notation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author func 06.01.2020
 */
public class XmlNotationParser implements NotationDataParser {

    private DocumentBuilder documentBuilder;
    private Document document;

    public XmlNotationParser() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);

        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Override
    public String parseNotationToFormat(List<Notation> notations) {
        document = documentBuilder.newDocument();

        Element rootElement = document.createElement("data");

        document.appendChild(rootElement);

        notations.forEach(notation -> {
            Element contentElement = document.createElement("sensor");
            createAndAddData(
                    contentElement,
                    notation.getLocation(),
                    notation.getTemperature().toString(),
                    notation.getPressure().toString(),
                    notation.getHumidity().toString()
            );
            rootElement.appendChild(contentElement);
        });

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            Transformer transformer = buildTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(dataOutputStream));

            dataOutputStream.flush();

            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Transformer buildTransformer() throws TransformerConfigurationException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

        return transformer;
    }

    private void createAndAddData(Element parent, String... args) {
        String[] nameArray = "location, temperature, pressure, humidity".split(", ");
        for (int i = 0; i < nameArray.length; i++) {
            Element location = document.createElement(nameArray[i]);
            location.setTextContent(args[i]);
            parent.appendChild(location);
        }
    }
}