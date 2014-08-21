package edu.pitt.sis.paws.dbsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
 
public final class GoogleUrlShortener {
 
    public static String shorten(String longUrl) {
        if (longUrl == null) {
            return longUrl;
        }
        System.out.println(longUrl);

        try {
            URL url = new URL("http://goo.gl/api/url");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "toolbar");
 
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("url=" + URLEncoder.encode(longUrl, "UTF-8"));
            writer.close();
 
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            String json = sb.toString();
            System.out.println(json);
            return json.substring(json.indexOf("http"), json.indexOf("\"", json.indexOf("http")));
        } catch (MalformedURLException e) {
            return longUrl;
        } catch (IOException e) {
            return longUrl;
        }
    }
    
    public static void main(String[] args){
    	String newUrl = shorten("http://halley.exp.sis.pitt.edu/cn3/presentation2.php?conferenceID=115&presentationID=5114");
    	System.out.println(newUrl);
    }
 
}