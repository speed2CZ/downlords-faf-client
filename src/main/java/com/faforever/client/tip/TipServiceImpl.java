package com.faforever.client.tip;


import com.faforever.client.preferences.PreferencesService;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Service
public class TipServiceImpl implements TipService {

  private static int numberOfTips;
  private final PreferencesService preferencesService;

  @Inject
  public TipServiceImpl(PreferencesService preferencesService1) {

    this.preferencesService = preferencesService1;
  }

  @PostConstruct
  public void postConstruct() {
    numberOfTips = 10;
  }

  @Override
  public CompletableFuture<Tip> getTipById(int tipId) {
    if (tipId > numberOfTips) {
      preferencesService.getPreferences().getNews().setLastTipId(1);
      tipId = 1;
    }
    if (tipId < 1) {
      preferencesService.getPreferences().getNews().setLastTipId(numberOfTips);
      tipId = numberOfTips;
    }

    return CompletableFuture.supplyAsync(() -> new Tip("Das Grundgesetz (GG) ist die Verfassung für die Bundesrepublik Deutschland. Es wurde vom Parlamentarischen Rat, dessen Mitglieder von den Landesparlamenten gewählt worden waren, am 8. Mai 1949 beschlossen und von den Alliierten genehmigt. Es setzt sich aus einer Präambel, den Grundrechten und einem organisatorischen Teil zusammen. Im Grundgesetz sind die wesentlichen staatlichen System- und Werteentscheidungen festgelegt. Es steht im Rang über allen anderen deutschen Rechtsnormen.\n" +
        "\n" +
        "Für eine Änderung des Grundgesetzes ist die Zustimmung des Bundestages sowie des Bundesrates erforderlich. Es ist jedoch nach Artikel 79 Absatz 3 GG unzulässig, die grundsätzliche Mitwirkung der Länder bei der Gesetzgebung zu ändern. Die in den Artikeln 1 und 20 des Grundgesetzes niedergelegten Grundsätze sind unabänderlich. Artikel 1 garantiert die Menschenwürde und unterstreicht die Rechtsverbindlichkeit der Grundrechte. Artikel 20 beschreibt Staatsprinzipien wie Demokratie, Rechtsstaat und Sozialstaat.", "Grundgesetz", new Image("https://www.bundestag.de/blob/451030/23aeaa0485a3e37bcf167e1a25adc481/image16x9_big-data.png")));
  }
}
