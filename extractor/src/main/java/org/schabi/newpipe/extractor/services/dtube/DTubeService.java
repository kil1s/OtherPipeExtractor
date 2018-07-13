package org.schabi.newpipe.extractor.services.dtube;

import org.schabi.newpipe.extractor.ListUrlIdHandler;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.SuggestionExtractor;
import org.schabi.newpipe.extractor.UrlIdHandler;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchEngine;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.DynamicSettings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.VIDEO;

public class DTubeService extends StreamingService {
    protected static Settings settings = new DTubeSettings();
    protected static boolean settingsAreDynamic = true;

    public DTubeService(int id) {
        super(id, "DTube", singletonList(VIDEO));
    }

    @Override
    public boolean isDynamicSettings() {
        return settingsAreDynamic;
    }

    @Override
    public void refreshSettings() throws IOException, ReCaptchaException {
        if (isDynamicSettings()) {
            ((DynamicSettings) settings).refresh();
        }
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void updateSettings(Settings newSettings) {
        settings = newSettings;
        settingsAreDynamic = newSettings instanceof DynamicSettings;
    }

    @Override
    public UrlIdHandler getStreamUrlIdHandler() {
        return DTubeUrlIdHandler.getVideoInstance();
    }

    @Override
    public ListUrlIdHandler getChannelUrlIdHandler() {
        return DTubeUrlIdHandler.getChannelInstance();
    }

    @Override
    public ListUrlIdHandler getPlaylistUrlIdHandler() {
        return null;
    }

    @Override
    public SearchEngine getSearchEngine() {
        return new DTubeSearchEngine(this);
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new DTubeSuggestionExtractor(getServiceId());
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        KioskList.KioskExtractorFactory kioskFactory = new KioskList.KioskExtractorFactory() {
            @Override
            public KioskExtractor createNewKiosk(StreamingService streamingService, String url, String id) throws ExtractionException {
                return new DTubeKioskExtractor(
                        DTubeService.this,
                        new DTubeUrlIdHandler(false, false, true).setUrl(url),
                        id
                );
            }
        };

        KioskList list = new KioskList(getServiceId());

        final DTubeUrlIdHandler h = new DTubeUrlIdHandler(false, false, true);

        try {
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.HOT.getId());
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.TRENDING.getId());
            list.addKioskEntry(kioskFactory, h, DTubeKiosk.NEW.getId());
        } catch (Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListUrlIdHandler urlIdHandler) throws ExtractionException {
        return new DTubeChannelExtractor(this, urlIdHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListUrlIdHandler urlIdHandler) throws ExtractionException {
        return null;
    }

    @Override
    public StreamExtractor getStreamExtractor(UrlIdHandler urlIdHandler) throws ExtractionException {
        return new DTubeStreamExtractor(this, urlIdHandler);
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }
}
