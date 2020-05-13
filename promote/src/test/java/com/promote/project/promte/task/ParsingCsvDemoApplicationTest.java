package com.promote.project.promte.task;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Iterator;

class ParsingCsvDemoApplicationTest {

    @Test
    void parsCsv() throws IOException {
        Reader reader = new FileReader("d:/input.csv");
//        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withDelimiter(',').withQuote('"').withRecordSeparator("\r\n").withIgnoreEmptyLines(true));
        final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withDelimiter(',').withQuote('"').withRecordSeparator("\r\n").withIgnoreEmptyLines(true));

        for (CSVRecord record : parser) {
            System.out.println(record.toString());
            System.out.println(record.get("pick"));
//            for(Iterator<String> iterator = record.iterator(); iterator.hasNext();){
//                System.out.println(iterator.next());
//            }
        }
//        File file = new File( true ? "d:\\hotel_whitelist.csv" : "d:\\store_whitelist.csv");
//        if (!file.exists()){
//            file.createNewFile();
//        }
//        CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(file,true),CSVFormat.DEFAULT.withDelimiter(',').withQuote('"').withRecordSeparator("\r\n").withIgnoreEmptyLines(true));
//        csvPrinter.print("111");
//        csvPrinter.print("222");
//        csvPrinter.println();
//        csvPrinter.flush();
//        csvPrinter.close();
    }

}
