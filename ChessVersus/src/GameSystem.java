public class GameSystem {
    private BoardFactory factory;
    private Board board;
    private int playerIndex;
    private Memento[] mementos;
    private int[] countUndo;
    private char kind;
    GameSystem(){
        factory = new BoardFactory();
        mementos = new Memento[2];
        mementos[0] = new Memento();
        mementos[1] = new Memento();
        countUndo = new int[2];
    }

    public boolean newGame(int size, char kind){
        playerIndex = 1;
        countUndo = new int[2];
        mementos[0] = new Memento();
        mementos[1] = new Memento();
        this.kind = kind;
        this.board = factory.generateBoard(size, kind);
        return this.board != null;
    }

    public int surrender(){
        System.out.println("Player" + playerIndex + " surrender");
        clearBoard();
        showBoard();
        return 1;
    }

    public void clearBoard(){
        mementos[0].clear();
        mementos[1].clear();
        board = null;
    }

    public void showBoard(){
        board.show();
    }

    public int dropChessPiece(int x, int y){
        mementos[playerIndex-1].save(board.getBoard(), this.playerIndex, board.getCountXuZhao(), this.countUndo, this.kind);
        int response = board.dropChessPiece(x, y, playerIndex);
        showBoard();
        if(response == 1){
            System.out.println("Player" + playerIndex + " win!");
            return 1;
        }else if(response == 0){
            playerIndex = 3 - playerIndex;
        }
        return 0;
    }

    public String getPlayer(){
        return "Player" + playerIndex;
    }

    public int undo(){
        if(countUndo[playerIndex-1]>0){
            System.out.print("无悔棋次数");
            return 0;
        }
        if(!mementos[playerIndex-1].isSaved()){
            System.out.print("无法悔棋,刚开局或者刚读取备份");
            return 0;
        }
        System.out.println(playerIndex-1);
        Memento memento = mementos[playerIndex-1];
        countUndo[playerIndex-1] = 1;
        board.setState(memento.getBoard(), memento.getCountXuZhao());
        board.show();
        return 0;
    }

    public char load(){
        Memento memento = new Memento();
        if(!memento.loadData())
            return ' ';
        this.playerIndex = memento.getPlayerIndex();
        this.kind = memento.getKind();
        int[][] newBoard = memento.getBoard();
        this.board = factory.generateBoard(newBoard.length, kind);
        this.board.setState(newBoard, memento.getCountXuZhao());
        this.countUndo = memento.getCountUndo();
        board.show();
        return kind;
    }

    public int xuZhao(){
        return board.xuZhao();
    }

    public int interrupt(){
        mementos[playerIndex-1].save(board.getBoard(), this.playerIndex, board.getCountXuZhao(), this.countUndo, this.kind);
        return 1;
    }
}