package io.zd.jvm.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Fuite mémoire contrôlée pour labs heap dump / MAT.
 */
public final class LeakStore {
  private static final List<byte[]> STORE = new ArrayList<>();

  private LeakStore() {}

  public static synchronized long addMegabytes(int mb) {
    int bytes = mb * 1024 * 1024;
    STORE.add(new byte[bytes]);
    return STORE.size();
  }

  public static synchronized void clear() {
    STORE.clear();
  }

  public static synchronized int count() {
    return STORE.size();
  }
}
