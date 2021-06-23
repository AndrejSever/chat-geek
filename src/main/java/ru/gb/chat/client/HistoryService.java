package ru.gb.chat.client;

import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HistoryService {
    private static String HistoryFile = "history.txt";

    private static int history_size = 100;

    public static String record() {
        File file = new File(HistoryFile);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String c;
            int i = 0;
            while ((c = bufferedReader.readLine()) != null)
            {
                if (i < history_size)
                {
                    stringBuilder.append(c + "\n");
                    i++;
                } else { break;}
            }
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void historyToFile(String text) {
        Platform.runLater(() -> { File file = new File(HistoryFile);
            try (OutputStream out = new FileOutputStream(file)) {
                out.write(text.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
