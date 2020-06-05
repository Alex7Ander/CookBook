package ru.pavlov.googleDrive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Service
public class GoogleDriveFileManager {
    private static final String applicationName = "CookBookWebClient";
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private static final String tokensDirectoryPath = "tokens";
    private static final List<String> scopes = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    
    private static String CredentialsFilePath = "/client_secret_507564032301-b9kdgcabrp2vh121m5d4vles3bc60d4m.apps.googleusercontent.com.json";
		
	private Drive service;
	
	public GoogleDriveFileManager(){}
	
	@PostConstruct
	public void init() throws GeneralSecurityException, IOException {
	    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	    this.service = new Drive.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT))
	            .setApplicationName(applicationName)
	            .build();
	}
	
    public Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        //Load client secrets.
        InputStream in = GoogleDriveFileManager.class.getResourceAsStream(CredentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CredentialsFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
	public boolean createFolder(String folderName) {
		File fileMetadata = new File();
		fileMetadata.setName(folderName);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");
		File file = null;
		try {
			file = this.service.files().create(fileMetadata)
				    .setFields("id")
				    .execute();
		}
		catch(IOException ioExp) {
			ioExp.printStackTrace();
			return false;
		}		
		System.out.println("Folder ID: " + file.getId());
		return true;
	}
	
	public boolean deleteFolder() {
		return false;
	}
	
	public String uploadFile() {
		String filePath = null;
		
		return filePath;
	}
	
	public boolean downloadFile() {
		return false;
	}
	
}
