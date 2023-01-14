package at.saunders.htmlunit;

import at.saunders.data.FlatAdvert;
import at.saunders.pokemon.PokemonDataScraper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUnitScraper {

   public List<FlatAdvert> scrapeStandard() {
        List<FlatAdvert> flats = new ArrayList<>();

       String url =  "https://immobilien.derstandard.at/immobiliensuche?SortOrder=2&SortType=1&ProjectUsageType=0&UsageType=1&RegionType=0&RentOrBuy=2&PropertyTypes=all_p&PropertyType=all_p";
       
       HtmlPage page = getDocument(url);

       if (page == null) {
           System.out.println("nothing returned");
           return flats;
       }

       HtmlElement body = page.getBody();
       DomNodeList<DomNode> resultData = body.querySelectorAll("div.result-data-container");


       for (DomNode node : resultData) {
           flats.add(createAdvertFrom((HtmlDivision) node));
       }

       return flats;

   }

   	/**
   	 * Create a FlatAdvert from the data an HtmlDivision
   	 * @param resultData the HtmlDivision
   	 * @return the FlatAdvert
   	 */
    private FlatAdvert createAdvertFrom(HtmlDivision resultData) {
        HtmlSpan resultDataMeta = resultData.querySelector("span.result-data-meta");
        DomNodeList<DomNode> spans = resultDataMeta.querySelectorAll("span");
        DomNode address = spans.get(0);
        DomNode type  = spans.get(1);

        HtmlDivision resultDataCont = resultData.querySelector("div.result-data-cont");
        spans = resultDataCont.querySelectorAll("span.result-data");
        DomNode area = spans.get(0);
        DomNode numberRooms = spans.get(1);
        DomNode price = spans.get(2);

        return new FlatAdvert(
                address.asNormalizedText(),
                type.asNormalizedText(),
                getTextValue(area),
                getTextValue(numberRooms),
                getTextValue(price));
    }
    
    /**
     * Get a text field from a result-data DomNode
     * @param node the result-data DomNode
     * @return the String of the data we need or null
     */
    private String getTextValue(DomNode node) {
       for (DomNode child : node.getChildren()) {
           if (!child.getVisibleText().isEmpty()){
               if (child instanceof DomText) {
                   return child.toString();
               }
           }
       }
       return null;
   }

    /**
     * Get an HTML page from the internet.
     * @param url the url to find 
     * @return the HtmlPage or null
     */
    private HtmlPage getDocument(String url) {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            return webClient.getPage(url);
        } catch (Exception e) {
            System.out.println("Standard not working today!");
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


}
