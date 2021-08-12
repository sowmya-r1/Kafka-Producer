package com.app.util;

public class TimestampProvider {

        public static java.sql.Timestamp getTimestamp(){
            return new java.sql.Timestamp(System.currentTimeMillis());
        }

}
