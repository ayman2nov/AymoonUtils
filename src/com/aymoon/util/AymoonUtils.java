/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aymoon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Ayman Mohammed
 */
public class AymoonUtils {

    private final String USER_AGENT = "Mozilla/5.0";

    public String getData(String address) throws Exception {
        URL page = new URL(address);
        StringBuffer text = new StringBuffer();
        HttpURLConnection conn = (HttpURLConnection) page.openConnection();
        conn.connect();
        InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
        BufferedReader buff = new BufferedReader(in);
        String line;
        do {
            line = buff.readLine();
            if (line != null) {
                text.append(line + "\n");
            }
        } while (line != null);
        return text.toString();
    }

    public String sendRequest(String endPoint, String method, ArrayList<HashMap<String, String>> headers, String body) throws Exception {
        URL url = new URL(endPoint);
        StringBuffer text = new StringBuffer();

        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Cookie", "name=value");
        con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        con.connect();

        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        out.write(body);
        out.close();

        InputStreamReader in = new InputStreamReader((InputStream) con.getContent());
        BufferedReader buff = new BufferedReader(in);
        String line;
        do {
            line = buff.readLine();
            if (line != null) {
                text.append(line + "\n");
            }
        } while (line != null);
        return text.toString();
    }

    public String readfile(String path) {
        String output = "";
        try {
            File fileDir = new File(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                output = output + str + "\n";
            }
            in.close();
        } catch (Exception ex) {
        }
        return output;
    }

    public void writeFile(String filePath, String content) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "utf-8"))) {
            writer.write(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getall(String mystring, String pat, int gid) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(pat,Pattern.DOTALL)
                .matcher(mystring);
        while (m.find()) {
            allMatches.add(m.group(gid));
        }
        return allMatches;
    }

    public ArrayList<String> removeDuplicates(ArrayList<String> list, boolean removeEmpty) {
        ArrayList<String> refined = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String element = list.get(i);
            if (!refined.contains(element)) {
                if (!(removeEmpty && element.trim().isEmpty())) {
                    refined.add(element);
                }
            }
        }
        return refined;
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0) {
                return true;
            }
            i += Character.charCount(c);
        }
        return false;
    }

    public void CreatePathStructure(String dir) {
        Path path = Paths.get(dir);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Cannot create directories - " + e);
        }
    }

    public String generateRandom(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(length);
    }

    private String sendGet(String url, HashMap<String, String> headers) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            con.setRequestProperty(key, value);
        }

        int responseCode = con.getResponseCode();
        System.err.println("\nSending 'GET' request to URL : " + url);
        System.err.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

    // HTTP POST request
    private String sendPost(String url, HashMap<String, String> headers, String urlParameters) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            con.setRequestProperty(key, value);
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.err.println("\nSending 'POST' request to URL : " + url);
        System.err.println("Post parameters : " + urlParameters);
        System.err.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }
    public void printAllFromFile(String filePath ,String pattern , int groupNumber)
    {
        String content = readfile(filePath);
        List<String> foundList = getall(content, pattern, groupNumber);
        for (int i = 0; i < foundList.size(); i++) {
            String value = foundList.get(i);
            System.out.println(value);
        }
    }
}
