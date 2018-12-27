package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Maps;
import  com.example.administrator.myapplication.Direction;

public class MainActivity extends Activity {
    private String tag = "GridLaoutActivity";
    GridLayout gridLayout;
    float startX = 0, startY = 0, endX, endY;
    Maps maps = new Maps();
    private TextView score;
    private TextView best;

    @SuppressLint("NewApi")
    void init() {
        // 获取View对象
        gridLayout = (GridLayout) findViewById(R.id.root);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button bn = new Button(this);
                bn.setClickable(false);
                bn.setText("");
                // 设置该按钮的字号大小
                bn.setTextSize(20);
                bn.setTextColor(getResources().getColor(R.color.colorPrimary));
                bn.setWidth(150);
                bn.setHeight(300);
                // 指定该组件所在的行q
                GridLayout.Spec rowSpec = GridLayout.spec(i + 2);
                // 指定该组件所在的列
                GridLayout.Spec columnSpec = GridLayout.spec(j);
                String msg = "rowSpec:" + (i + 2) + " - columnSpec:" + (j);
                Log.d(tag, msg);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        rowSpec, columnSpec);
                // 指定该组件占满容器
                // params.setGravity(Gravity.FILL);
                gridLayout.addView(bn, params);
                maps.addButton(i, j, bn);
            }
        }
        score = (TextView) findViewById(R.id.score);
        score.setText("0");
        best = (TextView) findViewById(R.id.best);
        maps.setScore(score);
        maps.setBest(best);
        maps.init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        // System.out.println("触摸");
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        } else if (action == MotionEvent.ACTION_UP) {
            endX = event.getX();
            endY = event.getY();
            int direction = GetSlideDirection(startX, startY, endX, endY);
            // System.out.println(startX+","+startY+"|"+endX+","+endY+"  "+direction);
            // Toast.makeText(this, direction+"", Toast.LENGTH_LONG).show();
            boolean gameOver = maps.Slide(direction);
            maps.numberColor(getResources());

            if (gameOver) {
                if(maps.getScore()>maps.getBestScore()){
                    Toast.makeText(this, "恭喜超过最佳记录！！！", Toast.LENGTH_SHORT).show();
                    maps.setBestScore(maps.getScore());
                    best.setText(maps.getScore()+"");
                }else{
                    Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
                }

            }
        }
        return super.dispatchTouchEvent(event);
    }

    // 返回角度
    private double GetSlideAngle(float dx, float dy) {
        return Math.atan2(dy, dx) * 180 / Math.PI;
    }

    // 根据起点和终点返回方向 1：向上，2：向下，3：向左，4：向右,0：未滑动
    private int GetSlideDirection(float startX, float startY, float endX,
                                  float endY) {
        float dy = startY - endY;
        float dx = endX - startX;
        int result = Direction.NONE;
        // 如果滑动距离太短
        if (Math.abs(dx) < 2 && Math.abs(dy) < 2) {
            return result;
        }
        double angle = GetSlideAngle(dx, dy);
        if (angle >= -45 && angle < 45) {
            return Direction.RIGHT;
        } else if (angle >= 45 && angle < 135) {
            return Direction.UP;
        } else if (angle >= -135 && angle < -45) {
            return Direction.DOWN;
        } else if ((angle >= 135 && angle <= 180)
                || (angle >= -180 && angle < -135)) {
            return Direction.LEFT;
        }
        return result;
    }

    public void reset(View view) {
        maps.init();
    }

}