package jp.co.tamushun.google;

import java.io.IOException;
import java.util.List;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

public class LabelsApi {

	/**
	 * Get specified Label.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @param labelId
	 *            ID of Label to get.
	 * @throws IOException
	 */
	public static void getLabel(Gmail service, String userId, String labelId) throws IOException {
		Label label = service.users().labels().get(userId, labelId).execute();

		System.out.println("Label " + label.getName() + " retrieved.");
		System.out.println(label.toPrettyString());
	}

	/**
	 * List the Labels in the user's mailbox.
	 *
	 * @param service
	 *            Authorized Gmail API instance.
	 * @param userId
	 *            User's email address. The special value "me" can be used to
	 *            indicate the authenticated user.
	 * @throws IOException
	 */
	public static void listLabels(Gmail service, String userId) throws IOException {
		ListLabelsResponse response = service.users().labels().list(userId).execute();
		List<Label> labels = response.getLabels();
		for (Label label : labels) {
			System.out.println(label.toPrettyString());
		}
	}

}
