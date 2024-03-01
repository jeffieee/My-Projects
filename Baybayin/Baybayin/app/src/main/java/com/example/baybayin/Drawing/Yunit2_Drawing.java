package com.example.baybayin.Drawing;

import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatA;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatEI;
import static com.example.baybayin.Fragment.Profile.Letrang_NasulatOU;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatba;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatdara;
import static com.example.baybayin.Fragment.Profile.Letrang_Nasulatga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baybayin.Activity.MainActivity;
import com.example.baybayin.R;
import com.example.baybayin.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;

public class Yunit2_Drawing extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static String a = "a";
    public static String ei = "ei";
    public static String ou = "ou";
    private DrawView drawView;
    private ImageButton drawButton, eraseButton;

    ImageButton back, next;
    Button suriin, bumalik;
    TextView result, labelTxt;

    String currentImage;

    String A;
    String EI;
    String OU;

    private int currentImageIndex = 0;
    private int[] imageResources = {
            R.drawable.baybayin_letter_a,
            R.drawable.baybayin_letter_ei,
            R.drawable.baybayin_letter_ou
    };

    private String[] groundTruthLabels = {
            "a",
            "ei",
            "ou"
    };
    ImageView image;
    private Bitmap bitmap = null;

    private static final String[] classNames = {"a", "ba", "dara", "ei", "ga", "ha", "ka", "la", "ma", "na", "nga", "ou", "pa", "sa", "ta", "wa", "ya"}; // Replace with your class names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yunit2_drawing);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();




        A = preferences.getString(a, null);
        EI = preferences.getString(ei, null);
        OU = preferences.getString(ou, null);

        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        suriin = findViewById(R.id.saved);
        image = findViewById(R.id.image_container);
        bumalik = findViewById(R.id.bumalik);
        result = findViewById(R.id.text);

        drawView = findViewById(R.id.drawView);
        drawButton = findViewById(R.id.pencil);
        eraseButton = findViewById(R.id.eraser);
        final FrameLayout drawContainer = findViewById(R.id.drawContainer);

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
                    if (currentImageIndex == 0){
                        currentImage = "a";
                        back.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    } else if (currentImageIndex == 1) {
                        currentImage = "ei";
                        next.setVisibility(View.VISIBLE);
                    } else if (currentImageIndex == 2) {
                        currentImage = "ou";
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
                    if (currentImageIndex == 0){
                        currentImage = "a";
                    } else if (currentImageIndex == 1) {
                        currentImage = "ei";
                    } else if (currentImageIndex == 2) {
                        currentImage = "ou";
                        next.setVisibility(View.INVISIBLE);
                    }
                    labelTxt = findViewById(R.id.labletxt);
                    labelTxt.setText(currentImage);
                }
            }
        });
        if (currentImageIndex == 0){
            currentImage = "a";
        } else if (currentImageIndex == 1) {
            currentImage = "ei";
        } else if (currentImageIndex == 2) {
            currentImage = "ou";
        }

        bumalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Yunit2_Drawing.this, MainActivity.class);
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
                    drawView.saveDrawingWithWhiteBackground();

                    String savedImageFileName = "your_image.jpg";
                    File savedImageFile = new File(getFilesDir(), savedImageFileName);

                    if (savedImageFile.exists()) {
                        Bitmap savedImageBitmap = BitmapFactory.decodeFile(savedImageFile.getAbsolutePath());
                        boolean isAllWhite = drawView.isImageAllWhite(savedImageBitmap);

                        if (isAllWhite) {
                            String message = "Draw first before clicking 'Suriin'.";
                            Toast.makeText(Yunit2_Drawing.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Model model = Model.newInstance(Yunit2_Drawing.this);
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
                                    if (currentImage == "a") {
                                        editor.putInt(Letrang_NasulatA, 1);
                                        editor.commit();
                                    }
                                    if (currentImage == "ei") {
                                        editor.putInt(Letrang_NasulatEI, 1);
                                        editor.commit();
                                    }

                                    if (currentImage == "ou") {
                                        editor.putInt(Letrang_NasulatOU, 1);
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



                            bitmap = resizedBitmap;
                            model.close();
                        }

                        savedImageFile.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Yunit2_Drawing.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Function to find the index with the maximum value
    // Function to find the index with the maximum value

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
