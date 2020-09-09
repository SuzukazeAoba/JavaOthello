import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Othello extends JFrame{
    private Othello(){
        int board_size = 6;
        setSize(board_size*100,board_size*100);
        setTitle("Othello game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        MyJPanel myJPanel = new MyJPanel();
        Container c = getContentPane();
        c.add(myJPanel);
        setVisible(true);
    }
    public static void main(String[] args){
        new Othello();
    }
    public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
        int board_size = 6;
        int [][] chess = new int[board_size][board_size];
        int input_x;
        int input_y;
        int chess_flag;
        boolean game_over;
        Timer timer;
        MyJPanel(){
            //initialize the chess status
            game_over = false;
            chess_flag = 1;
            input_x = board_size/2;
            input_y = board_size/2;
            for(int i = 0;i < board_size;i++){
                for(int j = 0;j < board_size;j++){
                    if((i == board_size/2 - 1 && j == board_size / 2 - 1)||(i == board_size/2 && j == board_size/2)){
                        chess[i][j] = 1;// 1 represents black chess
                    }
                    else if ((i == board_size/2 && j ==  board_size/2 - 1)||(i ==  board_size/2 - 1 && j == board_size/2)){
                        chess[i][j] = -1;//-1 represents white chess
                    }
                    else chess[i][j] = 0;//0 represents no chess
                }
            }
            //add listeners and timer
            addMouseListener(this);
            addMouseMotionListener(this);
            timer = new Timer(200,this);
            timer.start();
        }
        public void paintComponent(Graphics g){
            //draw the chess board
            g.setColor(Color.green);
            g.fillRect(0,0,board_size*100,board_size*100);
            g.setColor(Color.black);
            for(int i = 100;i < board_size*100; i = i + 100){
                g.drawLine(i,0,i,board_size*100);
                g.drawLine(0,i,board_size*100,i);
            }
            //draw the chess
            for(int i = 0; i < board_size; i++){
                for(int j = 0; j < board_size; j++){
                    if(chess[i][j] == 1){
                        g.setColor(Color.black);
                        g.fillOval(i*100, j*100, 100,100);
                    }
                    else if(chess[i][j] == -1){
                        g.setColor(Color.white);
                        g.fillOval(i*100, j*100, 100,100);
                    }
                }
            }
        }
        public void actionPerformed(ActionEvent e){
            if(game_over){
                JOptionPane.showMessageDialog(null,"This game is over.");
                timer.stop();
            }
            else{
                if(chess[input_x][input_y] == 0){//if there is no chess on that block
                    if(!is_valid(input_x, input_y)){//if it is not a valid position
                        JOptionPane.showMessageDialog(null,"Not valid position! Try again.");
                        //reset the input chess coordinate
                        input_x = board_size/2;
                        input_y = board_size/2;
                    }
                    else{
                        revise(input_x,input_y);//revise the chess
                        chess_flag = - chess_flag;//change player
                        repaint();
                        if(!has_position()){//if next player does not have valid position
                            chess_flag = - chess_flag;//change player
                            if(!has_position()){//if either the next player
                                winner();
                                game_over = true;//game is over
                            }
                            else{//this player continues
                                JOptionPane.showMessageDialog(null,"No valid position for next player. Please continue");
                            }
                        }
                    }
                }
            }
        }
        public void mouseClicked(MouseEvent me){
            input_x = me.getX()/100;
            input_y = me.getY()/100;
        }
        public void mousePressed(MouseEvent me){}
        public void mouseReleased(MouseEvent me){}
        public void mouseExited(MouseEvent me){}
        public void mouseEntered(MouseEvent me){}
        public void mouseMoved(MouseEvent me){}
        public void mouseDragged(MouseEvent me){}

        //check whether it is a valid position
        boolean is_valid(int x, int y){
            if(chess[x][y] != 0){
                return false;
            }
            for(int i = 0;i < board_size;i++){
                //check the chess on the right side of the input chess
                if(((i - x) >= 2) && (chess[i][y] == chess_flag)){
                    int count = 0;
                    for(int k = x;k < i;k++){
                        if(chess[k][y] == -chess_flag) count++;
                    }
                    if(count == (i - x - 1)){
                        return true;
                    }
                }
                //check the chess on the left side of input chess
                if((x - i) >= 2 && (chess[i][y] == chess_flag)){
                    int count = 0;
                    for(int k = x;k > i;k--){
                        if(chess[k][y] == -chess_flag) count++;
                    }
                    if(count == (x - i - 1)){
                        return true;
                    }
                }
                //check the chess under the input chess
                if(((i - y) >= 2) && (chess[x][i] == chess_flag)){
                    int count = 0;
                    for(int k = y;k < i;k++){
                        if(chess[x][k] == -chess_flag) count++;
                    }
                    if(count == (i - y - 1)){
                        return true;
                    }
                }
                //check the chess above the input chess
                if((y - i) >= 2 && (chess[x][i] == chess_flag)){
                    int count = 0;
                    for(int k = y;k > i;k--){
                        if(chess[x][k] == -chess_flag) count++;
                    }
                    if(count == (y - i - 1)){
                        return true;
                    }
                }
                //check diagonal
                for(int j = 0;j < board_size;j++){
                    //check right-up side of the input chess
                    if(((i - x) >= 2) && (y - j)==(i - x) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k < i;k++){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy--;
                        }
                        if(count == (i - x - 1)){
                            return true;
                        }
                    }
                    //check right-down side of the input chess
                    if(((i - x) >= 2) && (j - y)==(i - x) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k < i;k++){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy++;
                        }
                        if(count == (i - x - 1)){
                            return true;
                        }
                    }
                    //check left-up side of the input chess
                    if((x - i) >= 2 && ((y - j)==(x - i)) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k > i;k--){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy--;
                        }
                        if(count == (x - i - 1)){
                            return true;
                        }
                    }
                    //check left-down side of the input chess
                    if((x - i) >= 2 && ((j - y)==(x - i)) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k > i;k--){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy++;
                        }
                        if(count == (x - i - 1)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        //revise the color of the chess
        void revise(int x, int y){
            for(int i = 0;i < board_size;i++){
                //check the chess on the right side of the input chess
                if(((i - x) >= 2) && (chess[i][y] == chess_flag)){
                    int count = 0;
                    for(int k = x;k < i;k++){
                        if(chess[k][y] == -chess_flag) count++;
                    }
                    if(count == (i - x - 1)){
                        for(int k = x;k < i;k++){
                            chess[k][y] = chess_flag;
                        }
                    }
                }
                //check the chess on the left side of input chess
                if((x - i) >= 2 && (chess[i][y] == chess_flag)){
                    int count = 0;
                    for(int k = x;k > i;k--){
                        if(chess[k][y] == -chess_flag) count++;
                    }
                    if(count == (x - i - 1)){
                        for(int k = x;k > i;k--){
                            chess[k][y] = chess_flag;
                        }
                    }
                }
                //check the chess under the input chess
                if(((i - y) >= 2) && (chess[x][i] == chess_flag)){
                    int count = 0;
                    for(int k = y;k < i;k++){
                        if(chess[x][k] == -chess_flag) count++;
                    }
                    if(count == (i - y - 1)){
                        for(int k = y;k < i;k++){
                            chess[x][k] = chess_flag;
                        }
                    }
                }
                //check the chess above the input chess
                if((y - i) >= 2 && (chess[x][i] == chess_flag)){
                    int count = 0;
                    for(int k = y;k > i;k--){
                        if(chess[x][k] == -chess_flag) count++;
                    }
                    if(count == (y - i - 1)){
                        for(int k = y;k > i;k--){
                            chess[x][k] = chess_flag;
                        }
                    }
                }
                //check diagonal
                for(int j = 0;j < board_size;j++){
                    //check right-up side of the input chess
                    if(((i - x) >= 2) && (y - j)==(i - x) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k < i;k++){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy--;
                        }
                        if(count == (i - x - 1)){
                            yy = y;
                            for(int k = x;k < i;k++){
                                chess[k][yy] = chess_flag;
                                yy--;
                            }
                        }
                    }
                    //check right-down side of the input chess
                    if(((i - x) >= 2) && (j - y)==(i - x) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k < i;k++){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy++;
                        }
                        if(count == (i - x - 1)){
                            yy = y;
                            for(int k = x;k < i;k++){
                                chess[k][yy] = chess_flag;
                                yy++;
                            }
                        }
                    }
                    //check left-up side of the input chess
                    if((x - i) >= 2 && ((y - j)==(x - i)) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k > i;k--){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy--;
                        }
                        if(count == (x - i - 1)){
                            yy = y;
                            for(int k = x;k > i;k--){
                                chess[k][yy] = chess_flag;
                                yy--;
                            }
                        }
                    }
                    //check left-down side of the input chess
                    if((x - i) >= 2 && ((j - y)==(x - i)) && (chess[i][j] == chess_flag)){
                        int yy = y;
                        int count = 0;
                        for(int k = x;k > i;k--){
                            if(chess[k][yy] == -chess_flag) count++;
                            yy++;
                        }
                        if(count == (x - i - 1)){
                            yy = y;
                            for(int k = x;k > i;k--){
                                chess[k][yy] = chess_flag;
                                yy++;
                            }
                        }
                    }
                }
            }
        }
        //check if the player has at least one valid position
        boolean has_position(){
            for(int i = 0;i < board_size;i++){
                for(int j = 0;j < board_size;j++){
                    if(is_valid(i,j)) return true;
                }
            }
            return false;
        }
        //calculate winner
        void winner(){
            int black = 0;
            int white = 0;
            for(int i = 0;i < board_size;i++){
                for(int j = 0;j < board_size;j++){
                    if(chess[i][j] == 1) black++;
                    if(chess[i][j] == -1) white++;
                }
            }
            if(black > white) JOptionPane.showMessageDialog(null,"Black win!");
            if(black < white) JOptionPane.showMessageDialog(null,"White win!");
            if(black == white) JOptionPane.showMessageDialog(null,"The score of both player is the same. No winner!");
        }
    }
}
