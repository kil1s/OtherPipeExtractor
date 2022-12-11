package org.schabi.newpipe.farm.stream.detail;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

import java.util.Collections;
import java.util.List;

public class OptionalStreamCollector extends InfoCollector<StreamExtractor, StreamInfo, InfoCollectorOption<?>> {
    private StreamExtractor extractor;
    private InfoBuilder<StreamInfo> builder;

    public OptionalStreamCollector(
        StreamExtractor extractor,
        InfoBuilder<StreamInfo> builder
    ) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }

    public StreamInfo thumbnail(StreamInfo info) {
        try {
            info.setThumbnailUrl(extractor.getThumbnailUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo length(StreamInfo info) {
        try {
            info.setDuration(extractor.getLength());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo uploaderName(StreamInfo info) {
        try {
            info.setUploaderName(extractor.getUploaderName());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo uploaderUrl(StreamInfo info) {
        try {
            info.setUploaderUrl(extractor.getUploaderUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo description(StreamInfo info) {
        try {
            info.setDescription(extractor.getDescription());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo count(StreamInfo info) {
        try {
            info.setViewCount(extractor.getViewCount());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo uploadDate(StreamInfo info) {
        try {
            info.setUploadDate(extractor.getUploadDate());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo uploaderAvatarUrl(StreamInfo info) {
        try {
            info.setUploaderAvatarUrl(extractor.getUploaderAvatarUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo time(StreamInfo info) {
        try {
            info.setStartPosition(extractor.getTimeStamp());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo like(StreamInfo info) {
        try {
            info.setLikeCount(extractor.getLikeCount());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo dislike(StreamInfo info) {
        try {
            info.setDislikeCount(extractor.getDislikeCount());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo next(StreamInfo info) {
        try {
            info.setNextVideo(extractor.getNextVideo());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo subtitles(StreamInfo info) {
        try {
            info.setSubtitles(extractor.getSubtitlesDefault());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public StreamInfo related(StreamInfo info) {
        try {
            InfoItemsCollector<? extends InfoItem, ?> collector = extractor.getRelatedVideos();
            builder.error(collector.getErrors());

            //noinspection unchecked
            info.setRelatedStreams((List<InfoItem>) collector.getItems());
        } catch (Exception e) {
            builder.error(e);
            info.setAudioStreams(Collections.emptyList());
        }

        return info;
    }

    @Override
    public StreamInfo all(StreamInfo info) {
        return related(subtitles(
            next(dislike(
                like(time(
                    uploaderAvatarUrl(uploadDate(
                        count(description(
                            uploaderUrl(uploaderName(
                                length(thumbnail(info))
                            ))
                        ))
                    ))
                ))
            ))
        ));
    }
}
