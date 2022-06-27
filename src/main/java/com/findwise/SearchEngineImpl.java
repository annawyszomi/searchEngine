package com.findwise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SearchEngineImpl implements SearchEngine {

    private final Map<String, List<String>> documents = new HashMap<>();
    private final Map<String, Set<String>> termsMap = new HashMap<>();
    private final Map<String, Integer> termFrequency = new HashMap<>();

    @Override
    public void indexDocument(String id, String content) {
        Set<String> docsIds;
        List<String> terms = tokenizeDocument(content.toLowerCase());
        documents.put(id, terms);

        for (var term : terms) {
            if (!termsMap.containsKey(term)) {
                docsIds = new HashSet<>();
                docsIds.add(id);
                termsMap.put(term, docsIds);
            } else {
                docsIds = termsMap.get(term);
                docsIds.add(id);
            }
            var sizeOfTermFrequencyMap = termsMap.get(term).size();
            termFrequency.put(term, sizeOfTermFrequencyMap);
        }
    }

    @Override
    public List<IndexEntry> search(String term) {
        if (term == null || !termsMap.containsKey(term)) {
            throw new IllegalArgumentException("term: " + term + " not found");
        }
        var filteredDocList = termsMap.get(term);

        return filteredDocList.stream().map(document -> mapDocumentToIndexEntry(document, term))
                .sorted(Comparator.comparingDouble(IndexEntry::getScore))
                .toList();
    }

    public void insertData(List<Doc> docs) {
        docs.forEach(doc -> indexDocument(doc.getId(), doc.getContent()));
    }

    public double calculateScore(String id, String term) {
        var termInDoc = documents.get(id).stream().filter(term::equals).count();
        double sizeOfDoc = documents.get(id).size();
        double nrOfDocuments = documents.size();
        var sizeOfTermFrequencyMap = termFrequency.get(term);
        var tf = termInDoc / sizeOfDoc;
        var idf = Math.log(nrOfDocuments / (sizeOfTermFrequencyMap));

        return tf * idf;
    }

    private List<String> tokenizeDocument(String doc) {
        return Arrays.stream(doc.split("\\W+")).toList();
    }

    private IndexEntry mapDocumentToIndexEntry(String id, String term) {
        var entry = new IndexEntryImpl();
        entry.setId(id);
        var score = calculateScore(id, term);
        entry.setScore(score);
        return entry;
    }
}
