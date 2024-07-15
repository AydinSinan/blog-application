package com.blog.application.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.blog.application.service.BucketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BucketServiceImpl implements BucketService {

    @Value("/C:/Users/sinan.aydin/OneDrive - Scale Focus AD/Desktop/")
    private String desktopPath;

    Logger LOG = LogManager.getLogger(BucketServiceImpl.class);

    @Autowired
    AmazonS3 s3Client;

    @Override
    public List<String> listObjectsInBucket(String bucketName) {
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        List<String> objectNames = new ArrayList<>();

        ListObjectsV2Result result;
        do {
            result = s3Client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                objectNames.add(objectSummary.getKey());
            }
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return objectNames;
    }


    @Override
    public List<Bucket> getBucketList() {
        LOG.info("getting bucket list... ");
        return s3Client.listBuckets();
    }

    @Override
    public boolean validateBucket(String bucketName) {
        List<Bucket> bucketList = getBucketList();
        LOG.info("Bucket list:"+bucketList);
        return bucketList.stream().anyMatch(m -> bucketName.equals(m.getName()));
    }

    @Override
    public void downloadObjectFromBucket(PresignedUrlDownloadRequest presignedUrlDownloadRequest, String bucketName) throws Exception {
        if(!validateBucket(bucketName)) {
            LOG.info("invalid bucket");
            throw new Exception("Invalid bucket");
        }
        LOG.info("downloading object...");
        s3Client.download(presignedUrlDownloadRequest,new File("opt/test/v1.txt"));
    }

    @Override
    public void getObjectFromBucket(String bucketName, String objectName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, objectName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        FileOutputStream fos = new FileOutputStream(new File(desktopPath + objectName));
        byte[] read_buf = new byte[1024];
        int read_len = 0;
        while ((read_len = inputStream.read(read_buf)) > 0) {
            fos.write(read_buf, 0, read_len);
        }
        inputStream.close();
        fos.close();
    }

    @Override
    public String putObjectIntoBucket(String bucketName, String objName, String filePathToUpload) throws Exception {
        try {
            s3Client.putObject(bucketName, objName, new File(filePathToUpload));
            // Construct and return the URL of the uploaded object
            return s3Client.getUrl(bucketName, objName).toString();
        } catch (AmazonServiceException e) {
            LOG.info(e.getErrorMessage());
            throw new Exception("Failed to upload object to S3: " + e.getMessage());
        }
    }

    @Override
    public void createBucket(String bucketName) {
        s3Client.createBucket(bucketName);
    }

    @Override
    public void uploadObjectToS3(PresignedUrlUploadRequest presignedUrlUploadRequest, String bucketName) throws Exception {
        if(!validateBucket(bucketName)) {
            LOG.info("invalid bucket");
            throw new Exception("Invalid bucket exception");
        }
        LOG.info("uploading object...");
        s3Client.upload(presignedUrlUploadRequest);
    }


}