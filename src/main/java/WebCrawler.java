import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebCrawler {
    private HashSet<String> visitedUrls = new HashSet<>();
    private LinkedList<String> urlQueue = new LinkedList<>();
    private static final int MAX_PAGES = 10;
    private Map<Integer, String> docIdToText = new HashMap<>();
    private int docId = 0;

    private static final String[] SEED_URLS = {
            "https://en.wikipedia.org/wiki/List_of_pharaohs",
            "https://en.wikipedia.org/wiki/Pharaoh"
    };

    public Map<Integer, String> startCrawl() {
        for (String url : SEED_URLS) {
            urlQueue.add(url);
        }

        while (!urlQueue.isEmpty() && visitedUrls.size() < MAX_PAGES) {
            String currentUrl = urlQueue.removeFirst();

            if (!visitedUrls.contains(currentUrl)) {
                try {
                    Document doc = Jsoup.connect(currentUrl)
                            .timeout(10_000)
                            .get();

                    visitedUrls.add(currentUrl);
                    System.out.println("Crawled: " + currentUrl);

                    // Store the text
                    String cleanText = doc.body().text();
                    docIdToText.put(docId++, cleanText);

                    extractAndEnqueueLinks(doc);

                } catch (IOException e) {
                    System.err.println("Failed to crawl: " + currentUrl + " | Error: " + e.getMessage());
                }
            }
        }

        return docIdToText;
    }

    private void extractAndEnqueueLinks(Document doc) {
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String url = link.absUrl("href");
            if (url.startsWith("https://en.wikipedia.org/wiki/")
                    && !visitedUrls.contains(url)
                    && !urlQueue.contains(url)
                    && visitedUrls.size() < MAX_PAGES) {
                urlQueue.add(url);
            }
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docIdToText = crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);

         // Test the TextProcessor with a sample document
//        TextProcessor processor = new TextProcessor();
//        String sampleText = docIdToText.get(0); // Get the text of the first document
//        if (sampleText != null) {
//            // Process only the first 100 characters for testing
//            String testText = sampleText.length() > 100 ? sampleText.substring(0, 100) : sampleText;
//            System.out.println("Sample Text: " + testText);
//
//            List<String> tokens = processor.processText(testText);
//            System.out.println("Processed Tokens: " + tokens);
//        }

        // Build the InvertedIndex
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildIndex(docIdToText);

        // Print the index
        invertedIndex.printIndex();
    }
}
