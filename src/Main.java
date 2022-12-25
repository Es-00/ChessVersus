public class Main {
    public static void main(String[] args) {
        GameSystem gameSystem = new GameSystem();
        InteractiveSystem menu = new InteractiveSystem(gameSystem);
        menu.call();
    }
}