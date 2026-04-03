package com.asledgehammer.webcalc.css.misc;

import com.asledgehammer.webcalc.css.WCStyleElementImpl;
import lombok.NonNull;

public class WCStyleBlockCommentImpl extends WCStyleElementImpl implements WCStyleCommentBlock {

    @NonNull
    final String comment;

    public WCStyleBlockCommentImpl(@NonNull String comment) {
        super();
        this.comment = comment;
    }

    @Override
    public @NonNull String getComment() {
        return comment;
    }
}
