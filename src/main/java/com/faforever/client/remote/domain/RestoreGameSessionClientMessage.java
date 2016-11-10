package com.faforever.client.remote.domain;

public class RestoreGameSessionClientMessage extends ClientMessage {
  private int gameId;

  public RestoreGameSessionClientMessage(int gameId) {
    super(ClientMessageType.RESTORE_GAME_SESSION);
    this.gameId = gameId;
  }
}
