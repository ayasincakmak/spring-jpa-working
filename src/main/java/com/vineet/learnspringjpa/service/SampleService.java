package com.vineet.learnspringjpa.service;

import com.vineet.learnspringjpa.model.request.SampleRequest;
import com.vineet.learnspringjpa.model.response.SampleResponse;

public interface SampleService {
	
	SampleResponse save(SampleRequest request);
	
	SampleResponse find(String name);
	
	SampleResponse update(SampleRequest request);
}
