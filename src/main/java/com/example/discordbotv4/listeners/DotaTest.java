package com.example.discordbotv4.listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DotaTest implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if(event.getMessageContent().startsWith("dota")){
            RestTemplate restTemplate = new RestTemplate();
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth("0467e1af-b9b0-4fde-8c43-8672ebb603e6");
                headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
                HttpEntity<String> request = new HttpEntity<String>(headers);
                ResponseEntity<String> response = restTemplate.exchange("https://api.opendota.com/api/matches/271145478", HttpMethod.GET,
                        request, String.class);

                String body = response.getBody();
                System.out.println(response);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
