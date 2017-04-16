package social_graph;

import java.net.URI;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Preprocessor {
	
	private LinkedList<String> Characters;
	
	public Preprocessor(){
		this.Characters = new LinkedList<String>();
	}
	
	public LinkedList<String> getCharacters(){
		return this.Characters;
	}
	
	public void exec()
	{
		try{
			String eicontinue = "";
			String[] bad = {"Unidentified", "Legends", ":"};
			String base_uri = "http://starwars.wikia.com/api.php?format=json&action=query&list=embeddedin&eititle=Template%3ACharacter&eilimit=500";
			do{
				System.out.println(base_uri);
				URI request = new URI(base_uri);
				String res = Request.Get(request)
				        .connectTimeout(5000)
				        .socketTimeout(5000)
				        .execute().returnContent().asString();

				
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(res);
				JsonNode charactersNode = rootNode.path("query").path("embeddedin");
				JsonNode contNode = rootNode.path("query-continue").path("embeddedin");
				
				for(JsonNode character: charactersNode){
					String name = character.get("title").asText();
					boolean is_bad = false;
					for(String s : bad)
						if(name.contains(s)) {is_bad = true; continue;}
					
					if(!is_bad)
						Characters.add(name);
				}
				
				if(!contNode.isMissingNode())
				{
					eicontinue = contNode.get("eicontinue").asText();
					base_uri = base_uri + "&eicontinue=" + URLEncoder.encode(eicontinue,"UTF-8");
				}
				else
					eicontinue = "";
				
				System.out.println(Characters.size());
				
			} while(eicontinue != "");
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
