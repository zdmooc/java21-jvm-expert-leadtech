package io.zd.jvm.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Main {

  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    String execMode = System.getenv().getOrDefault("SERVER_EXECUTOR", "virtual").toLowerCase(); // virtual|fixed
    int fixedPool = Integer.parseInt(System.getenv().getOrDefault("SERVER_FIXED_POOL", "200"));

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/health", Handlers::health);
    server.createContext("/cpu", Handlers::cpu);
    server.createContext("/alloc", Handlers::alloc);
    server.createContext("/sleep", Handlers::sleep);
    server.createContext("/lock", Handlers::lock);
    server.createContext("/leak", Handlers::leak);
    server.createContext("/direct", Handlers::direct);
    server.createContext("/metrics", Handlers::metrics);

    Executor executor = createExecutor(execMode, fixedPool);
    server.setExecutor(executor);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Shutdown requested...");
      server.stop((int) Duration.ofSeconds(1).toSeconds());
      if (executor instanceof java.util.concurrent.ExecutorService es) es.close();
    }, "shutdown-hook"));

    System.out.printf("jvm-lab-server listening on :%d (executor=%s)%n", port, execMode);
    System.out.println("Try: curl 'http://localhost:8080/alloc?mb=10'");
    server.start();
  }

  private static Executor createExecutor(String execMode, int fixedPool) {
    Objects.requireNonNull(execMode);
    return switch (execMode) {
      case "virtual" -> Executors.newVirtualThreadPerTaskExecutor();
      case "fixed" -> Executors.newFixedThreadPool(fixedPool);
      default -> throw new IllegalArgumentException("Unknown SERVER_EXECUTOR=" + execMode + " (use virtual|fixed)");
    };
  }
}
