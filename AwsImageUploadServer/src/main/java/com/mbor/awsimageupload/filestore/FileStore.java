package com.mbor.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 amazonS3;

    @Autowired
    public FileStore(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void save(String path,
                     String fileName,
                     InputStream inputStream,
                     Optional<Map<String, String>> optionalMetadata){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }

    }
}
