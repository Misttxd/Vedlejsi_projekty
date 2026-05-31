package lab;

import javafx.geometry.Point2D;

public class RotatingUfoFormation extends Formation<Ufo> {

    private double angle = 0;
    private double rotationSpeed = 70;
    public RotatingUfoFormation(World world, Point2D position, Ufo... entities) {
        super(world, position, entities);
    }

    @Override
    public void simulateElements(double delta) {
        angle = angle + rotationSpeed * delta;
        if(entitiesInFormation.isEmpty()){
            return;
        }
        Ufo middle = entitiesInFormation.getFirst();
        middle.setPositionOfMiddle(position);
        int rotCount = entitiesInFormation.size()-1;
        double radius = 100;
        for (int i = 1; i < entitiesInFormation.size(); i++) {
            Ufo ufo = entitiesInFormation.get(i);
            double currentAngle = Math.toRadians(angle + 360/rotCount*(i-1));
            Point2D ufoPosition = position.add(Math.cos(currentAngle)*radius, Math.sin(currentAngle)*radius);
            ufo.setPositionOfMiddle(ufoPosition);
        }
    }
}
