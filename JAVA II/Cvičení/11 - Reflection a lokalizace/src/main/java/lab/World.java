package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public class World {

    public static final MyPoint GRAVITY = new MyPoint(0, Setting.getInstance().getGravity());
    @Getter
    private final double width;

    @Getter
    private final double height;

    private List<DrawableSimulable> entities;
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();

    @Getter
    private final Cannon cannon;

    private Comparator<DrawableSimulable> comparator;

    @Getter
    private List<Ufo.DestroyInfo> destroyInfos = new LinkedList<>();

    private boolean spectatorMode;
    private final Object entitiesLock = new Object();

    public World(double width, double height) {
        this.width = width;
        this.height = height;
        entities = new ArrayList<>();
        entities.add(new UfoSpawner(this));
        cannon = new Cannon(this, new MyPoint(0, height - 20), -45);
        entities.add(cannon);
        entities.addAll(Stream.generate(() -> new Ufo(this)).limit(Setting.getInstance().getNumberOfUfos()).toList());
        entitiesToAdd.add(
            new RotatingUfoFormation(this, new MyPoint(100, 200), new Ufo(this), new Ufo(this), new Ufo(this),
                new Ufo(this), new Ufo(this), new Ufo(this)));
        comparator = new Comparator<DrawableSimulable>() {
            @Override
            public int compare(DrawableSimulable o1, DrawableSimulable o2) {
                if (o1 instanceof Bullet b1 && o2 instanceof Bullet b2) {
                    return 0;
                }
                if (o1 instanceof Bullet && !(o2 instanceof Bullet)) {
                    return 1;
                }
                if (!(o1 instanceof Bullet) && o2 instanceof Bullet) {
                    return -1;
                }
                if (o1 instanceof Ufo u1 && o2 instanceof Ufo u2) {
                    if (u1.getWidth() < u2.getWidth() && u1.getHeight() < u2.getHeight()) {
                        return -1;
                    }
                    if (u1.getWidth() > u2.getWidth() && u1.getHeight() > u2.getHeight()) {
                        return 1;
                    }
                    return Double.compare(u2.getWidth(), u1.getWidth());
                }
                return 0;
            }
        };
        comparator = comparator.reversed();
        entities.sort(comparator);
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        gc.save();
        synchronized (entitiesLock) {
            for (DrawableSimulable entity : entities) {
                entity.draw(gc);
            }
        }
        gc.restore();
    }

    public void simulate(double deltaTime) {
        if (spectatorMode) {
            return;
        }
        synchronized (entitiesLock) {
            for (DrawableSimulable entity : entities) {
                entity.simulate(deltaTime);
            }
            for (DrawableSimulable e1 : entities) {
                if (e1 instanceof Collisionable c1) {
                    for (DrawableSimulable e2 : entities) {
                        if (e2 instanceof Collisionable c2) {
                            if (c1 != c2 && c1.intersect(c2)) {
                                c1.hitBy(c2);
                            }
                        }
                    }
                }
            }
            for (DrawableSimulable e2 : entitiesToRemove) {
                if (!entities.remove(e2)) {
                    for (Formation<? extends DrawableSimulable> formation : entities.stream()
                        .filter(e -> e instanceof Formation<? extends DrawableSimulable>).map(Formation.class::cast)
                        .toList()) {
                        if (formation.remove(e2)) {
                            break;
                        }
                    }
                }
            }
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
            entitiesToRemove.clear();
            entities.sort(comparator);
        }
    }

    public void add(DrawableSimulable entity) {
        if (spectatorMode) {
            return;
        }
        entitiesToAdd.add(entity);
    }

    public void remove(DrawableSimulable entity) {
        if (spectatorMode) {
            return;
        }
        entitiesToRemove.add(entity);
    }

    public void setSpectatorMode(boolean spectatorMode) {
        this.spectatorMode = spectatorMode;
        if (!spectatorMode) {
            startServer();
        } else {
            connectToServer();
        }
    }

    public void startServer() {
        Thread serverThread = new Thread(this::runServer, "Server Accept Thread");
        serverThread.start();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(4600)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(() -> this.sendDataToClient(clientSocket),
                    "Server thread to comunicate with client");
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDataToClient(Socket clientSocket) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (entitiesLock) {
                    objectOutputStream.writeObject(entities);
                }
                objectOutputStream.reset();
                sleepForWhile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleepForWhile() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 4600);
            Thread clientThread = new Thread(() -> this.readDataFromserver(socket),
                "Client thread to comunicate with server");
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataFromserver(Socket socket) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                Object dataFromServer = objectInputStream.readObject();
                if (dataFromServer instanceof List<?> list) {
                    synchronized (entitiesLock) {
                        entities = (List<DrawableSimulable>) list;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
