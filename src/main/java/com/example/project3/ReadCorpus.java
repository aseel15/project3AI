package com.example.project3;

import java.io.*;
import java.util.HashMap;

//if the csv file is not found then read the corpus
public class ReadCorpus {
    private final HashMap<String, Integer> freq1 = new HashMap<>();
    private final HashMap<String, Integer> freq2 = new HashMap<>();
    private final HashMap<String, Integer> freq3 = new HashMap<>();
    FileWriter csvFile;
    String fileName;

    public ReadCorpus() {

        //this.fileName=csvFile;

        selectFolder();
        writeToCSVFiles();
        System.out.println("here init");
    }

    public void selectFolder() {
        String folderPath = "DataNew";
        File folder = new File(folderPath);

        //Read multiple files from multiple folders
        if (folder.isDirectory()) {
                        for (File fileHere : folder.listFiles()) {
                            //call read files method for each file in specific folder
                            readFiles(fileHere);
                        }
                    }

    }

    //read files
    private void readFiles(File file) {

        StringBuilder contentBuilder = new StringBuilder();
        String str;
        BufferedReader scan;
        try {

            scan = new BufferedReader(new FileReader(file));

            while((str = scan.readLine()) != null){
                contentBuilder.append(str + "\n");
            }
            String s = contentBuilder.toString();
            // call the tokenize method to split the arabic words
            String[] str1 = tokenizeString(s);

            String[] twoWords = new String[str1.length - 1];
            String[] threeWords = new String[str1.length - 2];
            //freq for 1 word
            for (String i : str1) {
                //count++;
                i=i.replaceAll("\\d","");
                if (!freq1.containsKey(i))
                    freq1.put(i, 1);
                else
                    freq1.replace(i, freq1.get(i) + 1);

            }
            for (int i = 0, j = 1, k = 2; i < str1.length - 2 && j < str1.length - 1 && k < str1.length; i++, j++, k++) {
                twoWords[i] = str1[i] + " " + str1[j];
                threeWords[i] = str1[i] + " " + str1[j] + " " + str1[k];
                //freq for 2 words
                if (!freq2.containsKey(twoWords[i]))
                    freq2.put(twoWords[i], 1);
                else
                    freq2.put(twoWords[i], freq2.get(twoWords[i]) + 1);


                //freq for 3 words
                if (!freq3.containsKey(threeWords[i]))
                    freq3.put(threeWords[i], 1);
                else
                    freq3.put(threeWords[i], freq3.get(threeWords[i]) + 1);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //tokenize the string and delete the digits and punctuations
    public String[] tokenizeString(String str) {
        return str.split("[\\w\\p{Punct}\\p{IsWhite_Space}،؛؟]+");
    }

    public void writeToCSVFiles() {
        //FileWriter bwrite;
        try {
            this.csvFile = new FileWriter("CSVWords.csv");
            for (String oneWord : freq1.keySet()) {

                this.csvFile.append(oneWord).append(",").append(String.valueOf(freq1.get(oneWord))).append("\n");

            }
            for (String twoWord : freq2.keySet()) {
                //count the probability of two words
                String[] parts = twoWord.split(" ");
                double counter = ((double) freq2.get(twoWord) / (double) freq1.get(parts[0]));

                this.csvFile.append(twoWord).append(",").append(String.valueOf(freq2.get(twoWord))).append(",").append(String.valueOf(counter)).append("\n");
            }
            for (String threeWord : freq3.keySet()) {
                //count the probability of three words
                String[] parts = threeWord.split(" ");
                double counter = ((double) freq3.get(threeWord) / (double) freq2.get(parts[0] + " " + parts[1]));

                this.csvFile.append(threeWord).append(",").append(String.valueOf(freq3.get(threeWord))).append(",").append(String.valueOf(counter)).append("\n");
            }

            this.csvFile.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
