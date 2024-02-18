package edu.java.bot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class CheckerURL {
    private CheckerURL() {
    }

    private static final List<String> TRUE_URLS = List.of("https://stackoverflow.com/", "https://github.com/");

    private static boolean checkUrls(String link) {
        return !TRUE_URLS.stream()
            .filter(link::startsWith)
            .toList()
            .isEmpty();
    }

    public static boolean check(String link) {
        try {
            URL islink = new URL(link);
            return checkUrls(link);
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
