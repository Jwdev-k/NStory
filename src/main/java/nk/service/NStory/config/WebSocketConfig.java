package nk.service.NStory.config;

import nk.service.NStory.handler.ChatHandler;
import nk.service.NStory.handler.ScreenShareHandler;
import nk.service.NStory.handler.TimeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(timeHandler(), "/realTime").setAllowedOrigins("/");
        registry.addHandler(chatHandler(), "/mainChat/{roomId}").setAllowedOrigins("/chatroom");
        registry.addHandler(screenShareHandler(), "/livestream/{roomId}").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        //Text Message의 최대 버퍼 크기 설정
        container.setMaxTextMessageBufferSize(104857600);
        //Binary Message의 최대 버퍼 크기 설정
        container.setMaxBinaryMessageBufferSize(104857600);
        return container;
    }

    @Bean
    public WebSocketHandler timeHandler() {
        return new TimeHandler();
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatHandler();
    }

    @Bean
    public WebSocketHandler screenShareHandler() {
        return new ScreenShareHandler();
    }
}
