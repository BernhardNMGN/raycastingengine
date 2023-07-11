package raycasting;

public class RayCasterOld {

    //    public RayResult castRay2(double posX, double posY, double rayDirX, double rayDirY) {
//        //which box of the map we're in
//        int mapX = (int) posX;
//        int mapY = (int) posY;
//
//        //length of ray from one x or y-side to next x or y-side
//        double deltaDistX = (rayDirX == 0.) ? 1e30 : Math.abs(1. / rayDirX);
//        double deltaDistY = (rayDirY == 0.) ? 1e30 : Math.abs(1. / rayDirY);
//
//        //length of ray from current position to next x or y-side
//        double sideDistX = (rayDirX < 0 ? posX - mapX : mapX + 1. - posX) * deltaDistX;
//        double sideDistY = (rayDirY < 0 ? posY - mapY : mapY + 1. - posY) * deltaDistY;
//
//        //what direction to step in x or y-direction (either +1 or -1)
//        int stepX = (int) rayDirX / (int) Math.abs(rayDirX);
//        int stepY = (int) rayDirY / (int) Math.abs(rayDirY);
//
//        boolean hit = false; //was there a wall hit?
//        boolean hitSide = false; //was the wall hit at a side or at top/bottom?
//        Segment nextSeg = segments.get(mapY).get(mapX);
//
//        while(!hit) {
//            //jump to next map square, either in x-direction, or in y-direction
//            if (sideDistX < sideDistY)
//            {
//                sideDistX += deltaDistX;
//                mapX += stepX;
//                hitSide = false;
//            }
//            else
//            {
//                sideDistY += deltaDistY;
//                mapY += stepY;
//                hitSide = true;
//            }
//
//
//
//            //Check if ray has hit a wall
//            nextSeg = segments.get(mapY).get(mapX);
//            if(nextSeg instanceof Wall)
//                hit = true;
//        }
//        double distance = hitSide ? sideDistY - deltaDistY : sideDistX - deltaDistX;
//        return new RayResult(distance, nextSeg);
//    }

//    public RayResult castRay(Point2D player, double angle, double maximumDistance) {
//        double x = player.getX();
//        double y = player.getY();
//        int rayDirX = getDirX(angle);
//        int rayDirY = getDirY(angle);
//        Point2D horVector = findBoundary(x, angle, rayDirX, Axis.HORIZONTAL);
//        Point2D vertVector = findBoundary(y, angle, rayDirY, Axis.VERTICAL);
//        Point2D horPos = player.add(horVector);
//        Point2D vertPos = player.add(vertVector);
//
//        horVector = scaleVector(horVector, Axis.HORIZONTAL, rayDirX);
//        double horVectorLength = zeroPoint.distance(horVector);
//        vertVector = scaleVector(vertVector, Axis.VERTICAL, rayDirY);
//        double vertVectorLength = zeroPoint.distance(vertVector);
//
//        double horDistance = player.distance(horPos);
//        double vertDistance = player.distance(vertPos);
//        Point2D nextPoint = null;
//        double distance = 0.;
//        Segment nextSegment = getSegment(x, y, rayDirX, rayDirY);
//        Axis currentAxis = null;
//        while(!(nextSegment instanceof Wall) && distance <= maximumDistance) {
//            if (horDistance < vertDistance) {
//                nextPoint = horPos;
//                horPos = horPos.add(horVector);
//                distance = horDistance;
//                horDistance += horVectorLength;
//                currentAxis = Axis.HORIZONTAL;
//            } else {
//                nextPoint = vertPos;
//                vertPos = vertPos.add(vertVector);
//                distance = vertDistance;
//                vertDistance += vertVectorLength;
//                currentAxis = Axis.VERTICAL;
//            }
//            nextSegment = getSegment(nextPoint.getX(), nextPoint.getY(), rayDirX, rayDirY);
//        }
//        double relativeXPos = getRelativeXPos(rayDirX, rayDirY, currentAxis, nextPoint);
//        return new RayResult(distance, nextSegment, nextPoint, relativeXPos, angle);
//    }
//
//    private double getRelativeXPos(int rayDirX, int rayDirY, Axis axis, Point2D point) {
//        double result;
//        if(axis.equals(Axis.HORIZONTAL)) {
//            result = (point.getY() % segmentSize) * segmentSize;
//            if(rayDirX < 0)
//                result = segmentSize - result;
//        } else {
//            result = (point.getX() % segmentSize) * segmentSize;
//            if(rayDirY > 0)
//                result = segmentSize - result;
//        }
//        return result;
//    }
//
//    private Point2D scaleVector(Point2D vector, Axis searchDir, int rayDir) {
//        double ratio = (rayDir * segmentSize) / (searchDir.equals(Axis.HORIZONTAL) ? vector.getX() : vector.getY());
//        return vector.multiply(ratio);
//    }
//
//    private Point2D findBoundary(double coordinate, double angle, int rayDir, Axis searchDir) {
//        double distanceToBoundary = findDeltaFromBoundary(rayDir, coordinate);
//        if(searchDir.equals(Axis.HORIZONTAL)) {
//            double delta = distanceToBoundary * tan(toRadians(angle));
//            return new Point2D(distanceToBoundary, delta);
//        }
//        else {
//            double vertAngle = incrementAngle(360., -incrementAngle(90., -(360. - angle)));
//            double delta = distanceToBoundary * tan(toRadians(vertAngle));
//            return new Point2D(delta, distanceToBoundary);
//        }
//    }
//
//    private double findDeltaFromBoundary(int direction, double pos) {
//        double newPos = direction > 0 ? segmentSize - (pos % segmentSize) : -(pos % segmentSize);
//        return newPos == 0 ? segmentSize : newPos;
//    }
//
//    private void printData (double x, double y, double angle, double rayDeltaX, double rayDeltaY, double rayDeltaYforHorizontalSearch, double rayDeltaXforVerticalSearch) {
//        System.out.println("x: " + x + ", y: " + y + ", angle: " + angle);
//        System.out.println("rayDeltaX : " + rayDeltaX + ", rayDeltaY: " + rayDeltaY);
//        System.out.println("deltaYhorSearch : " + rayDeltaYforHorizontalSearch + ", deltaXvertSearch: " + rayDeltaXforVerticalSearch);
//        System.out.println("-------------------------------------------------------------------------------------");
//    }
//
//    private Segment getSegment(double x, double y, int rayDirX, int rayDirY) {
//        int xInd = (int) (x/segmentSize + offset*rayDirX);
//        int yInd = (int) (y/segmentSize + offset*rayDirY);
//        if(xInd < 0 || xInd >= segments.get(0).size() || yInd < 0 || yInd >= segments.size())
//            System.out.println("debug: index for segment out of bounds");
//        return segments.get(yInd).get(xInd);
//    }
//
//    private Segment getSegment(double x, double y) {
//        int xInd = (int) (x/segmentSize);
//        int yInd = (int) (y/segmentSize);
//        return segments.get(yInd).get(xInd);
//    }
//
//    private int getDirX(double angle) {
//        if(angle < 90. || angle > 270.)
//            return 1;
//        else if(angle > 90. && angle < 270.)
//            return -1;
//        else return 0;
//    }
//
//    private int getDirY(double angle) {
//        if(angle > 0. && angle < 180.)
//            return 1;
//        else if(angle > 180.)
//            return -1;
//        else return 0;
//    }
//
//    private void calculateAngleBounds(double playerAngle, int fov) {
//        double halfFov = ((double) fov)/2.;
//        this.leftAngleBound = incrementAngle(playerAngle, -halfFov);
//        this.rightAngleBound = incrementAngle(playerAngle, halfFov);
//    }
//
//    private Point2D rotateVector(Point2D vector, double degrees) {
//        return null;
//    }
}
