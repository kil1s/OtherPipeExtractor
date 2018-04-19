package org.schabi.newpipe.extractor.url.helper;

import org.junit.Test;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.extractor.url.model.UrlParsingFeature;
import org.schabi.newpipe.extractor.url.model.UrlQuery;
import org.schabi.newpipe.extractor.url.model.UrlQueryState;
import org.schabi.newpipe.extractor.url.model.list.UrlQueryList;
import org.schabi.newpipe.extractor.url.model.list.domain.UrlDomainPublic;
import org.schabi.newpipe.extractor.url.model.protocol.wellknown.WellKnownProtocolHelper;
import org.schabi.newpipe.extractor.url.model.protocol.wellknown.model.UrlUnknownProtocol;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HashMap<String, Object[][]> requirements = new HashMap<String, Object[][]>();

        UrlUnknownProtocol protocol = new UrlUnknownProtocol("FOO", WellKnownProtocolHelper.UNKNOWN.getReadableName(), UrlQueryState.PUBLIC, 8042);
        UrlQueryList<String> = new UrlQueryList<String>();
        requirements.put(
                "foo://example.com:8042/over/there?name=ferret&other&another#foo://example.com:8042/over/there#?name=ferret&other&anothh#!ando",
                new Object[][]{
                        {
                            protocol.getClass(),
                            protocol
                        },
                        {
                            UrlDomainPublic<String>.class,

                        }
                }
        );

        try {
            for (Map.Entry<String, Object[][]> entry:requirements.entrySet()) {
                Object[][] args = entry.getValue();
                List<UrlQuery> queries = UrlParsingHelper.getQueriesFromUrl(entry.getKey(), Encodings.UTF_8, UrlParsingFeature.values());

                //assertEquals(args.length, queries.size());
                for (int i = 0; i < queries.size(); i++) {
                    Object[] expected = args[i];
                    Class<?> expectedClass = (Class<?>) expected[0];
                    Object expectedObject = expected[1];

                    UrlQuery is = queries.get(i);

                    assertEquals(true, expectedClass.isInstance(is));
                    assertEquals(expectedObject, is);
                }
            }
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