package org.schabi.newpipe.extractor.services.dtube;

import org.junit.BeforeClass;
import org.junit.Test;
import org.schabi.newpipe.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link DTubeUrlIdHandler} (Kiosk Instance)
 */
public class DTubeKioskUrlIdHandlerTest {
    private static DTubeUrlIdHandler urlIdHandler;

    @BeforeClass
    public static void setUp() throws Exception {
        urlIdHandler = DTubeUrlIdHandler.getKioskInstance();
        NewPipe.init(Downloader.getInstance());
    }

    @Test
    public void getUrl() throws ParsingException {
        assertEquals(urlIdHandler.getUrl("trendingvideos"), "https://d.tube/trendingvideos");
        assertEquals(urlIdHandler.getUrl("hotvideos"), "https://d.tube/hotvideos");
        assertEquals(urlIdHandler.getUrl("newvideos"), "https://d.tube/newvideos");
    }

    @Test
    public void getId() throws ParsingException {
        assertEquals(urlIdHandler.getId("https://d.tube/#!/trendingvideos"), "trendingvideos");
        assertEquals(urlIdHandler.getId("https://d.tube/#!/hotvideos"), "hotvideos");
        assertEquals(urlIdHandler.getId("https://d.tube/#!/newvideos"), "newvideos");

        assertEquals(urlIdHandler.getId("https://d.tube/trendingvideos"), "trendingvideos");
        assertEquals(urlIdHandler.getId("https://d.tube/hotvideos"), "hotvideos");
        assertEquals(urlIdHandler.getId("https://d.tube/newvideos"), "newvideos");
    }

    @Test
    public void acceptUrl() {
        assertTrue(urlIdHandler.acceptUrl("https:d.tube/#!/trendingvideos"));
        assertTrue(urlIdHandler.acceptUrl("https://d.tube/#!/hotvideos"));
        assertTrue(urlIdHandler.acceptUrl("HTTP://d.tube/#!/newvideos"));
        assertTrue(urlIdHandler.acceptUrl("HTTPS://d.tube/trendingvideos"));
        assertTrue(urlIdHandler.acceptUrl("hTTp://d.tube/#!/newvideos"));

        assertFalse(urlIdHandler.acceptUrl("udp://d.tube/#!/trendingvideos"));
        assertFalse(urlIdHandler.acceptUrl("d.tube tls is not safe enough"));
        assertFalse(urlIdHandler.acceptUrl("ht://d.tube"));
        assertFalse(urlIdHandler.acceptUrl("magnet:d.tu be"));
    }
}
