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

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReimbursementServiceTest {

    @Mock
    ReimbursementDao reimbursementDao;

    @Mock
    UploadImageUtility uploadImageUtility;


    @InjectMocks
    ReimbursementService reimbursementService;

    private AutoCloseable closeable;
    @BeforeAll
    public void set_up() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterAll
    public void close_mocks() throws Exception {
        closeable.close();
    }

    @Ignore
    @Test
    public void testAddValidReimbursement() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp submitted = Timestamp.valueOf(LocalDateTime.now());

        InputStream image = new FileInputStream("C://Users/Gizu Main/Desktop/Food.jpg");



        UploadedFile mockFile = new UploadedFile(image, "image/png", "image", ".png", 1000);

        ResponseReimbursementDTO expected = new ResponseReimbursementDTO();
        expected.setStatus("Pending");

        when(uploadImageUtility.uploadImage(mockFile)).thenReturn("www.false.com");
        when(reimbursementDao.addReimbursement(any(AddReimbursementDTO.class), any(String.class))).thenReturn(expected);

        AddReimbursementDTO newReimbursement = new AddReimbursementDTO(100, submitted, "Description", mockFile, 1, 1);
        ResponseReimbursementDTO actual = reimbursementService.addReimbursement(newReimbursement);
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Ignore
    @Test
    public void testAddValidReimbursementTrimDescription() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp submitted = Timestamp.valueOf(LocalDateTime.now());

        InputStream image = new FileInputStream("C://Users/Gizu Main/Desktop/Food.jpg");



        UploadedFile mockFile = new UploadedFile(image, "image/png", "image", ".png", 1000);

        ResponseReimbursementDTO expected = new ResponseReimbursementDTO();
        expected.setStatus("Pending");

        when(uploadImageUtility.uploadImage(mockFile)).thenReturn("www.false.com");
        when(reimbursementDao.addReimbursement(any(AddReimbursementDTO.class), any(String.class))).thenReturn(expected);

        AddReimbursementDTO newReimbursement = new AddReimbursementDTO(100, submitted, "          Description             ", mockFile, 1, 1);
        ResponseReimbursementDTO actual = reimbursementService.addReimbursement(newReimbursement);
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Ignore
    @Test
    public void testAddReimbursementInvalidImage() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp submitted = Timestamp.valueOf(LocalDateTime.now());

        InputStream image = new FileInputStream("C://Users/Gizu Main/Desktop/Food.jpg");



        UploadedFile mockFile = new UploadedFile(image, "image/gif", "image", ".gif", 1000);

        ResponseReimbursementDTO expected = new ResponseReimbursementDTO();
        expected.setStatus("Pending");

        when(reimbursementDao.addReimbursement(any(AddReimbursementDTO.class), any(String.class))).thenReturn(expected);

        AddReimbursementDTO newReimbursement = new AddReimbursementDTO(100, submitted, "Description", mockFile, 1, 1);
        Assertions.assertThrows(InvalidFileTypeException.class, () ->{
            reimbursementService.addReimbursement(newReimbursement);
        });
    }

    @Test
    public void testAddReimbursementInvalidType() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        AddReimbursementDTO newReimbursement = new AddReimbursementDTO();
        newReimbursement.setReimbAmount(100);
        newReimbursement.setReimbDescription("Content");
        newReimbursement.setReimbType(50);
        Assertions.assertThrows(InvalidJsonBodyProvided.class, () ->{
            reimbursementService.addReimbursement(newReimbursement);
        });
    }
    @Test
    public void testAddReimbursementNegativeAmount() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        AddReimbursementDTO newReimbursement = new AddReimbursementDTO();
        newReimbursement.setReimbAmount(-100);
        newReimbursement.setReimbDescription("Content");
        newReimbursement.setReimbType(50);
        Assertions.assertThrows(InvalidJsonBodyProvided.class, () ->{
            reimbursementService.addReimbursement(newReimbursement);
        });
    }

    @Ignore
    @Test
    public void testAddReimbursementImageSizeTooLarge() throws SQLException, SizeLimitExceededException, InvalidFileTypeException, IOException, InvalidJsonBodyProvided {
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        Timestamp submitted = Timestamp.valueOf(LocalDateTime.now());

        InputStream image = new FileInputStream("C://Users/Gizu Main/Desktop/Food.jpg");



        UploadedFile mockFile = new UploadedFile(image, "image/gif", "image", ".gif", 100000000);

        ResponseReimbursementDTO expected = new ResponseReimbursementDTO();
        expected.setStatus("Pending");

        when(reimbursementDao.addReimbursement(any(AddReimbursementDTO.class), any(String.class))).thenReturn(expected);

        AddReimbursementDTO newReimbursement = new AddReimbursementDTO(100, submitted, "Description", mockFile, 1, 1);
        Assertions.assertThrows(SizeLimitExceededException.class, () ->{
            reimbursementService.addReimbursement(newReimbursement);
        });
    }

    @Test
    public void getReimbursementByValidId() throws SQLException, ReimbursementDoesNotExist {
        Reimbursement expected = new Reimbursement(1, 92.20, "05/10/2023", null, "Some expense", "www.mock.com", "Michael", "Jordan", "email@email.com", 0, "food", "pending" );
        when(reimbursementDao.getReimbursementById(eq(1))).thenReturn(expected);
        Reimbursement actual = reimbursementService.getReimbursementById("1");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getReimbursementByInvalidId() {

        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            reimbursementService.getReimbursementById("def");
        });
    }

    @Test
    public void getReimbursementThatDoesNotExist() throws SQLException {
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(null);

        Assertions.assertThrows(ReimbursementDoesNotExist.class, ()->{
            reimbursementService.getReimbursementById("1");
        });
    }

    @Test
    public void getAllReumbursementsForAUser() throws SQLException {
        List<ResponseReimbursementDTO> mockReimbursements = new ArrayList<>();
        for (int i =0; i < 5; i++) {
            mockReimbursements.add(new ResponseReimbursementDTO());
        }

        when(reimbursementDao.getReimbursementsByUser(anyInt())).thenReturn(mockReimbursements);

        List<ResponseReimbursementDTO> actualReimbursements = reimbursementService.getReimbursementsByUser("1");

        Assertions.assertEquals(mockReimbursements, actualReimbursements);
        Assertions.assertEquals(mockReimbursements.size(), actualReimbursements.size());
        Assertions.assertEquals(mockReimbursements.get(2), actualReimbursements.get(2));

    }

    @Test
    public void getAllReimbursementsForAnInvalidUser() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            reimbursementService.getReimbursementsByUser("def");
        });
    }

    @Test
    public void getReimbursementsByValidUserAndValidStatus() throws SQLException, InvalidQueryParamProvided {
        List<ResponseReimbursementDTO> mockReimbursements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO();
            reimbursement.setStatus("Pending");
            mockReimbursements.add(reimbursement);
        }

        when(reimbursementDao.getReimbursementsByUserAndStatus(1, "Pending")).thenReturn(mockReimbursements);

        List<ResponseReimbursementDTO> actualReimbursements = reimbursementService.getReimbursementsByUserAndStatus("1", "Pending");

        Assertions.assertEquals(mockReimbursements, actualReimbursements);
        Assertions.assertEquals("Pending", actualReimbursements.get(0).getStatus());
        Assertions.assertEquals(mockReimbursements.size(), actualReimbursements.size());

    }

    @Test
    public void getReimbursementsByValidUserAndValidStatusWithOddCasing() throws SQLException, InvalidQueryParamProvided {
        List<ResponseReimbursementDTO> mockReimbursements = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO();
            reimbursement.setStatus("Approved");
            mockReimbursements.add(reimbursement);
        }

        when(reimbursementDao.getReimbursementsByUserAndStatus(1, "Approved")).thenReturn(mockReimbursements);

        List<ResponseReimbursementDTO> actualReimbursements = reimbursementService.getReimbursementsByUserAndStatus("1", "ApPrOveD");

        Assertions.assertEquals(mockReimbursements, actualReimbursements);
        Assertions.assertEquals("Approved", actualReimbursements.get(0).getStatus());
        Assertions.assertEquals(mockReimbursements.size(), actualReimbursements.size());

    }

    @Test
    public void getReimbursementsByValidUserAndInvalidStatus() {

        Assertions.assertThrows(InvalidQueryParamProvided.class, ()->{
            reimbursementService.getReimbursementsByUserAndStatus("1", "Pend");
        });
    }

    @Test
    public void getReimbursementsByInvalidUserAndValidStatus() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            reimbursementService.getReimbursementsByUserAndStatus("def", "Approved");
        });
    }
    @Test
    public void getAllReimbursementsByValidStatus() throws SQLException, InvalidQueryParamProvided {
        List<ResponseReimbursementDTO> mockReimbursements = new ArrayList<>();
        for(int i =0; i < 5; i++) {
            ResponseReimbursementDTO reimbursement = new ResponseReimbursementDTO();
            reimbursement.setStatus("Rejected");
            mockReimbursements.add(reimbursement);
        }

        when(reimbursementDao.getAllReimbursementsByStatus("Rejected")).thenReturn(mockReimbursements);

        List<ResponseReimbursementDTO> actualReimbursements = reimbursementService.getAllReimbursementsByStatus("Rejected");

        Assertions.assertEquals(mockReimbursements, actualReimbursements);
        Assertions.assertEquals(mockReimbursements.get(0).getStatus(), actualReimbursements.get(0).getStatus());

    }

    @Test
    public void getAllReimbursementsByInvalidStatus() {
        Assertions.assertThrows(InvalidQueryParamProvided.class, ()->{
            reimbursementService.getAllReimbursementsByStatus("Finalized");
        });
    }

    @Test
    public void getAllReimbursements() throws SQLException {
        List<ResponseReimbursementDTO> mockReimbursements = new ArrayList<>();
        for (int i =0; i < 5; i++) {
            mockReimbursements.add(new ResponseReimbursementDTO());
        }

        when(reimbursementDao.getAllReimbursements()).thenReturn(mockReimbursements);

        List<ResponseReimbursementDTO> actualReimbursements = reimbursementService.getAllReimbursements();

        Assertions.assertEquals(mockReimbursements, actualReimbursements);
    }

    @Test
    public void editUnresolvedReimbursementUser() throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist, InvalidJsonBodyProvided {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);
        when(reimbursementDao.editUnresolvedReimbursement(anyInt(), any(UpdateReimbursementDTO.class))).thenReturn(new Reimbursement());

        Reimbursement expectedReimbursement = new Reimbursement();
        UpdateReimbursementDTO dto = new UpdateReimbursementDTO(100, "Description", "www.google.com", 1);
        Reimbursement actualReimbursement = reimbursementService.editUnresolvedReimbursement("1", dto);

        Assertions.assertEquals(expectedReimbursement, actualReimbursement);
    }

    @Test
    public void editUnresolvedReimbursementUserNegativeAmount() throws SQLException{
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);
        when(reimbursementDao.editUnresolvedReimbursement(anyInt(), any(UpdateReimbursementDTO.class))).thenReturn(new Reimbursement());

        UpdateReimbursementDTO dto = new UpdateReimbursementDTO(-100, "Description", "www.google.com", 10);

        Assertions.assertThrows(InvalidJsonBodyProvided.class, ()->{
            reimbursementService.editUnresolvedReimbursement("1", dto);
        });
    }

    @Test
    public void editUnresolvedReimbursementInvalidType() throws SQLException{
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);
        when(reimbursementDao.editUnresolvedReimbursement(anyInt(), any(UpdateReimbursementDTO.class))).thenReturn(new Reimbursement());

        UpdateReimbursementDTO dto = new UpdateReimbursementDTO(100, "Description", "www.google.com", 50);

        Assertions.assertThrows(InvalidJsonBodyProvided.class, ()->{
            reimbursementService.editUnresolvedReimbursement("1", dto);
        });
    }

    @Test
    public void editUnresolvedReimbursementInvalidReimbursementId() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            reimbursementService.editUnresolvedReimbursement("def", new UpdateReimbursementDTO());
        });
    }

    @Test
    public void attemptToEditResolvedReimbursement() throws SQLException {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Approved");

        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);

        Assertions.assertThrows(ReimbursementAlreadyResolved.class, ()->{
            reimbursementService.editUnresolvedReimbursement("1", new UpdateReimbursementDTO());
        });
    }

    @Test
    public void updateUnresolvedReimbursementStatus() throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist, InvalidJsonBodyProvided {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");

        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);
        when(reimbursementDao.updateReimbursementStatus(any(UpdateReimbursementStatusDTO.class), anyInt())).thenReturn(true);

        UpdateReimbursementStatusDTO dto = new UpdateReimbursementStatusDTO();
        dto.setStatusId(3);

        Assertions.assertTrue(reimbursementService.updateReimbursementStatus(dto, "1"));
    }

    @Test
    public void failToUpdateResolvedReimbursementStatus() throws SQLException {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Rejected");

        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);


        Assertions.assertThrows(ReimbursementAlreadyResolved.class, ()->{
            reimbursementService.updateReimbursementStatus(new UpdateReimbursementStatusDTO(), "1");
        });
    }

    @Test
    public void failToUpdateNonexistentReimbursementStatus() throws SQLException {
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(null);
        Assertions.assertThrows(ReimbursementDoesNotExist.class, ()->{
            reimbursementService.updateReimbursementStatus(new UpdateReimbursementStatusDTO(), "1");
        });
    }

    @Test
    public void updateReimbursementStatusWithInvalidStatusCode() throws SQLException {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);

        UpdateReimbursementStatusDTO dto = new UpdateReimbursementStatusDTO();
        dto.setStatusId(104);

        Assertions.assertThrows(InvalidJsonBodyProvided.class, ()->{
            reimbursementService.updateReimbursementStatus(dto, "1");
        });
    }

    @Test
    public void deleteUnresolvedReimbursement() throws SQLException, ReimbursementAlreadyResolved, ReimbursementDoesNotExist {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Pending");
        mockReimbursement.setReceiptUrl("https://www.false.com/images/receipt.png");

        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);
        when(reimbursementDao.deleteUnresolvedReimbursement(anyInt())).thenReturn(true);
        when(uploadImageUtility.deleteImage(anyString())).thenReturn(true);

        Assertions.assertTrue(reimbursementService.deleteUnresolvedReimbursement("1"));
    }

    @Test
    public void deleteResolvedReimbursement() throws SQLException {
        Reimbursement mockReimbursement = new Reimbursement();
        mockReimbursement.setStatus("Approved");

        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(mockReimbursement);

        Assertions.assertThrows(ReimbursementAlreadyResolved.class, ()->{
            reimbursementService.deleteUnresolvedReimbursement("1");
        });
    }

    @Test
    public void attemptToDeleteNonExistentReimbursement() throws SQLException {
        when(reimbursementDao.getReimbursementById(anyInt())).thenReturn(null);

        Assertions.assertThrows(ReimbursementDoesNotExist.class, ()->{
            reimbursementService.deleteUnresolvedReimbursement("1");
        });

    }

}