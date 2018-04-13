package org.schabi.newpipe.extractor.url.model.list.filepath;

import org.schabi.newpipe.extractor.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.extractor.url.model.list.UrlQueryList;

public class UrlFilepathPrivate<T> extends UrlPseudoQueryList<T> {
    public UrlFilepathPrivate(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
