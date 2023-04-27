package nk.service.NStory.config;

import nk.service.NStory.handler.ChatHandler;
import nk.service.NStory.handler.TimeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Controller
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(timeHandler(), "/realTime").setAllowedOrigins("/");
        registry.addHandler(chatHandler(), "/mainChat").setAllowedOrigins("/chatroom");
    }

    @Bean
    public WebSocketHandler timeHandler() {
        return new TimeHandler();
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatHandler();
    }
}
