/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aymoon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ayman Mohammed
 */
public class AymoonUtils {

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
    
    public String sendRequest(String endPoint , String method, ArrayList<HashMap<String,String>> headers , String body) throws Exception {
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
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pat)
                .matcher(mystring);
        while (m.find()) {
            allMatches.add(m.group(gid));
        }
        return allMatches;
    }

    public Vector<String> removeDuplicates(Vector<String> list, boolean removeEmpty) {
        Vector<String> refined = new Vector<String>();
        for (int i = 0; i < list.size(); i++) {
            String element = list.elementAt(i);
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
}
