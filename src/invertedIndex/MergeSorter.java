package invertedIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * this class merge number of files into a one file
 */
public class MergeSorter extends Thread {
    private int numberOfFiles;
    private int fileNumber;
    private static int counter;
    String folder;

    /**
     * constructor
     *
     * @param fileNum
     */
    public MergeSorter(int fileNum, String folderLocation) {
        fileNumber = fileNum;
        this.folder = folderLocation;
    }

    /**
     * this funcion do binary merge for files
     *
     * @param numberOfFiles
     */
    public void startMergingfiles(int numberOfFiles) {
        int objectCounter = 0;
        //counter represent the name of the next file
        counter = numberOfFiles + 1;
        // if number of files is 1 -> finish merging
        while (numberOfFiles > 1) {
            try {
                ArrayList<MergeSorter> objectList = new ArrayList<MergeSorter>();
                File folder = new File(this.folder);
                SortedSet<Integer> filesNames = new TreeSet<>();
                File[] listOfFiles = folder.listFiles();

                //make a list of the files names
                for (int i = 0; i < listOfFiles.length; i++) {
                    String s = listOfFiles[i].getName();
                    s = s.replace(".txt", "");
                    filesNames.add(Integer.parseInt(s));
                }
                numberOfFiles = filesNames.size();

                //send every 2 files to the merge function with threads
                for (int j = filesNames.first(); j < filesNames.last(); j = j + 2) {
                    objectList.add(new MergeSorter(j,this.folder));
                    objectList.get(objectCounter).start();
                    objectList.get(objectCounter).join();
                    objectCounter++;
                }
                objectCounter = 0;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * this function merge 2 files
     */
    public void run() {

        try {
            // create the file we will merge inside him the two files
            FileWriter pw = new FileWriter(folder+"/" + counter + ".txt", false);
            counter++;
            String lastWord = "";
            String lastIndexDoc = "";
            // BufferedReader object for the 2 files
            BufferedReader br1 = new BufferedReader(new FileReader(folder+"/" + Integer.toString(fileNumber) + ".txt"));
            BufferedReader br2 = new BufferedReader(new FileReader(folder+"/" + Integer.toString(fileNumber + 1) + ".txt"));
            String previousLine = "";


            //check if the first line in the two files are "".
            String line1 = br1.readLine();
            if (line1==null||line1.equals("")) {
                line1 = br1.readLine();
            }
            String line2 = br2.readLine();
            if (line2==null||line2.equals("")) {
                line2 = br2.readLine();
            }
            // while we didn't finish both files
            while (line1 != null || line2 != null) {
                //get the word and doc from file 2
                if (line1 == null) {
                    String[] lineB = line2.split(" ");
                    String word2 = calculateWord(lineB);
                    String docIndex2 = lineB[lineB.length - 2];
                    // if it's the same word and doc -> add the location
                    if (word2.equals(lastWord) && docIndex2.equals(lastIndexDoc)) {
                        previousLine = previousLine + "," + lineB[lineB.length - 1];
                    }
                    //not the same word or doc -> write the previous, and update the parameters
                    else {
                        pw.write(previousLine + "\r\n");
                        previousLine = line2;
                        lastWord = word2;
                        lastIndexDoc = docIndex2;
                    }
                    line2 = br2.readLine();

                }
                //get the word and doc from file 1
                else if (line2 == null) {
                    String[] lineA = line1.split(" ");
                    String word1 = calculateWord(lineA);
                    String docIndex1 = lineA[lineA.length - 2];
                    // if it's the same word and doc -> add the location
                    if (word1.equals(lastWord) && docIndex1.equals(lastIndexDoc)) {
                        previousLine = previousLine + "," + lineA[lineA.length - 1];
                    }
                    //not the same word or doc -> write the previous, and update the parameters
                    else {
                        pw.write(previousLine + "\r\n");
                        previousLine = line1;
                        lastWord = word1;
                        lastIndexDoc = docIndex1;
                    }
                    line1 = br1.readLine();
                }
                //both lines are not null
                else {
                    //get the words and docs from the lines
                    String[] lineA = line1.split(" ");
                    String[] lineB = line2.split(" ");
                    String word1 = calculateWord(lineA);
                    String word2 = calculateWord(lineB);
                    String docIndex1 = lineA[lineA.length - 2];
                    String docIndex2 = lineB[lineB.length - 2];

                    //choose LineA -> lineA before lineB
                    if ((word1.compareTo(word2) < 0) || (word1.equals(word2) && Integer.parseInt(docIndex1) < Integer.parseInt(docIndex2))) {
                        if (lastIndexDoc.equals(docIndex1) && lastWord.equals(word1)) {
                            previousLine = previousLine + "," + lineA[lineA.length - 1];
                        } else {
                            pw.write(previousLine + "\r\n");
                            previousLine = line1;
                            lastWord = word1;
                            lastIndexDoc = docIndex1;
                        }
                        line1 = br1.readLine();

                    }
                    //choose LineB -> lineB before lineA
                    else if ((word1.compareTo(word2) > 0) || (word1.equals(word2) && Integer.parseInt(docIndex1) > Integer.parseInt(docIndex2))) {
                        if (lastIndexDoc.equals(docIndex2) && lastWord.equals(word2)) {
                            previousLine = previousLine + "," + lineB[lineB.length - 1];
                        } else {
                            pw.write(previousLine + "\r\n");
                            previousLine = line2;
                            lastWord = word2;
                            lastIndexDoc = docIndex2;
                        }
                        line2 = br2.readLine();
                    }

                    //line A and line B are equal
                    else if (word1.equals(word2)&& Integer.parseInt(docIndex1)== Integer.parseInt(docIndex2)){
                         String[] position = lineA[lineA.length-1].split(",");
                         int position1 = Integer.parseInt(position[0]);
                         position = lineB[lineB.length-1].split(",");
                         int position2 = Integer.parseInt(position[0]);
                         if (position1<position2){
                             if (lastIndexDoc.equals(docIndex1) && lastWord.equals(word1)) {
                                 previousLine = previousLine + "," + lineA[lineA.length - 1];
                             } else {
                                 pw.write(previousLine + "\r\n");
                                 previousLine = line1;
                                 lastWord = word1;
                                 lastIndexDoc = docIndex1;
                             }
                             line1 = br1.readLine();
                         }else{
                             if (lastIndexDoc.equals(docIndex2) && lastWord.equals(word2)) {
                                 previousLine = previousLine + "," + lineB[lineB.length - 1];
                             } else {
                                 pw.write(previousLine + "\r\n");
                                 previousLine = line2;
                                 lastWord = word2;
                                 lastIndexDoc = docIndex2;
                             }
                             line2 = br2.readLine();
                         }
                    }
                }
            }
            pw.write(previousLine + "\r\n");
            pw.flush();
            // closing files
            br1.close();
            br2.close();
            pw.close();
            File file1 = new File(folder+"/" + fileNumber + ".txt");
            file1.delete();
            File file2 = new File(folder+"/" + Integer.toString(fileNumber + 1) + ".txt");
            file2.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function get a split line and find the word inside
     *
     * @param line
     * @return the word
     */
    private String calculateWord(String[] line) {
        if (line.length > 3) {
            String word = line[0];
            for (int i = 1; i < line.length - 2; i++) {
                word = word + " " + line[i];
            }
            return word;
        } else {
            return line[0];
        }
    }
}
