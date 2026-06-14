package Game.Engine.Inventory;

import java.util.ArrayList;
import Game.Engine.Inventory.Items.*;
import java.util.List;
import java.util.Optional;

public class Inventory {
    private final List<ItemStack> stacks = new ArrayList<>();
    private final float maxWeight;
    private float currentWeight = 0;

    public Inventory(float maxWeight) {
        this.maxWeight = maxWeight;
    }

    /** Add items — returns how many were actually added (may be limited by weight). */
    public int add(Item item, int quantity) {
        float weightPerItem = item.getWeight();
        int canFit = (int) ((maxWeight - currentWeight) / weightPerItem);
        int toAdd  = Math.min(quantity, canFit);
        if (toAdd <= 0) return 0;

        Optional<ItemStack> existing = find(item);
        if (existing.isPresent()) {
            existing.get().add(toAdd);
        } else {
            stacks.add(new ItemStack(item, toAdd));
        }
        currentWeight += weightPerItem * toAdd;
        return toAdd;
    }

    /** Remove items — returns true if successful. */
    public boolean remove(Item item, int quantity) {
        Optional<ItemStack> existing = find(item);
        if (existing.isEmpty()) return false;
        ItemStack stack = existing.get();
        if (stack.getQuantity() < quantity) return false;
        stack.remove(quantity);
        currentWeight -= item.getWeight() * quantity;
        if (stack.isEmpty()) stacks.remove(stack);
        return true;
    }

    public boolean hasItem(Item item, int quantity) {
        return find(item).map(s -> s.getQuantity() >= quantity).orElse(false);
    }

    public boolean hasAny(Class<?> itemClass) {
        return stacks.stream().anyMatch(s -> itemClass.isInstance(s.getItem()) && !s.isEmpty());
    }

    public boolean hasType(ItemType type) {
        return stacks.stream()
                .anyMatch(s -> s.getItem().getType() == type && !s.isEmpty());
    }

    public List<ItemStack> getByType(ItemType type) {
        return stacks.stream()
                .filter(s -> s.getItem().getType() == type && !s.isEmpty())
                .toList();
    }
    public List<ItemStack> getStacks()   { return stacks; }
    public float getCurrentWeight()      { return currentWeight; }
    public float getMaxWeight()          { return maxWeight; }
    public boolean isFull()              { return currentWeight >= maxWeight; }
    public boolean isEmpty()             { return stacks.isEmpty(); }

    private Optional<ItemStack> find(Item item) {
        return stacks.stream()
                .filter(s -> s.getItem().getClass() == item.getClass())
                .findFirst();
    }

}