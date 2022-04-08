package com.revature.dao;

import com.revature.dto.AddReimbursementDTO;
import com.revature.dto.ResponseReimbursementDTO;
import com.revature.dto.UpdateReimbursementDTO;
import com.revature.dto.UpdateReimbursementStatusDTO;
import com.revature.model.*;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReimbursementDao {

    public ResponseReimbursementDTO addReimbursement(AddReimbursementDTO addReimbursementDTO, String receiptUrl) throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "insert into ERS_Reimbursement (Reimb_Amount, Reimb_Submitted, Reimb_Description, Reimb_Receipt, Reimb_Author, Reimb_Type_Id) " +
                    "values " +
                    "(?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setDouble(1, addReimbursementDTO.getReimbAmount());
            pstmt.setTimestamp(2, addReimbursementDTO.getReimbSubmitted());
            pstmt.setString(3, addReimbursementDTO.getReimbDescription());
            pstmt.setString(4, receiptUrl);
            pstmt.setInt(5, addReimbursementDTO.getReimbAuthor());
            pstmt.setInt(6, addReimbursementDTO.getReimbType());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();
            int reimbId = rs.getInt(1);

            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE  t.Reimb_Id = ?";

            PreparedStatement pstmtSelect = con.prepareStatement(sqlSelect);
            pstmtSelect.setInt(1, reimbId);

            ResultSet rsNewReimb = pstmtSelect.executeQuery();
            rsNewReimb.next();

            int id = rsNewReimb.getInt("Reimb_Id");
            double amount = rsNewReimb.getDouble("Reimb_Amount");
            String date = new Date(rsNewReimb.getTimestamp("Reimb_Submitted").getTime()).toString();
            String description = rsNewReimb.getString("Reimb_Description");
            String firstName = rsNewReimb.getString("User_First_Name");
            String lastName = rsNewReimb.getString("User_Last_Name");
            String type = rsNewReimb.getString("Reimb_Type");
            String status = rsNewReimb.getString("Reimb_Status");

            String urlDetails = "http://localhost:8080/users/" + addReimbursementDTO.getReimbAuthor() + "/reimbursements/" + id;


            ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
            con.commit();
            return reimbursement;

        }
    }


    public Reimbursement getReimbursementById(int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "SELECT *" +
                    "FROM tickets t " +
                    "WHERE t.Reimb_Id = ?";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, reimbId);

            ResultSet rs = pstmtSelect.executeQuery();
            if (!rs.next())  {
                return null;
            }

            int id = rs.getInt("Reimb_Id");
            double amount = rs.getDouble("Reimb_Amount");

            String submitDateString = new Date(rs.getTimestamp("Reimb_Submitted").getTime()).toString();

            Date resolveDate = rs.getTimestamp("Reimb_Resolved");
            String resolveDateString;
            if (resolveDate != null) {
                resolveDateString = new Date(rs.getTimestamp("Reimb_Resolved").getTime()).toString();
            } else {
                resolveDateString = null;
            }
            String description = rs.getString("Reimb_Description");
            String url = rs.getString("Reimb_Receipt");
            String firstName = rs.getString("User_First_Name");
            String lastName = rs.getString("User_Last_Name");
            String email = rs.getString("User_Email");
            int resolverId = rs.getInt("Reimb_Resolver");
            String type = rs.getString("Reimb_Type");
            String status = rs.getString("Reimb_Status");


            Reimbursement reimbursement = new Reimbursement(id, amount, submitDateString, resolveDateString, description, url, firstName, lastName, email, resolverId, type, status);
            con.commit();
            return reimbursement;
        }
    }


    public List<ResponseReimbursementDTO> getReimbursementsByUser(int userId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE e.User_Id = ?";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, userId);

            ResultSet rsEmail = pstmtSelect.executeQuery();
            rsEmail.next();

            String email = rsEmail.getString("User_Email");


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE t.User_Email = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("Reimb_Id");
                double amount = rs.getDouble("Reimb_Amount");
                String date = new Date(rs.getTimestamp("Reimb_Submitted").getTime()).toString();
                String description = rs.getString("Reimb_Description");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String type = rs.getString("Reimb_Type");
                String status = rs.getString("Reimb_Status");

                String urlDetails = "http://localhost:8080/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }
    public List<ResponseReimbursementDTO> getReimbursementsByUserAndStatus(int userId, String currentStatus) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE e.User_Id = ? ";
            PreparedStatement pstmtSelect = con.prepareStatement(sql);
            pstmtSelect.setInt(1, userId);

            ResultSet rsEmail = pstmtSelect.executeQuery();
            rsEmail.next();

            String email = rsEmail.getString("User_Email");


            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE t.User_Email = ? AND t.Reimb_Status = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, email);
            pstmt.setString(2, currentStatus);
            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("Reimb_Id");
                double amount = rs.getDouble("Reimb_Amount");
                String date = new Date(rs.getTimestamp("Reimb_Submitted").getTime()).toString();
                String description = rs.getString("Reimb_Description");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String type = rs.getString("Reimb_Type");
                String status = rs.getString("Reimb_Status");

                String urlDetails = "http://localhost:8080/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date,  description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }

    public List<ResponseReimbursementDTO> getAllReimbursements() throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){


            String sqlSelect = "SELECT * " +
                    "FROM tickets t ";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);

            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("Reimb_Id");
                double amount = rs.getDouble("Reimb_Amount");
                String date = new Date(rs.getTimestamp("Reimb_Submitted").getTime()).toString();
                String description = rs.getString("Reimb_Description");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String type = rs.getString("Reimb_Type");
                String status = rs.getString("Reimb_Status");

                int userId = rs.getInt("User_Id");

                String urlDetails = "http://localhost:8080/users/" + userId + "/reimbursements/" + id;


                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }
    public List<ResponseReimbursementDTO> getAllReimbursementsByStatus(String status) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()){

            String sqlSelect = "SELECT * " +
                    "FROM tickets t " +
                    "WHERE Reimb_Status = ?";
            PreparedStatement pstmt = con.prepareStatement(sqlSelect);

            pstmt.setString(1, status);

            ResultSet rs = pstmt.executeQuery();

            List<ResponseReimbursementDTO> reimbursements = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("Reimb_Id");
                double amount = rs.getDouble("Reimb_Amount");
                String date = new Date(rs.getTimestamp("Reimb_Submitted").getTime()).toString();
                String description = rs.getString("Reimb_Description");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String type = rs.getString("Reimb_Type");

                int userId = rs.getInt("User_Id");
                String urlDetails = "http://localhost:8080/users/" + userId + "/reimbursements/" + id;

                ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO(id, amount, date, description, firstName, lastName, status, type, urlDetails);
                reimbursements.add(reimbursement);
            }
            return reimbursements;
        }
    }

    public Reimbursement editUnresolvedReimbursement(int reimbId, UpdateReimbursementDTO reimbursement) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "UPDATE ERS_Reimbursement r " +
                    "SET Reimb_Amount=?, Reimb_Description=?, Reimb_Receipt=?, Reimb_Type_Id=? " +
                    "WHERE r.Reimb_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, reimbursement.getAmount());
            pstmt.setString(2, reimbursement.getDescription());
            pstmt.setString(3, reimbursement.getReceiptUrl());
            pstmt.setInt(4, reimbursement.getType());
            pstmt.setInt(5, reimbId);

            pstmt.executeUpdate();

            ReimbursementDao dao = new ReimbursementDao();

            con.commit();
            return dao.getReimbursementById(reimbId);
        }
    }
    public boolean updateReimbursementStatus(UpdateReimbursementStatusDTO dto, int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "UPDATE ERS_Reimbursement  " +
                    "SET Reimb_Status_Id = ?, Reimb_Resolved = ?, Reimb_Resolver = ?  " +
                    "WHERE Reimb_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, dto.getStatusId());
            pstmt.setTimestamp(2, dto.getTimestamp());
            pstmt.setInt(3, dto.getResolverId());
            pstmt.setInt(4, reimbId);

            if(pstmt.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean deleteUnresolvedReimbursement(int reimbId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            con.setAutoCommit(false);
            String sql = "DELETE FROM ERS_Reimbursement r " +
                    "WHERE r.Reimb_Id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reimbId);

            if(pstmt.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                return false;
            }
        }
    }
}