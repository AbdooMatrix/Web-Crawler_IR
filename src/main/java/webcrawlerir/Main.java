package webcrawlerir;

import webcrawlerir.indexing.InvertedIndex;
import webcrawlerir.indexing.Posting;
import webcrawlerir.processing.QueryProcessor;
import webcrawlerir.processing.TFIDFCalculator;
import webcrawlerir.processing.TextProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Step 1: Run the web crawler
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docIdToText = crawler.startCrawl();
        System.out.println("Crawled Pages: " + crawler.visitedUrls);

        // Step 2: Build the inverted index
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.buildIndex(docIdToText);


        // Step 3 : Print the inverted index
        invertedIndex.printIndex();

        // Step 4: Verify the inverted index
        String testTerm = "pharaoh"; // Example term to verify
        List<Posting> postings = invertedIndex.getIndex().get(testTerm);

        if (postings != null) {
            System.out.println("Term '" + testTerm + "' is mapped to the following documents:");
            for (Posting posting : postings) {
                System.out.println(posting);
            }
        } else {
            System.out.println("Term '" + testTerm + "' is not found in the inverted index.");
        }
        // Step 5: TF-IDF Calculator
        int documentSize=invertedIndex.getTotalDocs();
        HashMap<String, List<Posting>> inde=invertedIndex.getIndex();
        TFIDFCalculator calculator = new TFIDFCalculator( documentSize,inde);
        calculator.operation();
        System.out.println("==== Term Frequency ====");
        calculator.print_term_frq();
        System.out.println("==== Inverse Document Frequency ====");
        calculator.print_idf();
        System.out.println("==== TF-IDF Table ====");
        calculator.Print_TfIdfTable();
        System.out.println("==== Norms ====");
        calculator.print_norm();



        // step 6: Handling user query
        String query ;
        System.out.println("Welcome to the Wikipedia Search Engine");
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.print("Enter your query (or type 'exit' to quit):");
            query = scanner.nextLine();
            if(query.equals("exit"))
            {
                System.out.println("the program exited.") ;
                break ;
            }
            TextProcessor textProcessor = new TextProcessor();
            List<String> words = textProcessor.processText(query) ;
            QueryProcessor queryProcessor = new QueryProcessor(calculator);
            queryProcessor.processQuery(words);
        }

    }
}
