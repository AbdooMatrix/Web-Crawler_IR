package webcrawlerir;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebCrawler {
    public HashSet<String> visitedUrls = new HashSet<>(); // Stores visited URLs to avoid revisits
    private LinkedList<String> urlQueue = new LinkedList<>(); // Queue for URLs to crawl
    private static final int MAX_PAGES = 10; // Limit on number of pages to crawl
    private Map<Integer, String> docIdToText = new HashMap<>(); // Map document ID to its text
    private int docId = 0; // Incremental ID for documents

    private static final String[] SEED_URLS = { // Starting URLs
            "https://en.wikipedia.org/wiki/List_of_pharaohs",
            "https://en.wikipedia.org/wiki/Pharaoh"
    };

    public Map<Integer, String> startCrawl() {
        for (String url : SEED_URLS) {
            urlQueue.add(url); // Add seed URLs to queue
        }

        while (!urlQueue.isEmpty() && visitedUrls.size() < MAX_PAGES) {
            String currentUrl = urlQueue.removeFirst(); // Get next URL

            if (!visitedUrls.contains(currentUrl)) {
                try {
                    // Fetch and parse the page
                    Document doc = Jsoup.connect(currentUrl)
                            .timeout(10_000)
                            .get();

                    visitedUrls.add(currentUrl); // Mark as visited
                    System.out.println("Crawled: " + currentUrl);

                    // Extract and store clean text from page
                    String cleanText = doc.body().text();
                    docIdToText.put(docId++, cleanText);

                    // Find and queue new links from the page
                    extractAndEnqueueLinks(doc);

                } catch (IOException e) {
                    System.err.println("Failed to crawl: " + currentUrl + " | Error: " + e.getMessage());
                }
            }
        }

        return docIdToText; // Return all collected texts
    }

    private void extractAndEnqueueLinks(Document doc) {
        Elements links = doc.select("a[href]"); // Find all hyperlink elements
        for (Element link : links) {
            String url = link.absUrl("href"); // Get absolute URL

            // Add valid Wikipedia links to the queue
            if (url.startsWith("https://en.wikipedia.org/wiki/")
                    && !url.contains("#")
                    && !visitedUrls.contains(url)
                    && !urlQueue.contains(url)
                    && visitedUrls.size() < MAX_PAGES) {
                urlQueue.add(url);
            }
        }
    }

}
