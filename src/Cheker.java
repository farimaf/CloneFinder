import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Farima on 3/27/2017.
 */
public class Cheker {
    HashMap<String,ArrayList<FileItem>> files=new HashMap<>();
    double similarityThreshold=0.8d;
    public static void main(String[] args) {

        Cheker cheker=new Cheker();
        cheker.loadFile();
        cheker.getSimilarFiles();
    }
    public void getSimilarFiles(){
        ArrayList<String> seenFiles=new ArrayList<>();
        try {
            PrintWriter printWriter = new PrintWriter("similar files.txt");
            for (String file1 : files.keySet()) {
                seenFiles.add(file1);
                ArrayList<FileItem> file1Content = files.get(file1);
                for (String file2 : files.keySet()) {
                    if (!file1.equals(file2) && !seenFiles.contains(file2)) {
                        int similarTokensNum = 0;
                        int file1Length = 0, file2Length = 0;
                        ArrayList<FileItem> file2Content = files.get(file2);
                        //int lenToIterate=file1Content.size()<=file2Content.size()?file1Content.size():file2Content.size();
                        for (int i = 0; i < file1Content.size(); i++) {
                            file1Length += file1Content.get(i).getFrequency();
                            for (int j = 0; j < file2Content.size(); j++) {
                                if (i == 0) file2Length += file2Content.get(j).getFrequency();
                                if (file1Content.get(i).getWord().equals(file2Content.get(j).getWord())) {
                                    similarTokensNum += file1Content.get(i).getFrequency() <= file2Content.get(j).getFrequency() ?
                                            file1Content.get(i).getFrequency() : file2Content.get(j).getFrequency();
                                }
                            }
                        }
                        int minNumberSimilarity = (int) (Math.ceil(similarityThreshold * (file1Length >= file2Length ? file1Length : file2Length)));
                        if (similarTokensNum >= minNumberSimilarity) {
                            //printWriter.append(file1 + "," + file2 + "," + similarTokensNum + System.lineSeparator());//with frequency
                            printWriter.append(file1 + "," + file2+","+System.lineSeparator());
                        }
                    }
                }
            }
            printWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadFile(){
        try {
            String eachFile="";
            BufferedReader bf=new BufferedReader(new FileReader("blocks.file"));

            while ((eachFile=bf.readLine())!=null){
                String[] separatedLineFromMeta=eachFile.split("@#@");
                if (Integer.valueOf(separatedLineFromMeta[0].split(",")[2])<65 ||Integer.valueOf(separatedLineFromMeta[0].split(",")[2])>5000 ) continue;
                String projectId=separatedLineFromMeta[0].split(",")[0];
                String fileId=separatedLineFromMeta[0].split(",")[1];
                String[] words=separatedLineFromMeta[1].split(",");
                String word="";
                ArrayList<FileItem> fileItems=new ArrayList<>();
                for (int i = 0; i <words.length ; i++) {
                    word=words[i].split("@@::@@")[0];
                    Integer frequency=Integer.valueOf(words[i].split("@@::@@")[1]);
                    fileItems.add(new FileItem(word,frequency));
                }
                files.put(projectId+","+fileId,fileItems);
            }
            bf.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
