import java.io.*;
import java.util.*;

class Movie {
    private String movieName;
    private String movieGenre;
    private int availableSeats;

    public Movie(String movieName, String movieGenre, int availableSeats) {
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.availableSeats = availableSeats;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void displayDetails() {
        System.out.println();
        System.out.println("Movie Name: " + movieName);
        System.out.println("Genre: " + movieGenre);
        System.out.println("Available Seats: " + availableSeats);
    }

    public boolean bookSeats(int seats) {
        if (availableSeats >= seats) {
            availableSeats -= seats;
            return true;
        } else {
            return false;
        }
    }

    public void addSeats(int seats) {
        availableSeats += seats;
    }

    @Override
    public String toString() {
        return movieName + "," + movieGenre + "," + availableSeats;
    }

    public static Movie fromString(String line) {
        String[] parts = line.split(",");
        return new Movie(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}

class MovieManager {
    private List<Movie> movies = new ArrayList<>();
    private static final String FILE_NAME = "movies.txt";

    public MovieManager() {
        loadMovies();
    }

    public void addMovie(String movieName, String movieGenre, int availableSeats) {
        movies.add(new Movie(movieName, movieGenre, availableSeats));
        System.out.println("\nMovie added successfully!");
        saveMovies();
    }

    public void removeMovie(String movieName) {
        movies.removeIf(movie -> movie.getMovieName().equalsIgnoreCase(movieName));
        System.out.println("\nMovie removed successfully!");
        saveMovies();
    }

    public void displayMovies() {
        if (movies.isEmpty()) {
            System.out.println("\nNo movies available.");
        } else {
            for (Movie movie : movies) {
                movie.displayDetails();
                System.out.println("------------------\n");
            }
        }
    }

    public Movie findMovie(String movieName) {
        for (Movie movie : movies) {
            if (movie.getMovieName().equalsIgnoreCase(movieName)) {
                return movie;
            }
        }
        return null;
    }

    private void saveMovies() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Movie movie : movies) {
                writer.println(movie);
            }
        } catch (IOException e) {
            System.out.println("Error saving movies: " + e.getMessage());
        }
    }

    private void loadMovies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                movies.add(Movie.fromString(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("\nNo saved movies found.");
        } catch (IOException e) {
            System.out.println("\nError loading movies: " + e.getMessage());
        }
    }
}

public class CinemaTicketBooking {

    private static boolean adminLogin(Scanner scanner) {
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_PASSWORD = "password";

        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("\nAdmin login successful.");
            return true;
        } else {
            System.out.println("\nInvalid admin credentials.");
            return false;
        }
    }

    private static boolean userLogin(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!username.isEmpty() && !password.isEmpty()) {
            System.out.println("\nUser login successful.");
            return true;
        } else {
            System.out.println("\nInvalid user credentials.");
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MovieManager manager = new MovieManager();

        System.out.println("\nWelcome to the Cinema Ticket Booking System");

        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int mainChoice = Integer.parseInt(scanner.nextLine());

            if (mainChoice == 1) {
                if (adminLogin(scanner)) {
                    System.out.println("\n--- Admin Menu ---");
                    while (true) {
                        System.out.println("1. Add Movie");
                        System.out.println("2. Remove Movie");
                        System.out.println("3. View All Movies");
                        System.out.println("4. Add Seats to a Movie");
                        System.out.println("5. Book Seats for a Movie");
                        System.out.println("6. Exit");
                        System.out.print("Enter your choice: ");
                        int adminChoice = Integer.parseInt(scanner.nextLine());

                        if (adminChoice == 1) {
                            System.out.print("Enter movie name: ");
                            String movieName = scanner.nextLine();
                            System.out.print("Enter genre: ");
                            String movieGenre = scanner.nextLine();
                            System.out.print("Enter available seats: ");
                            int seats = Integer.parseInt(scanner.nextLine());
                            manager.addMovie(movieName, movieGenre, seats);
                        } else if (adminChoice == 2) {
                            System.out.print("Enter movie name to remove: ");
                            String movieName = scanner.nextLine();
                            manager.removeMovie(movieName);
                        } else if (adminChoice == 3) {
                            manager.displayMovies();
                        } else if (adminChoice == 4) {
                            System.out.print("Enter movie name to add seats: ");
                            String movieName = scanner.nextLine();
                            Movie movie = manager.findMovie(movieName);
                            if (movie != null) {
                                System.out.print("Enter number of seats to add: ");
                                int seats = Integer.parseInt(scanner.nextLine());
                                movie.addSeats(seats);
                                System.out.println("\nSeats added successfully!");
                                manager.displayMovies();
                            } else {
                                System.out.println("\nMovie not found.");
                            }
                        } else if (adminChoice == 5) {
                            System.out.print("Enter movie name to book seats: ");
                            String movieName = scanner.nextLine();
                            Movie movie = manager.findMovie(movieName);
                            if (movie != null) {
                                System.out.print("Enter number of seats to book: ");
                                int seatsToBook = Integer.parseInt(scanner.nextLine());
                                if (movie.bookSeats(seatsToBook)) {
                                    System.out.println("\nSuccessfully booked " + seatsToBook + " seat(s)!");
                                    manager.displayMovies();
                                } else {
                                    System.out.println("\nNo seats available.");
                                }
                            } else {
                                System.out.println("\nMovie not found.");
                            }
                        } else if (adminChoice == 6) {
                            break;
                        } else {
                            System.out.println("\nInvalid choice. Try again.");
                        }
                    }
                }
            } else if (mainChoice == 2) {
                if (userLogin(scanner)) {
                    System.out.println("\n--- User Menu ---");
                    while (true) {
                        System.out.println("1. View Available Movies");
                        System.out.println("2. Book a Seat");
                        System.out.println("3. Exit");
                        System.out.print("Enter your choice: ");
                        int userChoice = Integer.parseInt(scanner.nextLine());

                        if (userChoice == 1) {
                            manager.displayMovies();
                        } else if (userChoice == 2) {
                            System.out.print("Enter movie name to book: ");
                            String movieName = scanner.nextLine();
                            Movie movie = manager.findMovie(movieName);
                            if (movie != null) {
                                System.out.print("Enter number of seats to book: ");
                                int seatsToBook = Integer.parseInt(scanner.nextLine());
                                if (movie.bookSeats(seatsToBook)) {
                                    System.out.println("\nSuccessfully booked " + seatsToBook + " seat(s)!");
                                    manager.displayMovies();
                                } else {
                                    System.out.println("\nNo seats available.");
                                }
                            } else {
                                System.out.println("\nMovie not found.");
                            }
                        } else if (userChoice == 3) {
                            break;
                        } else {
                            System.out.println("\nInvalid choice. Try again.");
                        }
                    }
                }
            } else if (mainChoice == 3) {
                System.out.println("\nThank you for using the Cinema Ticket Booking System.");
                break;
            } else {
                System.out.println("\nInvalid choice. Try again.");
            }
        }
    }
}
