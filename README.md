# java-test

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Bird {
    private int birdId;
    private int wheatCount;
    private int stoneCount;

    public Bird(int birdId) {
        this.birdId = birdId;
        this.wheatCount = 0;
        this.stoneCount = 0;
    }

    public synchronized void eatFood(String food) {
        if (food.equalsIgnoreCase("Wheat")) {
            if (wheatCount == 2) {
                wheatCount++;
                System.out.println("Bird " + birdId + " ate wheat (Full stomach)");
            } else {
                wheatCount = 2;
                System.out.println("Bird " + birdId + " ate wheat (Half stomach)");
            }
        } else if (food.equalsIgnoreCase("Stone")) {
            System.out.println("Bird " + birdId + " ate stone (Empty stomach)");
        }
    }

    public synchronized int getWheatCount() {
        return wheatCount;
    }
}

class Plate {
    private volatile int wheatCount;
    private volatile int stoneCount;

    public Plate(int wheatCount, int stoneCount) {
        this.wheatCount = wheatCount;
        this.stoneCount = stoneCount;
    }

    public synchronized String takeFood() {
        if (wheatCount > 0 && stoneCount > 0) {
            Random random = new Random();
            int choice = random.nextInt(2);
            if (choice == 0 && wheatCount > 0) {
                wheatCount--;
                return "Wheat";
            } else {
                stoneCount--;
                return "Stone";
            }
        } else if (wheatCount > 0) {
            wheatCount--;
            return "Wheat";
        } else if (stoneCount > 0) {
            stoneCount--;
            return "Stone";
        }
        return "Empty"; // Handle the case when both are empty
    }

    public synchronized int getWheatCount() {
        return wheatCount;
    }

    public synchronized int getStoneCount() {
        return stoneCount;
    }
}

public class BirdFeedingSimulation {
    public static void main(String[] args) {
        int numBirds = 50;
        int numWheat = 50;
        int numStone = 50;
        int numSimultaneousBirds = 6;
        int numSimulations = numBirds / numSimultaneousBirds;

        Plate plate = new Plate(numWheat, numStone);

        int totalFullStomachCount = 0;
        int totalHalfStomachCount = 0;
        int totalEmptyStomachCount = 0;

        for (int simulation = 1; simulation <= numSimulations; simulation++) {
            List<Thread> birdThreads = new ArrayList<>();

            int[] fullStomachCount = new int[1]; // Using an array to make it effectively final
            int[] halfStomachCount = new int[1]; // Using an array to make it effectively final
            int[] emptyStomachCount = new int[1]; // Using an array to make it effectively final

            for (int i = (simulation - 1) * numSimultaneousBirds + 1; i <= simulation * numSimultaneousBirds; i++) {
                Bird bird = new Bird(i);
                Thread birdThread = new Thread(() -> {
                    for (int j = 0; j < 2; j++) {
                        String food = plate.takeFood();
                        bird.eatFood(food);
                    }
                    synchronized (plate) {
                        if (bird.getWheatCount() == 3) {
                            fullStomachCount[0]++;
                        } else if (bird.getWheatCount() == 2) {
                            halfStomachCount[0]++;
                        } else {
                            emptyStomachCount[0]++;
                        }
                    }
                });
                birdThreads.add(birdThread);
                birdThread.start();
            }

            for (Thread thread : birdThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            totalFullStomachCount += fullStomachCount[0];
            totalHalfStomachCount += halfStomachCount[0];
            totalEmptyStomachCount += emptyStomachCount[0];

            System.out.println("Simulation " + simulation + " Results:");
            System.out.println("Total Full Stomach Birds: " + totalFullStomachCount);
            System.out.println("Total Half Stomach Birds: " + totalHalfStomachCount);
            System.out.println("Total Empty Stomach Birds: " + totalEmptyStomachCount);
            System.out.println("Remaining Wheat: " + plate.getWheatCount());
            System.out.println("Remaining Stone: " + plate.getStoneCount());
            System.out.println("---------------------------------------------");
        }

        System.out.println("Final Results:");
        System.out.println("Total Full Stomach Birds: " + totalFullStomachCount);
        System.out.println("Total Half Stomach Birds: " + totalHalfStomachCount);
        System.out.println("Total Empty Stomach Birds: " + totalEmptyStomachCount);
    }
}
```
