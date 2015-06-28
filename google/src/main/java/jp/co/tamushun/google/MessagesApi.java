package jp.co.tamushun.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

public class MessagesApi {

	/**
	 * Get Message with given ID.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param messageId
	 *            ID of Message to retrieve.
	 * @return Message Retrieved Message.
	 * @throws IOException
	 */
	public static Message getMessage(Gmail service, String userId, String messageId) throws IOException {
		Message message = service.users().messages().get(userId, messageId).execute();

		System.out.println("Message snippet: " + message.getSnippet());

		return message;
	}

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

	/**
	 * Modify the labels a message is associated with.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param messageId
	 *            ID of Message to Modify.
	 * @param labelsToAdd
	 *            List of label ids to add.
	 * @param labelsToRemove
	 *            List of label ids to remove.
	 * @throws IOException
	 */
	public static void modifyMessage(Gmail service, String userId, String messageId, List<String> labelsToAdd, List<String> labelsToRemove)
			throws IOException {
		ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(labelsToAdd).setRemoveLabelIds(labelsToRemove);
		Message message = service.users().messages().modify(userId, messageId, mods).execute();

		System.out.println("Message id: " + message.getId());
		System.out.println(message.toPrettyString());
	}

}
