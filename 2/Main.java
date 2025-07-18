import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

enum CarType {
    SEDAN, SUV, ELECTRIC
}

class Car implements Comparable<Car> {
    private String vin;
    private String model;
    private String manufacturer;
    private int year;
    private int mileage;
    private double price;
    private CarType type;

    public Car(String vin, String model, String manufacturer, int year, int mileage, double price, CarType type) {
        this.vin = vin;
        this.model = model;
        this.manufacturer = manufacturer;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        this.type = type;
    }

    // Getters
    public String getVin() { return vin; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public int getYear() { return year; }
    public int getMileage() { return mileage; }
    public double getPrice() { return price; }
    public CarType getType() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return vin.equals(car.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }

    @Override
    public int compareTo(Car other) {
        return Integer.compare(other.year, this.year); // Newest to oldest
    }

    @Override
    public String toString() {
        return String.format("Car[vin=%s, model=%s, manufacturer=%s, year=%d, mileage=%d, price=%.2f, type=%s]",
                vin, model, manufacturer, year, mileage, price, type);
    }
}

class CarDealership {
    private Set<Car> cars = new HashSet<>();

    public boolean addCar(Car car) {
        return cars.add(car); // HashSet handles duplicates by VIN
    }

    public List<Car> findByManufacturer(String manufacturer) {
        return cars.stream()
                .filter(car -> car.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public double getAveragePriceByType(CarType type) {
        return cars.stream()
                .filter(car -> car.getType() == type)
                .mapToDouble(Car::getPrice)
                .average()
                .orElse(0.0);
    }

    public List<Car> getCarsSortedByYear() {
        return cars.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<CarType, Long> getTypeStatistics() {
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getType, Collectors.counting()));
    }

    public Car getOldestCar() {
        return cars.stream()
                .min(Comparator.comparingInt(Car::getYear))
                .orElse(null);
    }

    public Car getNewestCar() {
        return cars.stream()
                .max(Comparator.comparingInt(Car::getYear))
                .orElse(null);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Массивы (Работа с парком машин)
        int[] carYears = new int[50];
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            carYears[i] = random.nextInt(26) + 2000; // 2000 to 2025
        }
        System.out.println("Cars after 2015:");
        for (int year : carYears) {
            if (year > 2015) System.out.print(year + " ");
        }
        System.out.println("\nAverage age of cars: " +
                Period.between(LocalDate.of(carYears[0], 1, 1),
                        LocalDate.now()).getYears() / 50.0);

        // 2. Коллекции (Управление моделями)
        List<String> models = new ArrayList<>(Arrays.asList(
                "Toyota Camry", "BMW X5", "Tesla Model 3", "Toyota Camry", "Tesla Model S", "BMW X5"));
        Set<String> uniqueModels = models.stream()
                .map(model -> model.contains("Tesla") ? "ELECTRO_CAR" : model)
                .distinct()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toSet());
        System.out.println("Unique sorted models: " + uniqueModels);

        // 3. equals/hashCode (Сравнение автомобилей)
        HashSet<Car> carSet = new HashSet<>();
        carSet.add(new Car("VIN1", "Model3", "Tesla", 2020, 10000, 50000, CarType.ELECTRIC));
        carSet.add(new Car("VIN2", "X5", "BMW", 2019, 20000, 40000, CarType.SUV));
        carSet.add(new Car("VIN1", "Model3", "Tesla", 2020, 10000, 50000, CarType.ELECTRIC)); // Duplicate VIN
        System.out.println("Car set size: " + carSet.size()); // Should be 2

        // 4. Stream API (Анализ автопарка)
        List<Car> carList = new ArrayList<>(carSet);
        carList.add(new Car("VIN3", "Camry", "Toyota", 2018, 30000, 30000, CarType.SEDAN));
        carList.add(new Car("VIN4", "ModelY", "Tesla", 2022, 5000, 60000, CarType.ELECTRIC));
        System.out.println("Top 3 expensive cars with mileage < 50,000:");
        carList.stream()
                .filter(car -> car.getMileage() < 50000)
                .sorted(Comparator.comparingDouble(Car::getPrice).reversed())
                .limit(3)
                .forEach(System.out::println);
        System.out.println("Average mileage: " +
                carList.stream().mapToInt(Car::getMileage).average().orElse(0.0));
        System.out.println("Cars grouped by manufacturer: " +
                carList.stream().collect(Collectors.groupingBy(Car::getManufacturer)));

        // 5. Практическое задание: Автоцентр
        CarDealership dealership = new CarDealership();
        dealership.addCar(new Car("VIN1", "Model3", "Tesla", 2020, 10000, 50000, CarType.ELECTRIC));
        dealership.addCar(new Car("VIN2", "X5", "BMW", 2019, 20000, 40000, CarType.SUV));
        dealership.addCar(new Car("VIN3", "Camry", "Toyota", 2018, 30000, 30000, CarType.SEDAN));
        dealership.addCar(new Car("VIN4", "ModelY", "Tesla", 2022, 5000, 60000, CarType.ELECTRIC));

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add car");
            System.out.println("2. Find cars by manufacturer");
            System.out.println("3. Get average price by type");
            System.out.println("4. Get cars sorted by year");
            System.out.println("5. Show type statistics");
            System.out.println("6. Show oldest and newest car");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 0) break;
            switch (choice) {
                case 1:
                    System.out.print("Enter VIN: ");
                    String vin = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter mileage: ");
                    int mileage = scanner.nextInt();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter type (SEDAN/SUV/ELECTRIC): ");
                    CarType type = CarType.valueOf(scanner.nextLine().toUpperCase());
                    Car car = new Car(vin, model, manufacturer, year, mileage, price, type);
                    if (dealership.addCar(car)) {
                        System.out.println("Car added successfully.");
                    } else {
                        System.out.println("Car with this VIN already exists.");
                    }
                    break;
                case 2:
                    System.out.print("Enter manufacturer: ");
                    String man = scanner.nextLine();
                    System.out.println("Cars: " + dealership.findByManufacturer(man));
                    break;
                case 3:
                    System.out.print("Enter type (SEDAN/SUV/ELECTRIC): ");
                    CarType t = CarType.valueOf(scanner.nextLine().toUpperCase());
                    System.out.println("Average price: " + dealership.getAveragePriceByType(t));
                    break;
                case 4:
                    System.out.println("Cars sorted by year: " + dealership.getCarsSortedByYear());
                    break;
                case 5:
                    System.out.println("Type statistics: " + dealership.getTypeStatistics());
                    break;
                case 6:
                    System.out.println("Oldest car: " + dealership.getOldestCar());
                    System.out.println("Newest car: " + dealership.getNewestCar());
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}