package pl.siimplon.reporter.report.style;

import java.util.Map;

public class Style {

    public enum Color {RED, BLUE, YELLOW, DEFAULT, WHITE, GREEN, LIGHT_BLUE, GREY_40_PERCENT, LIGHT_TURQUOISE,
        TURQUOISE, BRIGHT_GREEN, VIOLET, LIGHT_ORANGE}

    private final Color defaultColor;

    private Map<String, Color> conditions;

    public Style(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Style(Color defaultColor, Map<String, Color> conditions) {
        this(defaultColor);
        this.conditions = conditions;
    }

    public Color getBGColor() {
        return defaultColor;
    }

    public Color getBGColor(String s) {
        if (conditions == null || !conditions.containsKey(s))
            return defaultColor;
        return conditions.get(s);
    }
}
