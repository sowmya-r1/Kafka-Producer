package com.app.model;

public class HealthData {
    private String year;
    private String brandName;
    private String genericName;
    private String coverageType;
    private String totalSpending;
    private String serialId;

    public HealthData() {

    }

    public HealthData(String year, String brandName, String genericName, String coverageType, String totalSpending, String serialId) {
        this.year = year;
        this.brandName = brandName;
        this.genericName = genericName;
        this.coverageType = coverageType;
        this.totalSpending = totalSpending;
        this.serialId = serialId;
    }
    public String getBrandName() {
        return brandName;
    }
    public String getYear() {
        return year;
    }
    public String getGenericName() {
        return genericName;
    }
    public String getCoverageType(){
        return coverageType;
    }
    public String getTotalSpending(){
        return totalSpending;
    }
    public String getserialId(){
        return serialId;
    }

}