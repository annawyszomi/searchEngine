package com.findwise;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchEngineApplicationTests {
    private static final SearchEngineImpl searchEngine = new SearchEngineImpl();

    @BeforeAll
    static void setUp() {
        var content1 = "the brown fox jumped over the brown dog";
        var content2 = "the lazy brown dog sat in the corner";
        var content3 = "the red fox bit the lazy dog";

        searchEngine.indexDocument("doc1", content1);
        searchEngine.indexDocument("doc2", content2);
        searchEngine.indexDocument("doc3", content3);
    }

    @Test
    void shouldCalculateScoreForTermFoxInDoc1() {
        var result = searchEngine.calculateScore("doc1", "fox");
        var expectedSore = 0.05068313851352055;

        assertEquals(expectedSore, result);
    }

    @Test
    void shouldCalculateScoreForTermFoxInDoc3() {
        var result = searchEngine.calculateScore("doc3", "fox");
        var expectedSore = 0.05792358687259491;

        assertEquals(expectedSore, result);
    }

    @Test
    void shouldCalculateScoreForTermBrownInDoc1() {
        var result = searchEngine.calculateScore("doc1", "brown");
        var expectedSore = 0.1013662770270411;

        assertEquals(expectedSore, result);
    }

    @Test
    void shouldCalculateScoreForTermBrownInDoc3() {
        var result = searchEngine.calculateScore("doc3", "brown");
        var expectedSore = 0.0;

        assertEquals(expectedSore, result);
    }

    @Test
    void shoudlReturnSearchForTermBrown() {
        List<IndexEntry> list = searchEngine.search("brown");

        var firstResult = list.get(0).getId();
        var secondResult = list.get(1).getId();
        var expectedResult1 = "doc2";
        var expectedResult2 = "doc1";

        assertEquals(expectedResult1, firstResult);
        assertEquals(expectedResult2, secondResult);
    }

    @Test
    void shoudlReturnSearchForTermFox() {

        List<IndexEntry> list = searchEngine.search("fox");
        var firstResult = list.get(0).getId();
        var secondResult = list.get(1).getId();
        var expectedResult1 = "doc1";
        var expectedResult2 = "doc3";
        assertEquals(expectedResult1, firstResult);
        assertEquals(expectedResult2, secondResult);
    }
}
