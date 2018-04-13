package org.schabi.newpipe.extractor.url.model.list.domain;

import org.schabi.newpipe.extractor.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.extractor.url.model.list.UrlQueryList;

public class UrlDomainPrivate<T> extends UrlPseudoQueryList<T> {
    public UrlDomainPrivate(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
