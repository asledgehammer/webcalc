package com.asledgehammer.webcalc.io;

import lombok.Getter;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.asledgehammer.webcalc.css.CSSUtils.PATTERN_NEWLINE;

public class MultiLineContext {

  private static final int[] BLANK_ROW_COL = new int[] {1, 1};

  @Getter private final String content;
  @Getter private final int[] rowIndexes;
  @Getter private final int length;

  public MultiLineContext(@NonNull final String content) {
    this.content = content;
    this.length = content.length();
    if (!this.content.isEmpty()) {
      this.rowIndexes = getRowIndexesFromString(content);
    } else {
      this.rowIndexes = new int[0];
    }
  }

  @NotNull
  public String sub(int start, int end) {
    return content.substring(start, end);
  }

  public int[] getRowCol(int index) {

    if (index < 0) {
      throw new IllegalArgumentException("The index out of bounds!");
    }

    int[] result = null;

    if (isEmpty()) return BLANK_ROW_COL;

    // First OR only line
    if (rowIndexes.length == 1 || rowIndexes[0] > index) {
      result = new int[] {1, index + 1};
    } else {
      for (int row = 0; row < rowIndexes.length - 1; row++) {
        int rowIndexCurr = rowIndexes[row];
        int rowIndexNext = rowIndexes[row + 1];
        if (rowIndexCurr <= index && rowIndexNext > index) {
          result = new int[] {row + 1, (index - rowIndexCurr) + 1};
          break;
        }
      }
      // Last line.
      if (result == null) {
        result = new int[] {rowIndexes.length, (index - rowIndexes[rowIndexes.length - 1]) + 1};
      }
    }

    if (result[1] < 1) {
      throw new RuntimeException(
          "The index out of bounds!" + "(index: " + index + ", col: " + result[1] + ")");
    }

    return result;
  }

  public boolean isEOS(int index) {
    return index >= length;
  }

  public int charAt(int index) {
    return content.charAt(index);
  }

  public boolean isEmpty() {
    return this.content.isEmpty();
  }

  public static int[] getRowIndexesFromString(@NotNull String contents) {
    final List<Integer> list = new ArrayList<>();
    val matcher = PATTERN_NEWLINE.matcher(contents);
    while (matcher.find()) {
      list.add(matcher.end());
    }
    val indexes = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      indexes[i] = list.get(i);
    }
    return indexes;
  }
}
