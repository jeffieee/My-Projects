package com.example.baybayin.Drawing;

import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatA;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatEI;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatOU;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatba;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatdara;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatga;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatha;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatka;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatla;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatma;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatna;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatnga;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatpa;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatsa;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatta;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatwa;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baybayin.Activity.MainActivity;
import com.example.baybayin.R;
import com.example.baybayin.databinding.Yunit3DrawingBinding;
import com.example.baybayin.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;

public class Yunit3_Drawing extends AppCompatActivity {

    Yunit3DrawingBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private DrawView drawView;
    private ImageButton drawButton, eraseButton;

    ImageButton back, next;
    Button suriin, bumalik;
    TextView result, labelTxt;

    String currentImage;

    private int currentImageIndex = 0;
    private int[] imageResources = {
            R.drawable.baybayin_letter_ba,
            R.drawable.baybayin_letter_dara,
            R.drawable.baybayin_letter_ga,
            R.drawable.baybayin_letter_ha,
            R.drawable.baybayin_letter_ka,
            R.drawable.baybayin_letter_la,
            R.drawable.baybayin_letter_ma,
            R.drawable.baybayin_letter_na,
            R.drawable.baybayin_letter_nga,
            R.drawable.baybayin_letter_pa,
            R.drawable.baybayin_letter_sa,
            R.drawable.baybayin_letter_ta,
            R.drawable.baybayin_letter_wa,
            R.drawable.baybayin_letter_ya

    };

    private String[] groundTruthLabels = {
            "ba", "dara", "ga", "ha", "ka", "la", "ma", "na", "nga", "pa", "sa", "ta", "wa", "ya"
    };
    ImageView image;
    private Bitmap bitmap = null;

    private static final String[] classNames = {"a", "ba", "dara", "ei", "ga", "ha", "ka", "la", "ma", "na", "nga", "ou", "pa", "sa", "ta", "wa", "ya",}; // Replace with your class names


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Yunit3DrawingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        suriin = findViewById(R.id.saved);
        image = findViewById(R.id.image_container);
        bumalik = findViewById(R.id.bumalik);
        result = findViewById(R.id.text);

        labelTxt = findViewById(R.id.labletxt);

        drawView = findViewById(R.id.drawView);
        drawButton = findViewById(R.id.pencil);
        eraseButton = findViewById(R.id.eraser);
        final FrameLayout drawContainer = findViewById(R.id.drawContainer);

        TextView text = findViewById(R.id.text);

