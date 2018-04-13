package org.schabi.newpipe.extractor.url.model.list.domain;

import org.schabi.newpipe.extractor.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.extractor.url.model.list.UrlQueryList;

public class UrlDomainPublic<T> extends UrlPseudoQueryList<T> {
    public UrlDomainPublic(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
