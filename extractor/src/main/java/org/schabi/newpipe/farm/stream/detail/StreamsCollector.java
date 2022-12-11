package org.schabi.newpipe.farm.stream.detail;

import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.util.ArrayList;

public class StreamsCollector extends InfoCollector<StreamExtractor, StreamInfo, StreamsOption> {
    private StreamExtractor extractor;
    private InfoBuilder<StreamInfo> builder;

    public StreamsCollector(StreamExtractor extractor, InfoBuilder<StreamInfo> builder) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }

    public StreamInfo dash(StreamInfo info) {
        try {
            info.setDashMpdUrl(extractor.getDashMpdUrl());
        } catch (Exception e) {
            builder.error(new ExtractionException("Couldn't get Dash manifest", e));
        }
        return info;
    }

    public StreamInfo hls(StreamInfo info) {
        try {
            info.setHlsUrl(extractor.getHlsUrl());
        } catch (Exception e) {
            builder.error(new ExtractionException("Couldn't get HLS manifest", e));
        }
        return info;
    }

    public StreamInfo audio(StreamInfo info) {
        try {
            info.setAudioStreams(extractor.getAudioStreams());
        } catch (Exception e) {
            builder.error(new ExtractionException("Couldn't get audio streams", e));
        }
        return info;
    }

    public StreamInfo video(StreamInfo info) {
        try {
            info.setVideoStreams(extractor.getVideoStreams());
        } catch (Exception e) {
            builder.error(new ExtractionException("Couldn't get video streams", e));
        }
        return info;
    }

    public StreamInfo videoOnly(StreamInfo info) {
        try {
            info.setVideoOnlyStreams(extractor.getVideoOnlyStreams());
        } catch (Exception e) {
            builder.error(new ExtractionException("Couldn't get video only streams", e));
        }
        return info;
    }

    private StreamInfo endUp(StreamInfo info) {
        if (info.getVideoStreams() == null) {
            info.setVideoStreams(new ArrayList<VideoStream>());
        }

        if (info.getVideoOnlyStreams() == null) {
            info.setVideoOnlyStreams(new ArrayList<VideoStream>());
        }

        if (info.getAudioStreams() == null) {
            info.setAudioStreams(new ArrayList<AudioStream>());
        }
        return info;
    }

    @Override
    public StreamInfo options(StreamInfo info, StreamsOption...options) throws Exception {
        if (options == null || options.length <= 0) {
            return super.options(info, options);
        }

        for (StreamsOption option:options) {
            switch (option) {
                case HLS:
                    info = hls(info);
                    break;
                case DASH:
                    info = dash(info);
                    break;
                case AUDIO:
                    info = audio(info);
                    break;
                case VIDEO:
                    info = video(info);
                    break;
                case VIDEO_ONLY:
                    info = videoOnly(info);
                    break;
            }
        }

        return endUp(info);
    }

    @Override
    public StreamInfo all(StreamInfo info) {
        return endUp(audio(video(videoOnly(
            dash(hls(info))
        ))));
    }
}

