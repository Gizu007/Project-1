package com.revature.dto;
import java.util.Objects;

public class ResponseReimbursementDTO {


    private int id;
    private double amount;
    private String submitTimestamp;
    private String description;
    private String firstName;
    private String lastName;
    private String type;
    private String status;
    private String urlDetails;

    public ResponseReimbursementDTO() {
    }

    public ResponseReimbursementDTO(int id, double amount, String submitTimestamp, String description, String firstName, String lastName, String status, String type, String urlDetails) {
        this.id = id;
        this.amount = amount;
        this.submitTimestamp = submitTimestamp;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.status = status;
        this.urlDetails = urlDetails;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUrlDetails() {
        return urlDetails;
    }

    public void setUrlDetails(String urlDetails) {
        this.urlDetails = urlDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseReimbursementDTO that = (ResponseReimbursementDTO) o;
        return id == that.id && Double.compare(that.amount, amount) == 0 && Objects.equals(submitTimestamp, that.submitTimestamp) && Objects.equals(description, that.description) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(type, that.type) && Objects.equals(status, that.status) && Objects.equals(urlDetails, that.urlDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, submitTimestamp, description, firstName, lastName, type, status, urlDetails);
    }

    @Override
    public String toString() {
        return "ResponseReimbursementDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", submitTimestamp='" + submitTimestamp + '\'' +
                ", description='" + description + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", urlDetail='" + urlDetails + '\'' +
                '}';
    }
}
