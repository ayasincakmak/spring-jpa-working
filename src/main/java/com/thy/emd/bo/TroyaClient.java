package com.thy.emd.bo;

import com.thy.mq.unified.MQConnectionManager;
import com.thy.mq.unified.config.FileMQUnifiedConfigurationLoader;
import com.thy.mq.unified.config.MQUnifiedConfiguration;
import com.thy.mq.unified.log.MQUnifiedLog;
import com.thy.mq.unified.troyaAPI.TroyaTerminalMQUnifiedImpl;
import com.thy.troyaapi.core.EntryValidationException;
import com.thy.troyaapi.core.StartupException;
import com.thy.troyaapi.core.TroyaResponseException;
import com.thy.troyaapi.core.TroyaUser;
import com.thy.troyaapi.core.terminal.TroyaTerminalException;
import com.thy.troyaapi.cripool.CriPoolException;
import com.thy.troyaapi.cripool.CriSessionManager;
import com.thy.troyaapi.cripool.CriSessionManagerException;
import com.thy.troyaapi.cripool.config.CriPoolConfigurationException;
import com.thy.troyaapi.cripool.config.CriSessionManagerConfiguration;
import com.thy.troyaapi.cripool.config.FileCriSessionManagerConfigurationLoader;
import com.thy.troyaapi.cripool.logging.CriPoolLog;
import com.thy.troyaapi.logging.TroyaAPILog;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author NETACAKMAK
 */
@Named
public  class TroyaClient implements Serializable {
	
	@Autowired
	private ConfigurationService configurationService;

	private static final long serialVersionUID = 786270745876712447L;

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(TroyaClient.class);

	private static String mqUnifiedConfigFilePath;
	private static String criPoolConfigFilePath;

	private TroyaTerminalMQUnifiedImpl terminal;
	private TroyaUser user;

	private long LastInitializationTime = 0L;
	private long LastAccessedTime = 0L;

	
	public TroyaClient() {
		// default kurucu
	}



	@PostConstruct
	public void init() {
		setFilesPath();
	}
	
	public void setFilesPath() {
		StringBuilder path = new StringBuilder();
		path.append(System.getProperty("thy.appdir"));
		path.append(File.separator);
		path.append("emd-console");
		path.append(File.separator);
		path.append("conf");
		path.append(File.separator);
		this.criPoolConfigFilePath = path.toString() + "cri-config.xml";
		this.mqUnifiedConfigFilePath = path.toString() + "mq-unified-config.xml";

	}

	public synchronized String execute(String entry) throws EntryValidationException, CriPoolException, TroyaTerminalException, TroyaResponseException, StartupException, CriSessionManagerException {
		String ret = null;
		do {
			initialize();
			try {
				ret = user.enter(entry);
				break;
			} catch (TroyaTerminalException e) {
				/// logger.error(e.getMessage(), e);
				release(true);
				try {
					Thread.sleep(1000);
				} catch (Exception tmp) {
				}
			}
		} while (true);
		LastAccessedTime = System.currentTimeMillis();
		return ret;
	}

	public long getLastAccessedTime() {
		return LastAccessedTime;
	}

	public long getLastInitializationTime() {
		return LastInitializationTime;
	}

	private synchronized void initialize() throws CriPoolConfigurationException, CriPoolException, TroyaTerminalException, TroyaResponseException, StartupException, CriSessionManagerException {
		boolean initializationNeeded = false;
		try {
			if (terminal == null) {
				initializationNeeded = true;
			} else if (user == null) {
				initializationNeeded = true;
			} else if (!user.isInSession()) {
				release(true);
				initializationNeeded = true;
			} else if (System.currentTimeMillis() - LastInitializationTime > 600000) { // 10 mins
				release(true);
				initializationNeeded = true;
			}
		} catch (Exception e) {
			initializationNeeded = true;
		}
		if (true) {
			TroyaAPILog.setLogger(new MQLogger());
			MQUnifiedLog.setInstance(new MQLogger());
			CriPoolLog.setLogger(new MQLogger());

			MQUnifiedConfiguration.setConfigurationLoader(new FileMQUnifiedConfigurationLoader(mqUnifiedConfigFilePath));
			CriSessionManagerConfiguration.getInstance().loadConfiguration(new FileCriSessionManagerConfigurationLoader(new File(criPoolConfigFilePath)));

			try {
				MQConnectionManager.getInstance();
			} catch (IllegalStateException ise) {
				MQConnectionManager.reinitialize();
				CriSessionManager.reinitialize();
			}

			terminal = new TroyaTerminalMQUnifiedImpl(configurationService.getMessageQueueConnectorPoolName(), CriSessionManager.getInstance().getPool(configurationService.getMessageQueueCriPoolName()));
			user = new TroyaUser();
			user.assignTerminal(terminal);
			user.startSession();
			List<String> entries = configurationService.getMessageQueueSessionInitializationEntries();
			if (entries != null) {
				for (int i = 0; i < entries.size(); i++) {
					user.enter(entries.get(i));
				}
			}

			LastInitializationTime = System.currentTimeMillis();
		}

	}

	private synchronized void release(boolean shutdownQueueManager) {
		if (user != null) {
			try {
				user.endSession();
				user = null;
			} catch (TroyaTerminalException | RuntimeException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		if (terminal != null) {
			try {
				terminal.close();
				terminal = null;
			} catch (RuntimeException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		if (configurationService.getMessageQueueCloseQmAfterExecution() && shutdownQueueManager) {
			try {
				MQConnectionManager.getInstance().shutdown();
				// logger.info("QueueManager has been closed.");
			} catch (RuntimeException ex) {
				// logger.error(ex.getMessage(), ex);
			}
			try {
				CriSessionManager.getInstance().shutdown();
				// logger.info("CriSessionManager has been closed.");
			} catch (RuntimeException ex) {
				// logger.error(ex.getMessage(), ex);
			}
		}
	}

}
