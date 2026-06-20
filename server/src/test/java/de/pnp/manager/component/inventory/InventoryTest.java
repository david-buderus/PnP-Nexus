package de.pnp.manager.component.inventory;

import de.pnp.manager.component.item.Item;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.utils.TestItemBuilder;
import de.pnp.manager.utils.TestUpgradeBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Inventory}
 */
class InventoryTest {

    @Test
    void hasSpaceFor() {
        Inventory inventory = new Inventory(1, List.of(createItemStack(8, "A")));
        assertThat(inventory.hasSpaceFor(createItemStack(1, "A"))).isTrue();
        assertThat(inventory.hasSpaceFor(createItemStack(2, "A"))).isTrue();
        assertThat(inventory.hasSpaceFor(createItemStack(3, "A"))).isFalse();
        assertThat(inventory.hasSpaceFor(createItemStack(4, "A"))).isFalse();
        assertThat(inventory.hasSpaceFor(createItemStack(4, "B"))).isFalse();
    }

    @Test
    void addItem() {
        Inventory inventory = new Inventory(3, new ArrayList<>());

        ItemStack<?> stackA = createItemStack(6, "A");
        assertThat(inventory.addItem(stackA)).isTrue();
        assertThat(inventory.getItems()).containsExactly(stackA);

        ItemStack<?> stackB = createItemStack(1, "B");
        assertThat(inventory.addItem(stackB)).isTrue();
        assertThat(inventory.getItems()).containsExactly(stackA, stackB);

        assertThat(inventory.addItem(stackB)).isTrue();
        assertThat(inventory.getItems()).containsExactly(stackA, createItemStack(2, "B"));

        assertThat(inventory.addItem(stackA)).isTrue();
        assertThat(inventory.getItems()).containsExactly(
                createItemStack(10, "A"),
                createItemStack(2, "B"),
                createItemStack(2, "A")
        );

        assertThat(inventory.addItem(createItemStack(2, "C"))).isFalse();
    }

    @Test
    void removeItem() {
        ItemStack<?> keep = createItemStack(2, "keep");
        Inventory inventory = new Inventory(10, List.of(createItemStack(4, "remove"), keep));

        inventory.removeItem(createItemStack(2, "remove"));
        assertThat(inventory.getItems()).containsExactly(createItemStack(2, "remove"), keep);

        inventory.removeItem(createItemStack(2, "remove"));
        assertThat(inventory.getItems()).containsExactly(keep);
    }

    @Test
    void handleUpgrades() {
        Inventory inventory = new Inventory(10, List.of());

        ItemStack<?> stackA = createItemStack(2, "A", List.of(createUpgrade("U1")));
        assertThat(inventory.addItem(stackA)).isTrue();
        assertThat(inventory.getItems()).containsExactly(stackA);

        assertThat(inventory.addItem(createItemStack(2, "A", Set.of(createUpgrade("U1"))))).isTrue();
        ItemStack<?> twoStackA = createItemStack(4, "A", List.of(createUpgrade("U1")));
        assertThat(inventory.getItems()).containsExactly(twoStackA);

        ItemStack<?> stackB = createItemStack(2, "A", List.of(createUpgrade("U2")));
        assertThat(inventory.addItem(stackB)).isTrue();
        assertThat(inventory.getItems()).containsExactly(twoStackA, stackB);

        inventory.removeItem(stackA);
        assertThat(inventory.getItems()).containsExactly(stackA, stackB);

        inventory.removeItem(stackB);
        assertThat(inventory.getItems()).containsExactly(stackA);
    }

    private ItemStack<?> createItemStack(int stackSize, String name) {
        return createItemStack(stackSize, name, List.of());
    }

    private ItemStack<?> createItemStack(int stackSize, String name, Collection<Upgrade> upgrades) {
        Item item = TestItemBuilder.createItemBuilder().withName(name).withMaximumStackSize(10)
                .withUpgradeSlots(upgrades.stream().mapToInt(Upgrade::getSlots).sum()).buildItem();
        ItemStack<? extends Item> stack = ItemStack.from(item, stackSize);
        stack.setUpgrades(upgrades);
        return stack;
    }

    private Upgrade createUpgrade(String name) {
        return TestUpgradeBuilder.createUpgrade().withName(name).build();
    }
}