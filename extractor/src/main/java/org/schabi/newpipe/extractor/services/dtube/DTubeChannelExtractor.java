package org.schabi.newpipe.extractor.services.dtube;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.Downloader;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.constants.Words;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DTubeChannelExtractor extends ChannelExtractor {
    public static final String PROFIL_SIZE_STEEMIT = "/128x128/";
    public static final String BANNER_SIZE_STEEMIT = "/2048x512/";

    protected DTubeStreamInfoItemNavigator pageNavi;
    protected JsonObject result;
    protected JsonObject meta;

    public DTubeChannelExtractor(StreamingService service, String url) {
        super(service, url);
    }

    @Override
    public String getAvatarUrl() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.STEEMIT_IMG_ENDPOINT+ PROFIL_SIZE_STEEMIT +DTubeParsingHelper.getStringFromJson(Words.META, meta, "profile_image");
    }

    @Override
    public String getBannerUrl() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.STEEMIT_IMG_ENDPOINT+ BANNER_SIZE_STEEMIT +DTubeParsingHelper.getStringFromJson(Words.META, meta, "cover_image");
    }

    @Override
    public String getFeedUrl() throws ParsingException {
        // NO SUPPORT YET
        return null;
    }

    @Override
    public long getSubscriberCount() throws ParsingException {
        Downloader dl = NewPipe.getDownloader();
        byte[] request = DTubeParsingHelper.getSteemitRequest("call", new Object[]{"follow_api","get_follow_count",new Object[]{getName()}}).getBytes();

        try {
            String response = dl.download(DTubeParsingHelper.STEEMIT_ENDPOINT, request);
            return JsonParser.object().from(response).getObject(Words.RESULT).getNumber("follower_count").longValue();
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        } catch (ReCaptchaException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        } catch (JsonParserException e) {
            throw new ParsingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String getDescription() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromJson(Words.META, meta,"about").getData();
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

    @Override
    public void onFetchPage(@Nonnull Downloader downloader) throws IOException, ExtractionException {
        DTubeParsingHelper.DTubeResultAndMeta resultAndMeta = DTubeParsingHelper.getResultAndMetaFromSteemitContent(downloader, Words.PROFILE, new Object[]{"database_api","get_accounts", new Object[]{new Object[]{getName()}}});
        meta = resultAndMeta.getMeta();
        result = resultAndMeta.getResult();
        pageNavi = new DTubeStreamInfoItemNavigator(
                getCleanUrl(),
                50,
                getName(),
                getService(),
                new Object[]{"database_api", "get_discussions_by_blog"},
                ((DTubeUrlIdHandler) getService().getStreamUrlIdHandler())
        );
    }

    @Nonnull
    @Override
    public String getId() throws ParsingException {
        assertPageFetched();
        return DTubeParsingHelper.getStringFromJson(Words.META, meta,"author").getData();
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return getId();
    }
}
