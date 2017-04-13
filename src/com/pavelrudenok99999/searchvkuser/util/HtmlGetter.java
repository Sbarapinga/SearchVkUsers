package com.pavelrudenok99999.searchvkuser.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Pavel on 05.03.2017.
 */
public class HtmlGetter {
    public static String getByUrl(String stringUrl) {
        BufferedReader br = null;
        StringBuilder sb = null;

        try {
            URL url = new URL(stringUrl);
            URLConnection conn = url.openConnection();

            sb = new StringBuilder();
            String line;

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

//            try {
//                Thread.sleep(500L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return sb.toString();
    }
}