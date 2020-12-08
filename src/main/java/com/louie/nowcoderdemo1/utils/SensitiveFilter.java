package com.louie.nowcoderdemo1.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        ) {
            String keyword;
            while ((keyword = bufferedReader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载文件失败： " + e.getMessage());
        }
    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                //init sub node
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // pointer at sub node, enter next loop
            tempNode = subNode;

            //set end signature
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * @param text before filter
     * @return text after filer
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // pointer 1
        TrieNode tempNode = rootNode;
        // pointer 2
        int begin = 0;
        // pointer 3
        int position = 0;

        StringBuilder stringBuilder = new StringBuilder();

        while (begin < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {
                //if pointer 1() is at root
                if (tempNode == rootNode) {
                    stringBuilder.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            //check sub node
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //word starts with 'begin' is not sensitive
                stringBuilder.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                stringBuilder.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            }else {
                //check the next character
                if (position < text.length() - 1) {
                    position++;
                }
            }

        }
        return stringBuilder.toString();
    }

    //
    private boolean isSymbol(Character character) {
        return !CharUtils.isAsciiAlphanumeric(character) && (character < 0x2E80 || character > 0x9FFF);
    }

    private class TrieNode {

        //keyword end signature
        private boolean isKeywordEnd = false;

        //child node
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //add child node
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //get child node
        private TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
