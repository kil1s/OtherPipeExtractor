package org.schabi.newpipe.farm.channel;

import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.channel.ChannelInfo;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

public class ChannelDetailsCollector extends InfoCollector<ChannelExtractor, ChannelInfo, InfoCollectorOption<?>> {
    private ChannelExtractor extractor;
    private InfoBuilder<ChannelInfo> builder;

    public ChannelDetailsCollector(ChannelExtractor extractor, InfoBuilder<ChannelInfo> builder) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }

    public ChannelInfo subscriber(ChannelInfo info) {
        try {
            info.setSubscriberCount(extractor.getSubscriberCount());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public ChannelInfo description(ChannelInfo info) {
        try {
            info.setDescription(extractor.getDescription());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public ChannelInfo donations(ChannelInfo info) {
        try {
            info.setDonationLinks(extractor.getDonationLinks());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    @Override
    public ChannelInfo all(ChannelInfo info) {
        return subscriber(description(donations(info)));
    }
}
