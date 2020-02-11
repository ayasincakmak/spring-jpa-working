package com.vineet.learnspringjpa.service.imp;

import com.vineet.learnspringjpa.model.request.SampleRequest;
import com.vineet.learnspringjpa.model.response.SampleResponse;
import com.vineet.learnspringjpa.model.response.SampleResponse.SampleBuilder;
import com.vineet.learnspringjpa.repository.SampleRepository;
import com.vineet.learnspringjpa.service.SampleService;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vineet.learnspringjpa.entity.*;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
	private SampleRepository sampleRepository;
	
	
	@PostConstruct void SampleServiceImpl() {
		find("abc");
	}
	
	public SampleResponse save(SampleRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public SampleResponse find(String name) {
		// TODO Auto-generated method stub
		SampleBuilder builder = new SampleBuilder();
		
		Optional<SampleEntity> optional = sampleRepository.findByName(name);
		
		optional.ifPresent(entity -> builder.
				withId(entity.getId()).
				withName(entity.getName()).
				withCity(entity.getCity()));
		
		System.out.println("--------------> " + builder.toString());
		return new SampleResponse(builder);
	}

	public SampleResponse update(SampleRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
