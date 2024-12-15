package src.diary;

public class ExerciseEntry extends DiaryEntry {
    private int duration;
    private String unit;

    public ExerciseEntry(String username, String type, String category, String itemName, double calories, int duration, String unit, String date) {
        super(username, type, category, itemName, calories, date);
        this.duration = duration;
        this.unit = unit;
    }

    public int getDuration() { return duration; }
    public String getUnit() { return unit; };

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = duration;
    }


    @Override
    public String toString() {
        return super.toString() + String.format(", Duration: %d %s", duration, unit);
    }
}
