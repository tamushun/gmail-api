package jp.co.tamushun.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

public class MessagesApi {

	/**
	 * List all Messages of the user's mailbox matching the query.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param query
	 *            String used to filter the Messages listed.
	 * @throws IOException
	 */
	public static List<Message> listMessagesMatchingQuery(Gmail service, String userId, String query) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

		return messages;
	}

	/**
	 * List all Messages of the user's mailbox with labelIds applied.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param labelIds
	 *            Only return Messages with these labelIds applied.
	 * @throws IOException
	 */
	public static List<Message> listMessagesWithLabels(Gmail service, String userId, List<String> labelIds) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setLabelIds(labelIds).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setLabelIds(labelIds).setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

		return messages;
	}

}
