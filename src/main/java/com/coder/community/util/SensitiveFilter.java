package com.coder.community.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    
    private static final String REPLACEMENT = "***";

    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("Load sensitive words fail", e.getMessage());
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;

            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * Filter sensitive words
     * 
     * @param text Text before filter
     * @return Filtered text
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // Pointer 1
        TrieNode tempNode = root;
        // Pointer 2
        int begin = 0;
        // Pointer 3
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {
                // Pointer 1 at root
                if (tempNode == root) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // Word start from begin is not sensitive
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = root; 
            } else if (tempNode.isKeyWordEnd()) {
                // Word start from begin is sensitive
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = root;
            } else {
                // Check next char
                position++;
            }
        }

        // Put last char into string builder
        sb.append(text.substring(begin));
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {
        private boolean isKeyWordEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }
        public void setKeyWordEnd(boolean isKeyWordEnd) {
            this.isKeyWordEnd = isKeyWordEnd;
        }
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
