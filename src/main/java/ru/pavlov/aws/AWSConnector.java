package ru.pavlov.aws;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

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
		    System.err.println("Bucket with name " + name + "is already exist!");
		    return;
		}		 
		s3client.createBucket(name);
	}
	
	public List<Bucket> getAllBuckets(){
		List<Bucket> buckets = s3client.listBuckets();
		return buckets;
	}

}
