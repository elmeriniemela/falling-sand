package hiekkaranta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulaatio {

    private Tyyppi[][] area;
    private int leveys;
    private int korkeus;

    public Simulaatio(int leveys, int korkeus) {
        this.leveys = leveys;
        this.korkeus = korkeus;

        area = new Tyyppi[leveys][korkeus];
        for (int x = 0; x < leveys; x++) {
            for (int y = 0; y < korkeus; y++) {
                area[x][y] = Tyyppi.TYHJA;
            }
        }
    }

    public void lisaa(int x, int y, Tyyppi tyyppi) {
        if (x < 0 || y < 0 || x >= leveys || y >= korkeus) {
            return;
        }
 
        this.area[x][y] = tyyppi;
    }

    public Tyyppi sisalto(int x, int y) {
        if (!inBounds(x, y)) {
            return Tyyppi.METALLI;
        }
        return area[x][y];
    }

    public void paivita() {
        for (int x = 0; x < leveys; x++) {
            for (int y = korkeus - 1; y > 0; y--) {
                if (this.area[x][y] == Tyyppi.HIEKKA) {
                    siirraHiekkaa(x, y);
                    continue;
                }
 
                if (this.area[x][y] == Tyyppi.VESI) {
                    siirraVetta(x, y);
                    continue;
                }
            }
 
        }
    }
 
    private void siirraHiekkaa(int x, int y) {
        List<Point> availableBelow = availableBelow(x, y, Tyyppi.TYHJA, Tyyppi.VESI);
        if (availableBelow.isEmpty()) {
            return;
        }
 
        swapRandomly(availableBelow, x, y);
    }
 
    private void siirraVetta(int x, int y) {
        List<Point> vapaatAlla = availableBelow(x, y, Tyyppi.TYHJA);
        vapaatAlla.addAll(availableHorizontally(x, y, Tyyppi.TYHJA));
 
        if (!vapaatAlla.isEmpty()) {
            swapRandomly(vapaatAlla, x, y);
        }
    }

    public List<Point> availableHorizontally(int x, int y, Tyyppi... acceptedTypes) {
        List<Point> availablePoints = new ArrayList<>();

        for (Tyyppi type : acceptedTypes) {
            if (sisalto(x + 1, y) == type) {
                availablePoints.add(new Point(x + 1, y));
            }
            if (sisalto(x - 1, y) == type) {
                availablePoints.add(new Point(x - 1, y));
            }
        }
        return availablePoints;
    }

    public boolean inBounds(int x, int y) {
         return !(x < 0 || y < 0 || x >= leveys || y >= korkeus);
    }

    public List<Point> availableBelow(int x, int y, Tyyppi... acceptedTypes) {
        List<Point> availablePoints = new ArrayList<>();

        for (int deltaX=-1; deltaX<=1; deltaX++) {
            Tyyppi typeInPoint = sisalto(x + deltaX, y + 1);
            for (Tyyppi type : acceptedTypes) {
                if (type == typeInPoint) {
                    availablePoints.add(new Point(x + deltaX, y + 1));
                }
            }
        }
        return availablePoints;
    }

    private void swapRandomly(List<Point> availablePoints, int x, int y) {
        Collections.shuffle(availablePoints);
        Point dest = availablePoints.get(0);
        Tyyppi current = this.area[x][y];
        this.area[x][y] = this.area[dest.getX()][dest.getY()];
        this.area[dest.getX()][dest.getY()] = current;
    }

}
