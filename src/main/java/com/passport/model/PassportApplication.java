package com.passport.model;

import java.time.LocalDate;

public class PassportApplication {
    private int applicationId;
    private int userId;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private String nationality;
    private String address;
    private String applicationType;

    public PassportApplication() {}

    public PassportApplication(int userId, String passportNumber, LocalDate dateOfBirth,
                               String nationality, String address, String applicationType) {
        this.userId = userId;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.address = address;
        this.applicationType = applicationType;
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getApplicationType() { return applicationType; }
    public void setApplicationType(String applicationType) { this.applicationType = applicationType; }
}
