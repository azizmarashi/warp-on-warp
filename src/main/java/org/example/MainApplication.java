package org.example;

import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApplication {

    public static void main(String[] args) {

        //get warp on warp config string
        String finalConfigString = finalConfigString(outbounds());

        //print warp on warp config string on console
        System.out.println(finalConfigString);

        //write warp on warp config string on text file
        makeTextConfigFile(finalConfigString);

    }

    @SneakyThrows
    public static void extractAndCreateCSVFile() {
        downloadAndCreateBashFile();
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "install.sh");
        Process process = processBuilder.start();
        process.getOutputStream().write("1\n".getBytes());
        process.getOutputStream().flush();
        InputStream inputStream = process.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((reader.readLine()) != null) {
        }
        InputStream inputStream2 = process.getInputStream();
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));
        while ((reader2.readLine()) != null) {
        }
    }

    @SneakyThrows
    public static void downloadAndCreateBashFile(){
        String url = "https://raw.githubusercontent.com/Ptechgithub/warp/main/endip/install.sh";
        InputStream in = new URL(url).openStream();
        OutputStream out = new FileOutputStream("install.sh");
        in.transferTo(out);
        in.close();
        out.close();
    }

    @SneakyThrows
    public static BestIpPort extractIpAndPortFromCSVFile() {
        extractAndCreateCSVFile();
        CSVReader csvReader = new CSVReader(new FileReader("result.csv"));
        String bestIpPort = Arrays.toString(csvReader.readAll().get(1));
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),");
        Matcher matcher = pattern.matcher(bestIpPort);
        matcher.find();
        return new BestIpPort(matcher.group(1), Long.valueOf(matcher.group(2)));
    }

    @SneakyThrows//(value = {IOException.class, InterruptedException.class})
    private static String getSecondJson() {

        String url = "https://api.zeroteam.top/warp?format=sing-box";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/vnd.api+json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        String secondJson = null;
        boolean successConnect = false;
        while (!successConnect){
            try {
                secondJson = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
                successConnect = true;
            }
            catch (RuntimeException e){
            }
        }

        return secondJson;
    }

    public static PrivateKey getPrivateKey(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, PrivateKey.class);
    }

    public static List<SecondValue> getList2SecondValues() {

        PrivateKey privateKey1 = getPrivateKey(getSecondJson());
        PrivateKey privateKey2 = getPrivateKey(getSecondJson());

        SecondValue secondValue1 = new SecondValue();
        SecondValue secondValue2 = new SecondValue();

        secondValue1.setLocal_address(privateKey1.getLocal_address().get(1));
        secondValue1.setPrivate_key(privateKey1.getPrivate_key());
        secondValue1.setReserved(privateKey1.getReserved());

        secondValue2.setLocal_address(privateKey2.getLocal_address().get(1));
        secondValue2.setPrivate_key(privateKey2.getPrivate_key());
        secondValue2.setReserved(privateKey2.getReserved());

        return Arrays.asList(secondValue1, secondValue2);
    }

    public static List<Outbound> outbounds() {

        Outbound outbound1 = new Outbound();
        Outbound outbound2 = new Outbound();

        BestIpPort ipPort = extractIpAndPortFromCSVFile();

        //set best ip and port for both
        outbound1.setServer(ipPort.getIp());
        outbound1.setServer_port(ipPort.getPort());
        outbound2.setServer(ipPort.getIp());
        outbound2.setServer_port(ipPort.getPort());

        SecondValue secondValue1 = getList2SecondValues().get(0);
        SecondValue secondValue2 = getList2SecondValues().get(1);

        //set second values for outbound1
        outbound1.setLocal_address(Arrays.asList("172.16.0.2/32",secondValue1.getLocal_address()));
        outbound1.setPrivate_key(secondValue1.getPrivate_key());
        outbound1.setReserved(secondValue1.getReserved());

        //set second values for outbound2
        outbound2.setLocal_address(Arrays.asList("172.16.0.2/32",secondValue2.getLocal_address()));
        outbound2.setPrivate_key(secondValue2.getPrivate_key());
        outbound2.setReserved(secondValue2.getReserved());

        //set others data for outbound1
        outbound1.setType("wireguard");
        outbound1.setTag("Warp-IR");
        outbound1.setPeer_public_key("bmXOC+F1FxEMF9dyiK2H5/1SUtzH0JuVo51h2wPfgyo=");
        outbound1.setMtu(1280);
        outbound1.setFake_packets("5-10");

        //set others data for outbound2
        outbound2.setType("wireguard");
        outbound2.setTag("Warp-Main");
        outbound2.setPeer_public_key("bmXOC+F1FxEMF9dyiK2H5/1SUtzH0JuVo51h2wPfgyo=");
        outbound2.setMtu(1280);
        outbound2.setFake_packets("5-10");
        outbound2.setDetour("Warp-IR");

        return Arrays.asList(outbound1,outbound2);

    }

    @SneakyThrows
    public static String finalConfigString(List<Outbound> outbounds){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(outbounds);
        String finalJson = "{\"outbounds\":" + json + "}";
        return finalJson;
    }

    @SneakyThrows
    public static void makeTextConfigFile(String finalConfigString){
        BufferedWriter writer = new BufferedWriter(new FileWriter("warp_on_warp_config.txt"));
        writer.write(finalConfigString);
        writer.close();
    }

}