package com.example.sandeepkumar.appagame;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class playgame extends AppCompatActivity {

    private int val;
    GridView gameGrid;
    ArrayList<Integer> gridValues;
    GridAdapter gridAdapter;
    int rolledSum = 0;

    int rnd;

    public int getRnd() {
        return rnd;
    }

    public void setRnd(int rnd) {
        this.rnd = rnd;

    }

    Button startBtn, shuffle, rollBtn;
    ImageView imgDice;
    private int diceIcons[] = {
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int[] mid = {0};
        setContentView(R.layout.activity_playgame);
        startBtn = (Button) findViewById(R.id.start);
        rollBtn = (Button) findViewById(R.id.roll);
        imgDice = (ImageView) findViewById(R.id.lido);
        shuffle = (Button) findViewById(R.id.reset);
        gameGrid = (GridView) findViewById(R.id.gridview);
        gridAdapter = new GridAdapter(this);
        final ShortestPath shortestPath = new ShortestPath();

        // Roll Btn

        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                setRnd(r.nextInt(6) + 1);
                imgDice.setImageResource(diceIcons[rnd - 1]);
                mid[0] = rnd;

                rolledSum+=rnd;
            }
        });

        //on shuffle click


        gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //   String value =  adapterView.getItemAtPosition(position);
                // Log.d("SHORTEST", " adding " +value);
                //ShortestPath shortestPath1=new ShortestPath();
                //shortestPath1.makeReadyMap(position);
//                gridAdapter.setPathItem(position,Constants.ITEM_TYPE_BANNED);
                val = position;
                int row = val / 10;
                int coll = val % 10;
//               area [row][coll]=Constants.ITEM_TYPE_BANNED;

            }
        });


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(playgame.this, playgame.class));

                finish();
            }
        });
        //on item click.
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ArrayList<Integer> sols = shortestPath.getSolution();

                if (sols.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Shortest_Path is not found!!!!!!!", Toast.LENGTH_LONG).show();
                }
                new Thread() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        int count = sols.size();
                        int count1 = count;

                        int temp_rnd = getRnd();
                        int startPos = 1, midPos = temp_rnd;

                         if(count1-rolledSum > temp_rnd){
                             Log.d("do something","good" +Integer.toString(count1-rolledSum));

                        do {
                            midPos = temp_rnd;
                            for (int i = startPos; i <= rolledSum; i++) {
                                Log.d("in for","good" +Integer.toString(rolledSum));

                                final int loopNum = i;

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        gridAdapter.setPathItem(sols.get(loopNum), Constants.ITEM_TYPE_INTERMEDIATE_ITEM);

                                    }
                                });
                            }
                            startPos = midPos + 1;

//                            midPos+= temp_rnd;
                        } while (startPos < count);

                    }
                        else {
                             rolledSum =rolledSum-temp_rnd;
                             Log.d("do nothing","good" +Integer.toString(rolledSum));
                         }

                    }
                }.start();

            }
        });

        gridValues = new ArrayList<>();
        shortestPath.makeReadyMap(val);
        gridAdapter.setPathValues(gridValues);
        gameGrid.setAdapter(gridAdapter);

        //set on item onclicklisner game grid


        // select possition of grid..

      /*  gameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long l) {
               Log.d("play game","position of click " + Integer.toString(position));

            }
        });*/


    }


    public class ShortestPath {
        int[][] area;

        public List<Field> findShortestPath(int[][] area, int x, int y) {
            Field[][] fields = new Field[area.length][area[0].length];
            for (int i = 0; i < fields.length; i++) {
                for (int j = 0; j < fields[0].length; j++) {
                    if (area[i][j] != 0) {
                        fields[i][j] = new Field(i, j, Integer.MAX_VALUE, null);
                    }
                }
            }

            LinkedList<Field> q = new LinkedList<>();
            LinkedList<Field> q1 = new LinkedList<>();

            Field start = fields[x][y];
            start.dist = 0;
            q.add(start);

            Field dest = null;
            Field cur;
            while ((cur = q.poll()) != null) {
                if (area[cur.x][cur.y] == 3) {
                    dest = cur;
                }
                visitNeighbour(fields, q, cur.x - 1, cur.y, cur);
                visitNeighbour(fields, q, cur.x + 1, cur.y, cur);
                visitNeighbour(fields, q, cur.x, cur.y - 1, cur);
                visitNeighbour(fields, q, cur.x, cur.y + 1, cur);
            }

            if (dest == null) {
                return Collections.emptyList();
            } else {
                LinkedList<Field> path = new LinkedList<>();
                cur = dest;
                do {
                    path.addFirst(cur);
                } while ((cur = cur.prev) != null);

                return path;
            }
        }

        private void visitNeighbour(Field[][] fields, LinkedList<Field> q, int x, int y, Field parent) {
            int dist = parent.dist + 1;
            if (x < 0 || x >= fields.length || y < 0 || y >= fields[0].length || fields[x][y] == null) {
                return;
            }
            Field cur = fields[x][y];
            if (dist < cur.dist) {
                cur.dist = dist;
                cur.prev = parent;
                q.add(cur);
            }
        }

        private class Field implements Comparable<Field> {
            public int x;
            public int y;
            public int dist;
            public Field prev;

            private Field(int x, int y, int dist, Field prev) {
                this.x = x;
                this.y = y;
                this.dist = dist;
                this.prev = prev;
            }

            @Override
            public int compareTo(Field o) {
                return dist - o.dist;
            }
        }


        private int XYtoIndex(int x, int y) {
            return x * 10 + y;
        }

        public void makeReadyMap(int p1) {
            Random rd = new Random();
            area = new int[][]{
                    {2, 1, 1, 1, 1, 0, 0, 1, 1, 1},
                    {1, 1, 1, 0, 1, 1, 1, 1, 1, 0},
                    {1, 0, 0, 1, 0, 1, 0, 1, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
                    {1, 1, 1, 0, 1, 1, 1, 0, 1, 0},
                    {1, 0, 0, 1, 0, 1, 0, 1, 0, 0},
                    {1, 1, 0, 1, 1, 1, 0, 0, 1, 0},
                    {0, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 3}

            };


            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (area[i][j] != 2 && area[i][j] != 3) {
                        area[i][j] = 1;
                    }
                }
            }
            int n, x, y;
            for (int i = 0; i < 30; i++) {
                n = rd.nextInt(100);
                x = n / 10;
                y = n % 10;
                if (area[x][y] != 2 && area[x][y] != 3) {
                    area[x][y] = 0;
                }

            }
            //shuffle(area,10,new Random());

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    gridValues.add(10 * i + j, area[i][j]);
                }
            }

        }


        public ArrayList<Integer> getSolution() {
            playgame ob = new playgame();
            ArrayList<Integer> solutions = new ArrayList<>();
            List<Field> shortestPath = findShortestPath(area, (ob.val) / 10, (ob.val) % 10);
            ArrayList<Integer> index = new ArrayList<>();
            int c = 0;
            for (Field f : shortestPath) {
                System.out.println(String.format("(%d;%d)", f.x, f.y));
                Log.d("SHORTEST", " adding " + XYtoIndex(f.x, f.y));
                solutions.add(c++, XYtoIndex(f.x, f.y));
            }

            return solutions;
        }
    }


}