package com.yash.phonenumberpicker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.api.credentials.*;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.ActivityResultListener;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;


public class PhoneNumberPicker extends AndroidNonvisibleComponent implements ActivityResultListener {
  public Context context;
  public Activity activity;
  public ComponentContainer myContainer;
  public int requestMyCode;
  public PhoneNumberPicker(ComponentContainer container) {
   super(container.$form());
   myContainer = container;
   context = container.$context();
   activity = container.$context();
   requestMyCode = form.registerForActivityResult(this);
  }

  @SimpleFunction() public void pickPhone(boolean CancelButton,boolean addAccountButton,int prompt){
    HintRequest hintRequest = new HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .setHintPickerConfig(new CredentialPickerConfig.Builder()
                    .setShowCancelButton(CancelButton)
                    .setShowAddAccountButton(addAccountButton)
                    .setPrompt(prompt)
                    .build()).build();
    PendingIntent intent = Credentials.getClient(context).getHintPickerIntent(hintRequest);
    requestMyCode = form.registerForActivityResult(this);
    try {activity.startIntentSenderForResult(intent.getIntentSender(),requestMyCode, null, 0, 0, 0,new Bundle());}
    catch (Exception e) {onError(e.toString());}}

  @SimpleProperty() public int ContinueWith(){return CredentialPickerConfig.Prompt.CONTINUE;}

  @SimpleProperty() public int SignInWith(){return CredentialPickerConfig.Prompt.SIGN_IN;}

  @SimpleProperty() public int SignUpWith(){return CredentialPickerConfig.Prompt.SIGN_UP;}

  @SimpleFunction() public void PickEmail(boolean CancelButton,boolean addAccountButton,int prompt){
    HintRequest hintRequest = new HintRequest.Builder()
                    .setAccountTypes(IdentityProviders.GOOGLE)
                    .setEmailAddressIdentifierSupported(true)
                    .setHintPickerConfig(new CredentialPickerConfig.Builder()
                    .setShowCancelButton(CancelButton)
                    .setShowAddAccountButton(addAccountButton)
                    .setPrompt(prompt)
                    .build()).build();
    PendingIntent intent = Credentials.getClient(context).getHintPickerIntent(hintRequest);
    requestMyCode = form.registerForActivityResult(this);
    try {activity.startIntentSenderForResult(intent.getIntentSender(),requestMyCode, null, 0, 0, 0,new Bundle());}
    catch (Exception e) {onError(e.toString());}}

  @SimpleEvent(description = "on get")
  public void onGet(String result){EventDispatcher.dispatchEvent(this,"onGet",result);}

  @SimpleEvent(description = "on error")
  public void onError(String error){EventDispatcher.dispatchEvent(this,"onError",error);}

  @SimpleEvent(description = "When no pick")
  public void whenNoPick(){
    EventDispatcher.dispatchEvent(this,"whenNoPick");
  }

  @Override
  public void resultReturned(int requestCode, int resultCode, Intent data) {
    if(requestCode == requestMyCode && resultCode == Activity.RESULT_OK) {
      Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
      onGet(credentials.getId());
    }else if (requestCode == requestMyCode && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
      onGet("No phone number available");
    }
    else whenNoPick();
  }

}
