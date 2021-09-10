package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.network.message.BaseMessage;
import de.pnp.manager.network.message.MessageType;
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

    }

    @Test
    public void baseMessageSerializer() throws JsonProcessingException {

        MessageType type = MessageType.createSession;
        Date time = Calendar.getInstance().getTime();

        String json = MAPPER.writeValueAsString(new BaseMessage(type, time));
        BaseMessage message = MAPPER.readValue(json, BaseMessage.class);

        assertEquals(type, message.type);
        assertEquals(time, message.timestamp);
    }
}
