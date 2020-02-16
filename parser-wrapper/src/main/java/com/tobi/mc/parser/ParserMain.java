package com.tobi.mc.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParserMain {

    public static void main(String[] args) {
        List<File> scanPaths = new ArrayList<>();
        String[] split = System.getProperty("java.library.path").split(":");
        for(String path : split) {
            if(path.isEmpty()) continue;
            scanPaths.add(new File(path));
        }
        scanPaths.add(new File("").getAbsoluteFile());

        String libName = System.mapLibraryName("parser");
        for (File scanPath : scanPaths) {
            File library = new File(scanPath, libName);
            System.out.println(library.getAbsolutePath());
            if(library.exists()) {
                System.load(library.getAbsolutePath());
                break;
            }
        }

        Node node = new Parser().parseProgram();
        if(node == null) {
            System.exit(1);
            return;
        }
        System.out.println("//RESULT IS: " + node);
    }
}
