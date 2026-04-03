package com.asledgehammer.webcalc.css.misc;

import com.asledgehammer.webcalc.css.WCStyleElement;
import lombok.NonNull;

public interface WCStyleCommentBlock extends WCStyleElement {
    @NonNull String getComment();
}
