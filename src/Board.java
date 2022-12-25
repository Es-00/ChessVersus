public abstract class Board {
    protected int[][] chessBoard;
    protected int size;
    protected int countXuZhao;

    protected int playerIndex;

    public Board(int size){
        this.size = size;
        this.chessBoard = new int[size][size];
        this.countXuZhao = 0;
        this.playerIndex = 1;
    }

    boolean checkValidationOfAxis(int x, int y){
        return x>=0&&x<size&&y>=0&&y<size;
    }

    protected abstract boolean checkValidationOfDrop(int x, int y, int playerIndex);

    protected abstract boolean updateBoard(int x, int y);

    public abstract int xuZhao();

    //
    public int dropChessPiece(int x, int y){
        if(playerIndex != 1 && playerIndex != 2){
            System.out.println("非法玩家序号: " + playerIndex);
            return 2;
        }
        if (!this.checkValidationOfDrop(x, y, playerIndex)){
            System.out.println("非法落点: (" + x + ", " + y + ")");
            return 2;
        }
        countXuZhao = 0;
        this.updateBoard(x, y);
        return 1;
    }

    public void show(){
        for(int i=-1; i<size; ++i){
            for(int j=-1; j<size; ++j){
                if(i==-1){
                    if(j==-1){
                        System.out.print("* ");
                    }else {
                        System.out.print(j);
                        if (j < 10) System.out.print(' ');
                    }
                }else if(j==-1){
                    System.out.print(i);
                    if(i < 10) System.out.print(' ');
                }else if(chessBoard[i][j] == 1) {
                    System.out.print("o ");
                }else if(chessBoard[i][j] == 2){
                    System.out.print("x ");
                }else {
                    System.out.print("  ");
                }
            }
            System.out.print('\n');
        }
    }

    public void setState(int[][] chessBoard, int playerIndex, int countXuZhao){
        for(int i=0; i<size; ++i){
            this.chessBoard[i] = chessBoard[i].clone();
        }
        this.size = chessBoard.length;
        this.playerIndex = playerIndex;
        this.countXuZhao = countXuZhao;
    }

    public int[][] getBoard(){
        return chessBoard;
    }

    public int getCountXuZhao(){
        return countXuZhao;
    }

    abstract public int checkWinner();

    public int getPlayerIndex(){
        return this.playerIndex;
    }
}

class FirBoard extends Board{
    private int winner;
    private static final int[][] direction = new int[][]{
            {1, 1},{1, 0},{0, 1},{1,-1}
    };

    public FirBoard(int size){
        super(size);
        winner = 0;
    }

    @Override
    protected boolean checkValidationOfDrop(int x, int y, int playerIndex) {
        return checkValidationOfAxis(x, y) && chessBoard[x][y] == 0;
    }

    @Override
    protected boolean updateBoard(int x, int y){
        this.chessBoard[x][y] = playerIndex;
        for(int i=0;i<4;++i){
            int count = 1;
            for(int j=-1;j<=1;j+=2) {
                int xx = x + j*direction[i][0];
                int yy = y + j*direction[i][1];
                while (checkValidationOfAxis(xx ,yy) && this.chessBoard[xx][yy]==playerIndex){
                    count += 1;
                    xx += j*direction[i][0];
                    yy += j*direction[i][1];
                }
            }
            if(count >= 5)
                winner = playerIndex;
        }
        this.playerIndex = 3-playerIndex;
        return true;
    }

    @Override
    public int xuZhao(){
        System.out.println("五子棋不存在虚着");
        return 0;
    }

    @Override
    public int checkWinner() {
        return winner;
    }
}

class GoBoard extends Board{
    private static final int[][] direction = new int[][]{
            {1, 0},{0, 1}
    };

    public GoBoard(int size){
        super(size);
    }

    @Override
    protected boolean checkValidationOfDrop(int x, int y, int playerIndex) {
        if (!(checkValidationOfAxis(x, y) && chessBoard[x][y] == 0))
            return false;
        if(!isAlive(x, y, playerIndex)){
            chessBoard[x][y] = playerIndex;
            for(int i=0;i<2;++i){
                for(int j=-1;j<=1;j+=2) {
                    int xx = x + j*direction[i][0];
                    int yy = y + j*direction[i][1];
                    if (checkValidationOfAxis(xx ,yy) && this.chessBoard[xx][yy] == 3 - playerIndex){
                        if(!isAlive(xx, yy, 3-playerIndex)){
                            chessBoard[x][y] = 0;
                            return true;
                        }
                    }
                }
            }
            chessBoard[x][y] = 0;
            return false;
        }
        return true;
    }

