package webcrawlerir.indexing;

public class Posting {
    int docId;
    int termFrequency;

    public Posting(int docId, int termFrequency) {
        this.docId = docId;
        this.termFrequency = termFrequency;
    }

    public int getDocId() {
        return docId;
    }

    public int getTermFrequency() {
        return termFrequency;
    }
//Important for correct print the posting
    @Override
    public String toString() {
        return "DocID: " + docId + ", TF: " + termFrequency;
    }
}