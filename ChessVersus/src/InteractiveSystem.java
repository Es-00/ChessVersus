import java.util.Scanner;

public class InteractiveSystem {
    private GameSystem system;
    IOStrategy strategy;


    InteractiveSystem(GameSystem system){
        this.system = system;
    }

    private char chooseGame(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择游戏类型： 五子棋（f） 围棋（g）恢复中断(l) 结束游戏(e)");
        String op = scanner.nextLine();
        return op.charAt(0);
    }

    private boolean chooseStrategy(char c){
        if(c=='f'){
            strategy = new FirStrategy();
            return true;
        }
        if(c=='g'){
            strategy = new GoStrategy();
            return true;
        }
        return false;
    }

    public void call(){
        Scanner scanner = new Scanner(System.in);
        boolean endSign = false;
        while(true){
            System.out.println("新的一局");
            while(true) {
                char gameKind = this.chooseGame();
                if (gameKind=='e'){
                    endSign = true;
                    System.out.println("游戏结束");
                    break;
                }
                if (gameKind == 'l'){
                    char c = system.load();
                    if(chooseStrategy(c)) {
                        System.out.println("读取成功");
                        break;
                    }else {
                        System.out.println("读取失败");
                        continue;
                    }
                }
                System.out.println("输入棋盘尺寸");
                int size = Integer.parseInt(scanner.nextLine());
                if(system.newGame(size, gameKind)) {
                    chooseStrategy(gameKind);
                    break;
                }
            }
            if(endSign) break;
            int response = 0;
            while (response != 1){
                System.out.println(system.getPlayer());
                strategy.outputMessage();
                response = strategy.executeOrder(scanner.nextLine(), system);
            }
        }
    }
}

interface IOStrategy{
    void outputMessage();
    int executeOrder(String order, GameSystem system);
}

class FirStrategy implements  IOStrategy{
    @Override
    public void outputMessage(){
        System.out.println("请输入落子位置(输入u悔棋 输入e投降 输入i中断)");
    }

    @Override
    // 0：正常 1:结束
    public int executeOrder(String order, GameSystem system){
        if(order.charAt(0)=='e') {
            return system.surrender();
        }
        if(order.charAt(0)=='u') {
            return system.undo();
        }
        if(order.charAt(0)=='i'){
            return system.interrupt();
        }
        if(order.charAt(0)=='x'){
            return system.xuZhao();
        }
        String[] orders = order.split(" ");
        if(orders.length != 2){
            System.out.println("输入错误");
            return 0;
        }
        int x,y;
        x = Integer.parseInt(orders[0]);
        y = Integer.parseInt(orders[1]);
        return system.dropChessPiece(x, y);
    }
}

class GoStrategy extends FirStrategy{
    private int count = 0;

    @Override
    public void outputMessage(){
        System.out.println("请输入落子位置(输入u悔棋 输入e投降 输入i中断 输入x虚着)");
    }
}
