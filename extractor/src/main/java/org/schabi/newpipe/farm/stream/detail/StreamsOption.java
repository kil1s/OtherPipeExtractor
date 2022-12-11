package org.schabi.newpipe.farm.stream.detail;

import org.schabi.newpipe.farm.collector.InfoCollectorOption;
import org.schabi.newpipe.extractor.stream.StreamInfo;

public enum StreamsOption implements InfoCollectorOption<StreamInfo> {
    MPD, DASH, HLS, AUDIO, VIDEO, VIDEO_ONLY;
}
