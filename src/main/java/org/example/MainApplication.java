package org.example;

import au.com.bytecode.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

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

        System.out.println(setIpAndPortToJson(extractIpAndPort()));


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
        return new BestIpPort(matcher.group(1),Integer.valueOf(matcher.group(2)));
    }






    public static String setIpAndPortToJson(BestIpPort ipPort){

        String firstJson = """
                  {
                  "outbounds":\s
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

}