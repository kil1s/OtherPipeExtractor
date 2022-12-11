package org.schabi.newpipe.farm.search;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.search.InfoItemsSearchCollector;
import org.schabi.newpipe.farm.CollectorFactory;

import java.util.HashMap;

public class SearchCollectorFactory implements CollectorFactory<InfoItem, InfoItemsCollector<InfoItem, ?>> {
    private HashMap<Integer, InfoItemsSearchCollector> searchCollectors = new HashMap<>();

    @Override
    public InfoItemsCollector<InfoItem, ?> collector(int serviceId) {
        if (!searchCollectors.containsKey(serviceId)) {
            searchCollectors.put(serviceId, new InfoItemsSearchCollector(serviceId));
        }
        return searchCollectors.get(serviceId);
    }
}
