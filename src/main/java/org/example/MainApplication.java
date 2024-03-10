package org.example;

import au.com.bytecode.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApplication {

    @SneakyThrows
    public static void main(String[] args) {

//        extractAndCreateCSVFile();
//        System.out.println(setIpAndPortToJson(extractIpAndPort()));

//        extractAndPrintOutput();

        System.out.println(getJsonResponse());

    }

    @SneakyThrows
    public static void extractAndCreateCSVFile() {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "curl -fsSL https://raw.githubusercontent.com/Ptechgithub/warp/main/endip/install.sh | bash");
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
    public static BestIpPort extractIpAndPort() {
        CSVReader csvReader = new CSVReader(new FileReader("result.csv"));
        String bestIpPort = Arrays.toString(csvReader.readAll().get(1));
        Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),");
        Matcher matcher = pattern.matcher(bestIpPort);
        matcher.find();
        return new BestIpPort(matcher.group(1), Integer.valueOf(matcher.group(2)));
    }

    public static String setIpAndPortToJson(BestIpPort ipPort) {

        String firstJson = """
                  {
                  "outbounds":
                  [
                    {
                      "type": "wireguard",
                      "tag": "Warp-IR",
                      "local_address": [
                        "172.16.0.2/32",
                        "2606:4700:110:8c91:4063:21d0:7dd5:f218/128"
                      ],
                      "private_key": "CBVIIWvXdLr4PbSrnm11ZJJ300IiPudRD4R62/IxV1g=",
                      "server": "162.159.195.93",
                      "server_port": 2506,
                      "peer_public_key": "bmXOC+F1FxEMF9dyiK2H5/1SUtzH0JuVo51h2wPfgyo=",
                      "reserved": "AAAA",
                      "mtu": 1280,
                      "fake_packets": "5-10"
                    },
                    {
                      "type": "wireguard",
                      "tag": "Warp-Main",
                      "detour": "Warp-IR",
                      "local_address": [
                        "172.16.0.2/32",
                        "2606:4700:110:8c15:3f90:ad2d:8810:77f3/128"
                      ],
                      "private_key": "CCC/TQTc82ub9i8f37Rpix2v425Sv/mxTzvE/iKRMkw=",
                      "server": "162.159.195.93",
                      "server_port": 2506,
                      "peer_public_key": "bmXOC+F1FxEMF9dyiK2H5/1SUtzH0JuVo51h2wPfgyo=",
                      "reserved": "AAAA",
                      "mtu": 1280,
                      "fake_packets": "5-10"
                    }
                  ]
                }""";

        String ip = ipPort.getIp();
        int port = ipPort.getPort();

        JSONObject json = new JSONObject(firstJson);
        JSONArray outbounds = json.getJSONArray("outbounds");

        for (int i = 0; i < outbounds.length(); i++) {
            JSONObject outbound = outbounds.getJSONObject(i);
            outbound.put("server", ip);
            outbound.put("server_port", port);
        }

        String modifiedJsonString = json.toString();
        return modifiedJsonString;
    }

    @SneakyThrows
    public static void extractAndPrintOutput() {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "curl -sL \"https://api.zeroteam.top/warp?format=sing-box\" | grep -Eo --color=never '\"2606:4700:[0-9a-f:]+/128\"|\"private_key\":\"[0-9a-zA-Z\\/+=]+=\"|\"reserved\":\\[[0-9]+(,[0-9]+){2}\\]'");
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

    }


    @SneakyThrows(value = {IOException.class, InterruptedException.class})
    private static String getJsonResponse() {

        String url = "https://api.zeroteam.top/warp?format=sing-box";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/vnd.api+json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    }


}