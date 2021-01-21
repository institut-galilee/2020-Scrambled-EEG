/**
 *  File Management
 */

package com.scrambled_eeg;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileWriter {

    private File file;

    public FileWriter(String name){

        file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + name);
        try{

            file.createNewFile();


        } catch (Exception e) {e.printStackTrace();}

    }

    public File getFile() {
        return file;
    }

    public boolean writeIn(byte[] b, int length) throws FileNotFoundException {

        if(file.exists()){

            OutputStream fo = new FileOutputStream(file, true);

            try {
                fo.write(b);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;

    }

    public boolean delFile(){

        if(file!=null){

            file.delete();
            if(file.exists()){
                return false;
            }

        }

        return true;
    }
}
