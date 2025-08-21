package com.example.mydocuments.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mydocuments.model.FileInfo;

import jakarta.transaction.Transactional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Integer> {

    Optional<FileInfo> findByFileName(String filename);
    boolean existsByFileName(String filename);
    @Transactional
    void deleteByFileName(String filename);
    
}
