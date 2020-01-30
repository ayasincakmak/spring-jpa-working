package com.thy.emd.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.api.UIColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.thy.emd.util.Util;
import com.thy.troyaapi.core.StartupException;
import com.thy.troyaapi.core.TroyaResponseException;
import com.thy.troyaapi.core.terminal.TroyaTerminalException;
import com.thy.troyaapi.cripool.CriSessionManagerException;




@Named
@Scope("session")
@ManagedBean
public class EmdValidationMessages implements Serializable {

	@Inject
	TroyaClient troyaClient;
	private static final long serialVersionUID = 7065853250288984422L;
	private List<Message> messages;
	private List<Message> filteredMessages;
	private String newSubject = "my subject";
	private String newText = "my text";
	private  static String[] states;
	private Date currentDate = new Date();
	private boolean checkValue;
	
	private static final String EBCDIC_ENCODING_VALUE = "Cp037";
	private static final String NULL_CHAR = "\0";
	private static final String EMPTY_STRING = "";
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	
	
	
	public ArrayList<Message> fillTable() {
		
		states = new String[10];
		for (int i = 0; i < 10; i++) {
			states[i] = "state" + i;
		}
	
		
		if (messages == null) {
			messages = new ArrayList<Message>();

			for (int i = 0; i < 10; i++) {
				final Message message = new Message();
				message.setSubject("subject " + i);
				message.setText("text " + i);
				message.setTextLength(i * 10 + 10 + "");
				message.setCountry("country" + i);
				message.setState("state" + i);
				message.setDeliveryStatus("successfull");
				messages.add(message);
			}
		}
		
		
		String response = null;
		try {

			byte EBCDIC_NL = 0x15; // next line
			byte EBCDIC_LF = 0x25; // line feed
			byte EBCDIC_CR = 0x0D; // carriage return
			Util.ebcdicToUtf16(EBCDIC_NL);
			Util.ebcdicToUtf16(EBCDIC_LF);
			Util.ebcdicToUtf16(EBCDIC_CR);

			// 155C
			byte[] array = new byte[2];
			array[0] = 00;
			array[1] = 85;

			String deneme = EMPTY_STRING;

			try {
				deneme = Util.ConvertByteArrayToEbcdicText(array);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String text = EMPTY_STRING;
			try {
				text = new String(array, 0, array.length, EBCDIC_ENCODING_VALUE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// text = text.replace(NULL_CHAR, StringConstants.EMPTY_STRING);

			// response = TroyaClient.getInstance().execute(
			// "T"+byteArrayToHexString(array)+"*EMD");
			response =troyaClient.execute("T" + "\u0085" + "*EMD");
			// response = TroyaClient.getInstance().execute( "Zukas 08F9E5" );
			// response = TroyaClient.getInstance().execute("*EMD-/2354562598663");

			System.out.println(response);
			System.out.println("------");
			// }
		} catch (StartupException | TroyaResponseException | TroyaTerminalException | CriSessionManagerException | RuntimeException e) {
			response = null;
		}

		System.out.println("response = " + response);
		
		return (ArrayList<Message>) messages;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(final List<Message> messages) {
		this.messages = messages;
	}

	public List<Message> getFilteredMessages() {
		return filteredMessages;
	}

	public void setFilteredMessages(final List<Message> filteredMessages) {
		this.filteredMessages = filteredMessages;
	}

	public String getNewSubject() {
		return newSubject;
	}

	public void setNewSubject(final String newSubject) {
		this.newSubject = newSubject;
	}

	public String getNewText() {
		return newText;
	}

	public void setNewText(final String newText) {
		this.newText = newText;
	}

	public String[] getStates() {
		return states;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(final Date currentDate) {
		this.currentDate = currentDate;
	}

	public boolean getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(final boolean checkValue) {
		this.checkValue = checkValue;
	}

	public String addMessage() {
		doSomething();

		final Message message = new Message();
		message.setSubject(newSubject);
		message.setText(newText);
		messages.add(0, message);

		newSubject = "";
		newText = "";

		final FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "New message has been added successfully", null));

		return null;
	}

	public void doSomething() {
		try {
			// simulate a long running request
			Thread.sleep(1500);
		} catch (final Exception e) {
			// ignore
		}
	}

	public String exportColumn(final UIColumn column) {
		return "PFE Rocks!";
	}

	public class Message implements Serializable {

		private static final long serialVersionUID = 1L;
		private String subject;
		private String text;
		private long time;
		private String textLength;
		private String country;
		private String state;
		private String deliveryStatus;

		public Message() {
			time = System.currentTimeMillis() + (long) (Math.random() * 10);
			textLength = Math.random() * 10 + "";
		}

		public final String getSubject() {
			return subject;
		}

		public final void setSubject(final String subject) {
			this.subject = subject;
		}

		public final String getText() {
			return text;
		}

		public final void setText(final String text) {
			this.text = text;
		}

		public long getTime() {
			return time;
		}

		public void setTime(final long time) {
			this.time = time;
		}

		public String getTextLength() {
			return textLength;
		}

		public void setTextLength(final String textLength) {
			this.textLength = textLength;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(final String country) {
			this.country = country;
		}

		public String getState() {
			return state;
		}

		public void setState(final String state) {
			this.state = state;
		}

		public String getDeliveryStatus() {
			return deliveryStatus;
		}

		public void setDeliveryStatus(final String deliveryStatus) {
			this.deliveryStatus = deliveryStatus;
		}
	}
	
	
	
}
            