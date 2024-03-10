package org.example;

import au.com.bytecode.opencsv.CSVReader;
import lombok.SneakyThrows;
import java.io.FileReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApplication {

    @SneakyThrows
    public static void main(String[] args) {

        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "curl -fsSL https://raw.githubusercontent.com/Ptechgithub/warp/main/endip/install.sh | bash");
        Process process = processBuilder.start();
        process.getOutputStream().write("1\n".getBytes());
        process.getOutputStream().flush();

        BestIpPort bestIpPort = extractBestIpAndPortFromCSVFile();
        System.out.println(bestIpPort);
    }

    @SneakyThrows
    public static BestIpPort extractBestIpAndPortFromCSVFile() {
        String csvFilePath = "result.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        String bestIpAndPort = Arrays.toString(csvReader.readAll().get(1));
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),");
        Matcher matcher = pattern.matcher(bestIpAndPort);
        matcher.find();
        return new BestIpPort(matcher.group(1),matcher.group(2));
    }

}