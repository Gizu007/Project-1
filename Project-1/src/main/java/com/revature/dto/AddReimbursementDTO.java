package com.revature.dto;
import io.javalin.http.UploadedFile;

import java.sql.Timestamp;
import java.util.Objects;

public class AddReimbursementDTO {

    private double reimbAmount;
    private Timestamp reimbSubmitted;
    private String reimbDescription;
    private UploadedFile reimbReceiptImage;
    private int reimbAuthor;
    private int reimbType;

    public AddReimbursementDTO(){

    }

    public AddReimbursementDTO(double reimbAmount, Timestamp reimbSubmitted, String reimbDescription,
                               UploadedFile reimbReceiptImage, int reimbAuthor, int reimbType) {
        this.reimbAmount = reimbAmount;
        this.reimbSubmitted = reimbSubmitted;
        this.reimbDescription = reimbDescription;
        this.reimbReceiptImage = reimbReceiptImage;
        this.reimbAuthor = reimbAuthor;
        this.reimbType = reimbType;
    }

    public double getReimbAmount() {
        return reimbAmount;
    }

    public void setReimbAmount(double reimbAmount) {
        this.reimbAmount = reimbAmount;
    }

    public Timestamp getReimbSubmitted() {
        return reimbSubmitted;
    }

    public void setReimbSubmitted(Timestamp reimbSubmitted) {
        this.reimbSubmitted = reimbSubmitted;
    }

    public String getReimbDescription() {
        return reimbDescription;
    }

    public void setReimbDescription(String reimbDescription) {
        this.reimbDescription = reimbDescription;
    }

    public UploadedFile getReimbReceiptImage() {
        return reimbReceiptImage;
    }

    public void setReimbReceiptImage(UploadedFile reimbReceiptImage) {
        this.reimbReceiptImage = reimbReceiptImage;
    }

    public int getReimbAuthor() {
        return reimbAuthor;
    }

    public void setReimbAuthor(int reimbAuthor) {
        this.reimbAuthor = reimbAuthor;
    }

    public int getReimbType() {
        return reimbType;
    }

    public void setReimbType(int reimbType) {
        this.reimbType = reimbType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddReimbursementDTO that = (AddReimbursementDTO) o;
        return Double.compare(that.reimbAmount, reimbAmount) == 0 && reimbAuthor == that.reimbAuthor && reimbType == that.reimbType && Objects.equals(reimbSubmitted, that.reimbSubmitted) && Objects.equals(reimbDescription, that.reimbDescription) && Objects.equals(reimbReceiptImage, that.reimbReceiptImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimbAmount, reimbSubmitted, reimbDescription, reimbReceiptImage, reimbAuthor, reimbType);
    }

    @Override
    public String toString() {
        return "AddReimbursementDTO{" +
                "reimbAmount=" + reimbAmount +
                ", reimbSubmitted='" + reimbSubmitted + '\'' +
                ", reimbDescription='" + reimbDescription + '\'' +
                ", reimbReceiptImage=" + reimbReceiptImage +
                ", reimbAuthor=" + reimbAuthor +
                ", reimbType=" + reimbType +
                '}';
    }
}
