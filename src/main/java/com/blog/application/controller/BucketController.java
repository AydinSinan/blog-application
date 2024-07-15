package com.blog.application.controller;
import com.amazonaws.services.s3.model.Bucket;

import com.blog.application.service.BlogService;
import com.blog.application.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/bucket")
public class BucketController {


    @Autowired
    private BlogService blogService;

    @Autowired
    BucketService bucketService;

    @GetMapping
    public List<Bucket> getBucketList() {
        List<Bucket> bucketList = bucketService.getBucketList();
        System.out.println("bucketList:"+bucketList);
        return bucketList;
    }

    @GetMapping("/listObjects") // tmm
    public List<String> listObjectsInBucket(@RequestParam("bucketName") String bucketName) {
        List<String> objectNames = bucketService.listObjectsInBucket(bucketName);
        return objectNames;
    }

    @GetMapping("/downloadObj")
    public void downloadObject(@RequestParam("bucketName") String bucketName, @RequestParam("objName") String objName) throws Exception {
        bucketService.getObjectFromBucket(bucketName, objName);
    }

@PostMapping("/uploadObj")
public void uploadObject(@RequestParam("blogId") Long blogId,
                         @RequestParam("bucketName") String bucketName,
                         @RequestParam("file") MultipartFile file) throws Exception {
    if (file.isEmpty()) {
        throw new IllegalArgumentException("File cannot be empty");
    }

    String fileName = file.getOriginalFilename();
    String filePath = "C:\\Users\\sinan.aydin\\OneDrive - Scale Focus AD\\Desktop\\" + fileName;
    file.transferTo(new File(filePath));

    String s3ObjectUrl = bucketService.putObjectIntoBucket(bucketName, fileName, filePath);

    blogService.addS3ObjectUrlToBlog(blogId, s3ObjectUrl);
}
    @PostMapping("/createBucket")
    public String createBucket(@RequestParam("bucketName") String bucketName) {
        bucketService.createBucket(bucketName);
        return "done";
    }



}
