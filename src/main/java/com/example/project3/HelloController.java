package com.example.project3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.*;

public class HelloController {
    @FXML
    private TextArea enterText;

    @FXML
    private ListView<String> listTopWord;
    ArrayList<Word> predictedWords = new ArrayList<>();
    HashMap<String, Word> predictedHash = new HashMap<>();
    ArrayList<Integer> saveCount = new ArrayList<>();


    @FXML
    protected void onSpellClicked() {
        //get the text
        //check the tri gram table (create obj wordFac then interface.Then call the trigram hash)
        //calculate the probability of p(#|wi-1)*p(#|wi-2wi-1) for all probabilities
        //get the top five
        //for each word calculate the probability of p(wi+1|#)*(wi+2|#wi+1)
        String enteredText = enterText.getText();
        String[] parts = enteredText.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("#")) {
                //listTopWord.getItems().add("The " + i + " #'s predicted words ");
                saveCount.add(i);
                predictWord(i, parts);

            }
        }


    }

    public void predictWord(int count, String[] parts) {

        ReadCSV readCSV = new ReadCSV();
        ArrayList<Word> wordsFromHash = new ArrayList<>();
        //if the word is the second word.Then, search in bi-gram
        if (count == 1) {
            for (String st : readCSV.getFreqTwoWords().keySet()) {
                String[] biGramSplit = st.split(" ");
                if (biGramSplit[0].equals(parts[count - 1])) {
                    double probability = readCSV.getFreqTwoWords().get(st).getProbability();
                    //System.out.println("the probability = "+probability);
                    wordsFromHash.add(new Word(biGramSplit[1], probability));
                    predictedHash.put(biGramSplit[1], new Word(biGramSplit[1], probability));
                }
            }
        }
        //list all sentences that contain (Wi-2 Wi-1)
        else {
            for (String st : readCSV.getFreqThreeWords().keySet()) {
                String[] splitTriGram = st.split(" ");
                if ((splitTriGram[0] + " " + splitTriGram[1]).equals(parts[count - 2] + " " + parts[count - 1])) {
                    //System.out.println(splitTriGram[0]+" "+splitTriGram[1]);
                   // System.out.println("the pro "+splitTriGram[0] + " " + splitTriGram[1]+readCSV.getFreqTwoWords().get(splitTriGram[0] + " " + splitTriGram[1]).getProbability());
                    double probability = readCSV.getFreqThreeWords().get(st).getProbability() *
                            readCSV.getFreqTwoWords().get(splitTriGram[0] + " " + splitTriGram[1]).getProbability();

                    //System.out.println("the probability = "+ readCSV.getFreqTwoWords().get(splitTriGram[0] + " " + splitTriGram[1]).getProbability());
                    wordsFromHash.add(new Word(splitTriGram[2], probability));
                    predictedHash.put(splitTriGram[2], new Word(splitTriGram[2], probability));

                }
            }
        }
        checkList(count, parts, wordsFromHash);

    }

    public void checkList(int count, String[] parts, ArrayList<Word> wordsFromHash) {
        //To sort the probability descending
        Collections.sort(wordsFromHash, new Comparator<Word>() {
            @Override
            public int compare(Word c1, Word c2) {
                return Double.compare(c2.getProbability(), c1.getProbability());
            }
        });

        //call method enhanceAccuracy to the top five words predicted

        if (wordsFromHash.size() > 5) {
            for (int i = 0; i < 5; i++) {
                enhanceAccuracy(wordsFromHash.get(i), count, parts);
                listTopWord.getItems().add(wordsFromHash.get(i).getToken() + " " + predictedHash.get(wordsFromHash.get(i).getToken()).getProbability());

            }
        } else {
            for (int i = 0; i < wordsFromHash.size(); i++) {
                enhanceAccuracy(wordsFromHash.get(i), count, parts);
                listTopWord.getItems().add(wordsFromHash.get(i).getToken() + " " + predictedHash.get(wordsFromHash.get(i).getToken()).getProbability());
            }
        }

    }

    //to enhance the accuracy I should calculate the probability of p(wi+1|#)*(wi+2|#wi+1) for top five words
    public void enhanceAccuracy(Word word, int count, String[] parts) {

        if (count != parts.length - 1) {
            ReadCSV readCSV = new ReadCSV();
            if (count == parts.length - 2) {
                //probability for one word next
                for (String st : readCSV.getFreqTwoWords().keySet()) {
                    String[] bigramSplit = st.split(" ");
                    if ((bigramSplit[0] + " " + bigramSplit[1]).equals(word.getToken() + " " + parts[count + 1])) {
                        double probability = readCSV.getFreqTwoWords().get(st).getProbability();
                        Word wordReplaced = predictedHash.get(bigramSplit[0]);
                        wordReplaced.setProbability(wordReplaced.getProbability() + probability);

                    }
                }
            } else {
                //probability for two words next
                for (String st : readCSV.getFreqThreeWords().keySet()) {
                    String[] trigramSplit = st.split(" ");
                    if ((trigramSplit[0] + " " + trigramSplit[1] + " " + trigramSplit[2]).equals(word.getToken() + " " + parts[count + 1] + " " + parts[count + 2])) {

                        double probability = readCSV.getFreqThreeWords().get(st).getProbability() *
                                readCSV.getFreqTwoWords().get(trigramSplit[1] + " " + trigramSplit[2]).getProbability();
                        double replacedValue = (predictedHash.get(word.getToken()).getProbability()) + probability;


                        Word wordReplaced = predictedHash.get(word.getToken());
                        wordReplaced.setProbability(replacedValue);
                    }
                }
            }
        }


    }


}