package de.pnp.manager.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.pnp.manager.main.Database;
import de.pnp.manager.model.Currency;
import de.pnp.manager.model.ICurrency;
import de.pnp.manager.model.item.IItem;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.network.serializer.ServerModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import de.pnp.manager.testHelper.TestWithDatabaseAccess;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializerTest extends TestWithDatabaseAccess {

    private static ObjectMapper MAPPER;

    @BeforeAll
    @Test
    public static void createMapper() {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new ServerModule());

    }

    @Test
    public void testItemSerializer() throws JsonProcessingException {

        String json = MAPPER.writeValueAsString(Database.itemList);
        List<IItem> items = Arrays.asList(MAPPER.readValue(json, IItem[].class));

        assertEquals(Database.itemList.size(), items.size());
        assertTrue(items.containsAll(Database.itemList));
        assertTrue(Database.itemList.containsAll(items));
    }

    @Test
    public void testTalentSerializer() throws JsonProcessingException {

        String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(Database.talentList);
        List<ITalent> talents = Arrays.asList(MAPPER.readValue(json, ITalent[].class));

        assertEquals(Database.talentList.size(), talents.size());
        assertTrue(talents.containsAll(Database.talentList));
        assertTrue(Database.talentList.containsAll(talents));
    }

    @Test
    public void testCurrencySerializer() throws JsonProcessingException {

        String value = "5G 10S 31K";
        ICurrency currency = new Currency(value);

        String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(currency);

        assertEquals("\"" + value + "\"", json);

        assertEquals(currency, MAPPER.readValue(json, ICurrency.class));
    }
}
