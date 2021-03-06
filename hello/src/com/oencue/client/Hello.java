package com.oencue.client;

import com.oencue.shared.FieldVerifier;
import com.oencue.shared.LoginInfo;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.bouncycastle.digests.SHA1Digest;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.googlecode.gwt.crypto.client.TripleDesKeyGenerator;
import com.google.gwt.user.client.ui.LayoutPanel;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Anchor;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Hello implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final noteMapperAsync noteMapper = GWT.create(noteMapper.class);
	
	  private static Logger rootLogger = Logger.getLogger("");
	  
	  	private LoginInfo loginInfo = null;

	    private VerticalPanel loginPanelx = new VerticalPanel();
	    private Label loginLabel = new Label(
	        "Please sign in to your Google Account to access the StockWatcher application.");
	    private Anchor signInLink = new Anchor("Sign In");
	  	
	    
	    private TripleDesCipher encryptor;  
	  
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Label errorLabel = new Label();
		errorLabel.setText("ErrorLabel");
		

		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  errorLabel.setText("Error " + GWT.getHostPageBaseURL() + " " + error);
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	          //loadStockWatcher();
	        } else {
	            signInLink.setHref(loginInfo.getLoginUrl());
			    loginPanelx.add(loginLabel);
			    loginPanelx.add(signInLink);
			    RootPanel.get("mainContainer").add(loginPanelx);
	        }
	      }
	    });
	  
				
		
		
		
		
		final Button sendButton = new Button("Login");
		final Button newNoteButton = new Button("New Note");
		final TextBox nameField = new TextBox();
		nameField.setText("Your Username");
		
		final TextBox nameField2 = new TextBox();
		nameField2.setText("List of previous Notes");
		
		final TextArea newNoteField = new TextArea();
		newNoteField.setText("Type New Note here\nThen press save");
		final Button saveButton = new Button("Save");

		// We can add style names to widgets
		//sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel.get("nameFieldContainer").add(nameField);
		//RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		
		final VerticalPanel panel1 = new VerticalPanel();
		final VerticalPanel panel2 = new VerticalPanel();
		final VerticalPanel panel3 = new VerticalPanel();

