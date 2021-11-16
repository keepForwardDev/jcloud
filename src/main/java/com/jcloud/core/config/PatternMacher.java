package com.jcloud.core.config;

import org.springframework.util.PathMatcher;
import org.springframework.util.PatternMatchUtils;

import java.util.Comparator;
import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/11/16
 */
public class PatternMacher implements PathMatcher {
    @Override
    public boolean isPattern(String s) {
        return true;
    }

    @Override
    public boolean match(String pattern, String path) {
        return PatternMatchUtils.simpleMatch(pattern, path);
    }

    @Override
    public boolean matchStart(String s, String s1) {
        return PatternMatchUtils.simpleMatch(s, s1);
    }

    @Override
    public String extractPathWithinPattern(String s, String s1) {
        return null;
    }

    @Override
    public Map<String, String> extractUriTemplateVariables(String s, String s1) {
        return null;
    }

    @Override
    public Comparator<String> getPatternComparator(String s) {
        return null;
    }

    @Override
    public String combine(String s, String s1) {
        return null;
    }
}
