package de.dhbw.ase.monopoly;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class UtilService {
  private UtilService() {
  }

  public static String joinMessages(String... messages) {
    return Arrays.stream(messages)
        .filter(message -> message != null && message.length() > 0)
        .collect(Collectors.joining("\n"));
  }
}
