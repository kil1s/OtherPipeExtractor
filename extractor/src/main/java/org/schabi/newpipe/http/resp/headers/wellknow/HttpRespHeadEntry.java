package org.schabi.newpipe.http.resp.headers.wellknow;

import org.schabi.newpipe.http.resp.headers.HttpRespHeadKey;
import org.schabi.newpipe.http.resp.headers.HttpRespHeadValue;

public interface HttpRespHeadEntry  {
    String getOriginalName();
    HttpRespHeadKey getKey();
    String getOriginalValue();
    HttpRespHeadValue getValue();
}
