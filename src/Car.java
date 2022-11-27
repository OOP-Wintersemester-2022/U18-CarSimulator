package Cars;

import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Rectangle;
import java.util.Random;

public class Car extends GraphicsObject {
    private float speed;
    private int canvasWidth;

    // Wegen der Polymorphie können alle Grafikprimitive in einem GraphicsObject-Array gespeichert werden
    // obwohl es sich um Kreise UND Rechtecke handelt.
    GraphicsObject[] carParts;

    private static final float MIN_SPEED = 2;
    private static final float MAX_SPEED = 10;

    private Random random;

    public Car(int carWidth, int carHeight, int canvasWidth, int canvasHeight) {
        // Der Super-Konstruktor wird aufgerufen, auch wenn die Werte nicht gebraucht werden.
        super(0, 0, carWidth, carHeight, Colors.WHITE);

        this.canvasWidth = canvasWidth;
        random = new Random();

        // Begrenzung des Farbwertbereichs nur auf valide RGB-Werte.
        int low = 0;
        int high = 255;

        int r = random.nextInt(high - low) + low;
        int g = random.nextInt(high - low) + low;
        int b = random.nextInt(high - low) + low;
        int a = random.nextInt(high - low) + low;

        Color carColor = new Color(r, g, b, a);
        this.setColor(carColor);

        int randomYPos = getRandomYPos(carHeight, canvasHeight);
        setYPos(randomYPos);
        speed = getRandomSpeed();

        carParts = new GraphicsObject[4];
        buildCar(carWidth, carHeight, carColor, randomYPos);
    }

    // Die buildCar-Methode baut das Auto aus der Karosserie, dem Dach und zwei Rädern zusammen.
    // Die Bauteile des Autos werden im GraphicsObject-Array carParts gespeichert.
    private void buildCar(int carWidth, int carHeight, Color carColor, int randomYPos) {
        float bodyHeight = getBodyHeight(carHeight);
        float roofHeight = getRoofHeight(bodyHeight, carHeight);
        float roofWidth = getRoofWidth(carWidth);
        float wheelRad = getWheelRadius(carHeight);

        // Die einzelnen Bauteile des Autos werden als graphische Primitive, also Rechtecke und Kreise, erzeugt.
        Rectangle carBody = new Rectangle(0, randomYPos, carWidth, bodyHeight, carColor);
        Rectangle roof = new Rectangle(roofWidth/2, randomYPos -roofHeight, roofWidth, roofHeight, carColor);
        Circle leftWheel = new Circle(0 + wheelRad, randomYPos + bodyHeight, wheelRad, carColor);
        Circle rightWheel = new Circle(carWidth - wheelRad, randomYPos + bodyHeight, wheelRad, carColor);

        carParts[0] = carBody;
        carParts[1] = roof;
        carParts[2] = leftWheel;
        carParts[3] = rightWheel;
    }

    // Die Karosserie nimmt 2/3 der gesamten Höhe des Autos ein.
    private float getBodyHeight(int carHeight) {
        return 2 * (carHeight / 3);
    }

    // Die Größe des Dachs ergibt sich aus der Größe des Autos abzüglich der Größe der Karosserie.
    private float getRoofHeight(float bodyHeight, int carHeight) {
        return carHeight - bodyHeight;
    }

    // Das Dach ist nur halb so lang wie das Auto.
    private float getRoofWidth(int carWidth) {
        return carWidth / 2;
    }

    // Der Radius der Reifen ist ein Viertel der Höhe des Autos. Der Durchmesser ist also die Hälfte der Höhe.
    private float getWheelRadius(int carHeight) {
        return carHeight / 4;
    }

    private float getRandomSpeed() {
        return random.nextFloat() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
    }

    private int getRandomYPos(int carHeight, int canvasHeight) {
        int laneNumTotal = canvasHeight / carHeight;
        int laneNum = random.nextInt(laneNumTotal-1)+1;
        return laneNum * carHeight;
    }

    // Die überschriebene draw-Methode ruft von allen GraphicsObjects im Array die draw-Methode auf.
    @Override
    public void draw() {
        for (int i = 0; i < carParts.length; i++) {
            carParts[i].draw();
        }
    }

    /*
        Das Auto wird um die in speed gespeicherte Zahl an Pixeln nach rechts bewegt.
        Fährt das Auto über den rechten Rand hinaus wird es wieder an den linken Rand zurück versetzt.
     */
    public void update() {
        move(speed, 0);
        if (getXPos() > canvasWidth) {
            setXPos(-getWidth());
        }
    }

    // Die überschriebene move-Methode ruft von allen GraphicsObjects im Array due move-Methode auf.
    @Override
    public void move(float x, float y) {
        for (int i = 0; i < carParts.length; i++) {
            carParts[i].move(x, y);
        }
    }

    /*
        Damit die GraphicsObjects im Array ihre relative Position zu einander behalten, kann nicht die setXPos-Methode
        jedes Primitivs aufgerufen werden.
        Stattdessen muss berechnet werden wie weit das Auto durch die setXPos bewegt wird und das über einen Aufruf der
        move-Methoden realisiert.
     */
    @Override
    public void setXPos(float xPos) {
        float dx = xPos - getXPos();
        for (int i = 0; i < carParts.length; i++) {
            carParts[i].move(dx, 0);
        }
    }

    // Die X-Position des Autos entspricht der X-Position der Karosserie, die das längste Element des Autos ist.
    @Override
    public float getXPos() {
        return carParts[0].getXPos();
    }

    // Die Breite des Autos entspricht der Breite der Karosserie, die das längste Element des Autos ist.
    @Override
    public float getWidth() {
        return carParts[0].getWidth();
    }
}
