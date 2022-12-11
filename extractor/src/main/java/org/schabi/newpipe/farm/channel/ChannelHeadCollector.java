package org.schabi.newpipe.farm.channel;

import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.channel.ChannelInfo;
import org.schabi.newpipe.farm.InfoBuilder;
import org.schabi.newpipe.farm.collector.InfoCollector;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

public class ChannelHeadCollector extends InfoCollector<ChannelExtractor, ChannelInfo, InfoCollectorOption<?>> {
    private ChannelExtractor extractor;
    private InfoBuilder<ChannelInfo> builder;

    public ChannelHeadCollector(ChannelExtractor extractor, InfoBuilder<ChannelInfo> builder) {
        super(extractor, builder);
        this.extractor = extractor;
        this.builder = builder;
    }

    public ChannelInfo avatar(ChannelInfo info) {
        try {
            info.setAvatarUrl(extractor.getAvatarUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public ChannelInfo banner(ChannelInfo info) {
        try {
            info.setBannerUrl(extractor.getBannerUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    public ChannelInfo feed(ChannelInfo info) {
        try {
            info.setFeedUrl(extractor.getFeedUrl());
        } catch (Exception e) {
            builder.error(e);
        }
        return info;
    }

    @Override
    public ChannelInfo all(ChannelInfo info) {
        return banner(avatar(feed(info)));
    }
}
