package com.findwise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexEntryImpl implements IndexEntry {
    private String id;
    private double score;
}
