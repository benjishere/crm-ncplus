package com.igorbrodevic.data;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Customer1 implements Serializable {

    public Customer1() {
    }

    public Customer1(String fistName, String lastName, String street, String city, LocalDate contractSignedDate,
                     LocalDate contractEndDate, boolean isDomesticClient, LocalDate lastContactDate, CustomerPackage customerPackage,
                     CustomerPackage potentialPackage, LocalDate plannedContactDate) {
        this.firstName = fistName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.contractSignedDate = contractSignedDate;
        this.contractEndDate = contractEndDate;
        this.isDomesticClient = isDomesticClient;
        this.lastContactDate = lastContactDate;
        this.customerPackage = customerPackage;
        this.potentialPackage = potentialPackage;
        this.plannedContactDate = plannedContactDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName = "";
    private String lastName = "";
    private String street = "";
    private String city = "";
    private LocalDate contractSignedDate;
    private LocalDate contractEndDate;
    private boolean isDomesticClient;
    private LocalDate lastContactDate;
    private CustomerPackage customerPackage;
    private CustomerPackage potentialPackage;
    private LocalDate plannedContactDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getContractSignedDate() {
        return contractSignedDate;
    }

    public void setContractSignedDate(LocalDate contractSignedDate) {
        this.contractSignedDate = contractSignedDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public boolean isDomesticClient() {
        return isDomesticClient;
    }

    public void setDomesticClient(boolean domesticClient) {
        isDomesticClient = domesticClient;
    }

    public LocalDate getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(LocalDate lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    public CustomerPackage getCustomerPackage() {
        return customerPackage;
    }

    public void setCustomerPackage(CustomerPackage customerPackage) {
        this.customerPackage = customerPackage;
    }

    public CustomerPackage getPotentialPackage() {
        return potentialPackage;
    }

    public void setPotentialPackage(CustomerPackage potentialPackage) {
        this.potentialPackage = potentialPackage;
    }

    public LocalDate getPlannedContactDate() {
        return plannedContactDate;
    }

    public void setPlannedContactDate(LocalDate plannedContactDate) {
        this.plannedContactDate = plannedContactDate;
    }
}
