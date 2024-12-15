package src.diary;

public class FoodEntry extends DiaryEntry {
    private int quantity;
    private String unit;

    public FoodEntry (String username, String type, String category, String itemName, double calories, int quantity, String unit, String date) {
        super(username, type, category, itemName, calories, date);
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Quantity: %d %s", quantity, unit);
    }
}
