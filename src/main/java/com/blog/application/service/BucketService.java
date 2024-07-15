package com.blog.application.service;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.PresignedUrlUploadRequest;

import java.io.IOException;
import java.util.List;

public interface BucketService {

    List<Bucket> getBucketList();

    boolean validateBucket(String bucketName);

    void downloadObjectFromBucket(PresignedUrlDownloadRequest presignedUrlDownloadRequest, String bucketName) throws Exception;

    void getObjectFromBucket(String bucketName, String objectName) throws IOException;

    String putObjectIntoBucket(String bucketName, String objectName, String filePathToUpload) throws Exception;

    void createBucket(String bucket);

    List<String> listObjectsInBucket(String bucketName);


    void uploadObjectToS3(PresignedUrlUploadRequest presignedUrlUploadRequest, String bucketName) throws Exception;
}