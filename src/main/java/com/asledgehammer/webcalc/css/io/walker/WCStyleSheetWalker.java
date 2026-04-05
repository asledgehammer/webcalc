package com.asledgehammer.webcalc.css.io.walker;

import com.asledgehammer.webcalc.css.io.token.WCSSToken;
import com.asledgehammer.webcalc.io.token.WCTokenWalker;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface WCStyleSheetWalker extends WCTokenWalker {
  @Nullable
  List<WCSSToken> walk(@NonNull List<WCSSToken> tokens, int index);
}
