package webcrawlerir.processing;

import webcrawlerir.indexing.Posting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFCalculator {
    // outcomes
    HashMap<Integer, HashMap<String, Double>> tf_idfTable=new HashMap<>();
    HashMap<Integer,Double> Norm=new HashMap<>();
    HashMap<Integer,HashMap<String,Integer>>Term_frequency=new HashMap<>();
    HashMap<String, Double> Inverse_Document_Frequency = new HashMap<>();

    //input
    HashMap<String, List<Posting>> Term_frequency_inverted;
    Integer docs_size;

    public TFIDFCalculator(int document_size,HashMap<String, List<Posting>> term_freq_test) {

        Term_frequency_inverted=term_freq_test;
        docs_size=document_size;
    }

    public void operation() {


        for(Map.Entry<String,List<Posting>> entry:Term_frequency_inverted.entrySet()){
            String word=entry.getKey();
            List<Posting>doc_frequency=entry.getValue();
            for(Posting posting:doc_frequency){
                Integer id= posting.getDocId();
                Integer freq= posting.getTermFrequency();

                Term_frequency.putIfAbsent(id,new HashMap<>());
                Term_frequency.get(id).put(word,freq);
            }
        }

        //calculate Term frequency weight

        HashMap<Integer,HashMap<String, Double>> Term_frq_weight=new HashMap<>();
        for(Map.Entry<Integer,HashMap<String, Integer>>entry1:Term_frequency.entrySet()){
            Integer doc_id=entry1.getKey();
            HashMap<String ,Integer>freq=entry1.getValue();
            HashMap<String,Double> weight_list=new HashMap<>();
            for(Map.Entry<String,Integer> term:freq.entrySet()){
                String word=term.getKey();
                Integer frq=term.getValue();
                double weight = 1 + Math.log10(frq);
                weight_list.put(word,weight);
            }
            Term_frq_weight.put(doc_id,weight_list);


        }


        // calculate inverse document frequency (IDF)

        for (Map.Entry<String, List<Posting>> entry : Term_frequency_inverted.entrySet()) {
            String word = entry.getKey();
            List<Posting> postings = entry.getValue();
            int num_docs_with_word = postings.size();
            double idf = Math.log10((double) docs_size / num_docs_with_word);
            Inverse_Document_Frequency.put(word, idf);
        }



        //calculate TF-IDF + Norm

        for(int i=0;i<docs_size;i++){
            Double new_norm=0.0;

            HashMap<String,Double>tfw=Term_frq_weight.get(i);
            if(tfw==null)continue;
            HashMap<String,Double>tf_idf_vector=new HashMap<>();
            for(Map.Entry<String,Double> entry:tfw.entrySet()){
                String word=entry.getKey();
                Double freq_weight= entry.getValue();
                Double idf=Inverse_Document_Frequency.get(word);

                Double tf_idf=idf*freq_weight;
                tf_idf_vector.put(word,tf_idf);
                new_norm+=tf_idf*tf_idf;
            }
            tf_idfTable.put(i,tf_idf_vector);
            Norm.put(i,Math.sqrt(new_norm));

        }
    }
    public void print_term_frq(){
        for(Map.Entry<Integer,HashMap<String,Integer>> entry:Term_frequency.entrySet()){
            Integer doc_id=entry.getKey();
            HashMap<String,Integer> freq=entry.getValue();
            System.out.println("Document id:" +doc_id);
            for(Map.Entry<String,Integer> term:freq.entrySet()){
                String word=term.getKey();
                Integer freq_term=freq.get(word);
                System.out.println(word+"\t"+freq_term);


            }
            System.out.println("-----------------------------------------");
        }
    }

    public void print_idf(){
        for(Map.Entry<String,Double> entry: Inverse_Document_Frequency.entrySet()){
            String word=entry.getKey();
            Double idf=Inverse_Document_Frequency.get(word);
            System.out.println(word+"\t"+idf);
            System.out.println("-----------------------------------------");
        }
    }

    public void Print_TfIdfTable() {
        for(Map.Entry<Integer,HashMap<String, Double>> entry:tf_idfTable.entrySet()){
            Integer id=entry.getKey();
            HashMap<String, Double> tf_idf=entry.getValue();
            System.out.println("Document id:" +id);
            for(Map.Entry<String,Double> entry2:tf_idf.entrySet()){
                String word=entry2.getKey();
                Double idf=tf_idf.get(word);
                System.out.println(word+" "+idf);

            }
            System.out.println("-----------------------------------------");
        }
    }

    public void print_norm() {
        System.out.println("Norm vector: ");
        for(Map.Entry<Integer,Double> entry:Norm.entrySet()){
            Integer id=entry.getKey();
            Double idf=entry.getValue();
            System.out.println(id+" "+idf);
            System.out.println("----------------------------------------");
        }
    }
    public HashMap<Integer,Double> Get_Norm(){
        return Norm;
    }

}
