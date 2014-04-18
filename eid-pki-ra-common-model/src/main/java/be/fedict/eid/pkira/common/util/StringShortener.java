package be.fedict.eid.pkira.common.util;

public class StringShortener {

    public static String shorten(String s, int maxLength) {
        if (s==null || s.length()<=maxLength) return s;
        return s.substring(0, maxLength) + "...";
    }
}
