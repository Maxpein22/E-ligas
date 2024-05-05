package com.example.app_e_ligas;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;

public class NativeVoiceRecorder {
    private MediaRecorder mediaRecorder;
    private String outputFile;

    public NativeVoiceRecorder() {
        // Set the output file path
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecording.3gp";
    }

    public void startRecording() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            // Set the audio source (MIC)
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // Set the output format
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // Set the audio encoder
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Set the output file path
            mediaRecorder.setOutputFile(outputFile);

            try {
                // Prepare the media recorder
                mediaRecorder.prepare();
                // Start recording
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            // Stop recording
            mediaRecorder.stop();
            // Release the media recorder
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public String getOutputFile() {
        return outputFile;
    }
}
