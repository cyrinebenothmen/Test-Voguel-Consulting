package com.cyrine.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cyrine.configuration.UploadFileResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

@Service
public interface FilesService {
	
	String storeFile(MultipartFile file);
	Resource loadFileAsResource(String fileName) throws Exception;
	UploadFileResponse uploadSingleFile(MultipartFile file);
	List<UploadFileResponse> uploadMultipleFiles(MultipartFile[] files);
	ResponseEntity<Resource> downloadFiles(String fileName, HttpServletRequest request) throws Exception;
	
//	void init();
//
//	void store(MultipartFile file);
//
//	Stream<Path> uploadAll();
//
//	Path upload(String filename);
//
//	Resource uploadAsResource(String filename);
}
