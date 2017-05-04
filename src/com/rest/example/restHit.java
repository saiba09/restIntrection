package com.rest.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.io.TextIO;
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions;
import com.google.cloud.dataflow.sdk.runners.BlockingDataflowPipelineRunner;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;
import com.google.cloud.dataflow.sdk.values.PDone;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import java.util.ArrayList;
public class restHit {
	private static final Logger LOG = LoggerFactory.getLogger(StarterPipeline.class);
	private static final String FILENAME = "Patient.json";
	static final DoFn<String, String> MUTATION_TRANSFORM = new DoFn<String, String>() { 
		private static final long serialVersionUID = 1L;
		@Override
    		public void processElement(DoFn<String, String>.ProcessContext c) throws Exception{
      			String line = c.element();
      			c.output(line);
 	    }
	};
	
	public static void main(String[] args) {
		DataflowPipelineOptions options = PipelineOptionsFactory.create().as(DataflowPipelineOptions.class);
		options.setRunner(BlockingDataflowPipelineRunner.class);
		options.setProject("healthcare-12");
		options.setStagingLocation("gs://mihin-data/staging1");
		Pipeline p = Pipeline.create(options);
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
		  
		  p.apply(TextIO.Read.from("Patient.json")).apply(ParDo.of(MUTATION_TRANSFORM))
			 //.apply(TextIO.Write.to("gs://mihin-data/temp-test.txt"));
			 .apply(TextIO.Write.to("gs:///mihin-data/temp123.txt"));
		  p.run();
		}
}
