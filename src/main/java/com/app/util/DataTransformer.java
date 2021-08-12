package com.app.util;

import com.app.model.HealthData;
import com.app.producer.KafkaProducer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


public class DataTransformer {
    public static ArrayList<CSVRecord> getValidatedRecords(String filePath, List<Integer> errorIndices) throws FileNotFoundException {

        ArrayList<CSVRecord> validatedRecordList = new ArrayList<CSVRecord>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<CSVRecord> records = csvParser.getRecords();
            ListIterator<CSVRecord> itr = records.listIterator();
            while (itr.hasNext()) {
                if (!errorIndices.contains(itr.nextIndex())) {
                    validatedRecordList.add(itr.next());
                } else {
                    itr.next();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validatedRecordList;
    }

    public static ArrayList<String> getKafkaData(ArrayList<CSVRecord> validatedRecordList) {

        ArrayList<String> jsonKafkaData = new ArrayList<String>();
        String jsonString = "";
        for (CSVRecord record : validatedRecordList) {
            HealthData data = new HealthData(record.get("year"), record.get("brand_name"), record.get("generic_name"), record.get("coverage_type"), record.get("total_spending"), record.get("serialid"));
            jsonString = Convertor.convertCsvRecordToJsonString(data);
            jsonKafkaData.add(jsonString);

        }
        return jsonKafkaData;
    }

    public static void main(String[] args) throws IOException {

        FileExtensionExtractor extnExtractor = new FileExtensionExtractor();
        ErrorLogger errLogger = new ErrorLogger();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file path:");
        String filePath = scanner.nextLine();

        Timestamp timestamp;
        String extension = extnExtractor.getFileExtension(filePath);
        timestamp = TimestampProvider.getTimestamp();
        errLogger.writeLogToFile("["+timestamp+"]:  "+"Input file found to be of type:" + extension + "\n");

        List<Integer> errorIndices;

        String log;


        switch (extension) {
            case "csv":
                CsvReader.readCsvFile(filePath);
                errorIndices = Validator.getErrorIndices();
                ArrayList<CSVRecord> validatedRecordList = getValidatedRecords(filePath, errorIndices);
                timestamp = TimestampProvider.getTimestamp();
                errLogger.writeLogToFile("["+timestamp+"]:  "+"Filtered out the validated data \n");
                ArrayList<String> jsonKafkaData = getKafkaData(validatedRecordList);
                KafkaProducer.writeToKafkaTopic(jsonKafkaData);
                timestamp = new Timestamp(System.currentTimeMillis());
                timestamp = TimestampProvider.getTimestamp();
                errLogger.writeLogToFile("["+timestamp+"]:  "+"Data written to kafka topic. \n");
                break;
            default:
                System.out.println("Invalid file type");
                timestamp = TimestampProvider.getTimestamp();
                errLogger.writeLogToFile("["+timestamp+"]:  "+"Invalid file type:" + extension + "\n");
        }


    }

}

//C:\\Users\\sr79\\Downloads\\health-data.csv