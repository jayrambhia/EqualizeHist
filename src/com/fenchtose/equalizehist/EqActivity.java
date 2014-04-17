package com.fenchtose.equalizehist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

public class EqActivity extends Activity{

	public native int eqhist(int width, int height, int [] mPhotoIntArray, int [] mCannyOutArray);

    static 
    {
        System.loadLibrary("native_activity");
        Log.i("EqActivity", "native library loaded successfully");
    }
    /** Called when the activity is first created. */ 
    ImageView imageview_1;
    ImageView imageview_2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);
         imageview_1=(ImageView) findViewById(R.id.imageView1);
         imageview_2=(ImageView) findViewById(R.id.imageView2);

        InputStream is;
        is = this.getResources().openRawResource(R.drawable.foot);
        Bitmap bmInImg = BitmapFactory.decodeStream(is);

        int [] mPhotoIntArray;
        int [] mCannyOutArray;

        mPhotoIntArray = new int[bmInImg.getWidth() * bmInImg.getHeight()];
        imageview_1.setImageBitmap(bmInImg);
        // Copy pixel data from the Bitmap into the 'intArray' array
        bmInImg.getPixels(mPhotoIntArray, 0, bmInImg.getWidth(), 0, 0, bmInImg.getWidth(), bmInImg.getHeight());

        //create the Brightness result buffer
        mCannyOutArray = new int[bmInImg.getWidth() * bmInImg.getHeight()];

        //
        // Do Brightness
        //
        eqhist(bmInImg.getHeight(), bmInImg.getWidth(), mPhotoIntArray, mCannyOutArray);

        //
        // Convert the result to Bitmap
        //
        Bitmap bmOutImg = Bitmap.createBitmap(bmInImg.getWidth(), bmInImg.getHeight(), Config.ARGB_8888);  
        bmOutImg.setPixels(mCannyOutArray, 0, bmInImg.getWidth(), 0, 0, bmInImg.getWidth(), bmInImg.getHeight());


        imageview_2.setImageBitmap(bmOutImg);

        //
        // Save the result to file
        //
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String outFileName = extStorageDirectory + "/Exposure/footresult.png";

        OutputBitmapToFile(bmOutImg, outFileName);





    }

    void OutputBitmapToFile(Bitmap InBm, String Filename)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        InBm.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File f = new File(Filename);
        try
        {
            f.createNewFile();
            //write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }           
    }
}
