import java.util.*;

public class Ring {

    int max_processes;
    int coordinator;
    boolean[] processes;
    ArrayList<Integer> pid;

    public Ring(int max) {
        coordinator = max;
        max_processes = max;
        pid = new ArrayList<>();
        processes = new boolean[max];

        for (int i = 0; i < max; i++) {
            processes[i] = true;
            System.out.println("P" + (i + 1) + " created.");
        }
        System.out.println("P" + coordinator + " is the coordinator");
    }

    void displayProcesses() {
        for (int i = 0; i < max_processes; i++) {
            if (processes[i])
                System.out.println("P" + (i + 1) + " is up.");
            else
                System.out.println("P" + (i + 1) + " is down.");
        }
        System.out.println("Current Coordinator: P" + coordinator);
    }

    void upProcess(int process_id) {
        if (process_id < 1 || process_id > max_processes) {
            System.out.println("Invalid process ID.");
            return;
        }

        if (!processes[process_id - 1]) {
            processes[process_id - 1] = true;
            System.out.println("Process P" + process_id + " is now up.");

            if (process_id > coordinator) {
                coordinator = process_id;
                System.out.println("Process P" + coordinator + " becomes the new coordinator.");
            }
        } else {
            System.out.println("Process P" + process_id + " is already up.");
        }
    }

    void downProcess(int process_id) {
        if (process_id < 1 || process_id > max_processes) {
            System.out.println("Invalid process ID.");
            return;
        }

        if (!processes[process_id - 1]) {
            System.out.println("Process P" + process_id + " is already down.");
        } else {
            processes[process_id - 1] = false;
            System.out.println("Process P" + process_id + " is now down.");

            if (process_id == coordinator) {
                System.out.println("Coordinator has gone down. Run election!");
            }
        }
    }

    void displayArrayList(ArrayList<Integer> pid) {
        System.out.print("[");
        for (int i = 0; i < pid.size(); i++) {
            System.out.print(pid.get(i));
            if (i < pid.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    void initElection(int process_id) {
        if (process_id < 1 || process_id > max_processes) {
            System.out.println("Invalid process ID.");
            return;
        }

        if (!processes[process_id - 1]) {
            System.out.println("Process P" + process_id + " is down. Cannot initiate election.");
            return;
        }

        pid.clear();
        pid.add(process_id);

        int temp = process_id;

        System.out.print("Process P" + process_id + " is sending election message with list: ");
        displayArrayList(pid);

        do {
            temp = (temp % max_processes); // Circular ring
            if (processes[temp]) {
                pid.add(temp + 1);
                System.out.print("Process P" + (temp + 1) + " is sending election message with list: ");
                displayArrayList(pid);
            }
            temp++;
        } while ((temp - 1) % max_processes != (process_id - 1));

        coordinator = Collections.max(pid);
        System.out.println("Process P" + process_id + " has declared P" + coordinator + " as the new coordinator.");
    }

    public static void main(String[] args) {
        Ring ring = null;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nRing Election Algorithm");
            System.out.println("1. Create processes");
            System.out.println("2. Display processes");
            System.out.println("3. Up a process");
            System.out.println("4. Down a process");
            System.out.println("5. Run election algorithm");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the number of processes: ");
                    int max = sc.nextInt();
                    ring = new Ring(max);
                    break;
                case 2:
                    if (ring != null) ring.displayProcesses();
                    else System.out.println("No processes created yet.");
                    break;
                case 3:
                    if (ring != null) {
                        System.out.print("Enter the process number to up: ");
                        int process_id = sc.nextInt();
                        ring.upProcess(process_id);
                    } else {
                        System.out.println("No processes created yet.");
                    }
                    break;
                case 4:
                    if (ring != null) {
                        System.out.print("Enter the process number to down: ");
                        int process_id = sc.nextInt();
                        ring.downProcess(process_id);
                    } else {
                        System.out.println("No processes created yet.");
                    }
                    break;
                case 5:
                    if (ring != null) {
                        System.out.print("Enter the process number to start election: ");
                        int process_id = sc.nextInt();
                        ring.initElection(process_id);
                    } else {
                        System.out.println("No processes created yet.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
