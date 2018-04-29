package org.schabi.newpipe.url.model.list.filepath;

import org.schabi.newpipe.url.model.list.UrlPseudoQueryList;
import org.schabi.newpipe.url.model.list.UrlQueryList;

public class UrlFilepathPrivate<T> extends UrlPseudoQueryList<T> {
    public UrlFilepathPrivate(UrlQueryList<T> queryList) {
        super(queryList);
    }
}
