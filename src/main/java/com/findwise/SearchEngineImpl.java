package com.findwise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchEngineImpl implements SearchEngine {

    private final Map<String, List<String>> documents = new HashMap<>();
    private final Map<String, Set<String>> termsMap = new TreeMap<>();
    private final Map<String, Integer> termFrequency = new TreeMap<>();

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
            throw new IllegalArgumentException("term: "+ term + " not found");
        }
        var filteredDocList = termsMap.get(term);

        return filteredDocList.stream().map(document -> mapDocumentToIndexEntry(document, term))
                .sorted(Comparator.comparingDouble(IndexEntry::getScore))
                .toList();
    }

    public void insertData(List<Doc> docs) {
        for (Doc doc : docs) {
            indexDocument(doc.getId(), doc.getContent());
        }
    }

    public double calculateScore(String id, String term) {
        long termInDoc = documents.get(id).stream().filter(term::equals).count();
        double sizeOfDoc = documents.get(id).size();
        long nrOfDocuments = documents.size();
        double sizeOfTermFrequencyMap = termFrequency.get(term);
        double tf = termInDoc / sizeOfDoc;
        double idf = Math.log(nrOfDocuments / (sizeOfTermFrequencyMap));

        return tf * idf;
    }

    private List<String> tokenizeDocument(String doc) {
        return Arrays.stream(doc.split("\\W+")).toList();
    }

    private IndexEntry mapDocumentToIndexEntry(String id, String term) {
        var entry = new IndexEntryImpl();
        entry.setId(id);
        double score = calculateScore(id, term);
        entry.setScore(score);
        return entry;
    }
}
