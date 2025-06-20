/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamemultiplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;

public class GamePanel extends JPanel implements KeyListener {
    int x = 100, y = 100;
    int dotX = 200, dotY = 200;
    int score = 0;
    PrintWriter out;

    public GamePanel(PrintWriter out) {
        this.out = out;
        setFocusable(true);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(dotX, dotY, 20, 20);
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 20, 20);
        g.drawString("Score: " + score, 10, 10);
    }

    public void handleServerMessage(String msg) {
        if (msg.startsWith("DOT:")) {
            String[] parts = msg.substring(4).split(",");
            dotX = Integer.parseInt(parts[0]);
            dotY = Integer.parseInt(parts[1]);
        } else if (msg.startsWith("SCORE:")) {
            String[] parts = msg.substring(6).split(",");
            int playerId = Integer.parseInt(parts[0]);
            int newScore = Integer.parseInt(parts[1]);
            if (playerId == 0) score = newScore;  // hanya contoh untuk player 0
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) x -= 10;
        if (key == KeyEvent.VK_RIGHT) x += 10;
        if (key == KeyEvent.VK_UP) y -= 10;
        if (key == KeyEvent.VK_DOWN) y += 10;
        out.println("POS:" + x + "," + y);
        repaint();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
