package org.example;

import lombok.SneakyThrows;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainApplication {

    @SneakyThrows
    public static void main(String[] args) {

        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "curl -fsSL https://raw.githubusercontent.com/Ptechgithub/warp/main/endip/install.sh | bash");
        Process process = processBuilder.start();

        process.getOutputStream().write("1\n".getBytes());
        process.getOutputStream().flush();

        InputStream inputStream2 = process.getErrorStream();
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));
        String line2;
        while ((line2 = reader2.readLine()) != null) {
            System.out.println(line2);
        }

        InputStream inputStream3 = process.getInputStream();
        BufferedReader reader3 = new BufferedReader(new InputStreamReader(inputStream3));
        String line3;
        while ((line3 = reader3.readLine()) != null) {
            System.out.println(line3);
        }

    }

}