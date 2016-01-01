/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aymoon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> getall(String mystring, String pat, int gid) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pat)
                .matcher(mystring);
        while (m.find()) {
            allMatches.add(m.group(gid));
        }
        return allMatches;
    }
}
