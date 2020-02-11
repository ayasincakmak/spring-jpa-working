package com.vineet.learnspringjpa.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.google.common.collect.ImmutableBiMap.of;


@JsonInclude(value = NON_EMPTY)
public class SampleResponse {

	private Integer id;
	private String name;
	private String city;
	
	public static class SampleBuilder{
		private Integer id;
		private String name;
		private String city;
		
		public SampleBuilder withId(Integer id) {
			this.id = id;
			return this;
		}
		
		public SampleBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public SampleBuilder withCity(String city) {
			this.city = city;
			return this;
		}
		public SampleResponse build() {
			return new SampleResponse(this);
		}

		@Override
		public String toString() {
			return "Lets Print SampleBuilder as :---- >>>  [id=" + id + ", name=" + name + ", city=" + city + "]";
		}
		
		
	}
	
	
	public SampleResponse (final SampleBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.city = builder.name;
	}


	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getCity() {
		return city;
	}
	
	
}
