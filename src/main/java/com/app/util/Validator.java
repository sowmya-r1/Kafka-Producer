package com.app.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Validator {
    public static List<Integer> errorIndices = new ArrayList<Integer>();

    public static List<Integer> validateBrandName(List<String> brandNameList) {
        ListIterator<String> brandNameItr = brandNameList.listIterator();
        Timestamp timestamp;
        ErrorLogger errLogger = new ErrorLogger();
        timestamp = TimestampProvider.getTimestamp();
        errLogger.writeLogToFile("["+timestamp+"]:  "+"Validating column: brand_name.\n");

        while (brandNameItr.hasNext()) {
            if (brandNameItr.next().equals("")) {
                errorIndices.add((brandNameItr.nextIndex() - 1));
                timestamp = TimestampProvider.getTimestamp();
                errLogger.writeLogToFile("["+timestamp+"]:  "+"(Error in row " + (brandNameItr.nextIndex() + 1) + ") field should not be empty\n");
            }
        }
        return errorIndices;
    }

    public static List<Integer> getErrorIndices() {
        return errorIndices;
    }
}