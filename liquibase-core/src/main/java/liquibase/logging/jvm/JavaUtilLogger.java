package liquibase.logging.jvm;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.logging.core.AbstractLogger;
import liquibase.logging.LogLevel;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.*;

public class JavaUtilLogger extends AbstractLogger {

    private java.util.logging.Logger logger;

    public int getPriority() {
        return 5;
    }

    public void setName(String name) {
        logger = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public void setLogLevel(LogLevel logLevel) {
        if (logger.getHandlers().length == 0) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(getFormatter());
            logger.addHandler(consoleHandler);
            logger.setUseParentHandlers(false);
        }
        
        if (logLevel == LogLevel.DEBUG) {
            logger.setLevel(Level.FINEST);
        } else if (logLevel == LogLevel.INFO) {
            logger.setLevel(Level.INFO);
        } else if (logLevel == LogLevel.WARNING) {
            logger.setLevel(Level.WARNING);
        } else if (logLevel == LogLevel.SEVERE) {
            logger.setLevel(Level.SEVERE);
        } else if (logLevel == LogLevel.OFF) {
            logger.setLevel(Level.OFF);
        } else {
            throw new UnexpectedLiquibaseException("Unknown log level: " + logLevel);
        }
        super.setLogLevel(logLevel);
    }


    /**
     * @param logLevel
     * @param logFile
     */
    public void setLogLevel(String logLevel, String logFile) {
        Handler fH;

        try {
            fH = new FileHandler(logFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open log file " + logFile + ". Reason: " + e.getMessage());
        }

        fH.setFormatter(getFormatter());
        logger.addHandler(fH);
        logger.setUseParentHandlers(false);
        setLogLevel(logLevel);

    }

    private Formatter getFormatter() {
        return new Formatter(){
            @Override
            public String format(LogRecord record) {
                return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(record.getMillis()))+" "+record.getLoggerName()+":"+record.getLevel().getName()+": "+record.getMessage()+ System.getProperty("line.separator");
            }
        };
    }

    public void severe(String message) {
        logger.severe(message);
    }

    public void severe(String message, Throwable e) {
        logger.log(Level.SEVERE, message, e);
    }

    public void warning(String message) {
        logger.warning(message);
    }

    public void warning(String message, Throwable e) {
        logger.log(Level.WARNING, message, e);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Throwable e) {
        logger.log(Level.INFO, message, e);
    }

    public void debug(String message) {
        logger.finest(message);

    }

    public void debug(String message, Throwable e) {
        logger.log(Level.FINEST, message, e);
    }

    public Handler[] getHandlers() {
        return logger.getHandlers();
    }

    public void removeHandler(Handler handler) {
        logger.removeHandler(handler);
    }

    public void addHandler(Handler handler) {
        logger.addHandler(handler);
    }

    public void setUseParentHandlers(boolean b) {
        logger.setUseParentHandlers(b);
    }
}
