package org.example.dto;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkParser {
    private LinkParser() {

    }

    private static final String REGEX_OWNER = "^https://github\\.com\\/([a-zA-Z0-9-_]+)\\/[a-zA-Z0-9-_]+\\/?$";
    private static final String REGEX_REPO = "^https://github\\.com\\/[a-zA-Z0-9-_]+\\/([a-zA-Z0-9-_]+)\\/?$";
    private static final String REGEX_QUESTION_ID = "^https://stackoverflow\\.com/questions/(\\d+)/([\\w-]+)$";
    public static final int STATUS_OK = 200;

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

    public static boolean check(String uri) {
        URL link;
        boolean result = false;
        try {
            link = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == STATUS_OK) {
                return !urlMatcher(link.toURI(), REGEX_OWNER).isEmpty()
                    || !urlMatcher(link.toURI(), REGEX_QUESTION_ID).isEmpty();
            }
        } catch (Exception ignored) {

        }
        return result;

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
