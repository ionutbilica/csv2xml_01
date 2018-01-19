package ro.bilica.ionut.hs.core;

import org.junit.Assert;
import org.junit.Test;
import ro.bilica.ionut.hs.xml.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CsvToXmlConverterTest {

    private static final File CSV_FILE = new File(CsvToXmlConverterTest.class.getResource("/library.csv").getPath());
    private static final File XML_FILE = new File(CSV_FILE.getParentFile(),"library.xml");

    @Test
    public void testConversion() throws IOException, JAXBException {
        XML_FILE.delete();
        CsvToXmlConverter converter = new CsvToXmlConverter();
        converter.convert(CSV_FILE, XML_FILE);
        JAXBContext context = JAXBContext.newInstance(Library.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Library library = (Library)unmarshaller.unmarshal(XML_FILE);
        Assert.assertEquals("Incorrect number of books.", 2, library.getBook().size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws IOException, JAXBException {
        CsvToXmlConverter converter = new CsvToXmlConverter();
        converter.convert(new File("non-existent.csv"), XML_FILE);
    }
}
