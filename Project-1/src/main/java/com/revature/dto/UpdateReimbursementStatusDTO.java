package com.revature.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class UpdateReimbursementStatusDTO {

    private int resolverId;
    private int statusId;
    private Timestamp Timestamp;

    public UpdateReimbursementStatusDTO() {
    }

    public UpdateReimbursementStatusDTO(int resolverId, int statusId, java.sql.Timestamp timestamp) {
        this.resolverId = resolverId;
        this.statusId = statusId;
        Timestamp = timestamp;
    }

    public int getResolverId() {
        return resolverId;
    }

    public void setResolverId(int resolverId) {
        this.resolverId = resolverId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public java.sql.Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        Timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateReimbursementStatusDTO that = (UpdateReimbursementStatusDTO) o;
        return resolverId == that.resolverId && statusId == that.statusId && Objects.equals(Timestamp, that.Timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolverId, statusId, Timestamp);
    }

    @Override
    public String toString() {
        return "UpdateReimbursementStatusDTO{" +
                "resolverId=" + resolverId +
                ", statusId=" + statusId +
                ", Timestamp=" + Timestamp +
                '}';
    }
}