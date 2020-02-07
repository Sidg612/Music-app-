package com.example.musicapp

import android.annotation.SuppressLint
import android.icu.util.Measure
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
     lateinit var animation: Animation

    private lateinit var mp:MediaPlayer
    private var totalTime: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animation=AnimationUtils.loadAnimation(this, R.anim.rotation_clockwise)

        mp = MediaPlayer.create(this,R.raw.music)
        mp.isLooping=true
        mp.setVolume(0.5f,0.5f)

        totalTime= mp.duration

        //volume bar

       volumeSb.setOnSeekBarChangeListener(

           object: SeekBar.OnSeekBarChangeListener{
               override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                   if(fromUser){

                       var volumeNum= progress/100.0f
                       mp.setVolume(volumeNum,volumeNum)
                   }
               }

               override fun onStartTrackingTouch(seekBar: SeekBar?) {

               }

               override fun onStopTrackingTouch(seekBar: SeekBar?) {
               }
           }

       )



        //position bar
        positionbar.max=totalTime

        positionbar.setOnSeekBarChangeListener(

            object :SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    if(fromUser){

                        mp.seekTo(progress)

                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )


       //positionBar

        positionbar.max=totalTime
        positionbar.setOnSeekBarChangeListener(
            object:SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if(fromUser)
                    {

                        mp.seekTo(progress)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }

        )


        //Thread
        Thread(Runnable {

            while(mp!=null){


                try{

                    var msg= Message()

                    msg.what=mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }catch(e: InterruptedException){


                }
            }

        }).start()
    }
    @SuppressLint("HandelerLeak")
    var handler= object: Handler(){
        override fun handleMessage(msg: Message) {
            var currentposition=msg.what

            //update position bar
            positionbar.progress= currentposition

            //update labels
            var elapsedTime=createTimeLabel(currentposition)

            timeElapsedTV.text=elapsedTime

            var remainingTime=createTimeLabel(totalTime-currentposition)

            timeLeftTV.text="$remainingTime"
        }
    }

    fun createTimeLabel(time:Int):String{

        var timelabel=""
        var min= time/ 1000/60
        var sec = time/1000%60

        var timelable = "$min:"

        if(sec < 10) timelabel+="0"
        timelabel+=sec

        return  timelabel

    }

    fun playbtnClick(v:View){

        if(mp.isPlaying){

            //stop

            mp.pause()
            musicImg.clearAnimation()
            playbtn.setBackgroundResource(R.drawable.playbtn)


        }

        else{

            //start
            mp.start()
            musicImg.startAnimation(animation)
            playbtn.setBackgroundResource(R.drawable.pausebutton)
        }

    }
}


