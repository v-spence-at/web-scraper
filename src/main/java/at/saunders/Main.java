package at.saunders;

import at.saunders.csv.CsvWriter;
import at.saunders.data.FlatAdvert;
import at.saunders.htmlunit.HtmlUnitScraper;

import java.util.List;

public class Main {

        public static void main(String[] args) {
            HtmlUnitScraper hScraper = new HtmlUnitScraper();
            List<FlatAdvert> flats = hScraper.scrapeStandard();
            flats.forEach(flat -> {
                System.out.println(flat.toString());
            });
            if (!flats.isEmpty()) {
                //write flats out into a csv file.
                CsvWriter writer = new CsvWriter("flats.csv");
                writer.writeOutAsCSV(flats, FlatAdvert.HEADERS);
            }
        }

}