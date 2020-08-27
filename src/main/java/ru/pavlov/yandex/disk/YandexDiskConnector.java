package ru.pavlov.yandex.disk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class YandexDiskConnector {

	private String token;

    @SuppressWarnings("unused")
	private void sendGet(String requestPath, CloseableHttpResponse response) throws Exception {
        HttpGet request = new HttpGet(requestPath); // https://www.google.com/search?q=mkyong
        request.addHeader("Authorization", "OAuth " + token); 
        CloseableHttpClient httpClient = HttpClients.createDefault();
        response = httpClient.execute(request);
        httpClient.close();
    }
    
    @SuppressWarnings("unused")
	private void sendPost(String requestPath, CloseableHttpResponse response, Map<String, String> parametrs) throws Exception {
        HttpPost post = new HttpPost(requestPath);
        post.addHeader("Authorization", "OAuth " + token);
        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        if(parametrs != null) {
            for(String key : parametrs.keySet()) {
            	String value = parametrs.get(key);
            	urlParameters.add(new BasicNameValuePair(key, value));
            }
        }
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        response = httpClient.execute(post);        
        String resp = EntityUtils.toString(response.getEntity());
        httpClient.close();
    }
    
    @SuppressWarnings("unused")
	private void sendPut(String requestPath) throws ClientProtocolException, IOException {
    	HttpPut put = new HttpPut(requestPath);
    	put.addHeader("Authorization", "OAuth " + token);    	
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	httpClient.execute(put); 
    	httpClient.close();       	
    }
    
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public YandexDiskInfo getDiskInfo() throws ClientProtocolException, IOException, YandexDiskException {
		ObjectMapper mapper = new ObjectMapper();
		CloseableHttpResponse response = null;
        HttpGet request = new HttpGet("https://cloud-api.yandex.net:443/v1/disk?fields=%2F"); 
        request.addHeader("Authorization", "OAuth " + token); 
        CloseableHttpClient httpClient = HttpClients.createDefault();
        response = httpClient.execute(request);        
		if(response == null) {
			throw new NoConnectionToYandexDiskException();
		}
		if(response.getStatusLine().getStatusCode() != 200) {
			throw new YandexDiskInternalException(Integer.toString(response.getStatusLine().getStatusCode()));
		}
		HttpEntity entity = response.getEntity();
        String result = "";
        if (entity != null) {
			result = EntityUtils.toString(entity);
			YandexDiskInfo dInfo = mapper.readValue(result, YandexDiskInfo.class);
			return dInfo;
        }		
        else return null;
	}
	
	public void createFolder(String internalPathToNewFolder) throws ClientProtocolException, IOException, YandexDiskException {
		internalPathToNewFolder = internalPathToNewFolder.replaceAll("/", "%2F");
		String url = "https://cloud-api.yandex.net:443/v1/disk/resources?path=" + internalPathToNewFolder;		
		HttpPut put = new HttpPut(url);
    	put.addHeader("Authorization", "OAuth " + token);    	
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	CloseableHttpResponse response = httpClient.execute(put);
    	if(response == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(response.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(response.getStatusLine().getStatusCode()));
    	}
	}
	
	public void uploadFile(String internalPathToTargetFolder, String internalFileName, String filePath) throws ClientProtocolException, IOException, YandexDiskException {
		internalPathToTargetFolder = internalPathToTargetFolder.replaceAll("/", "%2F");
		String getReqUrl = "https://cloud-api.yandex.net:443/v1/disk/resources/upload?path=" + internalPathToTargetFolder + "%2F" + internalFileName;			
        HttpGet get = new HttpGet(getReqUrl);
        get.addHeader("Authorization", "OAuth " + token); 
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse getUploadUrlResponse = httpClient.execute(get);
    	if(getUploadUrlResponse == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(getUploadUrlResponse.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(getUploadUrlResponse.getStatusLine().getStatusCode()));
    	}
		HttpEntity entity = getUploadUrlResponse.getEntity();
        String result = "";
        if (entity != null) {
			result = EntityUtils.toString(entity);
        }
		ObjectMapper mapper = new ObjectMapper();
		YandexDiskResponse ydResp = mapper.readValue(result, YandexDiskResponse.class);
		
		String url = ydResp.getHref();
		File file = new File(filePath);	
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost uploadingPostRequest = new HttpPost(url);
	    HttpEntity multipartEntity = MultipartEntityBuilder.create().addPart("file", new FileBody(file)).build();
	    uploadingPostRequest.setEntity(multipartEntity);
	    CloseableHttpResponse uploadingResponse = client.execute(uploadingPostRequest);
    	if(uploadingResponse == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(uploadingResponse.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(uploadingResponse.getStatusLine().getStatusCode()));
    	}
	}
	
	public void delete(String sourceInternalPath) throws ClientProtocolException, IOException, YandexDiskException {
		sourceInternalPath = sourceInternalPath.replaceAll("/", "%2F");
		String url = "https://cloud-api.yandex.net:443/v1/disk/resources?path=" + sourceInternalPath;
		HttpDelete delete = new HttpDelete(url);
		delete.addHeader("Authorization", "OAuth " + token);
		CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(delete);
    	if(response == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(response.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(response.getStatusLine().getStatusCode()));
    	}
	}
	
	public String getDownloadLink(String internalPathToTargetFile) throws ClientProtocolException, IOException, NoConnectionToYandexDiskException, YandexDiskInternalException {
		internalPathToTargetFile = internalPathToTargetFile.replaceAll("/", "%2F");
		String dwnlUrl = "https://cloud-api.yandex.net:443/v1/disk/resources/download?path=" + internalPathToTargetFile;
		HttpGet getLinkRequest = new HttpGet(dwnlUrl);		
		getLinkRequest.addHeader("Authorization", "OAuth " + token); 
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse getDownloadLinkResponse = httpClient.execute(getLinkRequest);
    	if(getDownloadLinkResponse == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(getDownloadLinkResponse.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(getDownloadLinkResponse.getStatusLine().getStatusCode()));
    	}
		HttpEntity entity = getDownloadLinkResponse.getEntity();
        String result = "";
        if (entity != null) {
			result = EntityUtils.toString(entity);	
        }
        
        ObjectMapper mapper = new ObjectMapper();
        YandexDiskResponse ydResp = mapper.readValue(result, YandexDiskResponse.class);
		String url = ydResp.getHref();
		return url;
	}
	
	private HttpEntity loadHttpEntityByUrl(String downloadingUrl) throws ClientProtocolException, IOException, YandexDiskException {
        HttpGet getFile = new HttpGet(downloadingUrl);
        // add request headers
        getFile.addHeader("Authorization", "OAuth " + token); 
        CloseableHttpClient gettingFileHttpClient = HttpClients.createDefault();
        CloseableHttpResponse responseGettingFile = gettingFileHttpClient.execute(getFile);	
    	if(responseGettingFile == null) {
    		throw new NoConnectionToYandexDiskException();
    	}
    	if(responseGettingFile.getStatusLine().getStatusCode() >= 400) {
    		throw new YandexDiskInternalException(Integer.toString(responseGettingFile.getStatusLine().getStatusCode()));
    	}         
		return responseGettingFile.getEntity(); 
	}
	
	public void downloadFile(String downloadingUrl, String downloadingPath) throws ClientProtocolException, IOException, YandexDiskException  {        
		HttpEntity gettingFileEntity = loadHttpEntityByUrl(downloadingUrl);       
        if (gettingFileEntity != null) {           
            BufferedInputStream bis = new BufferedInputStream(gettingFileEntity.getContent());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(downloadingPath)));
            int inByte;
            while((inByte = bis.read()) != -1) 
            	bos.write(inByte);
            bis.close();
            bos.close();
        } 
        else {
        	throw new NoSuchSourceException();
        }
	}
	
	public byte[] getTargetFileByteArray(String downloadingUrl) throws ClientProtocolException, IOException, YandexDiskException {
		List<Byte> bytesList = new ArrayList<>();
		HttpEntity gettingFileEntity = loadHttpEntityByUrl(downloadingUrl);		
		BufferedInputStream bis = new BufferedInputStream(gettingFileEntity.getContent());		
		int inByte;
		while((inByte = bis.read()) != -1) {
			bytesList.add((byte) inByte);
		}		
		byte[] bytes = new byte[bytesList.size()];
		for(int i = 0; i < bytesList.size(); i++) {
			byte value = bytesList.get(i);
			bytes[i] = value;
		} 
		return bytes;
	}
	
	public byte[] getTargetFileByteArrayByPath(String internalPathToTargetFile) throws ClientProtocolException, IOException, YandexDiskException {
		String url = getDownloadLink(internalPathToTargetFile);
		byte[] bytes = getTargetFileByteArray(url);		
		return bytes; 
	}
}