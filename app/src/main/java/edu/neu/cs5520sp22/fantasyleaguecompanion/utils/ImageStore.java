package edu.neu.cs5520sp22.fantasyleaguecompanion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import edu.neu.cs5520sp22.fantasyleaguecompanion.api.ImageAPI;

public class ImageStore {

    private static final String avatarUrl = "https://sleepercdn.com/avatars";
    private static final String playerAvatarUrl = "https://sleepercdn.com/content/nfl/players";
    private static final String teamAvatarURl = "https://sleepercdn.com/images/team_logos/nfl";

    private static boolean saveImage(Context context, Bitmap image, String type, String fileName){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(context, type, fileName));
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static File createFile(Context context, String type, String fileName) {
        File directory;
        directory = context.getDir(type + "_image", Context.MODE_PRIVATE);
        if(!directory.exists() && !directory.mkdirs()){
            Log.e("ImageSaver","Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }

    public static Bitmap loadUserImage(Context context, String fileName){
        return ImageStore.loadImage(context, "user", fileName);
    }

    public static Bitmap loadPlayerImage(Context context, String fileName){
        return ImageStore.loadImage(context, "player", fileName);
    }

    public static Bitmap loadTeamImage(Context context, String fileName){
        return ImageStore.loadImage(context, "team", fileName);
    }

    public static Bitmap loadImage(Context context, String fileName){
        return ImageStore.loadImage(context, "other", fileName);
    }

    private static Bitmap loadImage(Context context, String type, String fileName){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(context, type, fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.d("image does not exist", fileName);
            Bitmap image;
            switch (type) {
                case "player":
                    if (fileName.equals("0")) {
                        image = getDefaultPlayer();
                    } else {
                        image = getPlayerAvatar(fileName);
                    }
                    break;
                case "team":
                    image = getTeamAvatar(fileName);
                    break;
                case "user":
                    image = getUserAvatar(fileName);
                    break;
                default:
                    image = ImageAPI.getImage(fileName);
                    break;
            }
            if(image != null) {
                saveImage(context, image, type, fileName);
                return image;
            }
            else{
                return ImageStore.loadPlayerImage(context, "0");
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e("image io error", Arrays.toString(e.getStackTrace()));
            }
        }
    }

    // Use Image Store instead
    private static Bitmap getDefaultPlayer(){
        return ImageAPI.getImage("https://sleepercdn.com/images/v2/icons/player_default.webp");
    }

    // Use Image Store instead
    private static Bitmap getPlayerAvatar(String id){
        return ImageAPI.getImage(playerAvatarUrl + "/thumb/" + id + ".jpg");
    }

    // Use Image Store instead
    private static Bitmap getTeamAvatar(String id){
        return ImageAPI.getImage(teamAvatarURl + "/" + id.toLowerCase(Locale.ROOT) + ".png");
    }

    // Use Image Store instead
    private static Bitmap getUserAvatar(String id){
        return ImageAPI.getImage(avatarUrl + "/thumbs/" + id);
    }

}