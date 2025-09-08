package com.dimu.dimuapi.service;


import com.dimu.dimuapi.Enum.AWSBucketList;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class S3Service {
    @Value("${aws.region}")
    private String region;

    @Autowired
    private S3Client s3Client;



    public String uploadFile(MultipartFile file, String bucketName) throws IOException {


        String fileName = System.currentTimeMillis() + "_" + file.getName();

        try  {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .key(fileName)
                            .bucket(bucketName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .contentLength(file.getSize())
                            .contentType(file.getContentType())
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

// Return the public URL
            log.info(generatePublicUrl(fileName, bucketName));

            log.info(generatePublicUrl(fileName,bucketName));
            return generatePublicUrl(fileName,bucketName);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }


    }

    public void deleteFile(String url){
        try{
            String[] bAndFN = extractBucketAndFileName(url);
            DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest.builder()
                                    .bucket(bAndFN[0])
                                    .key(bAndFN[1])
                                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("file deleted successfully");
        } catch (Exception e) {
            throw new CustomException("An error occured: "+e.getMessage());
        }

    }


    private String[] extractBucketAndFileName(String url){
        return url.replace("https://","").split(".s3.amazonaws.com/");
    }

    private String generatePublicUrl(String fileName,String bucketName){
        try{
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        }
        catch (Exception ex){
            throw new CustomException("Unable to generate a url for the uploaded file: "+ ex.getMessage());
        }
    }
}
