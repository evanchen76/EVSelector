package evan.chen.app.evselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Evan on 2017/5/5.
 */

public class EVSelector extends LinearLayout {

    private  int[] drawables = {};

    private LinearLayout dialog_linearlayout;
    private ImageView select_imageview;
    private boolean isSelected = false;

    private final int ROW_HEIGHT = 150;
    private Context context;

    public interface IconSelectListener {
        public void onOpen();
        public void onSelected(int iconIndex);
        public void onCancel();
    }

    private IconSelectListener listener;

    public EVSelector(Context context) {
        super(context);
        init(context, null);
    }

    public EVSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EVSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setSelectIcon(int[] drawables){
        this.drawables = drawables;

        this.drawButton();
        this.drawDialog();
        DialogDisplay(this.isSelected);
    }

    private void rotateSelectImageView(){
        float rotateDegree;

        if ( isSelected == true) {
            rotateDegree = -45.0f;
        }else{
            rotateDegree = 0f;
        }

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, rotateDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(300);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        this.select_imageview.startAnimation(animSet);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View.inflate(context, R.layout.ev_selector, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        this.dialog_linearlayout = (LinearLayout)findViewById(R.id.dialog_select_linearlayout);
        this.select_imageview = (ImageView)findViewById(R.id.select_imageview);

        this.select_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( isSelected == false) {
                    isSelected = true;
                }else{
                    isSelected = false;
                }

                rotateSelectImageView();
                DialogDisplay(isSelected);
                drawButton();

                if ( isSelected == false) {
                    listener.onCancel();
                }else{
                    listener.onOpen();
                }
            }
        });

        this.drawButton();
        this.drawDialog();
        DialogDisplay(this.isSelected);
    }

    private void dismissDialog(){
        this.isSelected = false;
        rotateSelectImageView();
        DialogDisplay(this.isSelected);;
        drawButton();
    }

    private void drawButton(){
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);

        // Draw RoundRect
        Paint p = new Paint();
        p.setStrokeWidth(2);
        if ( this.isSelected == true){
            p.setColor(Color.parseColor("#7B8C97"));
        }else{
            p.setColor(Color.parseColor("#419DFF"));
        }

        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(25, 25, 25, p);

        int circlePadding = 12;
        p.setColor(Color.WHITE);

        RectF rect = new RectF(25-3,circlePadding,25+3,50-circlePadding);
        canvas.drawRect(rect, p);

        RectF rect2 = new RectF(circlePadding, 25-3,50-circlePadding,25+3);
        canvas.drawRect(rect2, p);

        this.select_imageview.setImageBitmap(bitmap);
    }

    private void drawDialog(){
        int triangleWidth = 50;
        int triangleHeight = 40;

        int imageRows = 1;
        int imagesPerRow = 3;

        imageRows = drawables.length / imagesPerRow ;

        if ( drawables.length % imagesPerRow > 0 ){
            imageRows ++;
        }
        if ( imageRows == 0){
            imageRows = 1;
        }

        int dialogWidth = 500;
        int dialogImagesHeight = ROW_HEIGHT * imageRows;
        int dialogHeight = ROW_HEIGHT * imageRows + triangleHeight;

        LinearLayout iconLinearLayout = (LinearLayout) findViewById(R.id.dialog_select_icon_linearlayout);

        for (int i=0; i<imageRows; i++){
            // through every row

            LinearLayout imageLayout = new LinearLayout(this.context);
            imageLayout.setOrientation(LinearLayout.HORIZONTAL);

            LayoutParams imageParam = new LayoutParams(
                    0,
                    dialogWidth/6,
                    1.0f
            );

            for (int j=0; j<imagesPerRow; j++) {
                final int nums = i * imagesPerRow + j;

                if ( nums < drawables.length) {
                    ImageView imageView = new ImageView(this.context);
                    imageView.setImageResource(drawables[nums]);
                    imageView.setLayoutParams(imageParam);

                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                            listener.onSelected(nums);
                        }
                    });

                    imageLayout.addView(imageView);
                }
            }

            iconLinearLayout.addView(imageLayout);
        }

        Bitmap bitmap = Bitmap.createBitmap(dialogWidth, dialogHeight, Bitmap.Config.ARGB_8888);

        // Draw RoundRect
        Paint p = new Paint();
        p.setStrokeWidth(2);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setShadowLayer(5, 2, 2, Color.LTGRAY);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setPathEffect(new CornerPathEffect(10) );

        Canvas canvas = new Canvas(bitmap);

        int leftX = 0;
        int leftY = 0;

        int rightX = dialogWidth;
        int rightY = dialogImagesHeight;

        int centerX = (rightX - leftX)/ 2 + leftX;

        Path path =new Path();

        path.moveTo(leftX, leftY);
        path.lineTo(rightX, leftY);
        path.lineTo(rightX, rightY);
        path.lineTo(centerX+triangleWidth, rightY);
        path.lineTo(centerX, rightY+triangleHeight);
        path.lineTo(centerX-triangleWidth, rightY);
        path.lineTo(leftX, rightY);
        path.close();

        canvas.drawPath(path, p);

        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        this.dialog_linearlayout.setBackground(drawable);
    }

    private void DialogDisplay(boolean display){

        if ( display == true) {
            this.dialog_linearlayout.setVisibility(View.VISIBLE);
            this.dialog_linearlayout.bringToFront();

            Animation anim = new ScaleAnimation(
                    0.2f, 1f,
                    0.2f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF , 1f);

            anim.setDuration(100);
            this.dialog_linearlayout.startAnimation(anim);
        }else{
            this.dialog_linearlayout.setVisibility(View.GONE);
        }
    }

    public void setListener(IconSelectListener listener) {
        this.listener = listener;
    }
}
