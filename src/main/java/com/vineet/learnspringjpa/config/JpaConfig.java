package com.vineet.learnspringjpa.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import static java.lang.Boolean.FALSE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "daoEntityManagerFactory", transactionManagerRef = "daoTransactionManager", basePackages = {
		"com.vineet.learnspringjpa.repository" })

@EnableTransactionManagement

public class JpaConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean daoEntityManagerFactory(
			final EclipseLinkJpaVendorAdapter eclipseLinkJpaVendorAdapter, final DataSource daoDataSource,
			final Properties jpaProperties) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPersistenceUnitName("spring-learn-table");
		factory.setJpaVendorAdapter(eclipseLinkJpaVendorAdapter);
		factory.setPackagesToScan("com.vineet.learnspringjpa.entity");
		factory.setDataSource(daoDataSource);
		factory.setJpaProperties(jpaProperties);
		return factory;

	}

	
	@Bean
	public EclipseLinkJpaVendorAdapter eclipseLinkVendorAdapter(
			@Value("${dao.db.show.sql:true}") final boolean showSql) {
		EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
		adapter.setGenerateDdl(true);
		adapter.setShowSql(showSql);
		return adapter;
		
	}
	
	
	
	
	
	@Bean
	public DataSource dapDataSource(
			@Value("${dao.db.driver.class}") final String driverClassName,
			@Value("${dao.db.url}") final String url,
			@Value("${dao.db.userName:{null}}") final String username,
			@Value("${dao.db.password:{null}}") final String password,
			@Value("${dao.db.intial.connection.pool.size:3}") final int initialConnectionPoolSize,
			@Value("${dao.db.max.connection.pool.size:10}") final int maxConnectionPoolSize) {

		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(initialConnectionPoolSize);
		ds.setMaxActive(maxConnectionPoolSize);

		return ds;

	}
	
	
	@Bean
	@Autowired
	public PlatformTransactionManager daoTransactionManager(
			final LocalContainerEntityManagerFactoryBean daoEntityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(daoEntityManagerFactory.getObject());
		
		return jpaTransactionManager;
	}

	
@Bean
public Properties jpaProperties(
		@Value("${dao.db.target.database}") final String targetDatabase,
		@Value("${dao.db.logging.level:INFO}") final String loggingLevel
		) {
	Properties p = new Properties();
	p.setProperty(TARGET_DATABASE, targetDatabase);
	p.setProperty(LOGGING_LEVEL, loggingLevel);
	p.setProperty(WEAVING, FALSE.toString());
	
	return p;
	
}	
}
