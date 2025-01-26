package com.t360.game.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: adelramezani.jd@gmail.com
 *
 */
public class ReplyMessageProvider {
    private final static Logger logger = LoggerFactory.getLogger(ReplyMessageProvider.class);
    public static String parseAndReply(String message) {
        Pattern pattern2No = Pattern.compile("[\\w\\s]+(\\d+)\\s+(\\d+)$");
        Pattern pattern1No = Pattern.compile("[\\w\\s]+(\\d+)$");
        Matcher matcher = pattern2No.matcher(message);
        if(matcher.find()){
            int lastMineNo = Integer.parseInt(matcher.group(1));
            logger.trace("The message was repeated for %d.".formatted(lastMineNo+1));
            return message+" "+(lastMineNo+1);
        }else{
            matcher = pattern1No.matcher(message);
            if(matcher.find()) {
                logger.trace("The message received first.");
                return message+" 1";
            }else{
                return message+" 0";
            }
        }
    }
}
