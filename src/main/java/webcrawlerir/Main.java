package webcrawlerir;

import webcrawlerir.indexing.InvertedIndex;
import webcrawlerir.indexing.Posting;
import webcrawlerir.processing.QueryProcessor;
import webcrawlerir.processing.Stemmer;
import webcrawlerir.processing.TFIDFCalculator;
import webcrawlerir.processing.TextProcessor;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Step 1: Crawl web pages
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docIdToText = crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);

        // Step 2: Build inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildIndex(docIdToText);

        // Step 3: Print inverted index
        invertedIndex.printIndex();

        // Step 4: Calculate TF-IDF
        int documentSize = invertedIndex.getTotalDocs();
        HashMap<String, List<Posting>> inde = invertedIndex.getIndex();
        TFIDFCalculator calculator = new TFIDFCalculator(documentSize, inde);
        calculator.operation();

        System.out.println("==== Term Frequency ====");
        calculator.print_term_frq();

        System.out.println("==== Inverse Document Frequency ====");
        calculator.print_idf();

        System.out.println("==== TF-IDF Table ====");
        calculator.Print_TfIdfTable();

        System.out.println("==== Norms ====");
        calculator.print_norm();

        // Step 5: Handle user queries
        String query;
        List<String> words = new ArrayList<>();
        System.out.println("Welcome to the Wikipedia Search Engine");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your query (or type 'exit' to quit): ");
            query = scanner.nextLine(); // Read user input

            if (query.equals("exit")) {
                System.out.println("the program exited.");
                break;
            }
            else if(query.equals("")) {
                System.out.println("Please enter a valid query.");
                continue;
            }

            // Process input text: tokenize
            TextProcessor textProcessor = new TextProcessor();
            words = textProcessor.processText(query);

            // Apply stemming
            Stemmer stemmer = new Stemmer();
            for (int i = 0; i < words.size(); ++i) {
                stemmer.addString(words.get(i));
                stemmer.stem();
                String stemmedWord = stemmer.toString();
                words.set(i, stemmedWord);
            }

            // Process the query using TF-IDF scores
            QueryProcessor queryProcessor = new QueryProcessor(calculator);
            queryProcessor.processQuery(words);
        }
    }
}
