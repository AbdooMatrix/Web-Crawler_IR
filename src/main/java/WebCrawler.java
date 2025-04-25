import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class WebCrawler {
    private HashSet<String> visitedUrls = new HashSet<>();
    private LinkedList<String> urlQueue = new LinkedList<>();
    private static final int MAX_PAGES = 10;

    // Seed URLs
    private static final String[] SEED_URLS = {
            "https://en.wikipedia.org/wiki/List_of_pharaohs",
            "https://en.wikipedia.org/wiki/Pharaoh"
    };

    public void startCrawl() {
        // Add seed URLs to the queue
        for (String url : SEED_URLS) {
            urlQueue.add(url);
        }

        while (!urlQueue.isEmpty() && visitedUrls.size() < MAX_PAGES) {
            String currentUrl = urlQueue.removeFirst();

            if (!visitedUrls.contains(currentUrl)) {
                try {
                    // Fetch and parse the page
                    Document doc = Jsoup.connect(currentUrl)
                            .timeout(10_000) // 10-second timeout
                            .get();

                    visitedUrls.add(currentUrl);
                    System.out.println("Crawled: " + currentUrl);

                    // Extract links and add valid ones to the queue
                    extractAndEnqueueLinks(doc);

                } catch (IOException e) {
                    System.err.println("Failed to crawl: " + currentUrl + " | Error: " + e.getMessage());
                }
            }
        }
    }

    private void extractAndEnqueueLinks(Document doc) {
        Elements links = doc.select("a[href]"); // Get all <a> tags with href

        for (Element link : links) {
            String url = link.absUrl("href"); // Resolve to absolute URL

            // Filter: Stay within Wikipedia, avoid duplicates, and limit to 10 pages
            if (url.startsWith("https://en.wikipedia.org/")
                    && !visitedUrls.contains(url)
                    && visitedUrls.size() < MAX_PAGES) {

                urlQueue.add(url);
            }
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);
    }
}
