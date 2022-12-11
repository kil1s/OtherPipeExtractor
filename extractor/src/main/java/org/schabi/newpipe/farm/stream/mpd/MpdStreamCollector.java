package org.schabi.newpipe.farm.stream.mpd;

import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.stream.detail.StreamsOption;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;

public class MpdStreamCollector extends InfoCollector<StreamExtractor, StreamInfo, StreamsOption> {
    private DashMpdParser parser;
    private StreamExtractor extractor;
    private InfoBuilder<StreamInfo> builder;

    public MpdStreamCollector(DashMpdParser parser, StreamExtractor extractor, InfoBuilder<StreamInfo> builder) {
        super(extractor, builder);
        this.parser = parser;
        this.extractor = extractor;
        this.builder = builder;
    }

    @Override
    public StreamInfo all(StreamInfo info) {
        if (info.getDashMpdUrl() != null && !info.getDashMpdUrl().isEmpty()) {
            try {
                parser.getStreams(info);
            } catch (DashMpdParser.DashMpdParsingException|ReCaptchaException e) {
                builder.error(e);
            }
        }
        return info;
    }

    @Override
    public StreamInfo options(StreamInfo info, StreamsOption... options) throws Exception {
        for (StreamsOption option : options) {
            if (option == StreamsOption.MPD) {
                return super.options(info, options);
            }
        }
        return info;
    }
}
