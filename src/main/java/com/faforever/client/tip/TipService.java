package com.faforever.client.tip;


import java.util.concurrent.CompletableFuture;

public interface TipService {
  CompletableFuture<Tip> getTipById(int Id);
}
