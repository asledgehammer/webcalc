package com.asledgehammer.webcalc.css.misc;

import com.asledgehammer.webcalc.css.WCStyleElementImpl;
import lombok.NonNull;

public class WCStyleCommentBlockImpl extends WCStyleElementImpl implements WCStyleCommentBlock {

    @NonNull
    final String comment;

    public WCStyleCommentBlockImpl(@NonNull String comment) {
        super();
        this.comment = comment;
    }

    @Override
    public @NonNull String getComment() {
        return comment;
    }
}
