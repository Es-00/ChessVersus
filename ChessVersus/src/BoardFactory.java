
public class BoardFactory {

    public Board generateBoard(int size, char kind){
        if (size < 8 || size > 19){
            System.out.println("错误的棋盘尺寸");
            return null;
        }
        if(kind == 'f') {
            System.out.println("五子棋 " + size + 'x' + size);
            return new FirBoard(size);
        }
        if(kind == 'g') {
            System.out.println("围棋 " + size + 'x' + size);
            return new GoBoard(size);
        }
        System.out.println("错误的棋盘类型");
        return null;
    }
}
