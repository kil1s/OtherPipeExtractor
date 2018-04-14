package org.schabi.newpipe.extractor.url.helper;

import org.junit.Test;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.url.model.UrlParsingFeature;
import org.schabi.newpipe.extractor.url.model.UrlQuery;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.*;

public class UrlParsingHelperTest {

    @Test
    public void parse() {
    }

    @Test
    public void parseTillPort() {
    }

    @Test
    public void getQueriesFromUrl() {
        String all = "foo://example.com:8042/over/there?name=ferret&other&another#foo://example.com:8042/over/there#?name=ferret&other&anothh#!ando";
        try {
            List<UrlQuery> queries = UrlParsingHelper.getQueriesFromUrl(all, Encodings.UTF_8, UrlParsingFeature.values());
            System.out.print("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getQueriesFromRawRequest() {
    }

    @Test
    public void getRawRequestFromUrl() {
    }

    @Test
    public void getRawQueriesFromRequest() {
    }

    @Test
    public void getRawQueryFromFirstQuery() {
    }

    @Test
    public void getQueriesFromRequestPath() {
    }

    @Test
    public void getQueriesFromDomainFilePath() {
    }

    @Test
    public void getUrlParamsQueryFromRawQuery() {
    }

    @Test
    public void getPartsFromPath() {
    }
}