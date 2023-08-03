package de.pnp.manager.component.inventory;

import static de.pnp.manager.utils.TestItemBuilder.createItemBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import de.pnp.manager.component.item.Item;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ItemStack}.
 */
class ItemStackTest {

    private static final Item TEST_ITEM = createItemBuilder().withMinimumStackSize(0).withMaximumStackSize(20)
        .buildItem();

    @Test
    void testAddAmount() {
        ItemStack<Item> itemStackA = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackA.addAmount(10)).isEqualTo(10);
        assertThat(itemStackA.getStackSize()).isEqualTo(20);

        ItemStack<Item> itemStackB = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackB.addAmount(20)).isEqualTo(10);
        assertThat(itemStackB.getStackSize()).isEqualTo(20);

        ItemStack<Item> itemStackC = new ItemStack<>(20, TEST_ITEM);
        assertThat(itemStackC.addAmount(5)).isEqualTo(0);
        assertThat(itemStackC.getStackSize()).isEqualTo(20);
    }

    @Test
    void testSubtractAmount() {
        ItemStack<Item> itemStackA = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackA.subtractAmount(10)).isEqualTo(-10);
        assertThat(itemStackA.getStackSize()).isEqualTo(0);

        ItemStack<Item> itemStackB = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackB.subtractAmount(20)).isEqualTo(-10);
        assertThat(itemStackB.getStackSize()).isEqualTo(0);
    }

    @Test
    void testSetAmount() {
        ItemStack<Item> itemStackA = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackA.setAmount(20)).isEqualTo(10);
        assertThat(itemStackA.getStackSize()).isEqualTo(20);

        ItemStack<Item> itemStackB = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackB.setAmount(30)).isEqualTo(10);
        assertThat(itemStackB.getStackSize()).isEqualTo(20);

        ItemStack<Item> itemStackC = new ItemStack<>(20, TEST_ITEM);
        assertThat(itemStackC.setAmount(25)).isEqualTo(0);
        assertThat(itemStackC.getStackSize()).isEqualTo(20);

        ItemStack<Item> itemStackD = new ItemStack<>(20, TEST_ITEM);
        assertThat(itemStackD.setAmount(5)).isEqualTo(-15);
        assertThat(itemStackD.getStackSize()).isEqualTo(5);

        ItemStack<Item> itemStackE = new ItemStack<>(10, TEST_ITEM);
        assertThat(itemStackE.setAmount(-5)).isEqualTo(-10);
        assertThat(itemStackE.getStackSize()).isEqualTo(0);
    }

    @Test
    void testGetAmount() {
        assertThat(new ItemStack<>(10, TEST_ITEM).getStackSize()).isEqualTo(10);
    }

    @Test
    void testGetItem() {
        assertThat(new ItemStack<>(10, TEST_ITEM).getItem()).isEqualTo(TEST_ITEM);
    }
}