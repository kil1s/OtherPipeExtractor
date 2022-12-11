package org.schabi.newpipe.farm;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.InfoItemsCollector;

public interface CollectorFactory<I extends InfoItem, C extends InfoItemsCollector<I, ?>> {
    InfoItemsCollector<I, ?> collector(int serviceId);
}
