package com.revature.model;

import java.util.Objects;

public class Reimbursement {
    private int id;
    private double amount;
    private String submitTimestamp;
    private String resolveTimestamp;
    private String description;
    private String receiptUrl;
    private String firstName;
    private String lastName;
    private String email;
    private int resolverId;
    private String type;
    private String status;

    public Reimbursement() {

    }

    public Reimbursement(int id, double amount, String submitTimestamp, String resolveTimestamp, String description, String receiptUrl, String firstName,
                         String lastName, String email, int resolverId, String type, String status) {
        this.id = id;
        this.amount = amount;
        this.submitTimestamp = submitTimestamp;
        this.resolveTimestamp = resolveTimestamp;
        this.description = description;
        this.receiptUrl = receiptUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.resolverId = resolverId;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSubmitTimestamp() {
        return submitTimestamp;
    }

    public void setSubmitTimestamp(String submitTimestamp) {
        this.submitTimestamp = submitTimestamp;
    }

    public String getResolveTimestamp() {
        return resolveTimestamp;
    }

    public void setResolveTimestamp(String resolveTimestamp) {
        this.resolveTimestamp = resolveTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getResolverId() {
        return resolverId;
    }

    public void setResolverId(int resolverId) {
        this.resolverId = resolverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursement that = (Reimbursement) o;
        return id == that.id && Double.compare(that.amount, amount) == 0 && resolverId == that.resolverId && Objects.equals(submitTimestamp, that.submitTimestamp) && Objects.equals(resolveTimestamp, that.resolveTimestamp) && Objects.equals(description, that.description) && Objects.equals(receiptUrl, that.receiptUrl) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(type, that.type) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, submitTimestamp, resolveTimestamp, description, receiptUrl, firstName, lastName, email, resolverId, type, status);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "id=" + id +
                ", amount=" + amount +
                ", submitTimestamp='" + submitTimestamp + '\'' +
                ", resolveTimestamp='" + resolveTimestamp + '\'' +
                ", description='" + description + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", resolverId=" + resolverId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
