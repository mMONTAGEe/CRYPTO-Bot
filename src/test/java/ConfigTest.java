
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.romvoid.config.ConfigBuilder;

import io.montage.bot.Config;

public class ConfigTest {

    @BeforeAll
    public static void init() throws Exception {
    	new ConfigBuilder(Config.class, new File("application.cfg")).build(false);
    }
    
    @Test
    public void getBotToken() {
    	String token =  Config.BOT_TOKEN;
    	assertEquals("NzM1Mjc4NzQxNTQyMDEwOTkw.Xxd7sA.2mAEtCvlYq8YkaynzsC-VfCPbr8", token);
    }
    
    @Test
    public void getBotPrefix() {
    	String prefix =  Config.BOT_COMMAND_PREFIX;
    	assertEquals("!", prefix);
    }
    
    @Test
    public void getWebhookUrl() {
    	String URL =  Config.WEBHOOK_URL;
    	assertEquals("https://discordapp.com/api/webhooks/738366348664635503/kijRpmuV9Gc3HatSqoUa6s4F0nSQdMsuAm3buRRXwBe1A6N0wOKZNCKe4JKkB7o9yqpG", URL);
    }
    
    @Test
    public void getHookPattern() {
    	String pattern =  Config.PATTERN;
    	assertEquals("[%d{HH:mm:ss}] [%level] %logger{0}: %msg%n", pattern);
    }
    
    @Test
    public void getWebhookEnabled() {
    	boolean isenabled =  Config.WEBOOK_CONSOLE;
    	assertTrue(isenabled, "Webhook for Console should be true");

    }

}
