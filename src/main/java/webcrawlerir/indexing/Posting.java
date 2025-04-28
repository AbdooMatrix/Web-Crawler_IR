package webcrawlerir.indexing;

public class Posting {
    int docId;  // Document ID where the term appears
    int termFrequency;  // Frequency of the term in the document

    // Constructor to initialize Posting object with document ID and term frequency
    public Posting(int docId, int termFrequency) {
        this.docId = docId;
        this.termFrequency = termFrequency;
    }

    // Getter for document ID
    public int getDocId() {
        return docId;
    }

    // Getter for term frequency
    public int getTermFrequency() {
        return termFrequency;
    }

    // Override toString for correct printing of Posting object
    @Override
    public String toString() {
        return "DocID: " + docId + ", TF: " + termFrequency;  // Format output for easier readability
    }
}
