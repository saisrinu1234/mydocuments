package com.example.mydocuments.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mydocuments.Repository.FileInfoRepository;
import com.example.mydocuments.model.FileInfo;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class FileInfoController {
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename) {

        if (fileInfoRepository.existsByFileName(filename)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("FileName Already Exists");
        }
        try {
            FileInfo fileInfo = new FileInfo();
            byte[] originalfile = file.getBytes();
            fileInfo.setImage(originalfile);
            fileInfo.setFileName(filename);
            fileInfoRepository.save(fileInfo);
            return ResponseEntity.ok("File Uploded Successfully");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }

    }


    @GetMapping("/getall")
    public ResponseEntity<?> getAllDocuments() {
        List<FileInfo> allDocuments = fileInfoRepository.findAll();
        return ResponseEntity.ok(allDocuments);
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<String> update(@RequestParam("filename") String filename, @RequestParam("newfilename") String newfilename) {

        if (fileInfoRepository.existsByFileName(newfilename)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("FileName Already Exists");
        }

        try {
            Optional<FileInfo> optionalFileInfo = fileInfoRepository.findByFileName(filename);

            if (optionalFileInfo.isPresent()) {
                FileInfo fileInfo = optionalFileInfo.get(); // Get the actual entity

                
                fileInfo.setFileName(newfilename);

                fileInfoRepository.save(fileInfo);

                return ResponseEntity.ok("File Uploaded Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + filename);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<?> deleteDocument(@PathVariable String filename) {
        if (fileInfoRepository.existsByFileName(filename)) {
            fileInfoRepository.deleteByFileName(filename);
            return ResponseEntity.ok("File Deletion Successful");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not Found");
        }
    }

}
