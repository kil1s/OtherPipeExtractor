package org.schabi.newpipe.extractor.playlist;

import com.github.kil1s.other.http.HttpDownloader;

import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

public abstract class PlaylistExtractor extends ListExtractor<StreamInfoItem> {

    public PlaylistExtractor(HttpDownloader downloader, StreamingService service, ListLinkHandler urlIdHandler) {
        super(downloader, service, urlIdHandler);
    }

    public abstract String getThumbnailUrl() throws ParsingException;
    public abstract String getBannerUrl() throws ParsingException;

    public abstract String getUploaderUrl() throws ParsingException;
    public abstract String getUploaderName() throws ParsingException;
    public abstract String getUploaderAvatarUrl() throws ParsingException;

    public abstract long getStreamCount() throws ParsingException;
}
