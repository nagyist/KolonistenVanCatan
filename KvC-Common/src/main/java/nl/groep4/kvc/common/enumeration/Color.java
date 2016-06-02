package nl.groep4.kvc.common.enumeration;

/**
 * All colour codes
 * 
 * @version 1.0
 * @author Tim
 */
public enum Color {

    BLUE(0, 0, 255), BROWN(127, 0, 0), GREEN(0, 255, 0), ORANGE(255, 108, 0), RED(255, 0, 0), WHITE(255, 255, 255);

    private int red;
    private int blue;
    private int green;

    private Color(int red, int green, int blue) {
	this.red = red;
	this.blue = blue;
	this.green = green;
    }

    /**
     * Returns colour back to javafx defined colour
     * 
     * @return Retrieves colour
     * @version 1.0
     * @author Tim
     */
    public javafx.scene.paint.Color getColor() {
	return new javafx.scene.paint.Color(red / 255D, green / 255D, blue / 255D, 0);
    }

}
