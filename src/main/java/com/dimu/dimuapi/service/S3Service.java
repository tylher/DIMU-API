package com.dimu.dimuapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dimu.dimuapi.Enum.AWSBucketList;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class S3Service {
    @Autowired
    AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {


        String fileName = System.currentTimeMillis() + "_" + file.getName();

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
            amazonS3.setObjectAcl(bucketName,fileName, CannedAccessControlList.PublicRead);
            log.info(generatePublicUrl(fileName,bucketName));
            return generatePublicUrl(fileName,bucketName);
        }


    }

    public void deleteFile(String url){
        try{
            String[] bAndFN = extractBucketAndFileName(url);
            DeleteObjectRequest deleteObjectRequest= new DeleteObjectRequest( bAndFN[0],bAndFN[1]);
            amazonS3.deleteObject(deleteObjectRequest);
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
