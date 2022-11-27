package Cars;

import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Compound;
import de.ur.mi.oop.graphics.Rectangle;

import java.util.Random;

public class Car {
    private float speed;
    private int canvasWidth;

    // In einem Compound Objekt lassen sich in der GraphicsApp mehrere Objekte bündeln und als eines verwalten/zeichnen.
    private Compound car;

    private static final float MIN_SPEED = 2;
    private static final float MAX_SPEED = 10;

    private Random random;

    public Car(int carWidth, int carHeight, int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        random = new Random();

        // Begrenzung des Farbwertbereichs nur auf valide RGB-Werte.
        int low = 0;
        int high = 256;

        int r = random.nextInt(high - low) + low;
        int g = random.nextInt(high - low) + low;
        int b = random.nextInt(high - low) + low;
        int a = random.nextInt(high - low) + low;

        Color carColor = new Color(r, g, b, a);

        int randomYPos = getRandomYPos(carHeight, canvasHeight);
        speed = getRandomSpeed();

        car = new Compound(0, randomYPos);
        buildCar(carWidth, carHeight, carColor, randomYPos);
    }

    // Die buildCar-Methode baut das Auto aus der Karosserie, dem Dach und zwei Rädern zusammen.
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

        // Die Primitive werden zum Auto zusammengefügt, indem sie dem Compound hinzugefügt werden.
        car.add(carBody);
        car.add(roof);
        car.add(leftWheel);
        car.add(rightWheel);
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
        int laneNum = random.nextInt(laneNumTotal);
        return laneNum * carHeight;
    }

    // Da alle Primitive im Compund-Objekt gebündelt wurden, muss nur von diesem die draw-Methode aufgerufen werden.
    public void draw() {
        car.draw();
    }

    /*
        Das Auto wird um die in speed gespeicherte Zahl an Pixeln nach rechts bewegt.
        Fährt das Auto über den rechten Rand hinaus wird es wieder an den linken Rand zurück versetzt.
     */
    public void update() {
        car.move(speed, 0);
        if (car.getXPos() > canvasWidth) {
            car.setXPos(-car.getWidth());
        }
    }
}
