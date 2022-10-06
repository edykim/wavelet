import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class SearchEngineHandler implements URLHandler {
	ArrayList<String> keywords = new ArrayList<>();

	public String handleRequest(URI url) {
		System.out.println("Path: " + url.getPath());

		if (url.getPath().equals("/")) {
			// print all keywords that registered
			return String.format("Total %d keywords:\n%s", keywords.size(), parseStringList(keywords));

		} else if (url.getPath().contains("/add")) {
			// add new keyword into keywords
			String[] parameters = url.getQuery().split("=");
			if (parameters[0].equals("s")) {
				keywords.add(parameters[1]);
				return String.format("\"%s\" is added in keywords! Total %d keywords.", parameters[1], keywords.size());
			}

		} else if (url.getPath().contains("/search")) {
			// search keyword from existing keywords
			String[] parameters = url.getQuery().split("=");
			if (parameters[0].equals("s")) {
				ArrayList<String> found = new ArrayList<>();
				for (String keyword : keywords) {
					if (keyword.contains(parameters[1])) {
						found.add(keyword);
					}
				}
				return String.format("Total %d keywords found:\n%s", found.size(), parseStringList(found));
			}
		}

		// Nothing matched return 404.
		return "404 Not Found!";
	}

	protected String parseStringList(ArrayList<String> items) {
		String result = "";
		for (String item : items) {
			result += String.format(" - %s\n", item);
		}
		return result;
	}
}

class SearchEngine {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Missing port number! Try any number between 1024 to 49151");
			return;
		}

		int port = Integer.parseInt(args[0]);

		Server.start(port, new SearchEngineHandler());
	}
}
