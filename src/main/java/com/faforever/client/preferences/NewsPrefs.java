package com.faforever.client.preferences;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewsPrefs {
  private final StringProperty lastReadNewsUrl;
  private final IntegerProperty lastTipId;
  private final BooleanProperty showTips;

  public NewsPrefs() {
    lastReadNewsUrl = new SimpleStringProperty();
    lastTipId = new SimpleIntegerProperty(0);
    showTips = new SimpleBooleanProperty(true);
  }

  public boolean isShowTips() {
    return showTips.get();
  }

  public void setShowTips(boolean showTips) {
    this.showTips.set(showTips);
  }

  public BooleanProperty showTipsProperty() {
    return showTips;
  }

  public int getLastTipId() {
    return lastTipId.get();
  }

  public void setLastTipId(int lastTipId) {
    this.lastTipId.set(lastTipId);
  }

  public IntegerProperty lastTipIdProperty() {
    return lastTipId;
  }

  public String getLastReadNewsUrl() {
    return lastReadNewsUrl.get();
  }

  public void setLastReadNewsUrl(String lastReadNewsUrl) {
    this.lastReadNewsUrl.set(lastReadNewsUrl);
  }

  public StringProperty lastReadNewsUrlProperty() {
    return lastReadNewsUrl;
  }
}
