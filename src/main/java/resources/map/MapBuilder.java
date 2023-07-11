package resources.map;

import javafx.geometry.Point2D;
import resources.segments.DefaultFloor;
import resources.segments.DefaultWall;
import resources.segments.PlayerStartFloor;
import resources.segments.Segment;
import resources.segments.walls.GreyLargeBrickWall;
import settings.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapBuilder {

    public GameMap buildFromFile(String fileName) {
        List<List<Character>> mapArray = readFromFile(fileName);
        double segmentSize = Settings.SEGMENT_SIZE;
        Point2D startCoords = new Point2D(0, 0);
        List<List<Segment>> segmentList = buildSegmentListStatic(mapArray, startCoords, segmentSize);
        Point2D playerStartCoords = findPlayerStartCoords(segmentList);
        return new GameMap(mapArray, segmentSize, segmentList, playerStartCoords);
    }

    private Point2D findPlayerStartCoords(List<List<Segment>> segmentList) {
        Optional<PlayerStartFloor> playerStartSegment = segmentList.stream()
                .flatMap(List::stream)
                .filter(seg -> seg instanceof PlayerStartFloor)
                .map(seg -> (PlayerStartFloor) seg)
                .findFirst();
        return playerStartSegment.isPresent() ? playerStartSegment.get().getPlayerStartCoords() : computePlayerStartCoords();
    }

    //todo: implement
    private Point2D computePlayerStartCoords() {
        return null;
    }

    private List<List<Character>> readFromFile(String fileName) {
        File mapFile = new File(Settings.FILE_PATH_FOR_MAP + fileName + ".txt");
        List<List<Character>> mapArray = new ArrayList<>();
        try
            (BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
            String next;
            while((next = reader.readLine()) != null) {
                List<Character> line = convertToCharList(next);
                mapArray.add(line);
            }
        } catch (IOException e) {
            System.out.println("File: " + fileName + " not found in " + Settings.FILE_PATH_FOR_MAP);
            System.out.println("Filepath searched: " + mapFile.getAbsolutePath());
            e.printStackTrace();
        }
        return mapArray;
    }

    private List<Character> convertToCharList(String next) {
        return next.chars()
                .mapToObj(c -> (char)c)
                .toList();
    }

    private List<List<Segment>> buildSegmentListStatic(List<List<Character>> mapArray, Point2D startCoords, double segmentSize) {
        List<List<Segment>> map = new ArrayList<>();
        double x = startCoords.getX();
        double y = startCoords.getY();
        for(int i = 0; i < mapArray.size(); i++) {
            List<Segment> nextLine = new ArrayList<>();
            for (int j = 0; j < mapArray.get(0).size(); j++) {
                char segmentId = mapArray.get(i).get(j);
                Segment nextSeg = SegmentFactory.buildSegment(segmentId, x, y, segmentSize);
                nextLine.add(nextSeg);
                x += segmentSize;
            }
            map.add(nextLine);
            x = startCoords.getX();
            y += segmentSize;
        }
        return map;
    }


    private List<List<Segment>> buildSegmentListScrolling(List<List<Character>> mapArray) {
        return mapArray.stream()
                .map(
                        line -> line.stream()
                        .map(id -> SegmentFactory.buildSegment(id))
                        .toList())
                .toList();
    }

    private Point2D initStartCoords(List<List<Character>> mapArray, double segmentSize) {
        int nrOfSegmentsV = mapArray.size();
        int nrOfSegmentsH = mapArray.get(0).size();
        boolean offsetHorizontal = ((double) Settings.HORIZONTAL_RESOLUTION / Settings.VERTICAL_RESOLUTION) > ((double) nrOfSegmentsV / nrOfSegmentsH);
        double offSet = offsetHorizontal ? ((double)Settings.HORIZONTAL_RESOLUTION - (nrOfSegmentsH * segmentSize)) / 2. : ((double)Settings.VERTICAL_RESOLUTION - (nrOfSegmentsV * segmentSize)) / 2.;

        double startX = offsetHorizontal ? offSet : 0.;
        double startY = offsetHorizontal ? 0. : offSet;
        return new Point2D(startX, startY);
    }

    private double calculateSegmentSize(List<List<Character>> mapArray) {
        int nrOfSegmentsV = mapArray.size();
        int nrOfSegmentsH = mapArray.get(0).size();

        double ratioV = Settings.VERTICAL_RESOLUTION/nrOfSegmentsV;
        double ratioH = Settings.HORIZONTAL_RESOLUTION/nrOfSegmentsH;

        return Math.min(ratioV, ratioH);
    }

    private class SegmentFactory {

        public static Segment buildSegment(char id, double x, double y, double segmentSize) {
            return switch (id) {
                case '0',' ' -> new DefaultFloor(segmentSize, x, y);
                case '1','x' -> new DefaultWall(segmentSize, x, y);
                case 'g' -> new GreyLargeBrickWall(segmentSize, x, y);
                case 'p' -> new PlayerStartFloor(segmentSize, x, y);
                default -> new DefaultFloor(segmentSize, x, y);
            };
        }

        public static Segment buildSegment(char id) {
            double segmentSize = Settings.SEGMENT_SIZE;
            return switch (id) {
                case '0',' ' -> new DefaultFloor(segmentSize);
                case '1','x' -> new DefaultWall(segmentSize);
                case 'g' -> new GreyLargeBrickWall(segmentSize);
                case 'p' -> new PlayerStartFloor(segmentSize);
                default -> new DefaultFloor(segmentSize);
            };
        }
    }
}
