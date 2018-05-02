package org.schabi.newpipe.url.helper;

import org.junit.Test;
import org.schabi.newpipe.extractor.constants.Encodings;
import org.schabi.newpipe.url.model.UrlParsingFeature;
import org.schabi.newpipe.url.model.UrlQuery;
import org.schabi.newpipe.url.model.UrlQueryState;
import org.schabi.newpipe.url.model.list.UrlQueryList;
import org.schabi.newpipe.url.model.list.domain.UrlDomainPrivate;
import org.schabi.newpipe.url.model.list.domain.UrlDomainPublic;
import org.schabi.newpipe.url.model.list.filepath.UrlFilepathPrivate;
import org.schabi.newpipe.url.model.list.filepath.UrlFilepathPublic;
import org.schabi.newpipe.url.model.params.UrlParamsQuery;
import org.schabi.newpipe.url.model.params.UrlPrivateParamsQuery;
import org.schabi.newpipe.url.model.params.UrlPublicParamsQuery;
import org.schabi.newpipe.url.model.protocol.wellknown.WellKnownProtocolHelper;
import org.schabi.newpipe.url.model.protocol.wellknown.model.UrlUnknownProtocol;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UrlParsingHelperTest {

    // TODO add more tests

    @Test
    public void parse() {
    }

    @Test
    public void parseTillPort() {
    }

    @Test
    public void getQueriesFromUrl() {
        HashMap<String, Object[][]> requirements = new HashMap<String, Object[][]>();

        UrlUnknownProtocol publicProtocol = new UrlUnknownProtocol("FOO", WellKnownProtocolHelper.UNKNOWN.getReadableName(), UrlQueryState.PUBLIC, 8042);

        UrlQueryList<String> urlQueryList = new UrlQueryList<String>();
        urlQueryList.add("example");
        urlQueryList.add("com");

        UrlQueryList<String> pathQueryList = new UrlQueryList<String>();
        pathQueryList.add("over");
        pathQueryList.add("there");

        UrlParamsQuery publicParams = new UrlParamsQuery();
        publicParams.add("name", "ferret");
        publicParams.add("other");
        publicParams.add("another");

        UrlParamsQuery privateParams = new UrlParamsQuery();
        privateParams.add("name", "ferret");
        privateParams.add("other");
        privateParams.add("anothh");

        UrlParamsQuery privateParams1 = new UrlParamsQuery();
        privateParams1.add("ando");

        requirements.put(
                "foo://example.com:8042/over/there?name=ferret&other&another#foo://example.com:8042/over/there#?name=ferret&other&anothh#!ando",
                new Object[][]{
                        {
                            UrlUnknownProtocol.class,
                            publicProtocol
                        },
                        {
                            UrlDomainPublic.class,
                            urlQueryList.toPublicDomain()
                        },
                        {
                            UrlFilepathPublic.class,
                            pathQueryList.toPublicFilepath()
                        },
                        {
                            UrlPublicParamsQuery.class,
                            publicParams.toPublic()
                        },
                        {
                            UrlUnknownProtocol.class,
                            publicProtocol.clonePrivate()
                        },
                        {
                            UrlDomainPrivate.class,
                            urlQueryList.toPublicDomain()
                        },
                        {
                            UrlFilepathPrivate.class,
                            pathQueryList.toPublicFilepath()
                        },
                        {
                            UrlPrivateParamsQuery.class,
                            privateParams.toPrivate()
                        },
                        {
                            UrlPrivateParamsQuery.class,
                            privateParams1.toPrivate()
                        }
                }
        );

        try {
            for (Map.Entry<String, Object[][]> entry:requirements.entrySet()) {
                Object[][] args = entry.getValue();
                List<UrlQuery> queries = UrlParsingHelper.getQueriesFromUrl(entry.getKey(), Encodings.UTF_8, UrlParsingFeature.values());

                assertEquals(args.length, queries.size());
                for (int i = 0; i < queries.size(); i++) {
                    Object[] expected = args[i];
                    Class<?> expectedClass = (Class<?>) expected[0];
                    Object expectedObject = expected[1];

                    UrlQuery is = queries.get(i);

                    assertEquals(expectedClass, is.getClass());
                    assertEquals(expectedObject, expectedClass.cast(is));
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