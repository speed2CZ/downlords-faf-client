package com.faforever.client.login;

import com.faforever.client.preferences.LoginPrefs;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.user.UserService;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.soap.Text;
import java.lang.invoke.MethodHandles;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegisterController {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @FXML
  Pane registerPane;
  @FXML
  Pane registerRoot;
  @FXML
  TextField usernameInput;
  @FXML
  TextField passwordInput;
  @FXML
  TextField emailInput;
  @FXML
  TextField usernameErrorMessage;
  @FXML
  TextField passwordErrorMessage;
  @FXML
  TextField emailErrorMessage;
  @FXML
  Button registerButton;
  @FXML
  Button cancelButton;
  @FXML
  ImageView usernameStatusImageView;
  @FXML
  ImageView passwordStatusImageView;
  @FXML
  ImageView emailStatusImageView;
  @FXML
  Label registerStatus;

  Image pass = new Image("images\\status\\pass.png"); //the check for the image location
  Image fail = new Image("images\\status\\fail.png"); //the X for the image location

  @Resource
  UserService userService;
  @Resource
  PreferencesService preferencesService;


  @FXML
  private void initialize() {
    registerPane.setVisible(true);
  }

  public void display() {

  }

  @FXML
  private void cancelButtonClicked(){
    //close out of window...
  }
  @FXML
  private void registerButtonClicked(){
    if (! isUsernameValid().equals(null)){
      usernameStatusImageView.setImage(fail);
      usernameStatusImageView.setVisible(true);
      usernameErrorMessage.setText(isUsernameValid());
      usernameErrorMessage.setVisible(true);
    } else {
      usernameStatusImageView.setImage(pass);
      usernameStatusImageView.setVisible(true);
      usernameErrorMessage.setVisible(false);
    }

    if (! isPasswordValid().equals(null)){
      passwordStatusImageView.setImage(fail);
      passwordStatusImageView.setVisible(true);
      passwordErrorMessage.setText(isUsernameValid());
      passwordErrorMessage.setVisible(true);
    } else {
      passwordStatusImageView.setImage(pass);
      passwordStatusImageView.setVisible(true);
      passwordErrorMessage.setVisible(false);
    }

    if (! isEmailValid().equals(null)){
      emailStatusImageView.setImage(fail);
      emailStatusImageView.setVisible(true);
      emailErrorMessage.setText(isUsernameValid());
      emailErrorMessage.setVisible(true);
    } else {
      emailStatusImageView.setImage(pass);
      emailStatusImageView.setVisible(true);
      emailErrorMessage.setVisible(false);
    }
    if (isUsernameValid().equals(null) && isPasswordValid().equals(null) && isEmailValid().equals(null)){
      //send verification email
      sendVerificationEmail();
      registerStatus.setText("Please check your email to verify your account");
      registerStatus.setVisible(true);
      //when the email has been verified.... idk how
      registerStatus.setText("Your email has been verified");
      //register account
      registerAccount();
      registerStatus.setText("Your account has been registered, you may now login");
      //close window
    }
  }

  //send the verification email
  private void sendVerificationEmail(){}
  //register the account
  private void registerAccount(){}

  //public for junit tests
  public String isUsernameValid(){
    if (usernameInput.getText().startsWith(","))
      return "Username may not start with a comma";
    else if (usernameInput.getText().length() < 1 || usernameInput.getText().length() > 20)
      return "Username must be between 1 and 20 characters long";
    return null;
  }

  private String isPasswordValid(){
    if (passwordInput.getText().length()>0)
      return null;
    return "Enter a password";
  }

  //public for junit tests
  public String isEmailValid(){
    Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
    if (pattern.matcher(emailInput.getText()).matches()) {
      return null;
    }
    return "Invalid email";
  }
}

/*
  THE PYTHON CODE FOR  CREATING AN ACCOUNT...
def command_create_account(self, message):
        login = message['login']
        user_email = message['email']
        password = message['password']

        username_pattern = re.compile(r"^[^,]{1,20}$")
        email_pattern = re.compile(r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$")

        def reply_no(error_msg):
            self.sendJSON({
                "command": "registration_response",
                "result": "FAILURE",
                "error": error_msg
            })

        if not email_pattern.match(user_email):
            reply_no("Please use a valid email address.")
            return

        if not username_pattern.match(login):
            reply_no("Please don't use \",\" in your username.")
            return

        with (yield from db.db_pool) as conn:
            cursor = yield from conn.cursor()
            yield from cursor.execute("SELECT id FROM `login` WHERE LOWER(`login`) = %s",
                                      (login.lower(),))
            if cursor.rowcount:
                reply_no("Sorry, that username is not available.")
                return

        if self.player_service.has_blacklisted_domain(user_email):
            # We don't like disposable emails.
            text = "Dear " + login + ",\n\n\
Please use a non-disposable email address.\n\n"
            yield from self.send_email(text, login, user_email, 'Forged Alliance Forever - Account validation')
            return

        # We want the user to validate their email address before we create their account.
        #
        # We want to email them a link to click which will lead to their account being
        # created, but without storing any data on the server in the meantime.
        #
        # This is done by sending a link of the form:
        # *.php?data=E(username+password+email+expiry+nonce, K)&token=$VERIFICATION_CODE
        # where E(P, K) is a symmetric encryption function with plaintext P and secret key K,
        # and
        # VERIFICATION_CODE = sha256(username + password + email + expiry + K + nonce)
        #
        # The receiving php script decrypts `data`, verifies it (username still free? etc.),
        # recalculates the verification code, and creates the account if it matches up.
        #
        # As AES is not readily available for both Python and PHP, Blowfish is used.
        #
        # We thus avoid a SYN-flood-like attack on the registration system.

        iv, ciphertext, verification_hex = self.generate_expiring_request(3600 * 25, login + "," + password + "," + user_email)


        link = {'a': 'v', 'iv': iv, 'c': ciphertext, 'v': verification_hex}

        passwordLink = urllib.parse.urljoin(config.APP_URL, "faf/validateAccount.php?" + urllib.parse.urlencode(link))

        text = "Dear " + login + ",\n\n\
Please visit the following link to validate your FAF account:\n\
-----------------------\n\
" + passwordLink + "\n\
-----------------------\n\n\
Thanks,\n\
-- The FA Forever team"

        yield from self.send_email(text, login, user_email, 'Forged Alliance Forever - Account validation')

        self.sendJSON(dict(command="notice", style="info",
                           text="A e-mail has been sent with the instructions to validate your account"))
        self._logger.debug("Sent mail")
        self.sendJSON(dict(command="registration_response", result="SUCCESS"))

    async def send_email(self, text, to_name, to_email, subject):
        msg = MIMEText(text)

        msg['Subject'] = subject
        msg['From'] = email.utils.formataddr(('Forged Alliance Forever', "admin@faforever.com"))
        msg['To'] = email.utils.formataddr((to_name, to_email))

        self._logger.debug("Sending mail to " + to_email)
        url = config.MANDRILL_API_URL + "/messages/send-raw.json"
        headers = {'content-type': 'application/json'}
        resp = await aiohttp.post(url,
                           data=json.dumps({
                "key": config.MANDRILL_API_KEY,
                "raw_message": msg.as_string(),
                "from_email": 'admin@faforever.com',
                "from_name": "Forged Alliance Forever",
                "to": [
                    to_email
                ],
                "async": False
            }),
            headers=headers)
        resp_text = await resp.text()
        self._logger.info("Mandrill response: {}".format(resp_text))

    @timed()

 */
