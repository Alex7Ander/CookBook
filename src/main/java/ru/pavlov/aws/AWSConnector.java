package ru.pavlov.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AWSConnector {
	
	private AWSCredentials credentials;
	private AmazonS3 s3client;
	
	public AWSConnector() {}
	
	public void setCredentials(String accesskey, String secretkey) {
		credentials = new BasicAWSCredentials(accesskey, secretkey);
		s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.EU_CENTRAL_1)
				  .build();
	}
	
	public void createBucket(String name) {
		if(s3client.doesBucketExist(name)) {
		    System.err.println("Bucket with name " + name + " is already exist!");
		    return;
		}		 
		s3client.createBucket(name);
	}
	
	public void deleteBucket(String name) {
		try {
		    s3client.deleteBucket(name);
		} catch (AmazonServiceException asExp) {
		    asExp.printStackTrace();
		    return;
		}
	}
	
	public List<Bucket> getAllBuckets(){
		List<Bucket> buckets = s3client.listBuckets();
		return buckets;
	}
	
	public void uploadFile(String bucketName, String fileKey, File file) {
		s3client.putObject(bucketName, fileKey, file);
		return;
	}
	
	public void downloadFile(String bucketName,  String fileKey, String downloadingPath) throws IOException {
		S3Object s3object = s3client.getObject(bucketName, fileKey);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		FileUtils.copyInputStreamToFile(inputStream, new File(downloadingPath));
	}

}
