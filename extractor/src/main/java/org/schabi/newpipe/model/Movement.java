package org.schabi.newpipe.model;

import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.farm.collector.InfoCollectorOption;

public enum Movement implements InfoCollectorOption<Info> {
    PREVIOUS, NEXT, MORE;
}
