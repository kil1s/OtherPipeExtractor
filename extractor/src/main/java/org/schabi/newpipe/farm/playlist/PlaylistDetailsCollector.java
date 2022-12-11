package org.schabi.newpipe.farm.playlist;

import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.playlist.PlaylistInfo;
import org.schabi.newpipe.farm.ErrorBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

public class PlaylistDetailsCollector extends InfoCollector<PlaylistExtractor, PlaylistInfo, InfoCollectorOption<?>> {
    private PlaylistExtractor extractor;
    private ErrorBuilder<PlaylistInfo> builder;
    
    public PlaylistDetailsCollector(PlaylistExtractor extractor, ErrorBuilder<PlaylistInfo> builder) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }
    
    public PlaylistInfo streams(PlaylistInfo info) {
        try {
            info.setStreamCount(extractor.getStreamCount());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public PlaylistInfo thumbnail(PlaylistInfo info) {
        try {
            info.setThumbnailUrl(extractor.getThumbnailUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public PlaylistInfo uploaderUrl(PlaylistInfo info) {
        try {
            info.setUploaderUrl(extractor.getUploaderUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public PlaylistInfo uploaderName(PlaylistInfo info) {
        try {
            info.setUploaderName(extractor.getUploaderName());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public PlaylistInfo avatar(PlaylistInfo info) {
        try {
            info.setUploaderAvatarUrl(extractor.getUploaderAvatarUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public PlaylistInfo banner(PlaylistInfo info) {
        try {
            info.setBannerUrl(extractor.getBannerUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    @Override
    public PlaylistInfo all(PlaylistInfo info) {
        return banner(avatar(
            uploaderName(uploaderUrl(
                thumbnail(streams(info))
            ))
        ));
    }
}
