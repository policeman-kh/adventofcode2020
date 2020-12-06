package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadDataClient {
    public static List<String> getInputDataList(String fileName) {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStream in = ReadDataClient.class.getClassLoader()
                                                 .getResourceAsStream(fileName);
            br = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
