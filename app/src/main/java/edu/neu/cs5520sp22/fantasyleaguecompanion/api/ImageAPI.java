package edu.neu.cs5520sp22.fantasyleaguecompanion.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAPI {

    public static Bitmap getImage(String url){
        try {
            return getBitMapFromThread(url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap(0,0, Bitmap.Config.ALPHA_8);
    }

    private static Bitmap getBitMapFromThread(String url) throws InterruptedException {
        imageThread thread =
                new imageThread(url);
        Thread t = new Thread(thread);
        t.start();
        t.join();
        return thread.getResults();
    }

    static class imageThread implements Runnable{

        private final String stringUrl;
        private Bitmap results;

        public imageThread(String stringUrl){
            this.stringUrl = stringUrl;
        }

        private Bitmap getLogo(){
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoInput(true);
                httpConnection.connect();
                Bitmap image = BitmapFactory.decodeStream(httpConnection.getInputStream());
                httpConnection.getInputStream().close();
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void run() {
            results = getLogo();
        }

        Bitmap getResults(){
            return results;
        }
    }
}