    @Override
    protected boolean updateBoard(int x, int y){
        this.chessBoard[x][y] = playerIndex;
        for(int i=0;i<2;++i){
            for(int j=-1;j<=1;j+=2) {
                int xx = x + j*direction[i][0];
                int yy = y + j*direction[i][1];
                if (checkValidationOfAxis(xx ,yy) && this.chessBoard[xx][yy] == 3 - playerIndex){
                    if(!isAlive(xx, yy, 3-playerIndex)){
                        chi(xx, yy);
                    }
                }
            }
        }
        playerIndex = 3-playerIndex;
        return false;
    }

    private boolean isAlive(int x, int y, int playerIndex){
        for(int i=0;i<2;++i){
            for(int j=-1;j<=1;j+=2) {
                int xx = x + j*direction[i][0];
                int yy = y + j*direction[i][1];
                if (checkValidationOfAxis(xx ,yy)){
                    if(this.chessBoard[xx][yy] == 0)
                        return true;
                    if(this.chessBoard[xx][yy] == playerIndex){
                        this.chessBoard[x][y] = 3 - this.chessBoard[x][y];
                        if(isAlive(xx, yy, playerIndex)){
                            this.chessBoard[x][y] = 3 - this.chessBoard[x][y];
                            return true;
                        }
                        this.chessBoard[x][y] = 3 - this.chessBoard[x][y];
                    }
                }
            }
        }
        return false;
    }

    private void chi(int x, int y){
        if(chessBoard[x][y]==0)
            return;
        int index = chessBoard[x][y];
        chessBoard[x][y] = 0;
        for(int i=0;i<2;++i){
            for(int j=-1;j<=1;j+=2) {
                int xx = x + j*direction[i][0];
                int yy = y + j*direction[i][1];
                if (checkValidationOfAxis(xx ,yy) && index == chessBoard[xx][yy]){
                    chi(xx, yy);
                }
            }
        }
    }

    @Override
    public int xuZhao(){
        if(countXuZhao > 0) {
            return 1;
        }
        countXuZhao += 1;
        return 0;
    }

    public int checkWinner(){
        return 0;
    }
}

class BWBoard extends Board{
    private static final int[][] direction = new int[][]{
            {1, 1},{1, 0},{0, 1},{1,-1}
    };

    public BWBoard(int size) {
        super(size);
        int center = size/2;
        chessBoard[center][center] = 1;
        chessBoard[center-1][center-1] = 1;
        chessBoard[center-1][center] = 2;
        chessBoard[center][center-1] = 2;
        show();
    }


    @Override
    protected boolean checkValidationOfDrop(int x, int y, int playerIndex) {
        if(!checkValidationOfAxis(x, y) || chessBoard[x][y]!=0){
            return false;
        }
        for(int[] d: direction){
            for(int j=-1; j<2; j+=2){
                int xx = x + j*d[0];
                int yy = y + j*d[1];
                int length = 0;
                while(checkValidationOfAxis(xx, yy) && chessBoard[xx][yy]!=0){
                    if (chessBoard[xx][yy] == playerIndex){
                        if(length>0) {
                            return true;
                        }
                        break;
                    }
                    xx += j*d[0];
                    yy += j*d[1];
                    length += 1;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean updateBoard(int x, int y) {
        chessBoard[x][y] = playerIndex;
        for(int[] d: direction){
            for(int j=-1; j<2; j+=2){
                int xx = x + j*d[0];
                int yy = y + j*d[1];
                int length = 0;
                while(checkValidationOfAxis(xx, yy) && chessBoard[xx][yy]!=0){
                    if (chessBoard[xx][yy] == playerIndex){
                        while(length>0){
                            length -= 1;
                            xx -= j*d[0];
                            yy -= j*d[1];
                            System.out.println(xx + " " + yy);
                            chessBoard[xx][yy] = playerIndex;
                        }
                        break;
                    }
                    xx += j*d[0];
                    yy += j*d[1];
                    length += 1;
                }
            }
        }
        playerIndex = 3-playerIndex;
        boolean flag = false;
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if(checkValidationOfDrop(i, j, playerIndex)) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag){
            playerIndex = 3-playerIndex;
        }
        return true;
    }

    public int checkWinner(){
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if(checkValidationOfDrop(i, j, 1) || checkValidationOfDrop(i, j, 2)) return 0;
            }
        }
        int count1=0;
        int count2=0;
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                if (chessBoard[i][j] == 1){
                    count1+=1;
                }else if(chessBoard[i][j]==2){
                    count2+=1;
                }
            }
        }
        if(count1 > count2){
            return 1;
        }
        else if(count1 < count2){
            return 2;
        }
        else{
            return -1;
        }
    }

    @Override
    public int xuZhao() {
        return 0;
    }
}
