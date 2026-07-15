package Game.Engine.Inventory;

import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Inventory.Items.ItemType;

import Game.Engine.Inventory.Delivery;

import java.util.*;

public class Inventory {
    private final List<ItemStack> stacks = new ArrayList<>();
    private float maxWeight;
    private float currentWeight = 0;

    // Tracks item types currently claimed for outbound transport
    private final Set<ItemType> claimedForTransport = new HashSet<>();

    // Work deliveries — separate from personal inventory
    private final List<Delivery> deliveries = new ArrayList<>();

    private final List<Inventory> partitions = new ArrayList<>();
    private boolean transportInProgress = false;

    public void addDelivery(Delivery delivery)       { deliveries.add(delivery); }
    public void removeDelivery(Delivery delivery)    { deliveries.remove(delivery); }
    public List<Delivery> getDeliveries()            { return deliveries; }
    public boolean hasDeliveries()                   { return !deliveries.isEmpty(); }

    /** Find a delivery going to a specific destination inventory. */
    public Delivery getDeliveryFor(Inventory destination) {
        return deliveries.stream()
                .filter(d -> d.getDestination() == destination)
                .findFirst()
                .orElse(null);
    }

    public Inventory(float maxWeight) {
        this.maxWeight = maxWeight;
    }
    public Inventory() {
    }

    public void setMaxWeight(int max){
        this.maxWeight = max;
    }


    // -------------------------------------------------------------------------
    // Core operations
    // -------------------------------------------------------------------------

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

    public boolean hasType(ItemType type) {
        return stacks.stream().anyMatch(s -> s.getItem().getType() == type && !s.isEmpty());
    }
    public boolean hasClass(Class<? extends Item> itemclass) {
        return stacks.stream().anyMatch(s -> s.getItem().getClass() == itemclass && !s.isEmpty());
    }

    public List<ItemStack> getByType(ItemType type) {
        return stacks.stream()
                .filter(s -> s.getItem().getType() == type && !s.isEmpty())
                .toList();
    }

    public boolean hasAny(Class<?> itemClass) {
        return stacks.stream().anyMatch(s -> itemClass.isInstance(s.getItem()) && !s.isEmpty());
    }
    public boolean hasItem(Class<? extends Item> itemClass, int quantity) {
        return stacks.stream()
                .filter(s -> s.getItem().getClass() == itemClass)
                .mapToInt(ItemStack::getQuantity)
                .sum() >= quantity;
    }

    public List<ItemStack> getByClass(Class<? extends Item> itemClass) {
        return stacks.stream()
                .filter(s -> s.getItem().getClass() == itemClass)
                .toList();
    }

    // -------------------------------------------------------------------------
    // Transport claim system
    // -------------------------------------------------------------------------

    /** Claim an item type for outbound transport — returns false if already claimed. */


    public boolean claimTransport() {
        if (transportInProgress) return false;
        transportInProgress = true;
        return true;
    }

    /** Release a transport claim — called once goods are successfully delivered. */
    public void releaseTransportClaim() { transportInProgress = false; }

    public boolean isClaimedForTransport(ItemType type) {
        return claimedForTransport.contains(type);
    }

    /**
     * Returns true if this inventory has items of this type that aren't
     * already claimed for transport by another colonist.
     */
    public boolean hasAvailableType(ItemType type) {
        return hasType(type) && !isClaimedForTransport(type);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public List<ItemStack> getStacks()  { return stacks; }
    public float getCurrentWeight()     { return currentWeight; }
    public float getMaxWeight()         { return maxWeight; }
    public boolean isFull()             { return currentWeight >= maxWeight; }
    public boolean isEmpty()            { return stacks.isEmpty(); }

    private Optional<ItemStack> find(Item item) {
        return stacks.stream()
                .filter(s -> s.getItem().getClass() == item.getClass())
                .findFirst();
    }

    public boolean addPartition(Inventory partition) {
        if(getCurrentWeight() + partition.getMaxWeight() <= getMaxWeight()){
            partitions.add(partition);
            return true;
        }
        return false;

    }
}