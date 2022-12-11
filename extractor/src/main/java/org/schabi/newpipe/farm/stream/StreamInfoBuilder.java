package org.schabi.newpipe.farm.stream;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.stream.detail.StreamsCollector;
import org.schabi.newpipe.farm.stream.detail.StreamsOption;
import org.schabi.newpipe.farm.stream.mpd.DashMpdParser;
import org.schabi.newpipe.farm.stream.mpd.MpdStreamCollector;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StreamInfoBuilder extends InfoBuilder<StreamInfo> {
    private NewPipe tracker;
    private StreamExtractor extractor;

    private List<InfoCollector<StreamExtractor, StreamInfo, ?>> collectors;
    private List<StreamsOption> streams;

    public StreamInfoBuilder(NewPipe tracker, StreamExtractor extractor) {
        super(tracker); // template
        this.tracker = tracker;
        this.extractor = extractor;
        collectors = new ArrayList<>();
        streams = new ArrayList<>();
    }

    @SuppressWarnings("RedundantThrows")
    public StreamInfoBuilder align(String url) throws IOException, ExtractionException {
        return align(tracker.getServiceByUrl(url), url);
    }

    @SuppressWarnings("RedundantThrows")
    public StreamInfoBuilder align(StreamingService service, String url) throws IOException, ExtractionException {
        extractor = service.getStreamExtractor(url);
        return this;
    }

    public StreamInfoBuilder download() throws ExtractionException, IOException {
        extractor.fetchPage();
        return this;
    }

    private void streams() {
        if (!streams.isEmpty()) {
            return;
        }
        collectors.add(new StreamsCollector(extractor, this));
    }

    public StreamInfoBuilder hls() {
        streams();
        return this;
    }

    public StreamInfoBuilder mpd() {
        streams();
        if (!streams.contains(StreamsOption.DASH)) {
            streams.add(StreamsOption.DASH);
        }
        streams.add(StreamsOption.MPD);
        collectors.add(new MpdStreamCollector(
            new DashMpdParser(tracker.getDownloader()), extractor, this
        ));
        return this;
    }

    public StreamInfoBuilder dash() {
        streams();
        streams.add(StreamsOption.DASH);
        return this;
    }

    public StreamInfoBuilder video() {
        streams();
        streams.add(StreamsOption.VIDEO);
        return this;
    }

    public StreamInfoBuilder videoOnly() {
        streams();
        streams.add(StreamsOption.VIDEO_ONLY);
        return this;
    }

    private StreamInfo setup() throws ExtractionException {
        /* ---- important data, without the video can't be displayed goes here: ---- */
        // if one of these is not available an exception is meant to be thrown directly into the frontend.
        int serviceId = extractor.getServiceId();
        String url = extractor.getUrl();
        String originalUrl = extractor.getOriginalUrl();
        StreamType streamType = extractor.getStreamType();
        String id = extractor.getId();
        String name = extractor.getName();
        int ageLimit = extractor.getAgeLimit();

        if ((streamType == StreamType.NONE)
                || (url == null || url.isEmpty())
                || (id == null || id.isEmpty())
                || (name == null /* streamInfo.title can be empty of course */)
                || (ageLimit == -1)) {
            throw new ExtractionException("Some important stream information was not given.");
        }

        return new StreamInfo(serviceId, url, originalUrl, streamType, id, name, ageLimit);
    }

    @Override
    public StreamInfo build() throws Exception {
        StreamInfo info = setup();
        for (InfoCollector<StreamExtractor, StreamInfo, ?> collector : collectors) {
            if (
                collector instanceof StreamsCollector ||
                collector instanceof MpdStreamCollector
            ) {
                //noinspection unchecked
                info = ((InfoCollector<StreamExtractor, StreamInfo, StreamsOption>) collector).
                        options(info, streams.toArray(new StreamsOption[0]));
            }
            info = collector.all(info);
        }
        return info;
    }

    @Override
    public void reset() {
        super.reset();
        collectors = new ArrayList<>();
        streams = new ArrayList<>();
    }
}