        Drawable imageDraw = image.getDrawable();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImageIndex > 0) {
                    currentImageIndex--;
                    image.setImageResource(imageResources[currentImageIndex]);
                    drawContainer.setBackgroundResource(R.drawable.container_question_black);
                    drawView.clearCanvas();
                    result.setText("");
                    if (currentImageIndex == 0) {
                        currentImage = "ba";
                        back.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }else if (currentImageIndex == 1){
                        currentImage = "dara";

                    } else if (currentImageIndex == 2) {
                        currentImage = "ga";

                    } else if (currentImageIndex == 3) {
                        currentImage = "ha";
                    }
                    else if (currentImageIndex == 4) {
                        currentImage = "ka";
                    }
                    else if (currentImageIndex == 5) {
                        currentImage = "la";
                    }
                    else if (currentImageIndex == 6) {
                        currentImage = "ma";
                    }
                    else if (currentImageIndex == 7) {
                        currentImage = "na";
                    }
                    else if (currentImageIndex == 8) {
                        currentImage = "nga";
                    }
                    else if (currentImageIndex == 9) {
                        currentImage = "pa";
                    }
                    else if (currentImageIndex == 10) {
                        currentImage = "sa";
                    }
                    else if (currentImageIndex == 11) {
                        currentImage = "ta";
                    }
                    else if (currentImageIndex == 12) {
                        currentImage = "wa";

                    }
                    else if (currentImageIndex == 13) {
                        currentImage = "ya";
                        next.setVisibility(View.VISIBLE);

                    }
                    labelTxt = findViewById(R.id.labletxt);
                    labelTxt.setText(currentImage);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.VISIBLE);
                if (currentImageIndex < imageResources.length - 1) {
                    currentImageIndex++;
                    image.setImageResource(imageResources[currentImageIndex]);
                    drawContainer.setBackgroundResource(R.drawable.container_question_black);
                    drawView.clearCanvas();
                    result.setText("");
                    if (currentImageIndex == 0) {
                        currentImage = "ba";
                    }else if (currentImageIndex == 1){
                        currentImage = "dara";
                    } else if (currentImageIndex == 2) {
                        currentImage = "ga";

                    } else if (currentImageIndex == 3) {
                        currentImage = "ha";
                    }
                    else if (currentImageIndex == 4) {
                        currentImage = "ka";
                    }
                    else if (currentImageIndex == 5) {
                        currentImage = "la";
                    }
                    else if (currentImageIndex == 6) {
                        currentImage = "ma";
                    }
                    else if (currentImageIndex == 7) {
                        currentImage = "na";
                    }
                    else if (currentImageIndex == 8) {
                        currentImage = "nga";
                    }
                    else if (currentImageIndex == 9) {
                        currentImage = "pa";
                    }
                    else if (currentImageIndex == 10) {
                        currentImage = "sa";
                    }
                    else if (currentImageIndex == 11) {
                        currentImage = "ta";
                    }
                    else if (currentImageIndex == 12) {
                        currentImage = "wa";
                    }
                    else if (currentImageIndex == 13) {
                        currentImage = "ya";
                        next.setVisibility(View.INVISIBLE);
                    }
                    labelTxt = findViewById(R.id.labletxt);
                    labelTxt.setText(currentImage);
                }
            }
        });
        if (currentImageIndex == 0) {
            currentImage = "ba";
        }else if (currentImageIndex == 1){
            currentImage = "dara";
        } else if (currentImageIndex == 2) {
            currentImage = "ga";

        } else if (currentImageIndex == 3) {
            currentImage = "ha";
        }
        else if (currentImageIndex == 4) {
            currentImage = "ka";
        }
        else if (currentImageIndex == 5) {
            currentImage = "la";
        }
        else if (currentImageIndex == 6) {
            currentImage = "ma";
        }
        else if (currentImageIndex == 7) {
            currentImage = "na";
        }
        else if (currentImageIndex == 8) {
            currentImage = "nga";
        }
        else if (currentImageIndex == 9) {
            currentImage = "pa";
        }
        else if (currentImageIndex == 10) {
            currentImage = "sa";
        }
        else if (currentImageIndex == 11) {
            currentImage = "ta";
        }
        else if (currentImageIndex == 12) {
            currentImage = "wa";
        }
        else if (currentImageIndex == 13) {
            currentImage = "ya";
        }

        bumalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Yunit3_Drawing.this, MainActivity.class);
                intent.putExtra("Refresh", false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setErase(false);
                drawButton.setBackgroundResource(R.color.pale_green);
                eraseButton.setBackgroundResource(R.color.white);
            }
        });
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setErase(true);
                eraseButton.setBackgroundResource(R.color.pale_green);
                drawButton.setBackgroundResource(R.color.white);
            }
        });

        suriin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    drawView.saveDrawingWithWhiteBackground(); // Save the user's drawing with a white background

                    String savedImageFileName = "your_image.jpg"; // Replace with your actual filename
                    File savedImageFile = new File(getFilesDir(), savedImageFileName);

                    if (savedImageFile.exists()) {

                        Bitmap savedImageBitmap = BitmapFactory.decodeFile(savedImageFile.getAbsolutePath());
                        boolean isAllWhite = drawView.isImageAllWhite(savedImageBitmap);

                        // Notify the user about the saved image

                        if (isAllWhite) {
                            String message = "Draw first before clicking 'Suriin'.";
                            Toast.makeText(Yunit3_Drawing.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            Model model = Model.newInstance(Yunit3_Drawing.this);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(savedImageBitmap, 32, 32, true);
                            Bitmap grayscaleBitmap = convertToGrayscale(resizedBitmap);

                            // Replace this with your actual preprocessing code
                            float[] floatArray = preprocessInput(grayscaleBitmap);

                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 1, 32, 32}, DataType.FLOAT32);
                            inputFeature0.loadArray(floatArray);

                            Model.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                            // Calculate softmax to get class probabilities
                            float[] probabilities = softmax(outputFeature0.getFloatArray());

                            // Find the index with the highest probability (predicted class)
                            int predictedIndex = getMax(probabilities);

                            // Get the predicted class and its probability
                            String predictedClass = classNames[predictedIndex];
                            float predictedProbability = probabilities[predictedIndex];

                            // Set a threshold for low confidence (adjust as needed)
                            float confidenceThreshold = 0.5f;

                            if (predictedProbability >= confidenceThreshold) {
                                if (predictedClass.equals(groundTruthLabels[currentImageIndex])) {
                                    // Correct prediction
                                    drawContainer.setBackgroundResource(R.drawable.container_drawing_green);
                                    result.setText("Tagumpay!\n" +
                                            "Subukan mo naman ang ibang patinig!\n\n" +
                                            "Predicted Baybayin Characters: " + predictedClass + "\n" +
                                            "Accuracy: " + (int) (predictedProbability * 100) + "%");
                                    if (currentImage == "ba") {
                                        editor.putInt(Letrang_Nasulatba, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "dara") {
                                        editor.putInt(Letrang_Nasulatdara, 1);
                                        editor.commit();
                                    }

                                    if (currentImage == "ga") {
                                        editor.putInt(Letrang_Nasulatga, 1);
                                        editor.commit();
                                    }


                                    if (currentImage == "ha") {
                                        editor.putInt(Letrang_Nasulatha, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "ka") {
                                        editor.putInt(Letrang_Nasulatka, 1);
                                        editor.commit();
                                    }

                                    if (currentImage == "la") {
                                        editor.putInt(Letrang_Nasulatla, 1);
                                        editor.commit();
                                    }


                                    if (currentImage == "ma") {
                                        editor.putInt(Letrang_Nasulatma, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "na") {
                                        editor.putInt(Letrang_Nasulatna, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "nga") {
                                        editor.putInt(Letrang_Nasulatnga, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "pa") {
                                        editor.putInt(Letrang_Nasulatpa, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "sa") {
                                        editor.putInt(Letrang_Nasulatsa, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "ta") {
                                        editor.putInt(Letrang_Nasulatta, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "wa") {
                                        editor.putInt(Letrang_Nasulatwa, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "ya") {
                                        editor.putInt(Letrang_Nasulatya, 1);
                                        editor.commit();
                                    }

                                    // Your existing code for updating preferences
                                } else {
                                    // Wrong prediction
                                    drawContainer.setBackgroundResource(R.drawable.container_drawing_red);
                                    result.setText("Patawad, dahil ang iyong nasulat ay mali. Subalit maari kang sumubok muli.\n\n" +
                                            "Predicted Baybayin Character: " + predictedClass + "\n" +
                                            "Accuracy: " + (int) (predictedProbability * 100) + "%");
                                }
                            } else {
                                // Low confidence for incorrect predictions
                                drawContainer.setBackgroundResource(R.drawable.container_drawing_red);
                                result.setText("Patawad, dahil ang iyong nasulat ay mali. Subalit maari kang sumubok muli.\n" +
                                        "Low Confidence: " + (int) (predictedProbability * 100) + "% (Below Threshold)");
                            }


                            // Store the resized image for future use
                            bitmap = resizedBitmap;

                            model.close();
                        }

                        savedImageFile.delete();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Yunit3_Drawing.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private float[] preprocessInput(Bitmap grayscaleBitmap) {
        float[] floatArray = new float[32 * 32];
        int index = 0;

        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                int pixel = grayscaleBitmap.getPixel(x, y);
                floatArray[index++] = Color.red(pixel) / 255.0f;
            }
        }

        return floatArray;
    }

    private Bitmap convertToGrayscale(Bitmap inputBitmap) {
        int width = inputBitmap.getWidth();
        int height = inputBitmap.getHeight();

        Bitmap grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = inputBitmap.getPixel(x, y);
                int gray = Color.red(pixel); // Assuming grayscale, so only red channel is relevant
                grayscaleBitmap.setPixel(x, y, Color.rgb(gray, gray, gray));
            }
        }

        return grayscaleBitmap;
    }

    private float calculateAccuracy(String predictedClass, String groundTruth) {
        return predictedClass.equals(groundTruth) ? 1.0f : 0.0f;
    }

    // Function to calculate softmax
    // Function to calculate softmax
    // Function to calculate softmax
    private float[] softmax(float[] values) {
        float max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }

        float scale = 0.0f;
        for (int i = 0; i < values.length; i++) {
            scale += Math.exp(values[i] - max);
        }

        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = (float) Math.exp(values[i] - max) / scale;
        }

        return result;
    }

    // Function to find the index with the maximum value
    private int getMax(float[] array) {
        int maxIndex = 0;
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
