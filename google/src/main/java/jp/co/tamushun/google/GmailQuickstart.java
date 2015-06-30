package jp.co.tamushun.google;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

public class GmailQuickstart {
	/** Application name. */
	private static final String APPLICATION_NAME = "Gmail API Java Quickstart";

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/gmail-api-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart. */
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.MAIL_GOOGLE_COM);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GmailQuickstart.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Gmail client service.
	 * 
	 * @return an authorized Gmail client service
	 * @throws IOException
	 */
	public static Gmail getGmailService() throws IOException {
		Credential credential = authorize();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws IOException {
		// Build a new authorized API client service.
		Gmail service = getGmailService();
		// quickStartSample(service);

		// 該当するメールリスト取得
		List<Message> messages = MessagesApi.listMessagesMatchingQuery(service, "me",
				"((label:from nagios) OR (label:from aws_alert)) is:unread");

		for (Message message : messages) {
			// 1件ずつメールの詳細情報を取得（list APIでは詳細情報が取得できない）
			message = MessagesApi.getMessage(service, "me", message.getId());
			List<MessagePartHeader> messagePartHeaders = message.getPayload().getHeaders();
			String title = "";
			for (MessagePartHeader messagePartHeader : messagePartHeaders) {
				if (messagePartHeader.getName().equals("Subject")) {
					title = messagePartHeader.getValue();
					break;
				}
			}
			String bodyEncodedByBase64 = String.valueOf(message.getPayload().getBody().getData());
			byte[] decodedBody = Base64.getUrlDecoder().decode(bodyEncodedByBase64);

			// Slackに送る文言作成
			String text = "@channel alert\n";
			text += "```" + title + "```\n";
			if (!title.matches(".*RECOVERY.*"))
				text += "```" + new String(decodedBody) + "```";
			text = text.replaceAll("\r\n", "\n");
			System.out.println(text);

			// Slackに通知
			String urlEncodedText = URLEncoder.encode(text, "utf-8");
			String url = "https://slack.com/api/chat.postMessage?token=" + Constants.SLACK_API_KEY + "&channel="
					+ Constants.SLACK_ALERT_CHANNEL + "&text=" + urlEncodedText + Constants.FROM_BOT;
			System.out.println(HttpUtils.getContents(url));

			// labelの付け替え（未読→既読）
			List<String> labelsToAdd = new ArrayList<String>();
			List<String> labelsToRemove = new ArrayList<String>();
			labelsToRemove.add("UNREAD");
			MessagesApi.modifyMessage(service, "me", message.getId(), labelsToAdd, labelsToRemove);
		}

	}

	public static void quickStartSample(Gmail service) throws IOException {

		// Print the labels in the user's account.
		String user = "me";
		ListLabelsResponse listResponse = service.users().labels().list(user).execute();
		List<Label> labels = listResponse.getLabels();
		if (labels.size() == 0) {
			System.out.println("No labels found.");
		} else {
			System.out.println("Labels:");
			for (Label label : labels) {
				System.out.printf("- %s\n", label.getName());
			}
		}

	}

}