package com.revature.dto;

import java.util.Objects;

public class UpdateReimbursementDTO {
    private double amount;
    private String description;
    private String receiptUrl;
    private int type;

    public UpdateReimbursementDTO() {
    }

    public UpdateReimbursementDTO(double amount, String description, String receiptUrl, int type) {
        this.amount = amount;
        this.description = description;
        this.receiptUrl = receiptUrl;
        this.type = type;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReimbursementDTO that = (UpdateReimbursementDTO) o;
        return Double.compare(that.amount, amount) == 0 && type == that.type && Objects.equals(description, that.description) && Objects.equals(receiptUrl, that.receiptUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, description, receiptUrl, type);
    }

    @Override
    public String toString() {
        return "UpdateReimbursementDTO{" +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
