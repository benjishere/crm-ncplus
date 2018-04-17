package com.igorbrodevic.data;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Customer1 implements Serializable {

    public Customer1(String name) {;
        this.firstName = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName = "";
    private String lastName = "";
    private String street = "";
    private String city = "";
    private Date contractSignedDate;
    private Date contractEndDate;
    private boolean isDomesticClient;
    private Date lastContactDate;
    private CustomerPackage customerPackage;
    private CustomerPackage potentialPackage;
    private Date plannedContactDate;

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

    public Date getContractSignedDate() {
        return contractSignedDate;
    }

    public void setContractSignedDate(Date contractSignedDate) {
        this.contractSignedDate = contractSignedDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public boolean isDomesticClient() {
        return isDomesticClient;
    }

    public void setDomesticClient(boolean domesticClient) {
        isDomesticClient = domesticClient;
    }

    public Date getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(Date lastContactDate) {
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

    public Date getPlannedContactDate() {
        return plannedContactDate;
    }

    public void setPlannedContactDate(Date plannedContactDate) {
        this.plannedContactDate = plannedContactDate;
    }
}
