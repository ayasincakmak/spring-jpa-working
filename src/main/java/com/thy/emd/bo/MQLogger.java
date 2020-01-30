package com.thy.emd.bo;

import org.slf4j.LoggerFactory;

/**
 *
 * @author M_TURKERI
 */
public final class MQLogger implements com.thy.troyaapi.logging.Logger, com.thy.mq.unified.log.Logger, com.thy.troyaapi.cripool.logging.Logger {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TroyaClient.class);

    public MQLogger() {
    }

    @Override
    public void log(String message, Throwable exception, int loggingLevel) {
        if (loggingLevel >= 16) {
            logger.error(message, exception);
        } else if (loggingLevel >= 12) {
            logger.warn(message, exception);
        } else if (loggingLevel >= 8) {
            logger.info(message, exception);
        } else {
            logger.debug(message, exception);
        }
    }

    @Override
    public void log(String message, int loggingLevel) {
        if (loggingLevel >= 16) {
            logger.error(message);
        } else if (loggingLevel >= 12) {
            logger.warn(message);
        } else if (loggingLevel >= 8) {
            logger.info(message);
        } else {
            logger.debug(message);
        }
    }
}
