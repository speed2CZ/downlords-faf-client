package com.faforever.client.login;

import com.faforever.client.test.AbstractPlainJavaFxTest;
import javafx.scene.control.TextField;
import org.eclipse.jgit.errors.LargeObjectException;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by noah on 5/16/2016.
 */
public class RegisterControllerTest extends AbstractPlainJavaFxTest {

  private RegisterController rc;

  @Before
  public void setUp() throws Exception {
    rc = new RegisterController();
  }

  @After
  public void tearDown() throws Exception {
    rc = null;
  }

  @Test
  public void isUsernameValidTest() throws Exception{
    rc.usernameInput = new TextField();
    rc.usernameInput.setText("");
    assertEquals(rc.isUsernameValid(),"Username must be between 1 and 20 characters long");
    rc.usernameInput.setText("1234567890123456789012");
    assertEquals(rc.isUsernameValid(),"Username must be between 1 and 20 characters long");
    rc.usernameInput.setText(",asdasd");
    assertEquals(rc.isUsernameValid(),"Username may not start with a comma");
    rc.usernameInput.setText("wrhie983rjwo");
    assertEquals(rc.isUsernameValid(),null);

  }

  @Test
  public void isEmailValidTest() throws Exception{
    rc.emailInput = new TextField();
    rc.emailInput.setText("");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("asdasdasdasd");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("a");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("aaaa");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("sssss#$%$#@.");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("@.");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("a@.");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("@a.");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("@.a");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("a@a.");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("a@.a");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("@a.a");
    assertEquals(rc.isEmailValid(),"Invalid email");
    rc.emailInput.setText("noah0m0jaffe@gmail.com");
    assertEquals(null,rc.isEmailValid());

  }
}








