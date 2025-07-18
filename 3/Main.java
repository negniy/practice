import java.util.Random;

class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

abstract class Room {
    protected int roomNumber;
    protected int maxOccupants;
    protected int pricePerNight;
    protected boolean isBooked;

    public Room(int roomNumber, int maxOccupants, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxOccupants = maxOccupants;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public int getMaxOccupants() { return maxOccupants; }
    public int getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
}

class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, new Random().nextInt(3) + 1, pricePerNight);
    }
}

abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int maxOccupants, int pricePerNight) {
        super(roomNumber, maxOccupants, pricePerNight);
    }
}

class StandardRoom extends ProRoom {
    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, new Random().nextInt(4) + 2, pricePerNight);
    }
}

class LuxRoom extends ProRoom {
    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, new Random().nextInt(3) + 4, pricePerNight);
    }
}

class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
}

interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

class HotelRoomService implements RoomService<Room> {
    public void clean(Room room) {
        System.out.println("Cleaning room " + room.getRoomNumber());
    }

    public void reserve(Room room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Room " + room.getRoomNumber() + " is already booked!");
        }
        room.isBooked = true;
        System.out.println("Room " + room.getRoomNumber() + " reserved.");
    }

    public void free(Room room) {
        if (!room.isBooked()) {
            System.out.println("Room " + room.getRoomNumber() + " is not booked.");
            return;
        }
        room.isBooked = false;
        System.out.println("Room " + room.getRoomNumber() + " freed.");
    }
}

public class Main {
    public static void main(String[] args) {
        HotelRoomService service = new HotelRoomService();

        EconomyRoom econRoom = new EconomyRoom(101, 50);
        StandardRoom stdRoom = new StandardRoom(102, 100);
        LuxRoom luxRoom = new LuxRoom(103, 200);
        UltraLuxRoom ultraLuxRoom = new UltraLuxRoom(104, 500);

        service.clean(econRoom);
        service.clean(stdRoom);
        service.clean(luxRoom);
        service.clean(ultraLuxRoom);

        try {
            service.reserve(econRoom);
            service.reserve(stdRoom);
            service.reserve(luxRoom);
            service.reserve(ultraLuxRoom);

            service.reserve(econRoom);
        } catch (RoomAlreadyBookedException e) {
            System.out.println(e.getMessage());
        }

        service.free(stdRoom);
        service.free(luxRoom);

        System.out.println("Room 101 booked: " + econRoom.isBooked());
        System.out.println("Room 102 booked: " + stdRoom.isBooked());
    }
}