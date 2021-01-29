package com.cyrine.serviceImp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cyrine.configuration.FilesProperties;
import com.cyrine.configuration.UploadFileResponse;
import com.cyrine.exception.FileNotFoundException;
import com.cyrine.exception.FileStoreException;
import com.cyrine.service.FilesService;

@Service
public class FilesServiceImp implements FilesService{

	final Path filesStorageLocation;
	private static final Logger logger = LoggerFactory.getLogger(FilesServiceImp.class);
	
	@Autowired
	public FilesServiceImp(FilesProperties filesProperties) {
		this.filesStorageLocation = Paths.get(filesProperties.getStoreDir())
                .toAbsolutePath().normalize();
		try {
            Files.createDirectories(this.filesStorageLocation);
        } catch (Exception ex) {
            throw new FileStoreException("Could not create the directory where the uploaded files will be stored.", ex);
        }
	}
	
	@Override
	public String storeFile(MultipartFile file) {
		// TODO Auto-generated method stub
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStoreException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.filesStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStoreException("Could not store file " + fileName + ". Please try again!", ex);
        }
	}
	
	@Override
	public Resource loadFileAsResource(String fileName){
		try {
            Path filePath = this.filesStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
	
	@Override
	public UploadFileResponse uploadSingleFile(MultipartFile file) {
		// TODO Auto-generated method stub
		String fileName = storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
	}

	@Override
	public List<UploadFileResponse> uploadMultipleFiles(MultipartFile[] files) {
		// TODO Auto-generated method stub
		return Arrays.asList(files)
                .stream()
                .map(file -> uploadSingleFile(file))
                .collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<Resource> downloadFiles(String fileName, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Resource resource = loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}

}
