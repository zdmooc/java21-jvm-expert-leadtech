package io.zd.jvm.server;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class Query {

  private Query() {}

  public static Map<String, String> parse(URI uri) {
    Map<String, String> map = new HashMap<>();
    String q = uri.getRawQuery();
    if (q == null || q.isBlank()) return map;
    for (String part : q.split("&")) {
      int idx = part.indexOf('=');
      if (idx < 0) {
        map.put(decode(part), "");
      } else {
        map.put(decode(part.substring(0, idx)), decode(part.substring(idx + 1)));
      }
    }
    return map;
  }

  private static String decode(String s) {
    return s.replace("+", " "); // minimal, suffisant pour labs
  }

  public static int intParam(Map<String, String> q, String name, int def) {
    String v = q.get(name);
    if (v == null || v.isBlank()) return def;
    return Integer.parseInt(v);
  }

  public static String strParam(Map<String, String> q, String name, String def) {
    String v = q.get(name);
    return (v == null || v.isBlank()) ? def : v;
  }
}
