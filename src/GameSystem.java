public class GameSystem {
    private BoardFactory factory;
    private Board board;
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
        countUndo = new int[2];
        mementos[0] = new Memento();
        mementos[1] = new Memento();
        this.kind = kind;
        this.board = factory.generateBoard(size, kind);
        return this.board != null;
    }

    public int surrender(){
        System.out.println("Player" + board.getPlayerIndex() + " surrender");
        clearBoard();
        showBoard();
        return 1;
    }

    private void clearBoard(){
        mementos[0].clear();
        mementos[1].clear();
        board = null;
    }

    public void showBoard(){
        board.show();
    }

    public int dropChessPiece(int x, int y){
        int playerIndex = board.getPlayerIndex();
        mementos[playerIndex-1].save(board.getBoard(), playerIndex, board.getCountXuZhao(), this.countUndo, this.kind);
        board.dropChessPiece(x, y);
        showBoard();
        int winner = board.checkWinner();
        if(winner != 0){
            if(winner == -1){
                System.out.println("Draw!");
            }else{
                System.out.println("Player" + winner + "win ");
            }
            return 1;
        }
        return 0;
    }

    public String getPlayer(){
        return "Player" + board.getPlayerIndex();
    }

    public int getPlayerIndex(){
        return board.getPlayerIndex();
    }

    public int undo(){
        int playerIndex = this.board.getPlayerIndex();
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
        board.setState(memento.getBoard(), memento.getPlayerIndex(), memento.getCountXuZhao());
        board.show();
        return 0;
    }

    public char load(){
        Memento memento = new Memento();
        if(!memento.loadData())
            return ' ';
        this.kind = memento.getKind();
        int[][] newBoard = memento.getBoard();
        this.board = factory.generateBoard(newBoard.length, kind);
        this.board.setState(newBoard, memento.getPlayerIndex(), memento.getCountXuZhao());
        this.countUndo = memento.getCountUndo();
        board.show();
        return kind;
    }

    public int xuZhao(){
        return board.xuZhao();
    }

    public int interrupt(){
        int playerIndex = board.getPlayerIndex();
        mementos[playerIndex-1].save(board.getBoard(), playerIndex, board.getCountXuZhao(), this.countUndo, this.kind);
        return 1;
    }
}