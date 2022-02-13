package handler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyClientHandler {

    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    private List<String> historyList = new ArrayList<>(101);

    public void loadHistory(String fileName) {

        try {
            bufferedWriter = new BufferedWriter(new FileWriter("history/" + fileName, true));
            bufferedReader = new BufferedReader(new FileReader("history/" + fileName));

            bufferedWriter.write("");

            while (bufferedReader.ready()) {
                historyList.add(bufferedReader.readLine());
                if (historyList.size() > 100) {
                    historyList.remove(0);
                }
            }
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(String history) {
        try {
            bufferedWriter.write(history + "\n");
        } catch (IOException e) {
            System.out.println("Запись истории не удалась.");
            e.printStackTrace();
        }
    }

    public void closeStreams() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHistoryList() {
        return historyList;
    }
}
