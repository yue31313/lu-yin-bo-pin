package com.demo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ZoomControls;

public class MainActivity extends Activity {

	Button btnStart,btnExit;
    SurfaceView sfv;
    ZoomControls zctlX,zctlY;
    
    ClsOscilloscope clsOscilloscope=new ClsOscilloscope();
    
    static final int frequency = 8000;//�ֱ���
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    static final int xMax = 16;//X����С�������ֵ,X���������޴����ײ���ˢ����ʱ
    static final int xMin = 8;//X����С������Сֵ
    static final int yMax = 10;//Y����С�������ֵ
    static final int yMin = 1;//Y����С������Сֵ
    
    int recBufSize;//¼����Сbuffer��С
    AudioRecord audioRecord;
    Paint mPaint;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //¼�����
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);
        //����
        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnExit = (Button) this.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new ClickEvent());
        //����ͻ���
        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01); 
        sfv.setOnTouchListener(new TouchEvent());
        mPaint = new Paint();  
        mPaint.setColor(Color.GREEN);// ����Ϊ��ɫ  
        mPaint.setStrokeWidth(1);// ���û��ʴ�ϸ 
        //ʾ�������
        clsOscilloscope.initOscilloscope(xMax/2, yMax/2, sfv.getHeight()/2);
        
        //���ſؼ���X���������С�ı��ʸ�Щ
        zctlX = (ZoomControls)this.findViewById(R.id.zctlX);
        zctlX.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateX>xMin)
                    clsOscilloscope.rateX--;
                setTitle("X����С"+String.valueOf(clsOscilloscope.rateX)+"��"
                        +","+"Y����С"+String.valueOf(clsOscilloscope.rateY)+"��");
            }
        });
        zctlX.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateX<xMax)
                    clsOscilloscope.rateX++;    
                setTitle("X����С"+String.valueOf(clsOscilloscope.rateX)+"��"
                        +","+"Y����С"+String.valueOf(clsOscilloscope.rateY)+"��");
            }
        });
        zctlY = (ZoomControls)this.findViewById(R.id.zctlY);
        zctlY.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateY>yMin)
                    clsOscilloscope.rateY--;
                setTitle("X����С"+String.valueOf(clsOscilloscope.rateX)+"��"
                        +","+"Y����С"+String.valueOf(clsOscilloscope.rateY)+"��");
            }
        });
        
        zctlY.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clsOscilloscope.rateY<yMax)
                    clsOscilloscope.rateY++;    
                setTitle("X����С"+String.valueOf(clsOscilloscope.rateX)+"��"
                        +","+"Y����С"+String.valueOf(clsOscilloscope.rateY)+"��");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    /**
     * �����¼�����
     * @author GV
     *
     */
    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnStart) {
                clsOscilloscope.baseLine=sfv.getHeight()/2;
                clsOscilloscope.Start(audioRecord,recBufSize,sfv,mPaint);
            } else if (v == btnExit) {
                clsOscilloscope.Stop();
            }
        }
    }
    /**
     * ��������̬���ò���ͼ����
     * @author GV
     *
     */
    class TouchEvent implements OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            clsOscilloscope.baseLine=(int)event.getY();
            return true;
        }
        
    }

}
