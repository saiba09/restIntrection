package com.rest.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedWriter;
import java.io.FileWriter;



public class restHit {
	private static final String FILENAME = "Patient.json";
	public static void main(String[] args) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		  try {

			URL url = new URL("https://fhirtest.uhn.ca/baseDstu2/Patient?_pretty=true");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml+fhir;q=1.0, application/json+fhir;q=1.0");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				bw.write(output);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		  }
		  finally{
			  try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
		  }
		}
}
