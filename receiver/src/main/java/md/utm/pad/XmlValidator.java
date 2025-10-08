package md.utm.pad;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.StringReader;

public class XmlValidator {
    public static boolean validate(String xmlString) {
        try {
            // Creează o fabrică de scheme pentru limbajul W3C XML Schema
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            // Încarcă schema XSD din fișier
            File schemaFile = new File(XmlValidator.class.getClassLoader().getResource("message.xsd").getFile());
            Schema schema = factory.newSchema(schemaFile);
            
            // Creează un validator pe baza schemei
            Validator validator = schema.newValidator();
            
            // Validează string-ul XML
            validator.validate(new StreamSource(new StringReader(xmlString)));
            
            return true;
        } catch (Exception e) {
            System.err.println("XML Validation Error: " + e.getMessage());
            return false;
        }
    }
}
