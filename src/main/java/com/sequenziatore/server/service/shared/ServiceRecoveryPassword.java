/*
 * Copyright 2014 Dainese Matteo, De Nadai Andrea, Girotto Giacomo, Pavanello Mirko, Romagnosi Nicolò, Sartoretto Massimiliano, Visentin Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 	File contentente la classe ServiceRecoveryPassword
 * 
 *	@file		ServiceRecoveryPassword.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.shared;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;

import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceRecoveryPassword offre il servizio di recupero della password.
 *
 *	@author 	DeSQ
 */
public class ServiceRecoveryPassword implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette il recupero della password grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'email o username dell'utente che fa la richiesta
	 * @return l'esito dell'operazione di recupero password
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		boolean recoveryPassword = true;
		if(entity.size()==2)
			recoveryPassword = false;
			
		User recipientUser = null;
		User user = (User) entity.get(0);
		
		JSONObject responseJSON = new JSONObject();
		
		iDAOFactory = new DAOFactory();
	    try {
	    	HibernateUtil.getSession().beginTransaction();
			recipientUser = iDAOFactory.createDAOUserData().findUserByEmail(user);
		    HibernateUtil.commitTransaction();
		    
		    //se non è stato trovato significa che ha inserito lo username e non la mail
	    	if(recipientUser==null)
	    	{
			    HibernateUtil.getSession().beginTransaction();
				recipientUser = iDAOFactory.createDAOUserData().findUserByUsername(user);
			    HibernateUtil.commitTransaction();
			}
		} catch (Exception e) {
			return responseJSON.put("Confirmation", "connectionError");
		}

	    if(recipientUser == null)
	    	return responseJSON.put("Confirmation", "wrongRecovery");
	    
		// recupero della password dell'utente
		String to = recipientUser.getEmail();
		
		// mail del mittente
		String from = "team.desq@gmail.com";//change accordingly

		Session session = this.createSession();

		try {
			// creazione dell'oggetto Message
			Message message = new MimeMessage(session);

			// impostazione del mittente della mail
			message.setFrom(new InternetAddress(from));

			// impostazione del destinatario della mail
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// password da inviare via mail all'utente
			String newPassword = this.createAndUpdatePassword(recipientUser);
			
			//imposta l'oggetto e il contenuto del messaggio da inviare
			createMessage(message, newPassword, recoveryPassword);

			// invia il messaggio
			Transport.send(message);
			responseJSON.put("Confirmation", "successRecovery");
		}
		catch (MessagingException e) {
			return responseJSON.put("Confirmation", "internetConnectionError");
		} catch (Exception e) {
			return responseJSON.put("Confirmation", "connectionError");
		}

		return responseJSON;
	}
	
	/**
	 * Imposta l'oggetto e il testo del messaggio da inviare all'utente.
	 * 
	 * @param message rappresenta il messaggio che verrà inviato
	 * @param newPassword la nuova password che l'utente dovrà usare per il login
	 * @param recoveryPassword il boleano che indica se si sta recuperando la password o se si tratta di una registrazione via Facebook
	 * @throws MessagingException indica un problema di connessione alla casella e-mail
	 */
	private void createMessage(Message message, String newPassword, boolean recoveryPassword) throws MessagingException	{
		String messageString = null;
		
		// reset della password richiesto dall'utente
		if(recoveryPassword == true)
		{
			// imposta l'oggetto della mail
			message.setSubject("Seq - Servizio di recupero password");
			// il testo della mail da inviare
			messageString = new String("Gentile utente,\n la sua password è stata reimpostata e al prossimo accesso dovrà utilizzare questa password provvisoria:\t"+newPassword+"\n\n È fortemente consigliato l'aggiornamento della password provvisoria.\n\n Cordiali saluti,\n team DeSQ.");
		}
		// impostazione nuova password dopo login da social network
		else if (recoveryPassword == false)
		{
			// imposta l'oggetto della mail
			message.setSubject("Seq - Servizio di registrazione");
			// il testo della mail da inviare
			messageString = new String("Gentile utente,\n grazie per essersi registrato a Seq.\n La sua password per l'accesso al sistema è:\t"+newPassword+"\n\n Al prossimo accesso è fortemente consigliato l'aggiornamento della password.\n\n Cordiali saluti,\n team DeSQ.");
		}
		
		// imposta il testo della mail
		message.setText(messageString);
	}
	
	/**
	 * Ritorna la sessione per  connettersi alla casella mail.
	 * 
	 * @return la sessione per  connettersi alla casella mail
	 */
	private Session createSession() {
		final String username = "team.desq";//change accordingly
		final String password = "desq2013";//change accordingly
		
		// mail inviata tramite Gmail
		String host = "smtp.gmail.com";
				
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// recupera la sessione
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		return session;
	}
	
	/**
	 * Crea una nuova password da inviare all'utente e aggiorna le informazioni nel database.
	 * 
	 * @param recipientUser l'utente che fa la richiesta di recupero password
	 * @return la nuova password da inviare all'utente
	 * @throws Exception indica un problema di connessione con il database
	 */
	private String createAndUpdatePassword(User recipientUser) throws Exception {
		String newPassword = generateRandomString();
		recipientUser.hashAndSetPassword(newPassword);
		
		//aggiornamento della password nel database
		HibernateUtil.getSession().beginTransaction();
		iDAOFactory.createDAOUserData().insertUser(recipientUser);
        HibernateUtil.commitTransaction();
        
		return newPassword;
	}
	
	/**
	 * Ritorna una stringa random di 10 caratteri che verrà usata come password temporanea.
	 * @return una stringa random di 10 caratteri 
	 */
	private String generateRandomString(){
		String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer randStr = new StringBuffer();
        for(int i = 0; i < 10; i++){
            int number = getRandomNumber(charList);
            char ch = charList.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

	/**
     * Ritorna un numero random usato per la selezione dei caratteri da usare nella password temporanea.
     * @param charList la lista dei caratteri e numeri da cui estrarne uno
     * @return un numero intero random
     */
    private int getRandomNumber(String charList) {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(charList.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
    
}
 
