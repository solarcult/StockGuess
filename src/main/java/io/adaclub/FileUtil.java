package io.adaclub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    public static String DELETE_SYMBOL=".sy";

    public static String getTimeString(){
        return sdf.format(Calendar.getInstance().getTime());

    }

    public static void write2disk(String filename,String whatiwant2records) {
        List<String> bestPutStockCodes = new ArrayList<>();
        try {
            String time = getTimeString();
            BufferedWriter out = new BufferedWriter(new FileWriter(filename+"_"+time+".sy",false));
            out.write("\n");
            out.newLine();
            out.write(whatiwant2records);
            out.newLine();
            out.flush();
            out.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearFolder(){
        File baseFile = new File(".");
        File[] files = baseFile.listFiles((dir, name) -> {
            return (name.contains(DELETE_SYMBOL));
        });
        assert files != null;
        Arrays.stream(files).forEach(file -> {
            System.out.println(file.getName() + " deleting...");
            file.delete();
        });
    }

    public static void main(String[] args){
        clearFolder();
    }
}
