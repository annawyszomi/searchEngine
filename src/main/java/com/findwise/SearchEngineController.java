package com.findwise;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class SearchEngineController {
    private final SearchEngine searchEngine;

    @PostMapping("documents")
    public ResponseEntity<?> addDocuments(@RequestBody List<Doc> docs) {
        searchEngine.insertData(docs);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("{term}")
    public ResponseEntity<List<IndexEntry>> getSearchResult(@PathVariable String term) {
        try {
            List<IndexEntry> list = searchEngine.search(term);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "term: " + term + " not found in the documents", exception);
        }

    }

}
