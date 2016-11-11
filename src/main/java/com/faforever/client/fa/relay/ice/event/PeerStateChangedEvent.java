package com.faforever.client.fa.relay.ice.event;

public class PeerStateChangedEvent {
  private final int remotePlayerId;
  private final String state;

  public PeerStateChangedEvent(int remotePlayerId, String state) {
    this.remotePlayerId = remotePlayerId;
    this.state = state;
  }

  public int getRemotePlayerId() {
    return remotePlayerId;
  }

  public String getState() {
    return state;
  }
}
