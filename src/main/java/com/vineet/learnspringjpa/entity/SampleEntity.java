package com.vineet.learnspringjpa.entity;

import javax.persistence.*;

@Entity( name = "LearnSpringBasics")
@Table(name = "learn_spring_basics")
public class SampleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name= "name")
	private String name;
	
	@Column(name = "city")
	private String city;
	
	
	public SampleEntity() {
		
	}
	
	public SampleEntity(final SampleEntityBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.city = builder.city;
	}
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}



	public static class SampleEntityBuilder{
		private Integer id;
		private String name;
		private String city;
		
		public SampleEntityBuilder withId(Integer id) {
			this.id = id;
			return this;
		}
		
		public SampleEntityBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public SampleEntityBuilder withCity(String city) {
			this.city = city;
			return this;
		}
		
		public SampleEntity build() {
			return new SampleEntity(this);
		}
		
	}
	
	
}
