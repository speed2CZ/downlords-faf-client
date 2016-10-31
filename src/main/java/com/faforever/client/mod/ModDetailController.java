package com.faforever.client.mod;

import com.faforever.client.i18n.I18n;
import com.faforever.client.io.Bytes;
import com.faforever.client.notification.ImmediateNotification;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.ReportAction;
import com.faforever.client.notification.Severity;
import com.faforever.client.reporting.ReportingService;
import com.faforever.client.util.IdenticonUtil;
import com.faforever.client.util.TimeService;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

import static java.util.Collections.singletonList;

public class ModDetailController {

  public Label scoreLabel;
  public Label updatedLabel;
  public Label sizeLabel;
  public Label installationsLabel;
  public Label versionLabel;
  public Label dependenciesTitle;
  public VBox dependenciesContainer;
  public Label progressLabel;
  public Button uninstallButton;
  public Button installButton;
  public ImageView thumbnailImageView;
  public Label nameLabel;
  public Label authorLabel;
  public ProgressBar progressBar;
  public Label modDescriptionLabel;
  public Node modDetailRoot;
  public Group reviewGroup;

  @Resource
  ModService modService;
  @Resource
  NotificationService notificationService;
  @Resource
  I18n i18n;
  @Resource
  TimeService timeService;
  @Resource
  ReportingService reportingService;

  private ModInfoBean mod;
  private ListChangeListener<ModInfoBean> installStatusChangeListener;

  @FXML
  void initialize() {
    uninstallButton.managedProperty().bind(uninstallButton.visibleProperty());
    installButton.managedProperty().bind(installButton.visibleProperty());
    progressBar.managedProperty().bind(progressBar.visibleProperty());
    progressBar.visibleProperty().bind(uninstallButton.visibleProperty().not().and(installButton.visibleProperty().not()));
    progressLabel.managedProperty().bind(progressLabel.visibleProperty());
    progressLabel.visibleProperty().bind(progressBar.visibleProperty());

    modDetailRoot.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode() == KeyCode.ESCAPE) {
        onCloseButtonClicked();
      }
    });

    installStatusChangeListener = change -> {
      while (change.next()) {
        for (ModInfoBean modInfoBean : change.getAddedSubList()) {
          if (mod.getId().equals(modInfoBean.getId())) {
            setInstalled(true);
            return;
          }
        }
        for (ModInfoBean modInfoBean : change.getRemoved()) {
          if (mod.getId().equals(modInfoBean.getId())) {
            setInstalled(false);
            return;
          }
        }
      }
    };

    // TODO hidden until review system is implemented
    reviewGroup.setVisible(false);
    reviewGroup.setManaged(false);

    // TODO hidden until dependencies are available
    dependenciesTitle.setManaged(false);
    dependenciesContainer.setManaged(false);
  }

  public void onCloseButtonClicked() {
    getRoot().setVisible(false);
  }

  private void setInstalled(boolean installed) {
    installButton.setVisible(!installed);
    uninstallButton.setVisible(installed);
  }

  public Node getRoot() {
    return modDetailRoot;
  }

  public void setMod(ModInfoBean mod) {
    this.mod = mod;
    thumbnailImageView.setImage(modService.loadThumbnail(mod));
    nameLabel.setText(mod.getName());
    authorLabel.setText(mod.getAuthor());

    boolean modInstalled = modService.isModInstalled(mod.getId());
    installButton.setVisible(!modInstalled);
    uninstallButton.setVisible(modInstalled);

    modDescriptionLabel.setText(mod.getDescription());
    modService.getInstalledMods().addListener(new WeakListChangeListener<>(installStatusChangeListener));
    setInstalled(modService.isModInstalled(mod.getId()));

    updatedLabel.setText(timeService.asDate(mod.getPublishDate()));
    sizeLabel.setText(Bytes.formatSize(getModSize(), i18n.getLocale()));
    installationsLabel.setText(String.format(i18n.getLocale(), "%d", mod.getDownloads()));
    versionLabel.setText(mod.getVersion());
  }

  private long getModSize() {
    return modService.getModSize(mod);
  }

  @FXML
  void onInstallButtonClicked() {
    installButton.setVisible(false);

    modService.downloadAndInstallMod(mod, progressBar.progressProperty(), progressLabel.textProperty())
        .exceptionally(throwable -> {
          notificationService.addNotification(new ImmediateNotification(
              i18n.get("errorTitle"),
              i18n.get("modVault.installationFailed", mod.getName(), throwable.getLocalizedMessage()),
              Severity.ERROR, throwable, singletonList(new ReportAction(i18n, reportingService, throwable))));
          return null;
        });
  }

  @FXML
  void onUninstallButtonClicked() {
    progressBar.progressProperty().unbind();
    progressBar.setProgress(-1);
    uninstallButton.setVisible(false);

    modService.uninstallMod(mod).exceptionally(throwable -> {
      notificationService.addNotification(new ImmediateNotification(
          i18n.get("errorTitle"),
          i18n.get("modVault.couldNotDeleteMod", mod.getName(), throwable.getLocalizedMessage()),
          Severity.ERROR, throwable, singletonList(new ReportAction(i18n, reportingService, throwable))));
      return null;
    });
  }

  @FXML
  void onDimmerClicked() {
    onCloseButtonClicked();
  }

  public void onContentPaneClicked(MouseEvent event) {
    event.consume();
  }
}
