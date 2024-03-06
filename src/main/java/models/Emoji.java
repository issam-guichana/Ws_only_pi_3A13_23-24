package models;
import javafx.scene.paint.Color;

public class Emoji {
    private String emoji;
    private Color color;

    public Emoji(String emoji, Color color) {
        this.emoji = emoji;
        this.color = color;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
