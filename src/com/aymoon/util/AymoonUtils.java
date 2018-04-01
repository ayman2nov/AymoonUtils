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
import java.io.FileWriter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Ayman Mohammed
 */
public class AymoonUtils {

    private final String USER_AGENT = "Mozilla/5.0";

    public String getData(String address, int timeout) throws Exception {
        URL page = new URL(address);
        StringBuffer text = new StringBuffer();
        HttpURLConnection conn = (HttpURLConnection) page.openConnection();
        if (timeout > 0) {
            conn.setConnectTimeout(timeout);
        }
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
        Matcher m = Pattern.compile(pat, Pattern.DOTALL)
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

    public String sendGet(String url, HashMap<String, String> headers) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                con.setRequestProperty(key, value);
            }
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
    public String sendPost(String url, HashMap<String, String> headers, String urlParameters) throws Exception {

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
//        System.err.println("\nSending 'POST' request to URL : " + url);
//        System.err.println("Post parameters : " + urlParameters);
//        System.err.println("Response Code : " + responseCode);

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

    public void printAllFromFile(String filePath, String pattern, int groupNumber) {
        String content = readfile(filePath);
        List<String> foundList = getall(content, pattern, groupNumber);
        for (int i = 0; i < foundList.size(); i++) {
            String value = foundList.get(i);
            System.out.println(value);
        }
    }

    public static void writeToFile(String path, String data, boolean append) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), append);
            bw = new BufferedWriter(fw);
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String mergeString(List<String> keywords, String separator) {
        String[] list = (String[]) keywords.toArray();
        String joined = String.join(",", list);
        return joined;
    }

    void downloadFromUrl(URL url, String localFilename) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URLConnection urlConn = url.openConnection();//connect

            is = urlConn.getInputStream();               //get connection inputstream
            fos = new FileOutputStream(localFilename);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static List<String> generateAllPossibleStrings(String start, String end) {
        if (start == null || end == null) {
            return null;
        }
        if (start.length() != end.length()) {
            return null;
        }
        int n = start.length();
        List<String> variants = new ArrayList<>();
        char startArray[] = start.toCharArray();
        char endArray[] = end.toCharArray();
        char currentArray[] = Arrays.copyOf(startArray, startArray.length);
        variants.add(new String(currentArray));

        //We check if the start string is really above the end string as specified
        //We output an empty string if it is not the case
        boolean possible = true;
        for (int i = 0; i < n; i++) {
            possible = possible && (startArray[i] <= endArray[i]);
        }
        if (!possible) {
            return variants;
        }

        while (!end.equals(new String(currentArray))) {
            currentArray[n - 1] += 1;
            int i = n - 1;
            while (currentArray[i] > endArray[i]) {
                currentArray[i] = startArray[i];
                i--;
                currentArray[i]++;
            }
            variants.add(new String(currentArray));
        }

        System.out.println(Arrays.toString(variants.toArray()));
        return variants;
    }
    public String sendBurpRequest(String path,String protocol,  String replacer)
    {
        HashMap<String, String> headers = new HashMap<>();
        String method = "";
        String URI = "";
        String host = "";
        String fullURL = "";
        int closeLineNo = 1000;
        String postVal = "";
        String txt = readfile(path);
        String[] lines = txt.trim().split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = line.replaceAll("AYREP", replacer);
            if (method.isEmpty() && line.startsWith("POST ")) {
                method = "POST";
                URI = line.split(" ")[1];
            }
            if (line.startsWith("Host:")) {
                host = line.split(" ")[1];
                fullURL = protocol + "://" + host + URI;
            }
            if (line.equalsIgnoreCase("Connection: close")) {
                closeLineNo = i;
            }
            if (line.contains(": ") && !line.equalsIgnoreCase("Connection: close")) {
                String[] kv = line.split(": ");
                headers.put(kv[0], kv[1]);
            }
            if (i > closeLineNo) {
                postVal += line + "\n";
            }
        }
        postVal = postVal.trim();
//        System.out.println("Method " + method);
//        System.out.println("Full URL " + fullURL);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
//            System.out.println(key + " = " + value);
        }
//        System.out.println("postVal " + postVal);
        
        try {
            String resp = sendPost(fullURL, headers, postVal);
            return resp;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
