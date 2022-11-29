package com.example.threeinarow

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.uiltel.OnswipeListener
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {
    var mobs = intArrayOf(R.drawable.petyh,
        R.drawable.zombi,
        R.drawable.sliz,
        R.drawable.varden,
        R.drawable.elei,
        R.drawable.djaba,
        R.drawable.bebeshka)
    var widthOfBlock:Int=0
    var noOfBlock:Int=8
    var widthOfScreen:Int=0
    lateinit var mob:ArrayList<ImageView>
    var mobToBeDragged:Int=0
    var mobToBeReplaced:Int=0
    var notMob:Int=R.drawable.transparent

    lateinit var mHandler: Handler
    private  lateinit var scoreResult: TextView
    var score=0
    var internal=100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        scoreResult=findViewById(R.id.score)
        val displayMetrics=DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        widthOfScreen=displayMetrics.widthPixels
        var heightOfScreen=displayMetrics.heightPixels
        widthOfBlock=widthOfScreen/noOfBlock
        mob= ArrayList()
        createBoard()
        for (imageView in mob){
            imageView.setOnTouchListener(object:OnswipeListener(this){
                override fun onSwipeRight(){
                    super.onSwipeRight()
                    mobToBeDragged=imageView.id
                    mobToBeReplaced=mobToBeDragged+1
                    mobInterChange()
                }
                override fun onSwipeLift(){
                    super.onSwipeLift()
                    mobToBeDragged=imageView.id
                    mobToBeReplaced=mobToBeDragged-1
                    mobInterChange()
                }
                override fun onSwipeTop(){
                    super.onSwipeTop()
                    mobToBeDragged=imageView.id
                    mobToBeReplaced=mobToBeDragged+noOfBlock
                    mobInterChange()
                }
                override fun onSwipeBottom(){
                    super.onSwipeBottom()
                    mobToBeDragged=imageView.id
                    mobToBeReplaced=mobToBeDragged-noOfBlock
                    mobInterChange()
                }
            })
        }
       mHandler=Handler()
        startRepeat()
    }



    private fun mobInterChange() {
      var background:Int=mob.get(mobToBeReplaced).tag as Int
        var background1:Int=mob.get(mobToBeDragged).tag as Int
        mob.get(mobToBeDragged).setImageResource(background)
        mob.get(mobToBeReplaced).setImageResource(background1)

        mob.get(mobToBeDragged).setTag(background)
        mob.get(mobToBeReplaced).setTag(background1)
    }
    private fun checkRowForThree(){
        for (i in 0..61){
            var chosedMob=mob.get(i).tag
            var isBlank:Boolean=mob.get(i).tag==notMob
            val notValid= arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list=asList(*notValid)
            if (!list.contains(i)){
                var x=i
                if (mob.get(x++).tag as Int==chosedMob
                    && !isBlank
                    && mob.get(x++).tag as Int==chosedMob
                    && mob.get(x).tag as Int==chosedMob
                ){
                    score=score+3
                    scoreResult.text="$score"
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                    x--
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                    x--
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                }
            }
        }
        moveDownMobs()
    }
    private fun checkColumnForThree(){
        for (i in 0..47){
            var chosedMob=mob.get(i).tag
            var isBlank:Boolean=mob.get(i).tag==notMob
            var x=i
                if (mob.get(x).tag as Int==chosedMob
                    && !isBlank
                    && mob.get(x+noOfBlock).tag as Int==chosedMob
                    && mob.get(x+2*noOfBlock).tag as Int==chosedMob){
                    score=score+3
                    scoreResult.text="$score"
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                    x=x+noOfBlock
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                    x=x+noOfBlock
                    mob.get(x).setImageResource(notMob)
                    mob.get(x).setTag(notMob)
                }

        }
        moveDownMobs()
    }
    private fun moveDownMobs() {
       val firctRow= arrayOf(1,2,3,4,5,6,7)
        val list= asList(*firctRow)
        for(i in 55 downTo 0){
            if (mob.get(i+noOfBlock).tag as Int==notMob){
                mob.get(i+noOfBlock).setImageResource(mob.get(i).tag as Int)
                mob.get(i+noOfBlock).setTag(mob.get(i).tag as Int)
                mob.get(i).setImageResource(noOfBlock)
                mob.get(i).setTag(noOfBlock)
                if (list.contains(i) && mob.get(i).tag == notMob){
                    var randomMod:Int=Math.abs(Math.random()*mobs.size).toInt()
                    mob.get(i).setImageResource(mobs[randomMod])
                    mob.get(i).setTag(mobs[randomMod])
                }
            }
        }
        for (i in 0..7){
            if (mob.get(i).tag as Int==notMob){
                var randomMod:Int=Math.abs(Math.random()*mobs.size).toInt()
                mob.get(i).setImageResource(mobs[randomMod])
                mob.get(i).setTag(mobs[randomMod])
            }
        }
    }
    val repeatChecker:Runnable=object :Runnable{
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownMobs()
            }
            finally {
                mHandler.postDelayed(this,internal)
            }
        }

    }
    private fun startRepeat() {
       repeatChecker.run()
    }
    private fun createBoard() {
        val gridLayout=findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount=noOfBlock
        gridLayout.columnCount=noOfBlock
        gridLayout.layoutParams.width=widthOfScreen
        gridLayout.layoutParams.height=widthOfScreen
        for (i in 0 until noOfBlock * noOfBlock){
            val imageView = ImageView(this)
            imageView.id=i
            imageView.layoutParams=android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock)
            imageView.maxHeight=widthOfBlock
            imageView.maxWidth= widthOfBlock
            var random:Int=Math.floor(Math.random()*mobs.size).toInt()
            imageView.setImageResource(mobs[random])
            imageView.setTag(mobs[random])
            mob.add(imageView)
            gridLayout.addView(imageView)
        }
    }
}