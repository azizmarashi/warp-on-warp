package org.example;

import au.com.bytecode.opencsv.CSVReader;
import lombok.SneakyThrows;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApplication {

    @SneakyThrows
    public static void main(String[] args) {

        extractAndCreateCSVFile();
        System.out.println(extractIpAndPort());

    }

    @SneakyThrows
    public static void extractAndCreateCSVFile(){
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "curl -fsSL https://raw.githubusercontent.com/Ptechgithub/warp/main/endip/install.sh | bash");
        Process process = processBuilder.start();
        process.getOutputStream().write("1\n".getBytes());
        process.getOutputStream().flush();
        InputStream inputStream2 = process.getErrorStream();
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));
        while ((reader2.readLine()) != null) {}
        InputStream inputStream3 = process.getInputStream();
        BufferedReader reader3 = new BufferedReader(new InputStreamReader(inputStream3));
        while ((reader3.readLine()) != null) {}
    }


    @SneakyThrows
    public static BestIpPort extractIpAndPort() {
        CSVReader csvReader = new CSVReader(new FileReader("result.csv"));
        String bestIpPort = Arrays.toString(csvReader.readAll().get(1));
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),");
        Matcher matcher = pattern.matcher(bestIpPort);
        matcher.find();
        return new BestIpPort(matcher.group(1),matcher.group(2));
    }

}