package org.schabi.newpipe.extractor.playlist;

import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

public class PlaylistInfo extends ListInfo<StreamInfoItem> {

    public PlaylistInfo(int serviceId, ListLinkHandler urlIdHandler, String name) throws ParsingException {
        super(serviceId, urlIdHandler, name);
    }

    private String thumbnailUrl;
    private String bannerUrl;
    private String uploaderUrl;
    private String uploaderName;
    private String uploaderAvatarUrl;
    private long streamCount = 0;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getUploaderUrl() {
        return uploaderUrl;
    }

    public void setUploaderUrl(String uploaderUrl) {
        this.uploaderUrl = uploaderUrl;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderAvatarUrl() {
        return uploaderAvatarUrl;
    }

    public void setUploaderAvatarUrl(String uploaderAvatarUrl) {
        this.uploaderAvatarUrl = uploaderAvatarUrl;
    }

    public long getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(long streamCount) {
        this.streamCount = streamCount;
    }
}
