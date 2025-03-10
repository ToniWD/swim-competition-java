package ro.mpp2024.Domain;

public class SwimmingEvent extends Entity<Long> {
    private int distance;
    private String style;

    public SwimmingEvent(int distance, String style) {
        this.distance = distance;
        this.style = style;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
