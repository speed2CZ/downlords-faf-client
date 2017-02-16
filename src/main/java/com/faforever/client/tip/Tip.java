package com.faforever.client.tip;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Tip {
  private String text;
  private String title;
  private Image image;
}
