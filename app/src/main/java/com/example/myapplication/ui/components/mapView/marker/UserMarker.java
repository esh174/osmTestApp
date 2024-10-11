package com.example.myapplication.ui.components.mapView.marker;

import static java.lang.Float.max;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import kotlin.Pair;

public class UserMarker extends Marker {
    Paint mImagePaint, mLabelTextPaint, mAdditionalTextPaint, mBackgroundPaint;
    float mTextOffsetX, mTextOffsetY, mTextSpacingVertical, mTextVerticalPadding, mTextHorizontalPadding;
    String mLabelText, mAdditionalText;
    Drawable mImageDrawable;

    public UserMarker(MapView mapView) {
        super(mapView);
        mImagePaint = new Paint();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);

        mLabelTextPaint = new Paint();
        mLabelTextPaint.setColor(Color.BLACK);
        mLabelTextPaint.setTextSize(16 * density);
        mLabelTextPaint.setAntiAlias(true);
        mLabelTextPaint.setTextAlign(Paint.Align.LEFT);

        mAdditionalTextPaint = new Paint();
        mAdditionalTextPaint.setColor(Color.GRAY);
        mAdditionalTextPaint.setTextSize(16 * density);
        mAdditionalTextPaint.setAntiAlias(true);
        mAdditionalTextPaint.setTextAlign(Paint.Align.LEFT);

        mTextOffsetX = (10 * density);
        mTextOffsetY = (10 * density);
        mTextSpacingVertical = (6 * density);
        mTextVerticalPadding = (8 * density);
        mTextHorizontalPadding = (16 * density);
    }

    public void setImageDrawable(final Drawable imageDrawable) {
        mImageDrawable = imageDrawable;
    }

    public void setLabel(final String text) {
        if (text != null) mLabelText = text;
    }

    public void setAdditionalText(final String text) {
        if (text != null) mAdditionalText = text;
    }

    public void draw(final Canvas c, final MapView osmv, boolean shadow) {
        draw(c, osmv);
    }

    public void draw(final Canvas c, final MapView osmv) {
        super.draw(c, osmv, false);
        Point p = this.mPositionPixels;
        Rect labelRect = new Rect(), addtionalTextRect = new Rect();

        float overallWidth, overallHeight;

        if (mImageDrawable != null) {
            Bitmap bitmap = drawableToBitmap(mImageDrawable);
            Bitmap croppedBitmap = getRoundedCroppedBitmap(bitmap);
            Pair<Float, Float> position = getImagePosition(bitmap);

            c.drawBitmap(croppedBitmap, position.getFirst(), position.getSecond(), mImagePaint);
        }

        if (mLabelText != null) {
            mLabelTextPaint.getTextBounds(mLabelText, 0, mLabelText.length(), labelRect);
        }

        if (mAdditionalText != null) {
            mAdditionalTextPaint.getTextBounds(mAdditionalText, 0, mAdditionalText.length(), addtionalTextRect);
        }

        overallWidth = max(labelRect.width(), addtionalTextRect.width());
        overallHeight = labelRect.height() + addtionalTextRect.height() + 2 * mTextVerticalPadding;

        if (mLabelText != null && mAdditionalText != null) {
            overallHeight += mTextSpacingVertical;
            overallWidth += mTextOffsetX + 2 * mTextHorizontalPadding;
        }

        RectF backgroundRect = new RectF(
                p.x + mTextOffsetX,
                p.y,
                p.x + overallWidth,
                p.y + overallHeight
        );

        float labelX = backgroundRect.left + mTextHorizontalPadding;
        float labelY = backgroundRect.top + labelRect.height() + mTextVerticalPadding;

        float additionalTextX = backgroundRect.left + mTextHorizontalPadding;
        float additionalTextY = labelY + addtionalTextRect.height();

        if (mLabelText != null || mAdditionalText != null) {
            c.drawRoundRect(
                    backgroundRect,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE,
                    mBackgroundPaint
            );
        }

        if (mLabelText != null) {
            c.drawText(
                    mLabelText,
                    labelX,
                    labelY,
                    mLabelTextPaint
            );
        }

        if (mAdditionalText != null) {
            if(mLabelText != null) {
                additionalTextY += mTextSpacingVertical;
            }

            c.drawText(
                    mAdditionalText,
                    additionalTextX,
                    additionalTextY,
                    mAdditionalTextPaint
            );
        }
    }

    private Pair<Float, Float> getImagePosition(Bitmap bitmap) {
        Point p = this.mPositionPixels;

        float xCoordinate = p.x - (float) (bitmap.getWidth() / 2) - 0.5f * density;
        float yCoordinate = (float) (p.y - this.getIcon().getIntrinsicHeight() + 7 * density);

        return new Pair<Float, Float>((float) xCoordinate, (float) yCoordinate);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap) {
        int widthLight = bitmap.getWidth();
        int heightLight = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, (float) widthLight / 2, (float) heightLight / 2, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paintImage);

        return output;
    }

    private final float density = Resources.getSystem().getDisplayMetrics().density;
}
