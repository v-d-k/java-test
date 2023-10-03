public class BombOnePolice {
    public static void main(String[] args) {
        int bombCount = 0;
        int injured = 0;
        int healthy = 20;
        int building = 20;

        while (building != 0) {
            healthy--;
            building--;
            bombCount++;

            if (injured == 1) {
                injured--;
                injured++;
            } else {
                injured++;
            }
        }

        if (injured != 0) {
            injured--;
            bombCount++;
        }

        System.out.println("Bomb count: " + bombCount);
        System.out.println("Injured: " + injured);
        System.out.println("Healthy: " + healthy);
        System.out.println("Building count " + building);

    }
}