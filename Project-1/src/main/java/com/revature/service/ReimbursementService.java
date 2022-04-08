package com.revature.service;

import com.revature.dao.ReimbursementDao;
import com.revature.dto.AddReimbursementDTO;
import com.revature.dto.ResponseReimbursementDTO;
import com.revature.dto.UpdateReimbursementDTO;
import com.revature.dto.UpdateReimbursementStatusDTO;
import com.revature.exception.*;
import com.revature.model.Reimbursement;
import com.revature.utility.UploadImageUtility;
import io.javalin.http.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

public class ReimbursementService {
    Logger logger = LoggerFactory.getLogger(ReimbursementService.class);


    private final ReimbursementDao reimbursementDao;
    private final UploadImageUtility uploadImageUtility;

    public ReimbursementService() {
        this.reimbursementDao = new ReimbursementDao();
        this.uploadImageUtility = new UploadImageUtility();

    }
    public ReimbursementService(ReimbursementDao mockDao, UploadImageUtility mockUploadImageUtility) {
        this.reimbursementDao = mockDao;
        this.uploadImageUtility = mockUploadImageUtility;
    }


    public ResponseReimbursementDTO addReimbursement(AddReimbursementDTO dto) throws SQLException, IOException, SizeLimitExceededException, InvalidFileTypeException, InvalidJsonBodyProvided {

        AddReimbursementDTO sanitizedDto = sanitizeNewReimbursement(dto);
        Instant instant = Instant.now();
        ZoneId z = ZoneId.of("America/New_York");
        ZonedDateTime zdt = instant.atZone(z);
        Timestamp submitted = Timestamp.from(zdt.toInstant());
        sanitizedDto.setReimbSubmitted(submitted);


        UploadedFile uploadedImage = sanitizedDto.getReimbReceiptImage();
        if (uploadedImage.getSize() > 10000000) {
            throw new SizeLimitExceededException("File size exceeded limit of 10MB. Uploaded File Size: " + uploadedImage.getSize());
        }
        if (!uploadedImage.getContentType().equals("image/png") && !uploadedImage.getContentType().equals("image/jpg") && !uploadedImage.getContentType().equals("image/jpeg")) {
            throw new InvalidFileTypeException("Invalid file type for uploaded image. Accepted files: .png .jpg .jpeg");
        }

        String url = uploadImageUtility.uploadImage(uploadedImage);



        logger.info("User " + dto.getReimbAuthor() + " added a reimbursement.");
        return reimbursementDao.addReimbursement(sanitizedDto, url);
    }


    public Reimbursement getReimbursementById(String reimbId) throws SQLException, ReimbursementDoesNotExist {
        try {
            int rId = Integer.parseInt(reimbId);
            Reimbursement reimbursement = reimbursementDao.getReimbursementById(rId);
            if (reimbursement == null) {
                throw new ReimbursementDoesNotExist("A reimbursement with an id of " + rId + " does not exist at this time.");
            }
            logger.info("User requested reimbursement of id " + reimbId + ".");
            return reimbursement;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid integer given for request.");
        }
    }

