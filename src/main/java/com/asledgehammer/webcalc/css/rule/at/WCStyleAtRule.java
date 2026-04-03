package com.asledgehammer.webcalc.css.rule.at;

import com.asledgehammer.webcalc.css.rule.WCStyleRule;
import lombok.NonNull;

public interface WCStyleAtRule extends WCStyleRule {
    @NonNull String getName();
}
