package mx.izzi.offboarding.shared.utils;

public class Logger {

    public static void info(org.slf4j.Logger logger, String message) {
        logger.info("[INFO]: {}", message);
    }
}
