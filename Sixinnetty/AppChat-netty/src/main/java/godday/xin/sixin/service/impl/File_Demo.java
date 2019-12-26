package godday.xin.sixin.service.impl;

import godday.xin.sixin.Application;

import java.io.File;

public class File_Demo {

    public static void main(String[] args) {
        String path= Application.class.getClassLoader().getResource("").getPath();
        String rel_path=path+"111/222.png";
        File file = new File(rel_path);
        if(!file.exists()){
            file.mkdirs();
        }
        System.out.println("success!!!");
    }
}
