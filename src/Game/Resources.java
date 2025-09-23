package Game;

public class Resources {
    private int food;
    private int wood;
    private int stone;

    public Resources(int food, int wood, int stone) {
        this.food = food;
        this.wood = wood;
        this.stone = stone;
    }

    public int getFood() { return food; }
    public int getWood() { return wood; }
    public int getStone() { return stone; }

    public void setFood(int amount) { this.food = amount; }

    public void addFood(int amount) { food += amount; }
    public void addWood(int amount) { wood += amount; }
    public void addStone(int amount) { stone += amount; }

    @Override
    public String toString() {
        return "Food: " + food + " | Wood: " + wood + " | Stone: " + stone;
    }
}