    public List<ResponseReimbursementDTO> getReimbursementsByUser(String userId) throws SQLException {
        try {
            int uId = Integer.parseInt(userId);
            logger.info("User requested all reimbursements for " + userId + ".");
            return reimbursementDao.getReimbursementsByUser(uId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid integer given for request.");
        }
    }

    public List<ResponseReimbursementDTO> getReimbursementsByUserAndStatus(String userId, String currentStatus) throws SQLException, InvalidQueryParamProvided {
        try {
            int uId = Integer.parseInt(userId);
            String status = validateStatus(currentStatus);
            logger.info("User requested all " + status + " reimbursements for user " + userId + ".");
            return reimbursementDao.getReimbursementsByUserAndStatus(uId, status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid integer given for the request.");
        }
    }

    public List<ResponseReimbursementDTO> getAllReimbursementsByStatus(String status) throws SQLException, InvalidQueryParamProvided {
        String stat = validateStatus(status);
        logger.info("User requested all " + stat +" reimbursements.");
        return reimbursementDao.getAllReimbursementsByStatus(stat);
    }

    public List<ResponseReimbursementDTO> getAllReimbursements() throws SQLException {
        logger.info("User requested all reimbursements.");
        return reimbursementDao.getAllReimbursements();
    }

    public Reimbursement editUnresolvedReimbursement(String reimbId, UpdateReimbursementDTO dto) throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist, InvalidJsonBodyProvided {

        if (!(getReimbursementById(reimbId).getStatus().equals("Pending"))) {
            throw new ReimbursementAlreadyResolved("This reimbursement has already been approved or denied. Unable to make changes at this time.");
        }
        int rId = Integer.parseInt(reimbId);
        UpdateReimbursementDTO sanitizedDto = sanitizeUpdateReimbursement(dto);

        logger.info("User edited reimbursement with an id of " + reimbId + ".");
        return reimbursementDao.editUnresolvedReimbursement(rId, sanitizedDto);
    }

    public boolean updateReimbursementStatus(UpdateReimbursementStatusDTO dto, String reimbId) throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist, InvalidJsonBodyProvided {

        if(!(getReimbursementById(reimbId).getStatus().equals("Pending"))) {
            throw new ReimbursementAlreadyResolved("This reimbursement has already been approved or denied. Unable to make changes at this time.");
        }
        if (!(dto.getStatusId() == 2 || dto.getStatusId() == 3)) {
            throw new InvalidJsonBodyProvided("You must provide an appropriate status code to update.");
        }
        int rId = Integer.parseInt(reimbId);
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp submitted = Timestamp.valueOf(LocalDateTime.now());
        dto.setTimestamp(submitted);
        logger.info("User updated the status of reimbursement with an id of " + reimbId + " to " + dto.getStatusId() + ".");
        return reimbursementDao.updateReimbursementStatus(dto, rId);
    }

    public boolean deleteUnresolvedReimbursement(String reimbId) throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist {
        Reimbursement reimbToDelete = getReimbursementById(reimbId);
        if(!(reimbToDelete.getStatus().equals("Pending"))) {
            throw new ReimbursementAlreadyResolved("This reimbursement has already been approved or denied. Unable to make changes at this time.");
        }
        int rId = Integer.parseInt(reimbId);
        String imageToDelete = reimbToDelete.getReceiptUrl().split("/")[4];
        uploadImageUtility.deleteImage(imageToDelete);
        logger.info("User deleted a reimbursement with an id of " + rId + ".");
        return reimbursementDao.deleteUnresolvedReimbursement(rId);
    }


    public String validateStatus(String status) throws InvalidQueryParamProvided {
        String stat = status.toLowerCase();
        if (!(stat.equals("pending") || stat.equals("approved") || stat.equals("rejected")))  {
            throw new InvalidQueryParamProvided("Provided status is not a valid status. Input: " + status);
        }
        return stat.substring(0, 1).toUpperCase() + stat.substring(1);
    }

    public AddReimbursementDTO sanitizeNewReimbursement(AddReimbursementDTO dto) throws InvalidJsonBodyProvided {
        double amount = dto.getReimbAmount();
        String descrip = dto.getReimbDescription().trim();
        int type = dto.getReimbType();
        if (!(type == 1 || type == 2 || type == 3 || type == 4)) {
            throw new InvalidJsonBodyProvided("Invalid type id provided. Input: " + type);
        }
        if (amount < 0) {
            throw new InvalidJsonBodyProvided("Amount provided cannot be negative. Input: " + amount);
        }
        dto.setReimbDescription(descrip);
        return dto;
    }

    public UpdateReimbursementDTO sanitizeUpdateReimbursement(UpdateReimbursementDTO dto) throws InvalidJsonBodyProvided {
        double amount = dto.getAmount();

        int type = dto.getType();
        if (!(type == 1 || type == 2 || type == 3 || type == 4)) {
            throw new InvalidJsonBodyProvided("Invalid type id provided. Input: " + type);
        }
        if (amount < 0) {
            throw new InvalidJsonBodyProvided("Amount provided cannot be negative. Input: " + amount);
        }
        String descrip = dto.getDescription().trim();
        dto.setDescription(descrip);
        return dto;
    }
}