package ro.bilica.ionut.hs.core;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import ro.bilica.ionut.hs.xml.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvToXmlConverter {

    private static final int TITLE_COL_INDEX = 0;
    private static final int AUTHOR_COL_INDEX = 1;
    private static final int PRICE_COL_INDEX = 2;

    public void convert(File csv, File xml) throws IOException, JAXBException {
        Library library = new Library();
        try(CSVReader csvReader = new CSVReaderBuilder(new FileReader(csv))
                .withSkipLines(1).build()) {
            String[] csvLine;
            while ((csvLine = csvReader.readNext()) != null) {
                Library.Book book = loadBook(csvLine);
                library.getBook().add(book);
            }
            writeXmlFile(library, xml);
        }
    }

    private Library.Book loadBook(String[] csvLine) {
        Library.Book book = new Library.Book();
        book.setTitle(csvLine[TITLE_COL_INDEX]);
        book.setAuthor(csvLine[AUTHOR_COL_INDEX]);
        book.setPrice(Float.parseFloat(csvLine[PRICE_COL_INDEX]));
        return book;
    }

    private void writeXmlFile(Library library, File xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Library.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(library, xml);
    }
}
