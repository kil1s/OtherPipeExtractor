package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.search.SearchEngine;
import org.schabi.newpipe.extractor.services.soundcloud.BaseSoundcloudSearchTest;

import static org.junit.Assert.assertTrue;
import static org.schabi.newpipe.extractor.ServiceList.DTube;

/**
 * Test for {@link SearchEngine}
 */
public class DTubeSearchEngineAllTest extends BaseSoundcloudSearchTest {

    @BeforeClass
    public static void setUp() throws Exception {
        NewPipe.init(Downloader.getInstance());
        SearchEngine engine = DTube.getSearchEngine();

        result = engine.search("lill uzi vert", 0, "de", SearchEngine.Filter.ANY)
                .getSearchResult();
    }

    @Ignore
    @Test
    public void testSuggestion() {
        //todo write a real test
        assertTrue(result.suggestion != null);
    }
}
