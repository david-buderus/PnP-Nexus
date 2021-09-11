package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;
import de.pnp.manager.network.message.login.LoginRequestMessage;
import de.pnp.manager.network.serializer.BaseModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerTest {

    private static ObjectMapper MAPPER;

    @BeforeAll
    @Test
    public static void createMapper() {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new BaseModule());
    }

    @Test
    public void baseMessageSerializer() throws JsonProcessingException {

        Date time = Calendar.getInstance().getTime();

        String json = MAPPER.writeValueAsString(new LoginRequestMessage("Test", time));
        BaseMessage message = MAPPER.readValue(json, BaseMessage.class);

        assertEquals(MessageType.loginRequest, message.getType());
        assertEquals(time, message.getTimestamp());
    }
}
