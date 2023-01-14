package at.saunders.csv;

import at.saunders.data.FlatAdvert;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {

    private String fileName;

    /**
     * Constructor
     * @param fileNameIn the file name to write out
     */
    public CsvWriter(String fileNameIn) {
        this.fileName = fileNameIn;
    }

    /**
     * Write out file data as in a CSV file.
     * @param flats the flat data
     * @param headers the headers to use
     */
    public  void writeOutAsCSV(List<FlatAdvert> flats, String[] headers) {
        try (CSVPrinter printer = new CSVPrinter(
                new FileWriter(fileName),
                CSVFormat.DEFAULT.withHeader(headers))) {
            flats.forEach(flat -> {
                try {
                    printer.printRecord(flat.toArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
