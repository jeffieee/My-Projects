// Generated by view binder compiler. Do not edit!
package com.example.baybayin.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public final class Yunit3DrawingBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageButton back;

  @NonNull
  public final Button bumalik;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final FrameLayout drawContainer;

  @NonNull
  public final ImageButton eraser;

  @NonNull
  public final ImageView imageContainer;

  @NonNull
  public final ImageFilterView imageView;

  @NonNull
  public final TextView labletxt;

  @NonNull
  public final ImageButton next;

  @NonNull
  public final ImageButton pencil;

  @NonNull
  public final Button saved;

  @NonNull
  public final TextView text;

  private Yunit3DrawingBinding(@NonNull RelativeLayout rootView, @NonNull ImageButton back,
      @NonNull Button bumalik, @NonNull CardView cardView, @NonNull FrameLayout drawContainer,
      @NonNull ImageButton eraser, @NonNull ImageView imageContainer,
      @NonNull ImageFilterView imageView, @NonNull TextView labletxt, @NonNull ImageButton next,
      @NonNull ImageButton pencil, @NonNull Button saved, @NonNull TextView text) {
    this.rootView = rootView;
    this.back = back;
    this.bumalik = bumalik;
    this.cardView = cardView;
    this.drawContainer = drawContainer;
    this.eraser = eraser;
    this.imageContainer = imageContainer;
    this.imageView = imageView;
    this.labletxt = labletxt;
    this.next = next;
    this.pencil = pencil;
    this.saved = saved;
    this.text = text;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static Yunit3DrawingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static Yunit3DrawingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.yunit3_drawing, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static Yunit3DrawingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back;
      ImageButton back = ViewBindings.findChildViewById(rootView, id);
      if (back == null) {
        break missingId;
      }

      id = R.id.bumalik;
      Button bumalik = ViewBindings.findChildViewById(rootView, id);
      if (bumalik == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.drawContainer;
      FrameLayout drawContainer = ViewBindings.findChildViewById(rootView, id);
      if (drawContainer == null) {
        break missingId;
      }

      id = R.id.eraser;
      ImageButton eraser = ViewBindings.findChildViewById(rootView, id);
      if (eraser == null) {
        break missingId;
      }

      id = R.id.image_container;
      ImageView imageContainer = ViewBindings.findChildViewById(rootView, id);
      if (imageContainer == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageFilterView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.labletxt;
      TextView labletxt = ViewBindings.findChildViewById(rootView, id);
      if (labletxt == null) {
        break missingId;
      }

      id = R.id.next;
      ImageButton next = ViewBindings.findChildViewById(rootView, id);
      if (next == null) {
        break missingId;
      }

      id = R.id.pencil;
      ImageButton pencil = ViewBindings.findChildViewById(rootView, id);
      if (pencil == null) {
        break missingId;
      }

      id = R.id.saved;
      Button saved = ViewBindings.findChildViewById(rootView, id);
      if (saved == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      return new Yunit3DrawingBinding((RelativeLayout) rootView, back, bumalik, cardView,
          drawContainer, eraser, imageContainer, imageView, labletxt, next, pencil, saved, text);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
