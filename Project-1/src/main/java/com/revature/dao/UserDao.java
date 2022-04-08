package com.revature.dao;
import com.revature.dto.LoginDTO;
import com.revature.dto.UserDTO;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.utility.ConnectionUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public UserDao() {
    }

    public User getUserByUsernameAndPassword(LoginDTO dto) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT u.User_Id, u.ERS_Username, u.User_First_Name, u.User_Last_Name, u.User_Email, ur.User_Role_Id, ur.role " +
            "FROM ERS_User u " +
            "INNER JOIN ERS_User_Roles ur " +
            "ON ur.User_Role_Id = u.User_Role_Id " +
            "WHERE u.ERS_Username = ? and u.ERS_Password = crypt(? , u.ERS_Password)";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, dto.getUsername());
            pstmt.setString(2, dto.getPassword());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("User_Id");
                String username = rs.getString("ERS_Username");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String email = rs.getString("User_Email");
                int userRoleId = rs.getInt("User_Role_Id");
                String role = rs.getString("role");

                UserRole userRole = new UserRole(userRoleId, role);
                return new User(userId, username, firstName, lastName, email, userRole);
            }

            return null;
        }
    }
    public UserDTO getUserByUserId(int id) throws SQLException {

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * " +
                    "FROM employees e " +
                    "WHERE User_Id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String email = rs.getString("User_Email");
                int userRoleId = rs.getInt("User_Role_Id");
                String role = rs.getString("role");

                UserRole userRole = new UserRole(userRoleId, role);
                return new UserDTO(id, firstName, lastName, email, userRole);
            }


            return null;

        }



    }

    public List<UserDTO> getAllUsers() throws SQLException {
        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * " +
                    "FROM employees";

            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            List<UserDTO> users = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("User_Id");
                String firstName = rs.getString("User_First_Name");
                String lastName = rs.getString("User_Last_Name");
                String email = rs.getString("User_Email");
                int userRoleId = rs.getInt("User_Role_Id");
                String role = rs.getString("role");

                UserRole userRole = new UserRole(userRoleId, role);
                UserDTO user = new UserDTO(id, firstName, lastName, email, userRole);

                users.add(user);
            }
            return users;
        }
    }
}
