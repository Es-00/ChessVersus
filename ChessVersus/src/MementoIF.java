import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;
import java.util.regex.Pattern;

public interface MementoIF {
}

class Memento implements MementoIF{
    private int[][] board;
    private int playerIndex;
    private int countXuZhao;
    private boolean isSaved;
    private int[] countUndo;
    private String dir;
    private char kind;

    public Memento(){
        isSaved = false;
        countUndo = new int[2];
        dir = ".\\saveData";
    }

    public void save(int[][] board, int playerIndex, int countXuZhao, int[] countUndo, char kind){
        isSaved = true;
        if(this.board == null){
            this.board = new int[board.length][board.length];
        }
        for(int i = 0; i < board.length; ++i)
            this.board[i] = board[i].clone();
        this.playerIndex = playerIndex;
        this.countXuZhao = countXuZhao;
        this.countUndo = countUndo;
        this.kind = kind;
        saveData();
    }

    private void saveData(){
        File file = new File(dir);
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(playerIndex + "\n");
            writer.write(countXuZhao + "\n");
            writer.write(countUndo[0] + "\n");
            writer.write(countUndo[1] + "\n");
            writer.write(kind + "\n");
            writer.write(board.length + "\n");
            for (int[] i:board) {
                for (int j:i) {
                    writer.write(j + "\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public boolean loadData(){
        File file = new File(dir);
        Vector<String> tmp = new Vector<String>();
        if(!file.exists())
            return false;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while((line = br.readLine()) != null){
                tmp.add(line);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        playerIndex = Integer.parseInt(tmp.get(0));
        countXuZhao = Integer.parseInt(tmp.get(1));
        countUndo[0] = Integer.parseInt(tmp.get(2));
        countUndo[1] = Integer.parseInt(tmp.get(3));
        kind = tmp.get(4).charAt(0);
        int size = Integer.parseInt(tmp.get(5));
        board = new int[size][size];
        for(int i=0;i<size;++i){
            for(int j=0;j<size;++j){
                board[i][j] = Integer.parseInt(tmp.get(6+i*size+j));
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board.clone();
    }

    public int getPlayerIndex(){
        return playerIndex;
    }

    public int getCountXuZhao(){
        return countXuZhao;
    }

    public int[] getCountUndo(){
        return countUndo;
    }

    public char getKind(){
        return kind;
    }

    public boolean isSaved(){
        return isSaved;
    }

    public void clear(){
        File f = new File(dir);
        f.delete();
        isSaved = false;
    }
}
