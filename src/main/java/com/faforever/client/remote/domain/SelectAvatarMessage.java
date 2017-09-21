package com.faforever.client.remote.domain;

import lombok.Data;

import java.net.URL;
import java.util.Objects;

@Data
public class SelectAvatarMessage extends ClientMessage {

  private final String action;
  private String avatar;

  public SelectAvatarMessage(URL url) {
    super(ClientMessageType.AVATAR);
    avatar = Objects.toString(url, "");
    this.action = "select";
  }
}
