package com.thy.emd.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@ManagedBean
@SessionScoped
public class ConfigurationService implements Serializable {
	private static final long serialVersionUID = 4744195706882610062L;
	private Map<String, String> map;

//	@Inject
	// private ConfigurationDataAccessService configurationDataAccessService;

	public ConfigurationService() {
		// default kurucu
	}

	public String getThyAppDirectory() {
		return System.getProperty("thy.appdir");
	}

	@PostConstruct
	public void init() {

		load();
	}

	public void load() {

		map = new HashMap<>();

		try {
			String path = getThyAppDirectory() + File.separatorChar + "emd-console" + File.separatorChar + "conf" + File.separatorChar + "project-config.properties";

			Properties properties = new Properties();

			try (FileInputStream fis = new FileInputStream(new File(path))) {
				properties.load(fis);
				for (String key : properties.stringPropertyNames()) {
					map.put(key, properties.getProperty(key));
				}
			}

		} catch (Exception e) {
			// Logger.getLogger(ConfigurationService.class.getName()).error("load", e);
		}

		try {
//			Configuration confInDB = configurationDataAccessService.getData();
//			if (confInDB != null && confInDB.getData() != null) {
//				for (ConfigurationItem item : confInDB.getData()) {
//					map.put(item.getConfigurationKey(), item.getConfigurationValue());
//				}
//			}

		} catch (Exception e) {
			// Logger.getLogger(ConfigurationService.class.getName()).error("configurationDataAccessService.getData()",
			// e);
		}

	}

	public String getConfigurationValue(String name) {

		if (map == null) {
			load();
		}

		return map.get(name);
	}

	public String getConfigurationValue(String name, String defaultValue) {

		String value = getConfigurationValue(name);

		if (value == null || value.isEmpty()) {
			return defaultValue;
		}

		return value;
	}

	public Boolean getConfigurationBooleanValue(String name, String defaultValue) {
		String value = getConfigurationValue(name);
		if (value == null || value.isEmpty()) {
			return false;
		}
		if (value.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}

	}

	private List<String> getStringListValue(String key) {
		String val = (String) map.get(key);
		if (val == null) {
			return null;
		} else {
			String att[] = val.trim().split(";");
			if (att.length < 1) {
				return null;
			} else {
				ArrayList<String> ll = new ArrayList<String>();
				for (String att1 : att) {
					if (att1 != null && !att1.trim().isEmpty()) {
						ll.add(att1.trim());
					}
				}
				if (ll.isEmpty()) {
					return null;
				} else {
					return ll;
				}
			}
		}
	}

	public String getStage() {
		return getConfigurationValue("stage");
	}

	public String getEnv() {
		return getConfigurationValue("env");
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getMessageQueueConnectorPoolName() {
		return getConfigurationValue("MESSAGE_QUEUE_CONNECTOR_POOL_NAME", "");
	}

	public String getMessageQueueCriPoolName() {

		return getConfigurationValue("MESSAGE_QUEUE_CRI_POOL_NAME", "");
	}

	public List<String> getMessageQueueSessionInitializationEntries() {

		return getStringListValue("MESSAGE_QUEUE_SESSION_INITIALIZATION_ENTRIES");
	}

	public Boolean getMessageQueueCloseQmAfterExecution() {

		return getConfigurationBooleanValue("MESSAGE_QUEUE_CLOSE_QM_AFTER_EXECUTION", "");
	}

}
