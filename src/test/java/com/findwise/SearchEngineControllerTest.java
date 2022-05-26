package com.findwise;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(SearchEngineController.class)
class SearchEngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchEngine searchEngine;

    SearchEngineImpl searchEngineImpl = mock(SearchEngineImpl.class);

    @Test
    void shouldGetListOfIndexEntries() throws Exception {

        List<IndexEntry> entries = List.of(new IndexEntryImpl("doc2",0.0506));

        when(searchEngine.search("term")).thenReturn(entries);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/term")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("doc2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].score", Matchers.is(0.0506)));
    }
}