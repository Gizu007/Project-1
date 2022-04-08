package com.revature.utility;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import io.javalin.http.UploadedFile;

import java.io.FileInputStream;
import java.io.IOException;


public class UploadImageUtility {
    private static final String bucketName = System.getenv("bucket_name");
    private static final String projectId = System.getenv("project_id");

    public UploadImageUtility() {
    }

    public  String uploadImage(UploadedFile uploadedFile) throws IOException {
        Bucket bucket = getBucket(bucketName);
        Blob blob = bucket.create(uploadedFile.getFilename(), uploadedFile.getContent(), uploadedFile.getContentType());

        return "https://storage.googleapis.com/buck_pro_1/" + blob.getName();
    }

    public boolean deleteImage(String imageName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        return storage.delete(bucketName, imageName);
    }
    private static Bucket getBucket(String bucketName) {
        try{
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            Bucket bucket = storage.get(bucketName);
            if (bucket == null) {
                throw new IOException("Bucket not found:"+bucketName);
            }
            return bucket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}