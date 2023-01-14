package at.saunders.pokemon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PokemonDataScraper {

    public static final String HTTPS_SCRAPEME_LIVE_SHOP_PAGE_1 = "https://scrapeme.live/shop/page/1/";

    public void scrapeData() {
        // initializing the list of Java object to store
        // the scraped data
        List<PokemonProduct> pokemonProducts = Collections.synchronizedList(new ArrayList<>());

        // initializing the set of web page urls
        // discovered while crawling the target website
        Set<String> pagesDiscovered = Collections.synchronizedSet(new HashSet<>());

        // initializing the queue of urls to scrape
        List<String> pagesToScrape = Collections.synchronizedList(new ArrayList<>());
        // initializing the scraping queue with the
        // first pagination page
        pagesToScrape.add(HTTPS_SCRAPEME_LIVE_SHOP_PAGE_1);

        // initializing the ExecutorService to run the
        // web scraping process in parallel on 4 pages at a time
        ExecutorService executorService = Executors.newFixedThreadPool(4) ;

        // launching the web scraping process to discover some
        // urls and take advantage of the parallelization process
        scrapeProductPage(pokemonProducts, pagesDiscovered, pagesToScrape);

/*
        // the number of iteration executed
        int i = 1;
        // to limit the number to scrape to 5
        int limit = 10;

        while (!pagesToScrape.isEmpty() && i < limit) {
            // registering the web scraping task
            executorService.execute(() -> scrapeProductPage(pokemonProducts, pagesDiscovered, pagesToScrape));

            // adding a 200ms delay for avoid overloading the server
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // incrementing the iteration number
            i++;
        }

        // waiting up to 300 seconds to all pending tasks to end
        executorService.shutdown();
        executorService.awaitTermination(300, TimeUnit.SECONDS);
*/

        System.out.println(pokemonProducts.size());

        pokemonProducts.forEach( product -> {
            System.out.println(product);
        });

        // writing the scraped data to a db or export it to a file...

    }

    public void scrapeProductPage(
            List<PokemonProduct> pokemonProducts,
            Set<String> pagesDiscovered,
            List<String> pagesToScrape) {

        if (!pagesToScrape.isEmpty()) {
            // the current web page is about to be scraped and
            // should no longer be part of the scraping queue
            String url = pagesToScrape.remove(0);

            pagesDiscovered.add(url);

            // initializing the HTML Document page variable
            Document doc;

            try {
                // fetching the target website
                doc = Jsoup
                        .connect(url)
                        .userAgent(
                                "Mozilla/5.0 "
                                        + "(Windows NT 10.0; Win64; x64) "
                                        +  "AppleWebKit/537.36 (KHTML, like Gecko) "
                                        + "Chrome/107.0.0.0 Safari/537.36").get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // retrieving the list of product HTML elements
            // in the target page
            Elements products = doc.select("li.product");

            Element productElement = products.get(0);
            PokemonProduct pokemonProduct = new PokemonProduct();

            // extracting the data of interest from the product HTML element
            // and storing it in pokemonProduct
            pokemonProduct.setUrl(productElement.selectFirst("a").attr("href"));
            pokemonProduct.setImage(productElement.selectFirst("img").attr("src"));
            pokemonProduct.setName(productElement.selectFirst("h2").text());
            pokemonProduct.setPrice(productElement.selectFirst("span").text());

            // adding pokemonProduct to the list of the scraped products
            pokemonProducts.add(pokemonProduct);

            // retrieving the list of pagination HTML element
            Elements paginationElements = doc.select("a.page-numbers");

            // iterating over the pagination HTML elements
            for (Element pageElement : paginationElements) {
                // the new link discovered
                String pageUrl = pageElement.attr("href");

                // if the web page discovered is new and should be scraped
                if (!pagesDiscovered.contains(pageUrl) && !pagesToScrape.contains(pageUrl)) {
                    pagesToScrape.add(pageUrl);
                }

                // adding the link just discovered
                // to the set of pages discovered so far
                pagesDiscovered.add(pageUrl);
            }

            // logging the end of the scraping operation
            System.out.println(url + " -> page scraped");
        }
    }


}
