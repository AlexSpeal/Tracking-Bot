package org.example.dto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkParser {
    private LinkParser() {

    }

    private static final String REGEX_OWNER = "https://github\\.com\\/([a-zA-Z0-9-_]+)\\/[a-zA-Z0-9-_]+\\/?";
    private static final String REGEX_REPO = "https://github\\.com\\/[a-zA-Z0-9-_]+\\/([a-zA-Z0-9-_]+)\\/?";
    private static final String REGEX_QUESTION_ID = "https://stackoverflow\\.com/questions/(\\d+)/([\\w-]+)";

    public static String getGitHubOwner(URI url) {
        return urlMatcher(url, REGEX_OWNER);
    }

    public static long getQuestionId(URI url) {
        String res = urlMatcher(url, REGEX_QUESTION_ID);
        return res.isEmpty() ? 0L : Long.parseLong(res);
    }

    public static String getGitHubRepo(URI url) {
        return urlMatcher(url, REGEX_REPO);
    }

    public static boolean check(String url) {
        URI link;
        try {
            link = new URI(url);
        } catch (URISyntaxException e) {
            return false;
        }
        return !urlMatcher(link, REGEX_OWNER).isEmpty() || !urlMatcher(link, REGEX_QUESTION_ID).isEmpty();
    }

    private static String urlMatcher(URI url, String regex) {
        String link = url.toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
