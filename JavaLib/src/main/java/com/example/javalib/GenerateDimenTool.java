package com.example.javalib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateDimenTool {

    public static void main(String[] args) {
        try {
            // Define your base dimensions (e.g., dp values)
            // Generate values for dimens_1 to dimens_1000 (1dp to 1000dp)
            float[] baseDimensions = new float[1000];
            for (int i = 0; i < baseDimensions.length; i++) {
                baseDimensions[i] = i + 1;  // Assign values 1dp, 2dp, ..., 1000dp
            }

            // Define base font sizes (sp values) from 1sp to 50sp
            float[] fontSizes = new float[50];
            for (int i = 0; i < fontSizes.length; i++) {
                fontSizes[i] = i + 1;  // Assign values 1sp, 2sp, ..., 50sp
            }

            // Folder names and corresponding scaling factors
            String[] widthFolders = {
                    "w320dp", "w360dp", "w480dp", "w600dp", "w720dp", "w1024dp"
            };
            float[] scalingFactors = {

                    0.6f,
                    0.75f, // Scaling factor for w320dp
                    1.0f,  // Scaling factor for w360dp
                    1.5f,  // Scaling factor for w480dp
                    2.0f,  // Scaling factor for w600dp
                    2.5f,  // Scaling factor for w720dp
                      // Scaling factor for w1024dp
            };

            // Loop through each screen width folder and create dimens.xml
            for (int i = 0; i < widthFolders.length; i++) {
                String outputPath = "res/values-" + widthFolders[i] + "/dimens.xml";
                File file = new File(outputPath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                // Create BufferedWriter to write to the file
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                writer.write("<resources>\n");

                // Apply scaling to the dimension values and write to the file
                for (int j = 0; j < baseDimensions.length; j++) {
                    float scaledValue = baseDimensions[j] * scalingFactors[i];
                    writer.write("    <dimen name=\"dimens_" + (j + 1) + "\">" + scaledValue + "dp</dimen>\n");
                }

                // Apply scaling to the font size values and write to the file
                for (int k = 0; k < fontSizes.length; k++) {
                    float scaledFontSize = fontSizes[k] * scalingFactors[i];
                    writer.write("    <dimen name=\"font_size_" + (k + 1) + "\">" + scaledFontSize + "sp</dimen>\n");
                }

                writer.write("</resources>\n");
                writer.close();

                System.out.println("Dimens.xml file has been generated at: " + outputPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
