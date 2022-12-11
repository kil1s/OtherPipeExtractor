package org.schabi.newpipe.farm.stream;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import org.schabi.newpipe.farm.CollectorFactory;

import java.util.HashMap;

public class StreamCollectorFactory implements CollectorFactory<StreamInfoItem, StreamInfoItemsCollector> {
    private HashMap<Integer, StreamInfoItemsCollector> streamCollectors = new HashMap<>();

    @Override
    public InfoItemsCollector<StreamInfoItem, ?> collector(int serviceId) {
        if (!streamCollectors.containsKey(serviceId)) {
            streamCollectors.put(serviceId, new StreamInfoItemsCollector(serviceId));
        }
        return streamCollectors.get(serviceId);
    }
}
