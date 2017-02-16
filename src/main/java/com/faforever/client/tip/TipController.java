package com.faforever.client.tip;

import com.faforever.client.fx.Controller;
import com.faforever.client.preferences.NewsPrefs;
import com.faforever.client.preferences.PreferencesService;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TipController implements Controller<Node> {

  private final PreferencesService preferencesService;
  private final NewsPrefs newsPrefs;
  public Region root;
  public Label title;
  public Label text;
  public ImageView image;

  @Inject
  public TipController(PreferencesService preferencesService) {
    this.preferencesService = preferencesService;
    this.newsPrefs = preferencesService.getPreferences().getNews();
    loadNext();
  }

  @Override
  public Region getRoot() {
    return root;
  }

  public void onPreviousClicked() {
    newsPrefs.setLastTipId(newsPrefs.getLastTipId() - 1);
    loadCurrentTip();
  }

  private void loadCurrentTip() {
    preferencesService.storeInBackground();
  }

  public void loadNext() {
    newsPrefs.setLastTipId(newsPrefs.getLastTipId() + 1);
    loadCurrentTip();
  }

  public void onCloseClicked() {
    ((Stage) root.getScene().getWindow()).close();
  }
}