//		class updatePanels {
//		public void updatePanel(VerticalPanel panel) {
//			panel.setWidth("100%");
//			panel.setHeight("100%");
//			panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//			
//		}
//		}
//		updatePanels updater = new updatePanels();
		updateVPanels updater = new updateVPanels();
		updater.updatePanel(panel1);
		updater.updatePanel(panel2);
		updater.updatePanel(panel3);

		panel1.add(new HTML("<b>Enter your name:</b>"));
		sendButton.getElement().setClassName("button");
		nameField.getElement().setClassName("textBox");

		Button[] b=new Button[5];

	    // Create a handler for the A-Z buttons
	    class ButtonHandler implements ClickHandler {
	        public void onClick(ClickEvent e) {
	            Button btn=(Button) e.getSource();
	            if(Integer.parseInt(btn.getElement().getId()) == 100) {
		            errorLabel.setText("100:" + btn.getElement().getId());

	            }else {
		            errorLabel.setText(btn.getElement().getId());

	            }
	            	
	       	        }
	    }

	    ButtonHandler buttonHandler = new ButtonHandler();
		final FlexTable flexTable = new FlexTable();
	
	    for(int i=0;i<5;i++) {
		   	Label title= new Label();
	    	title.setText(String.valueOf(i)+":");
	        b[i] = new Button(String.valueOf(i));
	        b[i].addStyleName("sendButton");
	        b[i].getElement().setId(String.valueOf(i+100));
            flexTable.setWidget(i, 0, title);
            flexTable.setWidget(i, 1, b[i]);

	        b[i].addClickHandler(buttonHandler);
	    }
		
		
		panel1.add(nameField);
		panel1.add(flexTable);
		panel1.add(sendButton);
		
		
		
		

	 
	 
		
		
		
		
		
		
		

		newNoteButton.getElement().setClassName("button");
		nameField2.getElement().setClassName("textBox");
		panel2.add(newNoteButton);
		panel2.add(nameField2);

		
		newNoteField.getElement().setClassName("textBox");
		saveButton.getElement().setClassName("button");
		panel3.add(newNoteField);
		panel3.add(saveButton);


		RootPanel.get("mainContainer").add(panel1);
		RootPanel.get("mainContainer").add(panel2);
		RootPanel.get("mainContainer").add(panel3);

		panel2.setVisible(false);
		panel3.setVisible(false);
		
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogVPanel.add(closeButton);
		dialogVPanel.setWidth("500px");
		dialogBox.setWidget(dialogVPanel);
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Add a handler to newNoteButton
		newNoteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				changeView();
			}
			
			private void changeView() {
				newNoteButton.setEnabled(true);
				newNoteButton.setFocus(true);
				dialogBox.hide();
				panel2.setVisible(false);
				panel3.setVisible(true);
				errorLabel.setText("ErrorLabel");
				
			}
		});
		
		// Add a handler to saveButton
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
	
				changeView();
		
		           rootLogger.log(Level.SEVERE, "pageIndex selected: " );	
				
			}
			
			private void changeView() {
				
				dialogBox.setText("Data is Saved");
				serverResponseLabel.removeStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML("");
				textToServerLabel.setText("");
				dialogBox.center();
				closeButton.setFocus(true);
				
				saveButton.setEnabled(true);
				saveButton.setFocus(true);
				panel3.setVisible(false);
				panel2.setVisible(true);

			}
		});
		
		
		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			
			String getSHA1for(String text) {
				  SHA1Digest sd = new SHA1Digest();
				  byte[] bs = text.getBytes();
				  sd.update(bs, 0, bs.length);
				  byte[] result = new byte[20];
				  sd.doFinal(result, 0);
				  return byteArrayToHexString(result);
				}

				String byteArrayToHexString(final byte[] b) {
				  final StringBuffer sb = new StringBuffer(b.length * 2);
				  for (int i = 0, len = b.length; i < len; i++) {
				    int v = b[i] & 0xff;
				    if (v < 16) sb.append('0');
				    sb.append(Integer.toHexString(v));
				  }
				  return sb.toString();
				}
				
		
				  private String encryptString(String string)
				    {
				        try 
				        {
				            string = encryptor.encrypt( string );
				        } 
				        catch (DataLengthException e1) 
				        {
				            e1.printStackTrace();
				        } 
				        catch (IllegalStateException e1) 
				        {
				            e1.printStackTrace();
				        } 
				        catch (InvalidCipherTextException e1) 
				        {
				            e1.printStackTrace();
				        }

				        return string;
				    }

				    private String decryptString(String string)
				    {
				        try 
				        {
				            string = encryptor.decrypt(string);
				        } 
				        catch (DataLengthException e) 
				        {
				            e.printStackTrace();
				        } catch (IllegalStateException e) 
				        {
				            e.printStackTrace();
				        } catch (InvalidCipherTextException e)
				        {
				            e.printStackTrace();
				        }

				        return string;
				    }
				
			private void sendNameToServer() {
				// First, we validate the input.
				 TripleDesKeyGenerator generator = new TripleDesKeyGenerator();
				    byte[] key = generator.decodeKey("04578a8f0be3a7109d9e5e86839e3bc41654927034df92ec"); //you can pass your own string here

				    //initializing encryptor with generated key
				    encryptor = new TripleDesCipher();
				    encryptor.setKey(key);
				    
								
				errorLabel.setText("password encripted: " + encryptString("password") + " decripted: " +  decryptString(encryptString("password")));
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				
				int command = 0;
				noteMapper.access2Database(textToServer,command, new AsyncCallback<String>() {
				//greetingService.greetServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						//dialogBox.setText("Remote Procedure Call - Failure");
						//serverResponseLabel.addStyleName("serverResponseLabelError");
						//serverResponseLabel.setHTML(SERVER_ERROR);
						//dialogBox.center();
						//closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
									
						closeButton.setFocus(true);
						panel1.setVisible(false);
						panel2.setVisible(true);

						

					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
