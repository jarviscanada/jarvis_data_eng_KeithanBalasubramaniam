package ca.jrvs.apps.grep.practice;

import java.util.regex.*;

public class RegexExcImp implements RegexExc {
    /**
     * return true if filename extension is jpg or jpeg (case insensitive)
     *
     * @param filename
     * @return
     */
    @Override
    public boolean matchJpeg(String filename) {
        Pattern jpegPattern = Pattern.compile("\\S+\\.(jpg|jpeg)\\b");
        Matcher jpegMatch = jpegPattern.matcher(filename);
        return jpegMatch.matches();
    }

    /**
     * return true if ip is valid
     * to simplify the problem, IP address rang is from 0.0.0.0 to 999.999.999.999
     *
     * @param ip
     * @return
     */
    @Override
    public boolean matchIp(String ip) {
        Pattern ipPattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");
        Matcher ipMatch = ipPattern.matcher(ip);
        return ipMatch.matches();
    }

    /**
     * return true if line is empty (e.g. empty, white space, tabs, etc...)
     *
     * @param line
     * @return
     */
    @Override
    public boolean isEmptyLine(String line) {
        Pattern emptyPattern = Pattern.compile("^[ \\t\\n]*$");
        Matcher emptyMatch = emptyPattern.matcher(line);
        return emptyMatch.matches();
    }
}
