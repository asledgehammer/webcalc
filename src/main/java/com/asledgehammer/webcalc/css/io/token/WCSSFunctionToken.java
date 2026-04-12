package com.asledgehammer.webcalc.css.io.token;

import com.asledgehammer.webcalc.io.token.WCReferenceRange;
import lombok.Getter;

public class WCSSFunctionToken extends WCSSToken {

  @Getter private final String functionName;

  public WCSSFunctionToken(WCReferenceRange ref, String functionName) {
    super(CSSTokenType.FUNCTION, ref, functionName + "(");
    this.functionName = functionName;
  }
}
