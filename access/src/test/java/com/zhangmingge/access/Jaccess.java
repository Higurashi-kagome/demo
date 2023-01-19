package com.zhangmingge.access;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.impl.CodecProvider;

import java.io.File;
import java.io.IOException;


public class Jaccess {
    public static void main(String[] args) throws IOException {
        Table table = DatabaseBuilder.open(new File("C:\\Users\\liuhao\\Desktop\\all_num.mdb")).getTable("all_num");
        for(Row row : table) {
            System.out.println("Column '日次' has value: " + row.get("日次"));
        }
    }
}
