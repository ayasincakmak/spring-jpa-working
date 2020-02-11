package com.vineet.learnspringjpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vineet.learnspringjpa.entity.SampleEntity;

public interface SampleRepository extends JpaRepository<SampleEntity, Integer>{

	Optional<SampleEntity> findByName(String name);
	
}
