package org.schabi.newpipe.farm.channel;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.channel.ChannelInfo;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.farm.CollectorFactory;
import org.schabi.newpipe.farm.stream.InfoContainerBuilder;

import java.io.IOException;

public final class ChannelInfoBuilder extends InfoContainerBuilder<String, StreamInfoItem, ChannelExtractor, ChannelInfo> {
    public ChannelInfoBuilder(CollectorFactory<StreamInfoItem, InfoItemsCollector<StreamInfoItem, ?>> factory, NewPipe tracker) {
        super(factory, tracker);
    }

    public ChannelInfoBuilder head() {
        collect(new ChannelHeadCollector(extractor(), this));
        return this;
    }

    public ChannelInfoBuilder details() {
        collect(new ChannelDetailsCollector(extractor(), this));
        return this;
    }

    @Override
    protected ChannelExtractor createExtractor(StreamingService service, String url) throws ExtractionException, IOException {
        return service.getChannelExtractor(url);
    }

    @Override
    protected ChannelInfo create() throws ParsingException {
        return new ChannelInfo(
            extractor().getServiceId(),
            extractor().getUIHandler(),
            extractor().getName()
        );
    }
}
