package com.example.project3;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

//to read the n-gram table
public class ReadCSV {
    private final HashMap<String, Word> freqOneWord = new HashMap<>();
    private final HashMap<String, Word> freqTwoWords = new HashMap<>();
    private final HashMap<String, Word> freqThreeWords = new HashMap<>();

    public ReadCSV() {
        //read the csv file here and full the hash maps
        try {
            File file = new File("CSVWords.csv");

            if (file.exists()) {
                //System.out.println("the file exist");
                BufferedReader csvReader = new BufferedReader(new FileReader(file));
                String str;
                while ((str = csvReader.readLine()) != null) {
                    String[] parts = str.split(",");

                    StringTokenizer tokens = new StringTokenizer(parts[0]);
                    int counterTokens = tokens.countTokens();
                    // System.out.println("the parts "+tokens.countTokens());
                    if (counterTokens == 1) {

                        freqOneWord.put(parts[0], new Word(parts[0], Integer.parseInt(parts[1]), 0.0));
                        //System.out.println("ready");
                    } else if (counterTokens == 2) {
                        freqTwoWords.put(parts[0], new Word(parts[0], Integer.parseInt(parts[1]), Double.parseDouble(parts[2])));
                    } else if (counterTokens == 3) {
                        freqThreeWords.put(parts[0], new Word(parts[0], Integer.parseInt(parts[1]), Double.parseDouble(parts[2])));
                    }
                }
            } else {
                //create object from class readCorpus to create the language model and store it in csv file

                ReadCorpus readCorpus = new ReadCorpus();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully reading");
                alert.setContentText("The file was successfully read");
                alert.show();


            }
        } catch (FileNotFoundException e) {


            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] splitString(String str) {
        return str.split(",");
    }

    public Word getOneWordFrequency(String str) {
        return freqOneWord.get(str);
    }

    public Word getTwoWordFrequency(String str) {
        return freqTwoWords.get(str);
    }

    public Word getThreeWordFrequency(String str) {
        return freqThreeWords.get(str);
    }

    public HashMap<String, Word> getFreqOneWord() {
        return freqOneWord;
    }

    public HashMap<String, Word> getFreqTwoWords() {
        return freqTwoWords;
    }

    public HashMap<String, Word> getFreqThreeWords() {
        return freqThreeWords;
    }
}
