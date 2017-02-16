package com.faforever.client.tip;

import com.faforever.client.fx.Controller;
import com.faforever.client.i18n.I18n;
import com.faforever.client.preferences.NewsPrefs;
import com.faforever.client.preferences.PreferencesService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TipController implements Controller<Node> {

  private final TipService tipService;
  private final PreferencesService preferencesService;
  private final NewsPrefs newsPrefs;
  private final I18n i18n;
  private final BooleanProperty loadedProperty;

  public Region root;
  public Label title;
  public Label text;
  public ImageView image;
  public CheckBox showAtStartUp;

  @Inject
  public TipController(PreferencesService preferencesService, TipServiceImpl tipService, I18n i18n) {
    this.preferencesService = preferencesService;
    this.newsPrefs = preferencesService.getPreferences().getNews();
    this.tipService = tipService;
    this.i18n = i18n;
    loadedProperty = new SimpleBooleanProperty(false);
  }

  public void initialize() {
    showAtStartUp.setText(i18n.get("settings.enableTips"));
    showAtStartUp.selectedProperty().bindBidirectional(preferencesService.getPreferences().getNews().showTipsProperty());
    showAtStartUp.selectedProperty().addListener(observable -> preferencesService.storeInBackground());
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
    CompletableFuture<Tip> tipFuture = tipService.getTipById(newsPrefs.getLastTipId());
    tipFuture.thenAccept(tip -> {
      Platform.runLater(() -> {
        title.setText(tip.getTitle());
        text.setText(tip.getText());
        image.setImage(tip.getImage());
      });
      preferencesService.storeInBackground();
      loadedProperty.set(true);
    });
  }

  public void loadNext() {
    newsPrefs.setLastTipId(newsPrefs.getLastTipId() + 1);
    loadCurrentTip();
  }

  public void onCloseClicked() {
    ((Stage) root.getScene().getWindow()).close();
  }

  public boolean isLoadedProperty() {
    return loadedProperty.get();
  }

  public BooleanProperty loadedPropertyProperty() {
    return loadedProperty;
  }
}
