// Generated by view binder compiler. Do not edit!
package com.example.baybayin.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.baybayin.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class Yunit3QuizYaBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button backBtn;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final Button choices1;

  @NonNull
  public final Button choices2;

  @NonNull
  public final Button choices3;

  @NonNull
  public final ImageFilterView imageView;

  @NonNull
  public final TextView qustion;

  @NonNull
  public final Button suriin;

  private Yunit3QuizYaBinding(@NonNull RelativeLayout rootView, @NonNull Button backBtn,
      @NonNull CardView cardView, @NonNull Button choices1, @NonNull Button choices2,
      @NonNull Button choices3, @NonNull ImageFilterView imageView, @NonNull TextView qustion,
      @NonNull Button suriin) {
    this.rootView = rootView;
    this.backBtn = backBtn;
    this.cardView = cardView;
    this.choices1 = choices1;
    this.choices2 = choices2;
    this.choices3 = choices3;
    this.imageView = imageView;
    this.qustion = qustion;
    this.suriin = suriin;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static Yunit3QuizYaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static Yunit3QuizYaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.yunit3_quiz_ya, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static Yunit3QuizYaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backBtn;
      Button backBtn = ViewBindings.findChildViewById(rootView, id);
      if (backBtn == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.choices_1;
      Button choices1 = ViewBindings.findChildViewById(rootView, id);
      if (choices1 == null) {
        break missingId;
      }

      id = R.id.choices_2;
      Button choices2 = ViewBindings.findChildViewById(rootView, id);
      if (choices2 == null) {
        break missingId;
      }

      id = R.id.choices_3;
      Button choices3 = ViewBindings.findChildViewById(rootView, id);
      if (choices3 == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageFilterView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.qustion;
      TextView qustion = ViewBindings.findChildViewById(rootView, id);
      if (qustion == null) {
        break missingId;
      }

      id = R.id.suriin;
      Button suriin = ViewBindings.findChildViewById(rootView, id);
      if (suriin == null) {
        break missingId;
      }

      return new Yunit3QuizYaBinding((RelativeLayout) rootView, backBtn, cardView, choices1,
          choices2, choices3, imageView, qustion, suriin);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
