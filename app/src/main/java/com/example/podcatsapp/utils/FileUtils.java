package com.example.podcatsapp.utils;

package com.example.podcatsapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static File createTempFileFromBitmap(Context context, Bitmap bitmap, String filename) {
        File file = new File(context.getCacheDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e){

        }

        return file;
    }
}
