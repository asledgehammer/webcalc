package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRangeImpl;
import lombok.Getter;

public class WCSSFunctionTokenImpl extends WCSSTokenImpl {

  @Getter private final String functionName;

  public WCSSFunctionTokenImpl(WCReferenceRangeImpl ref, String functionName) {
    super(CSSTokenType.FUNCTION, ref, functionName + "(", false, false);
    this.functionName = functionName;
  }
}
