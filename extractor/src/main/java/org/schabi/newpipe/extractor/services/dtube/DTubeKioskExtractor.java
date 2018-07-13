package org.schabi.newpipe.extractor.services.dtube;

import com.github.FlorianSteenbuck.other.http.HttpDownloader;
import org.schabi.newpipe.extractor.ListUrlIdHandler;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.UrlIdHandler;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DTubeKioskExtractor extends KioskExtractor {
    private DTubeStreamInfoItemNavigator pageNavi;
    private DTubeKiosk kiosk;

    public DTubeKioskExtractor(StreamingService streamingService, ListUrlIdHandler url, String kioskId) throws ExtractionException {
        super(streamingService, url, kioskId);
    }

    @Nonnull
    @Override
    protected UrlIdHandler getUrlIdHandler()  {
        return DTubeUrlIdHandler.getKioskInstance();
    }

    @Override
    public void onFetchPage(@Nonnull HttpDownloader downloader) throws IOException, ExtractionException {
        kiosk = DTubeKiosk.getKioskById(getUrlIdHandler().getId());
        pageNavi = new DTubeStreamInfoItemNavigator(
                12,
                "dtube",
                getService(),
                new Object[]{"database_api", "get_discussions_by_"+kiosk.getBy()},
                ((DTubeUrlIdHandler) getService().getStreamUrlIdHandler().setUrl(getOriginalUrl()))
        );
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        assertPageFetched();
        return kiosk.getName();
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        return pageNavi.getInitialPage();
    }

    @Override
    public String getNextPageUrl() throws IOException, ExtractionException {
        return pageNavi.getNextPageUrl();
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(String nextPageUrl) throws IOException, ExtractionException {
        return pageNavi.getPage(nextPageUrl);
    }
}
