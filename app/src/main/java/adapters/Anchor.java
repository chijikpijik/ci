package adapters;

import android.content.Context;
import android.view.View;

public class Anchor extends View {

    private AttachListener mAttachListener;

    private NonZeroListener mNonZeroListener;

    private boolean isAttached = false;

    private float x;
    private float y;

    public Anchor(Context context) {
        super(context);
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                isAttached = true;
                if (mAttachListener != null) {
                    mAttachListener.onAttach();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                isAttached = false;
            }
        });
    }

    public void setAttachListener(AttachListener listener) {
        mAttachListener = listener;
        if (listener != null && isAttached) {
            mAttachListener.onAttach();
        }
    }

    public void setNonZeroListener(NonZeroListener nonZeroListener) {
        mNonZeroListener = nonZeroListener;
        checkNoneZero();
    }

    private void checkNoneZero() {
        boolean isNoneZero = y != 0 && x != 0;
        if(isNoneZero && mNonZeroListener!=null){
            mNonZeroListener.onNoneZero();
        }
    }

    public interface AttachListener{
        void onAttach();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        x = getX();
        y = getY();
        checkNoneZero();
    }

    public interface NonZeroListener{
        void onNoneZero();
    }

}