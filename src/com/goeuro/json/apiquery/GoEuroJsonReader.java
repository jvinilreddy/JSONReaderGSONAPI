package com.goeuro.json.apiquery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import com.goeuro.json.model.Position;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public final class GoEuroJsonReader {

	// To set the properties for this instance
	static {
		// Setting the Proxy Host and Port for the current instance
		// System.setProperty("https.proxyHost", "53.88.72.33");
		// System.setProperty("https.proxyPort", "3128");

		// Overriding the Default Hostname Verifier for the current instance to
		// avoid security exception
		javax.net.ssl.HttpsURLConnection
				.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
					@Override
					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("api.goeuro.de")) {
							return true;
						}
						return false;
					}
				});
	}

	/**
	 * @author Vinil J
	 * @param input
	 *            Input parameter passed to the application to query for
	 * @return Returns the JSON Response by processing the response received
	 *         from the server
	 * @throws GoEuroException
	 *             Custom Exception class for the GoEuroJsonReader
	 */
	private static String urlStreamReader(String input) throws GoEuroException {
		String encodedInput;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader bfr = null;
		String inputLine = null;
		String jsonResponse = null;
		try {
			// Encoding the URL, creating the API Query and calling it
			encodedInput = URLEncoder.encode(input, "UTF-8");
			URL url = new URL(
					"https://api.goeuro.de/api/v1/suggest/position/de/name/"
							+ encodedInput);
			URLConnection uconn = url.openConnection();

			// Creating Stream Readers to process the streamed data
			is = uconn.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			bfr = new BufferedReader(isr);

			// Reading the Buffered Reader to process the JSON response received
			while ((inputLine = bfr.readLine()) != null) {
				jsonResponse = new StringBuilder().append(inputLine).toString();
			}

			// Closing the outermost stream
			bfr.close();
		} catch (UnsupportedEncodingException e) {
			throw new GoEuroException(e.getMessage(), "ENCODING_EXCEPTION");
		} catch (MalformedURLException e) {
			throw new GoEuroException(e.getMessage(), "URL_EXCEPTION");
		} catch (IOException e) {
			throw new GoEuroException(e.getMessage(), "IO_EXCEPTION");
		}
		return jsonResponse;
	}

	/**
	 * @author Vinil J
	 * @param jsonResponse
	 *            JSON Reponse passed as input parameter
	 * @return Returns a JSON Array by processing the JSON Response
	 */
	private static JsonArray jsonResponseReader(String jsonResponse) {
		final JsonParser jsonParser = new JsonParser();
		final JsonElement jsonElement = jsonParser.parse(jsonResponse);
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final JsonArray jsonArray = jsonObject.getAsJsonArray("results");

		return jsonArray;
	}

	/**
	 * @author Vinil J
	 * @param jsonArray
	 *            JSON Array passed as input
	 * @param csvFileName
	 *            Name of the CSV file in which the data needs to be stored
	 * @return This method deserializes the Json read from the specified parse
	 *         tree into an object of the specified type.
	 * @throws GoEuroException
	 *             Custom Exception class for the GoEuroJsonReader
	 */
	private static int deserialzeJsonArray(JsonArray jsonArray,
			String csvFileName) throws GoEuroException {
		int result = -1;
		Gson gson = new Gson();
		try {
			if (jsonArray.size() > 0) {
				for (final JsonElement obj : jsonArray) {
					Position customModel = gson.fromJson(obj, Position.class);
					GoEuroCsvWriter goEuroCsvWriter = new GoEuroCsvWriter();
					result = goEuroCsvWriter.createCsvFile(customModel,
							csvFileName);
					// System.out.println(customModel);
				}
			} else {
				return result;
			}
		} catch (JsonIOException e) {
			throw new GoEuroException(e.getMessage(), "JSON_IO_EXCEPTION");
		} catch (JsonSyntaxException e) {
			throw new GoEuroException(e.getMessage(), "JSON_SYNTAX_EXCEPTION");
		} catch (JsonParseException e) {
			throw new GoEuroException(e.getMessage(), "JSON_PARSE_EXCEPTION");
		}
		return result;
	}

	/**
	 * @author Vinil J
	 * @param e
	 *            Parameter passed as an object of type Exception
	 * @throws GoEuroException
	 *             Custom Exception class for the GoEuroJsonReader
	 */
	private static void processErrorCodes(GoEuroException e)
			throws GoEuroException {
		switch (e.getErrorMessage()) {
		case "ENCODING_EXCEPTION":
			System.err
					.println("Unsupported Encoding exception occured. Please check");
			System.exit(1);
		case "URL_EXCEPTION":
			System.err
					.println("There is some problem with URL. Please check it");
			System.exit(1);
		case "IO_EXCEPTION":
			System.err
					.println("An IO Error occured in streaming the URL or data.\n"
							+ "Input should not contain numbers or special characters or spaces. Please check");
			System.exit(1);
		case "JSON_IO_EXCEPTION":
			System.err.println("Error: Unable to process the JSON object");
			System.exit(1);
		case "JSON_SYNTAX_EXCEPTION":
			System.err
					.println("Error: Provided JSON object syntax is incorrect");
			System.exit(1);
		case "JSON_PARSE_EXCEPTION":
			System.err.println("Error: Unable to parse the JSON object ");
			System.exit(1);
		default:
			System.err
					.println("Unknown exception occured. Sorry for the incovenience"
							+ e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * @author Vinil J
	 * @param args
	 *            To pass the input through Command Line
	 * @throws GoEuroException
	 *             Custom Exception class for the GoEuroJsonReader
	 */
	public static void main(String[] args) throws GoEuroException {
		String input = null;
		String csvFileName = "goeuro_" + System.currentTimeMillis() + ".csv";
		int result = -1;
		try {
			if (args.length != 0) {
				input = args[0];
				String jsonResponse = urlStreamReader(input);
				JsonArray jsonArray = jsonResponseReader(jsonResponse);
				result = deserialzeJsonArray(jsonArray, csvFileName);
			} else {
				System.err
						.println("Please provide a valid input with atleast 2 characters ");
				result = 1;
			}
			if (result == 0) {
				System.out.println("The program executed successfully");
			} else if (result == 1) {
				System.err
						.println("Oops...The program failed because of Input or Directory permissions");
			} else {
				System.err
						.println("No data retrieved or saved. "
								+ "Please provide a valid input with atleast 2 characters.\n"
								+ "Input should not contain numbers or special characters or spaces. Please check ");
			}

		} catch (GoEuroException e) {
			processErrorCodes(e);
		}
	}

}