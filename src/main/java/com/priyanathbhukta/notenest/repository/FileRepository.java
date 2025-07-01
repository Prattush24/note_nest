package com.priyanathbhukta.notenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.priyanathbhukta.notenest.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails, Long> {

}
