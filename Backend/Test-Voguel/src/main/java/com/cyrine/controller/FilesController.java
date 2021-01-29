package com.cyrine.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cyrine.configuration.UploadFileResponse;
import com.cyrine.serviceImp.FilesServiceImp;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FilesController {
	
	@Autowired
	private FilesServiceImp filesServiceImp;
	
	@PostMapping("/uploadSingleFile")
	public UploadFileResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
		return filesServiceImp.uploadSingleFile(file);
	}

	@PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return filesServiceImp.uploadMultipleFiles(files);
	}
	
	@GetMapping("/uploads/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception {
		return filesServiceImp.downloadFiles(fileName, request);
	}
}
